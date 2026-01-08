# Portal Backend Architecture

## Descripción General

El backend del portal de CodeFlowX está diseñado para gestionar usuarios, roles, menús y proporcionar un sistema completo de autenticación y autorización con auditoría de acceso.

## Estructura del Proyecto

```
portal-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/codeflowx/portal/
│   │   │       ├── PortalApplication.java
│   │   │       ├── config/
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   ├── JwtConfig.java
│   │   │       │   └── CorsConfig.java
│   │   │       ├── controllers/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── UserController.java
│   │   │       │   ├── RoleController.java
│   │   │       │   ├── MenuController.java
│   │   │       │   └── AuditController.java
│   │   │       ├── services/
│   │   │       │   ├── UserService.java
│   │   │       │   ├── RoleService.java
│   │   │       │   ├── MenuService.java
│   │   │       │   ├── JwtService.java
│   │   │       │   └── AuditService.java
│   │   │       ├── repositories/
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── RoleRepository.java
│   │   │       │   ├── MenuRepository.java
│   │   │       │   └── AuditRepository.java
│   │   │       ├── models/
│   │   │       │   ├── User.java
│   │   │       │   ├── Role.java
│   │   │       │   ├── Menu.java
│   │   │       │   ├── UserRole.java
│   │   │       │   ├── RoleMenu.java
│   │   │       │   └── AuditLog.java
│   │   │       ├── dto/
│   │   │       │   ├── UserDto.java
│   │   │       │   ├── RoleDto.java
│   │   │       │   ├── MenuDto.java
│   │   │       │   ├── LoginRequest.java
│   │   │       │   ├── LoginResponse.java
│   │   │       │   └── AuditLogDto.java
│   │   │       ├── security/
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   ├── UserDetailsServiceImpl.java
│   │   │       │   └── PasswordEncoder.java
│   │   │       └── exceptions/
│   │   │           ├── GlobalExceptionHandler.java
│   │   │           ├── UserNotFoundException.java
│   │   │           └── UnauthorizedException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   └── test/
│       └── java/
│           └── com/codeflowx/portal/
│               ├── PortalApplicationTests.java
│               ├── controllers/
│               ├── services/
│               └── repositories/
├── pom.xml
└── README.md
```

## Modelos de Datos

### 1. Usuario (User)

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
```

### 2. Rol (Role)

```java
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_menus",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private Set<Menu> menus = new HashSet<>();
}
```

### 3. Menú (Menu)

```java
@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column
    private String icon;
    
    @Column
    private String href;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "order_index")
    private Integer orderIndex;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "requires_auth")
    private Boolean requiresAuth = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    private List<Menu> children;
}
```

### 4. Auditoría (AuditLog)

```java
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "action")
    private String action;
    
    @Column(name = "resource")
    private String resource;
    
    @Column(name = "resource_id")
    private String resourceId;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "request_method")
    private String requestMethod;
    
    @Column(name = "request_url")
    private String requestUrl;
    
    @Column(name = "request_body")
    private String requestBody;
    
    @Column(name = "response_status")
    private Integer responseStatus;
    
    @Column(name = "execution_time")
    private Long executionTime;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "error_message")
    private String errorMessage;
}
```

## API Endpoints

### Autenticación

```
POST /api/v1/auth/login
POST /api/v1/auth/logout
POST /api/v1/auth/refresh
POST /api/v1/auth/register
```

### Usuarios

```
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
PUT    /api/v1/users/{id}
DELETE /api/v1/users/{id}
GET    /api/v1/users/{id}/roles
PUT    /api/v1/users/{id}/roles
```

### Roles

```
GET    /api/v1/roles
GET    /api/v1/roles/{id}
POST   /api/v1/roles
PUT    /api/v1/roles/{id}
DELETE /api/v1/roles/{id}
GET    /api/v1/roles/{id}/menus
PUT    /api/v1/roles/{id}/menus
```

### Menús

```
GET    /api/v1/menus
GET    /api/v1/menus/{id}
POST   /api/v1/menus
PUT    /api/v1/menus/{id}
DELETE /api/v1/menus/{id}
GET    /api/v1/menus/tree
GET    /api/v1/menus/user/{userId}
```

### Auditoría

```
GET    /api/v1/audit
GET    /api/v1/audit/user/{userId}
GET    /api/v1/audit/action/{action}
GET    /api/v1/audit/resource/{resource}
GET    /api/v1/audit/date-range
```

## Seguridad

### JWT Configuration

```yaml
jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: ${JWT_EXPIRATION:86400000} # 24 hours
  refresh-expiration: ${JWT_REFRESH_EXPIRATION:604800000} # 7 days
