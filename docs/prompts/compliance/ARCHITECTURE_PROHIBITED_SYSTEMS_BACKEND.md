# 🏗️ ARQUITECTURA DE INTEGRACIÓN - SISTEMAS PROHIBIDOS (Art. 5)

**Módulo:** Compliance - Prohibited Systems
**Fecha:** Diciembre 2025
**Estado:** 📋 Documentación de integración

---

## 📋 RESUMEN EJECUTIVO

Este documento describe la arquitectura de integración entre el frontend Next.js y el backend Java para el módulo de Sistemas Prohibidos según el Art. 5 del EU AI Act.

### **Flujo de Integración**

```
Frontend (Next.js) → API Routes (Next.js) → Backend Java (Spring Boot) → Base de Datos (PostgreSQL)
```

---

## 🔄 CAPAS DE INTEGRACIÓN

### **1. Frontend (Next.js) - Páginas React**

**Ubicación:** `app/(app)/governance/compliance/prohibited-systems/`

**Patrón de llamadas:**
- Las páginas hacen llamadas directas a las API routes de Next.js usando `fetch()`
- No llaman directamente al backend Java
- Manejan estados de carga y errores localmente

**Ejemplo:**
```typescript
const response = await fetch("/api/compliance/prohibited-systems", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ action: "verify", projectId: 5001 }),
});
```

### **2. API Routes (Next.js) - Proxy/Intermediario**

**Ubicación:** `app/api/compliance/prohibited-systems/`

**Responsabilidades:**
- Actúan como proxy entre frontend y backend Java
- Validan requests
- Transforman datos entre formatos frontend/backend
- Manejan errores y respuestas
- En modo demo, retornan mock data

**Estructura:**
```
app/api/compliance/prohibited-systems/
├── route.ts                    # GET/POST principal
├── catalog/
│   └── route.ts               # CRUD catálogo
└── [id]/
    └── route.ts               # Detalle y acciones
```

### **3. Backend Java (Spring Boot)**

**Ubicación:** `nocode.service/` y `codeflowx.govern.*/`

**Componentes principales:**

#### **3.1. Business Services**
**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

**ProhibitedSystemBusinessService.java**
```java
@Service
public class ProhibitedSystemBusinessService {

    /**
     * Verifica si un proyecto usa sistemas prohibidos
     * @param projectId ID del proyecto
     * @return ProhibitedSystemCheckResult con sistemas detectados
     */
    public ProhibitedSystemCheckResult checkProhibitedSystem(Long projectId) {
        // 1. Obtener proyecto
        Project project = projectService.findById(projectId);

        // 2. Obtener catálogo activo
        List<ProhibitedSystem> activeSystems = getActiveProhibitedSystems();

        // 3. Verificar coincidencias por keywords
        List<ProhibitedSystem> detected = new ArrayList<>();
        for (ProhibitedSystem system : activeSystems) {
            if (matchesKeywords(project, system.getKeywords())) {
                detected.add(system);
            }
        }

        // 4. Si se detecta, bloquear despliegue automáticamente
        if (!detected.isEmpty()) {
            blockDeployment(projectId,
                "Sistema prohibido detectado: " + detected.get(0).getName());
        }

        return new ProhibitedSystemCheckResult(detected, !detected.isEmpty());
    }

    /**
     * Obtiene catálogo activo de sistemas prohibidos
     * @return Lista de sistemas activos ordenados por nombre
     */
    public List<ProhibitedSystem> getActiveProhibitedSystems() {
        return prohibitedSystemService.findByActiveTrueOrderByName();
    }

    /**
     * Bloquea el despliegue de un proyecto
     * @param projectId ID del proyecto
     * @param reason Razón del bloqueo
     */
    public void blockDeployment(Long projectId, String reason) {
        Project project = projectService.findById(projectId);
        project.setDeploymentBlocked(true);
        project.setBlockReason(reason);
        projectService.save(project);
    }

    /**
     * Verifica coincidencias de keywords
     */
    private boolean matchesKeywords(Project project, List<String> keywords) {
        String projectText = (project.getName() + " " +
                              project.getDescription() + " " +
                              getModelDescriptions(project)).toLowerCase();

        for (String keyword : keywords) {
            if (projectText.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
```

