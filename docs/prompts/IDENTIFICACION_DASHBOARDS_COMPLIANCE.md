Failed to compile

Next.js (14.2.30) is outdated (learn more)
./app/(app)/governance/compliance/dashboard/page.tsx
Error:
  × Unexpected token `div`. Expected jsx identifier
     ╭─[C:\Users\ManuelGonzalez\git\codeflowx-studio\app\(app)\governance\compliance\dashboard\page.tsx:223:1]
 223 │   }
 224 │
 225 │   return (
 226 │     <div className="space-y-4">
     ·      ───
 227 │         {/* Header - Título alineado a la izquierda */}
 228 │         <div className="flex items-center justify-between">
 229 │           <div className="space-y-1">
     ╰────

Caused by:
    Syntax Error# Dashboard de Compliance - Identificación de Prompts

**Ruta:** `app/(app)/governance/compliance/dashboard/page.tsx` | **URL:** `/governance/compliance/dashboard`

---

## 📊 Información del Dashboard

**Tipo:** Dashboard con métricas y KPIs de compliance
**ZUL Original:** `console/gobierno/compliance/dashboard.zul`
**ViewModel Java:** `ComplianceDashboardViewModel` (`com.codeflowx.govern.viewmodel.compliance`)
**Servicio Backend:** `ComplianceDashboardService`

**Funcionalidades:**
- Métricas principales de compliance (evaluaciones, FRIAs, registros UE)
- KPIs de cumplimiento y visualizaciones
- Estado de sistemas de alto riesgo
- Métricas de QMS y documentación técnica

---

## 📚 Prompts Identificados

| Prompt | Tipo | Ubicación |
|--------|------|-----------|
| **MIGRACION_COMPLIANCE_DASHBOARDS.md** | Migración | `codeflowx-studio/docs/prompts/MIGRACION_COMPLIANCE_DASHBOARDS.md` |
| **BUSINESS_LOGIC_COMPLIANCE.md** | Lógica de Negocio | `codeflowx-studio/docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md` |

**Detalles:**
- **MIGRACION_COMPLIANCE_DASHBOARDS.md:** Estrategia de migración, patrones de implementación, configuración de mock
- **BUSINESS_LOGIC_COMPLIANCE.md:** Business Services, consultas BBDD, métodos para obtener métricas

---

## 🏗️ Arquitectura de Comunicación

```
Frontend (Next.js)
    ↓ HTTP Request
API Route: app/api/governance/compliance/dashboard/route.ts
    ↓ (con soporte mock: NEXT_PUBLIC_USE_MOCK=true)
BFF: codeflowx.govern.bff.compliance [Reactivo - WebFlux]
    ↓ HTTP/WebClient (Reactivo)
Microservicios de Negocio [Reactivo - WebFlux]
    ↓ Mono.fromCallable()
Servicios de Negocio (codeflowx.govern.business) [Síncrono]
    ↓ Repositorios JPA
Entidades JPA
```

### 1. API Route (Next.js)

**Ubicación:** `app/api/governance/compliance/dashboard/route.ts`

```typescript
import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    const mockData = await import('@/mocks/governance/compliance/dashboard.json');
    return NextResponse.json(mockData.default);
  }

  const bffUrl = process.env.NEXT_PUBLIC_BFF_COMPLIANCE_URL || 'http://localhost:8080';
  const response = await fetch(`${bffUrl}/api/v1/compliance/dashboard/metrics`);
  if (!response.ok) {
    return NextResponse.json({ error: 'Error al obtener métricas' }, { status: response.status });
  }
  return NextResponse.json(await response.json());
}
```

**Variables de Entorno:**
```bash
NEXT_PUBLIC_USE_MOCK=true
NEXT_PUBLIC_BFF_COMPLIANCE_URL=http://localhost:8080
```

### 2. BFF Controller

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/ComplianceDashboardController.java`

```java
@RestController
@RequestMapping("/api/v1/compliance/dashboard")
@RequiredArgsConstructor
@Slf4j
public class ComplianceDashboardController {
    private final ComplianceDashboardService complianceDashboardService;

    @GetMapping("/metrics")
    public Mono<ResponseEntity<ComplianceDashboardMetricsDto>> getMetrics() {
        return complianceDashboardService.getDashboardMetrics()
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error obteniendo métricas", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }
}
```

### 3. BFF Service

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/ComplianceDashboardService.java`

**Características:**
- ✅ Reactivo (retorna `Mono<T>`)
- ✅ WebClient para llamadas HTTP
- ✅ Circuit Breaker y Retry (Resilience4j)
- ✅ Trabaja SOLO con DTOs (nunca entidades JPA)

### 4. Configuración BFF

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

```yaml
services:
  compliance:
    base-url: ${COMPLIANCE_SERVICE_BASE_URL:http://localhost:8090}

resilience4j:
  circuitbreaker:
    instances:
      complianceDashboard:
        registerHealthIndicator: true
        slidingWindowSize: 10
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      complianceDashboard:
        maxAttempts: 3
        waitDuration: 1s
```

### 5. Mock Data

**Ubicación:** `app/mocks/governance/compliance/dashboard.json`

```json
{
  "summary": {
    "totalAssessments": 45,
    "totalFrias": 32,
    "totalEuRegistrations": 18,
    "highRiskProjects": 23,
    "averageComplianceScore": 0.78,
    "readyToCertify": 12,
    "pendingGaps": 8
  },
  "metrics": {
    "assessmentsByStatus": { "COMPLIANT": 25, "NON_COMPLIANT": 8, "PENDING": 12 },
    "friasByRisk": { "HIGH": 5, "MEDIUM": 15, "LOW": 12 }
  }
}
```

### 6. DTOs Requeridos

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

- `ComplianceDashboardMetricsDto` - Métricas principales
- `ComplianceSummaryDto` - Resumen de compliance
- `AssessmentStatusDto` - Estado de evaluaciones

---

## ✅ Checklist de Implementación

**Frontend:**
- [ ] Crear `app/(app)/governance/compliance/dashboard/page.tsx`
- [ ] Crear `app/api/governance/compliance/dashboard/route.ts`
- [ ] Crear mock data en `app/mocks/governance/compliance/dashboard.json`
- [ ] Implementar métricas principales (cards, gráficos, KPIs)
- [ ] Agregar traducciones en `app/config/i18n/modules/governance/compliance.ts`
- [ ] Agregar entrada al menú en `app/config/modules.ts`

**Backend (BFF):**
- [ ] Crear `ComplianceDashboardController` en BFF
- [ ] Crear `ComplianceDashboardService` en BFF
- [ ] Configurar Circuit Breaker y Retry en `application.yml`
- [ ] Crear DTOs en `codeflowx.govern.nocode.dtos`

**Backend (Business Service):**
- [ ] Crear `ComplianceDashboardBusinessService` (si no existe)
- [ ] Implementar métodos para obtener métricas
- [ ] Consultas BBDD para métricas agregadas

---

## 📚 Referencias

**Servicios Backend:**
- `ComplianceAssessmentBusinessService` - Evaluaciones de conformidad
- `FriaAssessmentBusinessService` - Evaluaciones FRIA
- `ProhibitedSystemBusinessService` - Sistemas prohibidos
- `QualityManagementSystemBusinessService` - QMS

**Entidades JPA:**
- `ComplianceAssessment`, `FriaAssessment`, `EuRegistration`, `ProhibitedSystem`, `Project`

**Arquitectura:**
- `codeflowx-studio/docs/ARQUITECTURA_FRONTEND.md` - Arquitectura completa frontend/backend

---

**Última actualización:** Diciembre 2025