```

### Roles por Defecto

- **SUPER_ADMIN**: Acceso completo al sistema
- **ADMIN**: Gestión de usuarios, roles y menús
- **USER**: Usuario estándar con acceso limitado
- **DEVELOPER**: Acceso a herramientas de desarrollo
- **ANALYST**: Acceso a reportes y análisis

## Base de Datos

### Scripts de Creación

```sql
-- Tabla de usuarios
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Tabla de roles
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de menús
CREATE TABLE menus (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    href VARCHAR(255),
    parent_id BIGINT REFERENCES menus(id),
    order_index INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    requires_auth BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de relación usuario-rol
CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Tabla de relación rol-menú
CREATE TABLE role_menus (
    role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
    menu_id BIGINT REFERENCES menus(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, menu_id)
);

-- Tabla de auditoría
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    username VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100),
    resource_id VARCHAR(100),
    ip_address INET,
    user_agent TEXT,
    request_method VARCHAR(10),
    request_url TEXT,
    request_body TEXT,
    response_status INTEGER,
    execution_time BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_id VARCHAR(100),
    error_message TEXT
);

-- Índices para optimización
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_resource ON audit_logs(resource);
```

## Configuración de Spring Boot

### application.yml

```yaml
spring:
  application:
    name: codeflowx-portal-backend
  
  datasource:
    url: jdbc:postgresql://localhost:5432/codeflowx_portal
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  security:
    user:
      name: ${ADMIN_USERNAME:admin}
      password: ${ADMIN_PASSWORD:admin}

server:
  port: ${SERVER_PORT:8080}
  servlet:
    context-path: /api/v1

logging:
  level:
    com.codeflowx.portal: DEBUG
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

## Dependencias Maven

```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Test Dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Implementación de Servicios

### UserService

```java
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuditService auditService;
    
    public User createUser(UserDto userDto) {
        // Validar que el username y email no existan
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        
        // Asignar roles por defecto
        if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()) {
            Set<Role> roles = roleRepository.findAllById(userDto.getRoleIds());
            user.setRoles(roles);
        }
        
        User savedUser = userRepository.save(user);
        
        // Registrar auditoría
        auditService.logAction(
            SecurityContextHolder.getContext().getAuthentication().getName(),
            "CREATE_USER",
            "USER",
            savedUser.getId().toString()
        );
        
        return savedUser;
    }
    
    public User updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        // Actualizar campos
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists");
            }
            user.setEmail(userDto.getEmail());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        
        // Registrar auditoría
        auditService.logAction(
            SecurityContextHolder.getContext().getAuthentication().getName(),
            "UPDATE_USER",
            "USER",
            updatedUser.getId().toString()
        );
        
        return updatedUser;
    }
    
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        userRepository.delete(user);
        
        // Registrar auditoría
        auditService.logAction(
            SecurityContextHolder.getContext().getContext().getAuthentication().getName(),
            "DELETE_USER",
            "USER",
            userId.toString()
        );
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
```

### AuditService

```java
@Service
public class AuditService {
    
    @Autowired
    private AuditRepository auditRepository;
    
    @Autowired
    private HttpServletRequest request;
    
    public void logAction(String username, String action, String resource, String resourceId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setResource(resource);
        auditLog.setResourceId(resourceId);
        auditLog.setIpAddress(getClientIpAddress());
        auditLog.setUserAgent(request.getHeader("User-Agent"));
        auditLog.setRequestMethod(request.getMethod());
        auditLog.setRequestUrl(request.getRequestURL().toString());
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setSessionId(request.getSession().getId());
        
        auditRepository.save(auditLog);
    }
    