#### **3.2. Services CRUD**
**Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/`

**ProhibitedSystemService.java**
```java
@Service
public class ProhibitedSystemService {

    public List<ProhibitedSystem> findAll() {
        return prohibitedSystemRepository.findAll();
    }

    public ProhibitedSystem findById(Long id) {
        return prohibitedSystemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProhibitedSystem not found"));
    }

    public List<ProhibitedSystem> findByActiveTrueOrderByName() {
        return prohibitedSystemRepository.findByPrsactiveTrueOrderByPrsname();
    }

    public ProhibitedSystem save(ProhibitedSystem system) {
        return prohibitedSystemRepository.save(system);
    }

    public void delete(Long id) {
        prohibitedSystemRepository.deleteById(id);
    }
}
```

#### **3.3. Entidades JPA**
**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

**ProhibitedSystem.java**
```java
@Entity
@Table(name = "GOVPROHIBITEDSYSTEMS")
public class ProhibitedSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPROHIBITEDSYSTEM")
    private Long idxprohibitedsystem;

    @Column(name = "PRSNAME", nullable = false, length = 255)
    private String prsname;

    @Column(name = "PRSDESCRIPTION", length = 2000)
    private String prsdescription;

    @Column(name = "PRSCATEGORY", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProhibitedSystemCategory prscategory;

    @Column(name = "PRSKEYWORDS", columnDefinition = "JSONB")
    @Type(JsonBinaryType.class)
    private List<String> prskeywords;

    @Column(name = "PRSACTIVE", nullable = false)
    private Boolean prsactive = true;

    @Column(name = "PRSCREATEDAT", nullable = false)
    private Timestamp prscreatedat;

    @Column(name = "PRSCREATEDBY", length = 100)
    private String prscreatedby;

    @Column(name = "IDUUID", unique = true, length = 36)
    private String iduuid;

    // Getters y Setters
}

public enum ProhibitedSystemCategory {
    ART_5_1_A,  // Manipulación subliminal
    ART_5_1_B,  // Explotación de vulnerabilidades
    ART_5_1_C,  // Scoring social por autoridades públicas
    ART_5_1_D   // Identificación biométrica remota en tiempo real
}
```

### **4. Base de Datos (PostgreSQL)**

**Tabla:** `GOVPROHIBITEDSYSTEMS`

**Estructura:**
```sql
CREATE TABLE GOVPROHIBITEDSYSTEMS (
    IDXPROHIBITEDSYSTEM BIGSERIAL PRIMARY KEY,
    PRSNAME VARCHAR(255) NOT NULL,
    PRSDESCRIPTION VARCHAR(2000),
    PRSCATEGORY VARCHAR(50) NOT NULL,
    PRSKEYWORDS JSONB,
    PRSACTIVE BOOLEAN NOT NULL DEFAULT true,
    PRSCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRSCREATEDBY VARCHAR(100),
    IDUUID VARCHAR(36) UNIQUE
);

CREATE INDEX idx_govprohibitedsystems_active ON GOVPROHIBITEDSYSTEMS(PRSACTIVE);
CREATE INDEX idx_govprohibitedsystems_category ON GOVPROHIBITEDSYSTEMS(PRSCATEGORY);
```

**Tabla relacionada:** `PRJPROJECTS`
```sql
-- Campos relevantes para sistemas prohibidos
ALTER TABLE PRJPROJECTS ADD COLUMN PRJDEPLOYMENTBLOCKED BOOLEAN DEFAULT false;
ALTER TABLE PRJPROJECTS ADD COLUMN PRJBLOCKREASON VARCHAR(500);
```

---

## 🔌 ENDPOINTS DEL BACKEND JAVA

### **REST Controller (a implementar)**

**Ubicación sugerida:** `codeflowx.govern.web/src/main/java/com/codeflowx/govern/web/rest/compliance/`

**ProhibitedSystemRestController.java**
```java
@RestController
@RequestMapping("/api/compliance/prohibited-systems")
public class ProhibitedSystemRestController {