    public void logRequest(String username, String action, String resource, 
                          String resourceId, String requestBody, Integer responseStatus, 
                          Long executionTime) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setResource(resource);
        auditLog.setResourceId(resourceId);
        auditLog.setIpAddress(getClientIpAddress());
        auditLog.setUserAgent(request.getHeader("User-Agent"));
        auditLog.setRequestMethod(request.getMethod());
        auditLog.setRequestUrl(request.getRequestURL().toString());
        auditLog.setRequestBody(requestBody);
        auditLog.setResponseStatus(responseStatus);
        auditLog.setExecutionTime(executionTime);
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setSessionId(request.getSession().getId());
        
        auditRepository.save(auditLog);
    }
    
    private String getClientIpAddress() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
    
    public List<AuditLog> getAuditLogsByUser(Long userId) {
        return auditRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditRepository.findByActionOrderByCreatedAtDesc(action);
    }
    
    public List<AuditLog> getAuditLogsByResource(String resource) {
        return auditRepository.findByResourceOrderByCreatedAtDesc(resource);
    }
    
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }
}
```

## Seguridad y Filtros

### JwtAuthenticationFilter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token inválido, continuar sin autenticación
        }
        
        filterChain.doFilter(request, response);
    }
}
```

## Testing

### UserControllerTest

```java
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UserControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        
        ResponseEntity<UserDto> response = restTemplate.postForEntity(
            "/api/v1/users",
            userDto,
            UserDto.class
        );
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }
    
    @Test
    void testGetUserById() {
        // Crear usuario de prueba
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        ResponseEntity<UserDto> response = restTemplate.getForEntity(
            "/api/v1/users/" + savedUser.getId(),
            UserDto.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedUser.getId(), response.getBody().getId());
    }
}
```

## Despliegue

### Docker

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  portal-backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=codeflowx_portal
      - DB_USERNAME=postgres
      - DB_PASSWORD=password
    depends_on:
      - postgres
    networks:
      - portal-network
  
  postgres:
    image: postgres:15
    environment:
      - POSTGRES_DB=codeflowx_portal
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - portal-network

volumes:
  postgres_data:

networks:
  portal-network:
    driver: bridge
```

## Monitoreo y Métricas

### Health Checks

```java
@Component
public class PortalHealthIndicator implements HealthIndicator {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public Health health() {
        try {
            long userCount = userRepository.count();
            long roleCount = roleRepository.count();
            
            return Health.up()
                .withDetail("users", userCount)
                .withDetail("roles", roleCount)
                .withDetail("status", "Portal backend is running")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

### Métricas Personalizadas

```java
@Component
public class PortalMetrics {
    
    @Autowired
    private AuditRepository auditRepository;
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    @PostConstruct
    public void init() {
        // Contador de usuarios activos
        Gauge.builder("portal.users.active")
            .description("Number of active users")
            .register(meterRegistry, this, PortalMetrics::getActiveUserCount);
        
        // Contador de acciones de auditoría
        Counter.builder("portal.audit.actions")
            .description("Total audit actions")
            .register(meterRegistry);
    }
    
    private double getActiveUserCount() {
        return userRepository.countByIsActiveTrue();
    }
    
    public void incrementAuditActions() {
        Counter.builder("portal.audit.actions")
            .register(meterRegistry)
            .increment();
    }
}
```

## Conclusión

Esta arquitectura proporciona un backend robusto y escalable para el portal de CodeFlowX, con:

- **Gestión completa de usuarios y roles**
- **Sistema de menús dinámico y configurable**
- **Auditoría detallada de todas las acciones**
- **Seguridad JWT robusta**
- **API RESTful bien estructurada**
- **Base de datos PostgreSQL optimizada**
- **Monitoreo y métricas integrados**
- **Testing completo**
- **Despliegue con Docker**

El sistema está diseñado para ser fácilmente extensible y mantener un alto nivel de seguridad y auditoría.