    @Autowired
    private ProhibitedSystemBusinessService businessService;

    @Autowired
    private ProhibitedSystemService service;

    /**
     * GET /api/compliance/prohibited-systems
     * Obtiene lista de proyectos verificados con sistemas prohibidos
     */
    @GetMapping
    public ResponseEntity<ProhibitedSystemsResponse> getProhibitedSystems(
            @RequestParam(required = false) Long projectId) {

        if (projectId != null) {
            ProhibitedSystemCheckResult result =
                businessService.checkProhibitedSystem(projectId);
            return ResponseEntity.ok(mapToResponse(result));
        }

        // Retornar todos los proyectos verificados
        List<Project> projects = projectService.findAll();
        // ... lógica de mapeo
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/compliance/prohibited-systems
     * Verifica un proyecto o bloquea despliegue
     */
    @PostMapping
    public ResponseEntity<?> handleProhibitedSystemAction(
            @RequestBody ProhibitedSystemRequest request) {

        if ("verify".equals(request.getAction())) {
            ProhibitedSystemCheckResult result =
                businessService.checkProhibitedSystem(request.getProjectId());
            return ResponseEntity.ok(result);
        }

        if ("block".equals(request.getAction())) {
            businessService.blockDeployment(
                request.getProjectId(),
                request.getReason());
            return ResponseEntity.ok(Map.of("success", true));
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * GET /api/compliance/prohibited-systems/catalog
     * Obtiene catálogo de sistemas prohibidos
     */
    @GetMapping("/catalog")
    public ResponseEntity<List<ProhibitedSystemDTO>> getCatalog(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean active) {

        List<ProhibitedSystem> systems;
        if (active != null && active) {
            systems = businessService.getActiveProhibitedSystems();
        } else {
            systems = service.findAll();
        }

        if (category != null) {
            systems = systems.stream()
                .filter(s -> s.getCategory().name().equals(category))
                .collect(Collectors.toList());
        }

        return ResponseEntity.ok(mapToDTOList(systems));
    }

    /**
     * POST /api/compliance/prohibited-systems/catalog
     * Crea nuevo sistema prohibido
     */
    @PostMapping("/catalog")
    public ResponseEntity<ProhibitedSystemDTO> createSystem(
            @RequestBody ProhibitedSystemDTO dto) {

        ProhibitedSystem system = mapFromDTO(dto);
        system.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        system.setCreatedBy(getCurrentUser());
        system.setUuid(UUID.randomUUID().toString());

        ProhibitedSystem saved = service.save(system);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    /**
     * PUT /api/compliance/prohibited-systems/catalog/{id}
     * Actualiza sistema prohibido
     */
    @PutMapping("/catalog/{id}")
    public ResponseEntity<ProhibitedSystemDTO> updateSystem(
            @PathVariable Long id,
            @RequestBody ProhibitedSystemDTO dto) {

        ProhibitedSystem system = service.findById(id);
        updateFromDTO(system, dto);
        system.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        ProhibitedSystem saved = service.save(system);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    /**
     * DELETE /api/compliance/prohibited-systems/catalog/{id}
     * Elimina sistema prohibido (soft delete)
     */
    @DeleteMapping("/catalog/{id}")
    public ResponseEntity<?> deleteSystem(@PathVariable Long id) {
        ProhibitedSystem system = service.findById(id);
        system.setActive(false);
        service.save(system);
        return ResponseEntity.ok(Map.of("success", true));
    }

    /**
     * GET /api/compliance/prohibited-systems/{id}
     * Obtiene detalle de detección
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetectionDetailDTO> getDetectionDetail(
            @PathVariable Long id) {

        // Obtener detección desde base de datos o calcular
        DetectionDetailDTO detail = buildDetectionDetail(id);
        return ResponseEntity.ok(detail);
    }
}
```

---

## 📊 MAPEO DE DATOS

### **Frontend → Backend**

**Request de Verificación:**
```typescript
// Frontend
{
  action: "verify",
  projectId: 5001
}
```

```java
// Backend
public class ProhibitedSystemRequest {
    private String action;
    private Long projectId;
    private String reason;
}
```

**Request de Catálogo:**
```typescript
// Frontend
{
  name: "Social Scoring by Public Authorities",
  category: "ART_5_1_C",
  description: "AI systems for social scoring...",
  keywords: ["social scoring", "public authority"],
  active: true
}
```

```java
// Backend
public class ProhibitedSystemDTO {
    private Long id;
    private String name;
    private String category;
    private String description;
    private List<String> keywords;
    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
```

### **Backend → Frontend**

**Response de Verificación:**
```java
// Backend
public class ProhibitedSystemCheckResult {
    private List<ProhibitedSystem> detectedSystems;
    private Boolean hasProhibitedSystem;
    private Double confidence;
    private List<String> matchedKeywords;
}
```

```typescript
// Frontend
interface ProhibitedSystemCheckResult {
  detected: boolean;
  systems: {
    id: number;
    name: string;
    category: string;
    detectedAt: string;
    confidence: number;
  }[];
  matchedKeywords: string[];
}
```

---

## 🔐 CONFIGURACIÓN Y VARIABLES DE ENTORNO

### **Frontend (.env.local)**
```env
# URL del backend Java
NEXT_PUBLIC_API_URL=http://localhost:8080

# Modo demo (usa mock data si no hay API_URL)
NEXT_PUBLIC_DEMO_MODE=false
```

### **Backend (application.properties)**
```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/codeflowx
spring.datasource.username=codeflowx
spring.datasource.password=***

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

---

## 🧪 TESTING Y MOCK DATA

### **Modo Demo (Frontend)**
Las API routes de Next.js retornan mock data cuando:
- `NEXT_PUBLIC_API_URL` no está configurada, o
- `NEXT_PUBLIC_DEMO_MODE=true`

**Ubicación mock data:** `app/api/compliance/prohibited-systems/route.ts`

### **Testing Backend**
```java
@SpringBootTest
class ProhibitedSystemBusinessServiceTest {

    @Autowired
    private ProhibitedSystemBusinessService service;

    @Test
    void testCheckProhibitedSystem() {
        Long projectId = 5001L;
        ProhibitedSystemCheckResult result =
            service.checkProhibitedSystem(projectId);

        assertNotNull(result);
        // Verificar lógica de detección
    }
}
```

---

## 📝 CHECKLIST DE INTEGRACIÓN

### **Frontend (Next.js)**
- [x] API routes creadas (`/api/compliance/prohibited-systems/*`)
- [x] Mock data implementado
- [x] Manejo de errores
- [ ] Integración con backend Java (pendiente)
- [ ] Variables de entorno configuradas

### **Backend (Java)**
- [ ] REST Controller implementado
- [ ] Business Service completo
- [ ] Service CRUD completo
- [ ] Entidad JPA mapeada
- [ ] Repositorio JPA creado
- [ ] DTOs de request/response
- [ ] Tests unitarios
- [ ] Tests de integración

### **Base de Datos**
- [ ] Tabla `GOVPROHIBITEDSYSTEMS` creada
- [ ] Índices creados
- [ ] Campos en `PRJPROJECTS` añadidos
- [ ] Datos de prueba insertados

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompt Prohibited Systems:** `docs/prompts/compliance/PROMPT_COMPLIANCE_PROHIBITED_SYSTEMS.md`
- **Entidades JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`
- **Business Services:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

---

**Última actualización:** Diciembre 2025
**Estado:** Documentación completa - Pendiente implementación backend



