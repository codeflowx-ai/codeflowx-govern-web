# Módulo de Formación de Usuarios (User Training) - Portal Backend

## Descripción General

El módulo de User Training gestiona la formación y capacitación de usuarios del sistema. Incluye la creación de cursos, lecciones, evaluaciones, certificaciones y seguimiento del progreso de aprendizaje. Es fundamental para mantener actualizados a los usuarios en las tecnologías y procesos del sistema.

## Arquitectura del Sistema

### Concepto Clave

El sistema de formación proporciona:

- **Cursos estructurados** por módulos funcionales del sistema
- **Lecciones interactivas** con contenido multimedia
- **Evaluaciones automáticas** para medir el progreso
- **Certificaciones** que validan competencias
- **Seguimiento del progreso** individual y por departamento
- **Integración con roles** para acceso a cursos específicos

### Flujo de Formación

```
Usuario → Acceso al Sistema → Dashboard de Formación
    ↓
Selección de Cursos Disponibles:
    ↓
├── Cursos Básicos (obligatorios)
├── Cursos Avanzados (por rol)
├── Cursos Especializados (por módulo)
└── Cursos Personalizados (por departamento)
    ↓
Progreso de Aprendizaje:
    ↓
├── Lecciones completadas
├── Evaluaciones aprobadas
├── Certificaciones obtenidas
└── Reportes de progreso
```

## Entidades del Sistema

### 1. Course

```java
@Entity
@Table(name = "trn_courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "course_code")
    private String courseCode; // Código único del curso

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column(name = "estimated_hours")
    private Integer estimatedHours;

    @Column(name = "is_mandatory")
    private Boolean isMandatory = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Long createdBy; // Referencia a users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseEnrollment> enrollments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "course_roles",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> requiredRoles = new HashSet<>();
}

public enum CourseCategory {
    SYSTEM_BASICS,        // Conceptos básicos del sistema
    AI_FUNDAMENTALS,      // Fundamentos de IA
    RAG_SYSTEM,          // Sistema RAG
    MODEL_MANAGEMENT,     // Gestión de modelos
    SERVING_DEPLOYMENT,   // Despliegue y serving
    GOVERNANCE,           // Gobierno y políticas
    TECHNOLOGY_STACK,     // Stack tecnológico
    BEST_PRACTICES,       // Mejores prácticas
    COMPLIANCE,           // Cumplimiento normativo
    SECURITY              // Seguridad del sistema
}

public enum DifficultyLevel {
    BEGINNER,             // Principiante
    INTERMEDIATE,         // Intermedio
    ADVANCED,             // Avanzado
    EXPERT                // Experto
}
```

### 2. Lesson

```java
@Entity
@Table(name = "trn_lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId; // Referencia a courses.id

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "lesson_number")
    private Integer lessonNumber; // Orden en el curso

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_url")
    private String contentUrl; // URL del contenido (video, documento, etc.)

    @Column(name = "content_text")
    private String contentText; // Contenido en texto

    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonResource> resources = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonProgress> progress = new ArrayList<>();
}

public enum ContentType {
    VIDEO,                // Video tutorial
    TEXT,                 // Texto con formato
    INTERACTIVE,          // Contenido interactivo
    QUIZ,                 // Cuestionario
    PRACTICAL_EXERCISE,   // Ejercicio práctico
    DOCUMENT,             // Documento PDF/Word
    PRESENTATION,         // Presentación
    AUDIO                 // Audio/podcast
}
```

### 3. CourseEnrollment

```java
@Entity
@Table(name = "course_enrollments")
public class CourseEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId; // Referencia a courses.id

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "progress_percentage")
    private Double progressPercentage = 0.0;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(name = "certificate_issued")
    private Boolean certificateIssued = false;

    @Column(name = "certificate_id")
    private String certificateId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum EnrollmentStatus {
    ENROLLED,             // Inscrito
    IN_PROGRESS,          // En progreso
    COMPLETED,            // Completado
    FAILED,               // Fallido
    DROPPED,              // Abandonado
    CERTIFIED             // Certificado
}
```

### 4. LessonProgress

```java
@Entity
@Table(name = "lesson_progress")
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_id")
    private Long lessonId; // Referencia a lessons.id

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "score")
    private Double score; // Puntuación si hay evaluación

    @Column(name = "attempts")
    private Integer attempts = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 5. Assessment

```java
@Entity
@Table(name = "assessments")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId; // Referencia a courses.id

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "assessment_type")
    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    @Column(name = "passing_score")
    private Double passingScore = 70.0;

    @Column(name = "max_attempts")
    private Integer maxAttempts = 3;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private List<AssessmentQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private List<AssessmentAttempt> attempts = new ArrayList<>();
}

public enum AssessmentType {
    QUIZ,                 // Cuestionario simple
    EXAM,                 // Examen formal
    PRACTICAL,            // Evaluación práctica
    PROJECT,              // Proyecto final
    PEER_REVIEW,          // Revisión entre pares
    SELF_ASSESSMENT       // Autoevaluación
}
```

### 6. AssessmentQuestion

```java
@Entity
@Table(name = "assessment_questions")
public class AssessmentQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id")
    private Long assessmentId; // Referencia a assessments.id

    @Column(nullable = false)
    private String question;

    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(name = "options")
    private String options; // JSON array para opciones múltiples

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "points")
    private Double points = 1.0;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum QuestionType {
    MULTIPLE_CHOICE,      // Opción múltiple
    TRUE_FALSE,           // Verdadero/Falso
    FILL_BLANK,           // Completar espacios
    SHORT_ANSWER,         // Respuesta corta
    ESSAY,                // Ensayo
    MATCHING,             // Relacionar
    ORDERING              // Ordenar elementos
}
```

### 7. AssessmentAttempt

```java
@Entity
@Table(name = "assessment_attempts")
public class AssessmentAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id")
    private Long assessmentId; // Referencia a assessments.id

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes;

    @Column(name = "score")
    private Double score;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "answers")
    private String answers; // JSON con respuestas del usuario

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 8. Certificate

```java
@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId; // Referencia a courses.id

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "certificate_number")
    private String certificateNumber; // Número único del certificado

    @Column(name = "issued_date")
    private LocalDateTime issuedDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "certificate_url")
    private String certificateUrl; // URL del PDF del certificado

    @Column(name = "is_valid")
    private Boolean isValid = true;

    @Column(name = "revoked_reason")
    private String revokedReason;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

## API Endpoints

### Course Management

```
GET    /api/v1/training/courses
GET    /api/v1/training/courses/{id}
POST   /api/v1/training/courses
PUT    /api/v1/training/courses/{id}
DELETE /api/v1/training/courses/{id}
GET    /api/v1/training/courses/category/{category}
GET    /api/v1/training/courses/difficulty/{level}
GET    /api/v1/training/courses/mandatory
```

### Lesson Management

```
GET    /api/v1/training/courses/{courseId}/lessons
GET    /api/v1/training/lessons/{id}
POST   /api/v1/training/lessons
PUT    /api/v1/training/lessons/{id}
DELETE /api/v1/training/lessons/{id}
GET    /api/v1/training/lessons/{id}/resources
```

### Enrollment Management

```
GET    /api/v1/training/enrollments
GET    /api/v1/training/enrollments/user/{userId}
GET    /api/v1/training/enrollments/course/{courseId}
POST   /api/v1/training/enrollments
PUT    /api/v1/training/enrollments/{id}
DELETE /api/v1/training/enrollments/{id}
POST   /api/v1/training/enrollments/{id}/complete
```

### Assessment Management

```
GET    /api/v1/training/assessments
GET    /api/v1/training/assessments/{id}
POST   /api/v1/training/assessments
PUT    /api/v1/training/assessments/{id}
DELETE /api/v1/training/assessments/{id}
GET    /api/v1/training/assessments/course/{courseId}
POST   /api/v1/training/assessments/{id}/submit
```

### Progress Tracking

```
GET    /api/v1/training/progress/user/{userId}
GET    /api/v1/training/progress/course/{courseId}
GET    /api/v1/training/progress/lesson/{lessonId}
GET    /api/v1/training/progress/department/{departmentId}
GET    /api/v1/training/progress/reports
```

### Certificate Management

```
GET    /api/v1/training/certificates
GET    /api/v1/training/certificates/{id}
GET    /api/v1/training/certificates/user/{userId}
GET    /api/v1/training/certificates/course/{courseId}
POST   /api/v1/training/certificates/generate
POST   /api/v1/training/certificates/{id}/revoke
GET    /api/v1/training/certificates/{id}/download
```

## Scripts de Base de Datos

### Script de Creación de Tablas

```sql
-- Tabla de cursos
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    course_code VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(50) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    estimated_hours INTEGER,
    is_mandatory BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla courses
COMMENT ON TABLE courses IS 'Tabla que almacena los cursos de formación del sistema';
COMMENT ON COLUMN courses.id IS 'Identificador único del curso';
COMMENT ON COLUMN courses.title IS 'Título del curso';
COMMENT ON COLUMN courses.description IS 'Descripción detallada del curso';
COMMENT ON COLUMN courses.course_code IS 'Código único del curso';
COMMENT ON COLUMN courses.category IS 'Categoría del curso (SYSTEM_BASICS, AI_FUNDAMENTALS, etc.)';
COMMENT ON COLUMN courses.difficulty_level IS 'Nivel de dificultad (BEGINNER, INTERMEDIATE, ADVANCED, EXPERT)';
COMMENT ON COLUMN courses.estimated_hours IS 'Horas estimadas para completar el curso';
COMMENT ON COLUMN courses.is_mandatory IS 'Indica si el curso es obligatorio';
COMMENT ON COLUMN courses.is_active IS 'Indica si el curso está activo';
COMMENT ON COLUMN courses.created_by IS 'ID del usuario que creó el curso';
COMMENT ON COLUMN courses.created_at IS 'Fecha y hora de creación del curso';
COMMENT ON COLUMN courses.updated_at IS 'Fecha y hora de la última actualización del curso';

-- Tabla de lecciones
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lesson_number INTEGER NOT NULL,
    content_type VARCHAR(30) NOT NULL,
    content_url VARCHAR(500),
    content_text TEXT,
    estimated_minutes INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla lessons
COMMENT ON TABLE lessons IS 'Tabla que almacena las lecciones de cada curso';
COMMENT ON COLUMN lessons.id IS 'Identificador único de la lección';
COMMENT ON COLUMN lessons.course_id IS 'ID del curso al que pertenece la lección';
COMMENT ON COLUMN lessons.title IS 'Título de la lección';
COMMENT ON COLUMN lessons.description IS 'Descripción de la lección';
COMMENT ON COLUMN lessons.lesson_number IS 'Número de orden de la lección en el curso';
COMMENT ON COLUMN lessons.content_type IS 'Tipo de contenido (VIDEO, TEXT, INTERACTIVE, etc.)';
COMMENT ON COLUMN lessons.content_url IS 'URL del contenido multimedia';
COMMENT ON COLUMN lessons.content_text IS 'Contenido en texto de la lección';
COMMENT ON COLUMN lessons.estimated_minutes IS 'Minutos estimados para completar la lección';
COMMENT ON COLUMN lessons.is_active IS 'Indica si la lección está activa';
COMMENT ON COLUMN lessons.created_at IS 'Fecha y hora de creación de la lección';
COMMENT ON COLUMN lessons.updated_at IS 'Fecha y hora de la última actualización de la lección';

-- Tabla de inscripciones a cursos
CREATE TABLE course_enrollments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ENROLLED',
    certificate_issued BOOLEAN DEFAULT false,
    certificate_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla course_enrollments
COMMENT ON TABLE course_enrollments IS 'Tabla que gestiona las inscripciones de usuarios a cursos';
COMMENT ON COLUMN course_enrollments.id IS 'Identificador único de la inscripción';
COMMENT ON COLUMN course_enrollments.course_id IS 'ID del curso';
COMMENT ON COLUMN course_enrollments.user_id IS 'ID del usuario inscrito';
COMMENT ON COLUMN course_enrollments.enrollment_date IS 'Fecha de inscripción al curso';
COMMENT ON COLUMN course_enrollments.completion_date IS 'Fecha de finalización del curso';
COMMENT ON COLUMN course_enrollments.progress_percentage IS 'Porcentaje de progreso en el curso';
COMMENT ON COLUMN course_enrollments.status IS 'Estado de la inscripción (ENROLLED, IN_PROGRESS, COMPLETED, etc.)';
COMMENT ON COLUMN course_enrollments.certificate_issued IS 'Indica si se ha emitido el certificado';
COMMENT ON COLUMN course_enrollments.certificate_id IS 'ID del certificado emitido';
COMMENT ON COLUMN course_enrollments.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN course_enrollments.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de progreso de lecciones
CREATE TABLE lesson_progress (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    time_spent_minutes INTEGER,
    is_completed BOOLEAN DEFAULT false,
    score DECIMAL(5,2),
    attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla lesson_progress
COMMENT ON TABLE lesson_progress IS 'Tabla que registra el progreso de los usuarios en cada lección';
COMMENT ON COLUMN lesson_progress.id IS 'Identificador único del progreso';
COMMENT ON COLUMN lesson_progress.lesson_id IS 'ID de la lección';
COMMENT ON COLUMN lesson_progress.user_id IS 'ID del usuario';
COMMENT ON COLUMN lesson_progress.started_at IS 'Fecha y hora de inicio de la lección';
COMMENT ON COLUMN lesson_progress.completed_at IS 'Fecha y hora de finalización de la lección';
COMMENT ON COLUMN lesson_progress.time_spent_minutes IS 'Tiempo dedicado a la lección en minutos';
COMMENT ON COLUMN lesson_progress.is_completed IS 'Indica si la lección ha sido completada';
COMMENT ON COLUMN lesson_progress.score IS 'Puntuación obtenida en la lección';
COMMENT ON COLUMN lesson_progress.attempts IS 'Número de intentos realizados';
COMMENT ON COLUMN lesson_progress.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN lesson_progress.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de evaluaciones
CREATE TABLE assessments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assessment_type VARCHAR(20) NOT NULL,
    passing_score DECIMAL(5,2) DEFAULT 70.00,
    max_attempts INTEGER DEFAULT 3,
    time_limit_minutes INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla assessments
COMMENT ON TABLE assessments IS 'Tabla que almacena las evaluaciones de los cursos';
COMMENT ON COLUMN assessments.id IS 'Identificador único de la evaluación';
COMMENT ON COLUMN assessments.course_id IS 'ID del curso al que pertenece la evaluación';
COMMENT ON COLUMN assessments.title IS 'Título de la evaluación';
COMMENT ON COLUMN assessments.description IS 'Descripción de la evaluación';
COMMENT ON COLUMN assessments.assessment_type IS 'Tipo de evaluación (QUIZ, EXAM, PRACTICAL, etc.)';
COMMENT ON COLUMN assessments.passing_score IS 'Puntuación mínima para aprobar';
COMMENT ON COLUMN assessments.max_attempts IS 'Número máximo de intentos permitidos';
COMMENT ON COLUMN assessments.time_limit_minutes IS 'Límite de tiempo en minutos';
COMMENT ON COLUMN assessments.is_active IS 'Indica si la evaluación está activa';
COMMENT ON COLUMN assessments.created_at IS 'Fecha y hora de creación de la evaluación';
COMMENT ON COLUMN assessments.updated_at IS 'Fecha y hora de la última actualización de la evaluación';

-- Tabla de preguntas de evaluación
CREATE TABLE assessment_questions (
    id BIGSERIAL PRIMARY KEY,
    assessment_id BIGINT REFERENCES assessments(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    options TEXT, -- JSON array para opciones múltiples
    correct_answer TEXT,
    points DECIMAL(5,2) DEFAULT 1.00,
    order_index INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla assessment_questions
COMMENT ON TABLE assessment_questions IS 'Tabla que almacena las preguntas de cada evaluación';
COMMENT ON COLUMN assessment_questions.id IS 'Identificador único de la pregunta';
COMMENT ON COLUMN assessment_questions.assessment_id IS 'ID de la evaluación a la que pertenece';
COMMENT ON COLUMN assessment_questions.question IS 'Texto de la pregunta';
COMMENT ON COLUMN assessment_questions.question_type IS 'Tipo de pregunta (MULTIPLE_CHOICE, TRUE_FALSE, etc.)';
COMMENT ON COLUMN assessment_questions.options IS 'Opciones de respuesta en formato JSON';
COMMENT ON COLUMN assessment_questions.correct_answer IS 'Respuesta correcta';
COMMENT ON COLUMN assessment_questions.points IS 'Puntos que vale la pregunta';
COMMENT ON COLUMN assessment_questions.order_index IS 'Orden de la pregunta en la evaluación';
COMMENT ON COLUMN assessment_questions.is_active IS 'Indica si la pregunta está activa';
COMMENT ON COLUMN assessment_questions.created_at IS 'Fecha y hora de creación de la pregunta';
COMMENT ON COLUMN assessment_questions.updated_at IS 'Fecha y hora de la última actualización de la pregunta';

-- Tabla de intentos de evaluación
CREATE TABLE assessment_attempts (
    id BIGSERIAL PRIMARY KEY,
    assessment_id BIGINT REFERENCES assessments(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    attempt_number INTEGER NOT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    time_spent_minutes INTEGER,
    score DECIMAL(5,2),
    is_passed BOOLEAN,
    answers TEXT, -- JSON con respuestas del usuario
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla assessment_attempts
COMMENT ON TABLE assessment_attempts IS 'Tabla que registra los intentos de evaluación de los usuarios';
COMMENT ON COLUMN assessment_attempts.id IS 'Identificador único del intento';
COMMENT ON COLUMN assessment_attempts.assessment_id IS 'ID de la evaluación';
COMMENT ON COLUMN assessment_attempts.user_id IS 'ID del usuario';
COMMENT ON COLUMN assessment_attempts.attempt_number IS 'Número del intento';
COMMENT ON COLUMN assessment_attempts.started_at IS 'Fecha y hora de inicio del intento';
COMMENT ON COLUMN assessment_attempts.completed_at IS 'Fecha y hora de finalización del intento';
COMMENT ON COLUMN assessment_attempts.time_spent_minutes IS 'Tiempo dedicado al intento en minutos';
COMMENT ON COLUMN assessment_attempts.score IS 'Puntuación obtenida';
COMMENT ON COLUMN assessment_attempts.is_passed IS 'Indica si el intento fue aprobado';
COMMENT ON COLUMN assessment_attempts.answers IS 'Respuestas del usuario en formato JSON';
COMMENT ON COLUMN assessment_attempts.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN assessment_attempts.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de certificados
CREATE TABLE certificates (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    certificate_number VARCHAR(100) UNIQUE NOT NULL,
    issued_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP,
    certificate_url VARCHAR(500),
    is_valid BOOLEAN DEFAULT true,
    revoked_reason TEXT,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla certificates
COMMENT ON TABLE certificates IS 'Tabla que almacena los certificados emitidos a los usuarios';
COMMENT ON COLUMN certificates.id IS 'Identificador único del certificado';
COMMENT ON COLUMN certificates.course_id IS 'ID del curso para el que se emitió el certificado';
COMMENT ON COLUMN certificates.user_id IS 'ID del usuario que recibió el certificado';
COMMENT ON COLUMN certificates.certificate_number IS 'Número único del certificado';
COMMENT ON COLUMN certificates.issued_date IS 'Fecha de emisión del certificado';
COMMENT ON COLUMN certificates.expiry_date IS 'Fecha de expiración del certificado';
COMMENT ON COLUMN certificates.certificate_url IS 'URL del PDF del certificado';
COMMENT ON COLUMN certificates.is_valid IS 'Indica si el certificado es válido';
COMMENT ON COLUMN certificates.revoked_reason IS 'Razón de revocación del certificado';
COMMENT ON COLUMN certificates.revoked_at IS 'Fecha de revocación del certificado';
COMMENT ON COLUMN certificates.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN certificates.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de relación curso-rol
CREATE TABLE course_roles (
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (course_id, role_id)
);

-- Comentarios de la tabla course_roles
COMMENT ON TABLE course_roles IS 'Tabla que relaciona cursos con roles requeridos';
COMMENT ON COLUMN course_roles.course_id IS 'ID del curso';
COMMENT ON COLUMN course_roles.role_id IS 'ID del rol requerido';

-- Índices para optimización
CREATE INDEX idx_courses_code ON courses(course_code);
CREATE INDEX idx_courses_category ON courses(category);
CREATE INDEX idx_courses_difficulty ON courses(difficulty_level);
CREATE INDEX idx_courses_active ON courses(is_active);
CREATE INDEX idx_lessons_course_id ON lessons(course_id);
CREATE INDEX idx_lessons_number ON lessons(lesson_number);
CREATE INDEX idx_enrollments_course_id ON course_enrollments(course_id);
CREATE INDEX idx_enrollments_user_id ON course_enrollments(user_id);
CREATE INDEX idx_enrollments_status ON course_enrollments(status);
CREATE INDEX idx_progress_lesson_id ON lesson_progress(lesson_id);
CREATE INDEX idx_progress_user_id ON lesson_progress(user_id);
CREATE INDEX idx_progress_completed ON lesson_progress(is_completed);
CREATE INDEX idx_assessments_course_id ON assessments(course_id);
CREATE INDEX idx_assessments_type ON assessments(assessment_type);
CREATE INDEX idx_questions_assessment_id ON assessment_questions(assessment_id);
CREATE INDEX idx_attempts_assessment_id ON assessment_attempts(assessment_id);
CREATE INDEX idx_attempts_user_id ON assessment_attempts(user_id);
CREATE INDEX idx_certificates_course_id ON certificates(course_id);
CREATE INDEX idx_certificates_user_id ON certificates(user_id);
CREATE INDEX idx_certificates_number ON certificates(certificate_number);
CREATE INDEX idx_certificates_valid ON certificates(is_valid);
```

## Scripts SQL Completos

### Script de DROP de Tablas

```sql
-- Script de eliminación de tablas (en orden inverso a la creación)
DROP TABLE IF EXISTS trn_course_roles CASCADE;
DROP TABLE IF EXISTS trn_certificates CASCADE;
DROP TABLE IF EXISTS trn_assessment_attempts CASCADE;
DROP TABLE IF EXISTS trn_assessment_questions CASCADE;
DROP TABLE IF EXISTS trn_assessments CASCADE;
DROP TABLE IF EXISTS trn_lesson_progress CASCADE;
DROP TABLE IF EXISTS trn_course_enrollments CASCADE;
DROP TABLE IF EXISTS trn_lessons CASCADE;
DROP TABLE IF EXISTS trn_courses CASCADE;
```

### Script de Creación de Tablas

```sql
-- Tabla de cursos
CREATE TABLE trn_courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    course_code VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(50) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    estimated_hours INTEGER,
    is_mandatory BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_courses
COMMENT ON TABLE trn_courses IS 'Tabla que almacena los cursos de formación del sistema';
COMMENT ON COLUMN trn_courses.id IS 'Identificador único del curso';
COMMENT ON COLUMN trn_courses.title IS 'Título del curso';
COMMENT ON COLUMN trn_courses.description IS 'Descripción detallada del curso';
COMMENT ON COLUMN trn_courses.course_code IS 'Código único del curso';
COMMENT ON COLUMN trn_courses.category IS 'Categoría del curso (SYSTEM_BASICS, AI_FUNDAMENTALS, etc.)';
COMMENT ON COLUMN trn_courses.difficulty_level IS 'Nivel de dificultad (BEGINNER, INTERMEDIATE, ADVANCED, EXPERT)';
COMMENT ON COLUMN trn_courses.estimated_hours IS 'Horas estimadas para completar el curso';
COMMENT ON COLUMN trn_courses.is_mandatory IS 'Indica si el curso es obligatorio';
COMMENT ON COLUMN trn_courses.is_active IS 'Indica si el curso está activo';
COMMENT ON COLUMN trn_courses.created_by IS 'ID del usuario que creó el curso';
COMMENT ON COLUMN trn_courses.created_at IS 'Fecha y hora de creación del curso';
COMMENT ON COLUMN trn_courses.updated_at IS 'Fecha y hora de la última actualización del curso';

-- Tabla de lecciones
CREATE TABLE trn_lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lesson_number INTEGER NOT NULL,
    content_type VARCHAR(30) NOT NULL,
    content_url VARCHAR(500),
    content_text TEXT,
    estimated_minutes INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_lessons
COMMENT ON TABLE trn_lessons IS 'Tabla que almacena las lecciones de cada curso';
COMMENT ON COLUMN trn_lessons.id IS 'Identificador único de la lección';
COMMENT ON COLUMN trn_lessons.course_id IS 'ID del curso al que pertenece la lección';
COMMENT ON COLUMN trn_lessons.title IS 'Título de la lección';
COMMENT ON COLUMN trn_lessons.description IS 'Descripción de la lección';
COMMENT ON COLUMN trn_lessons.lesson_number IS 'Número de orden de la lección en el curso';
COMMENT ON COLUMN trn_lessons.content_type IS 'Tipo de contenido (VIDEO, TEXT, INTERACTIVE, etc.)';
COMMENT ON COLUMN trn_lessons.content_url IS 'URL del contenido multimedia';
COMMENT ON COLUMN trn_lessons.content_text IS 'Contenido en texto de la lección';
COMMENT ON COLUMN trn_lessons.estimated_minutes IS 'Minutos estimados para completar la lección';
COMMENT ON COLUMN trn_lessons.is_active IS 'Indica si la lección está activa';
COMMENT ON COLUMN trn_lessons.created_at IS 'Fecha y hora de creación de la lección';
COMMENT ON COLUMN trn_lessons.updated_at IS 'Fecha y hora de la última actualización de la lección';

-- Tabla de inscripciones a cursos
CREATE TABLE trn_course_enrollments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ENROLLED',
    certificate_issued BOOLEAN DEFAULT false,
    certificate_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_course_enrollments
COMMENT ON TABLE trn_course_enrollments IS 'Tabla que gestiona las inscripciones de usuarios a cursos';
COMMENT ON COLUMN trn_course_enrollments.id IS 'Identificador único de la inscripción';
COMMENT ON COLUMN trn_course_enrollments.course_id IS 'ID del curso';
COMMENT ON COLUMN trn_course_enrollments.user_id IS 'ID del usuario inscrito';
COMMENT ON COLUMN trn_course_enrollments.enrollment_date IS 'Fecha de inscripción al curso';
COMMENT ON COLUMN trn_course_enrollments.completion_date IS 'Fecha de finalización del curso';
COMMENT ON COLUMN trn_course_enrollments.progress_percentage IS 'Porcentaje de progreso en el curso';
COMMENT ON COLUMN trn_course_enrollments.status IS 'Estado de la inscripción (ENROLLED, IN_PROGRESS, COMPLETED, etc.)';
COMMENT ON COLUMN trn_course_enrollments.certificate_issued IS 'Indica si se ha emitido el certificado';
COMMENT ON COLUMN trn_course_enrollments.certificate_id IS 'ID del certificado emitido';
COMMENT ON COLUMN trn_course_enrollments.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN trn_course_enrollments.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de progreso de lecciones
CREATE TABLE trn_lesson_progress (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT REFERENCES trn_lessons(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    time_spent_minutes INTEGER,
    is_completed BOOLEAN DEFAULT false,
    score DECIMAL(5,2),
    attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_lesson_progress
COMMENT ON TABLE trn_lesson_progress IS 'Tabla que registra el progreso de los usuarios en cada lección';
COMMENT ON COLUMN trn_lesson_progress.id IS 'Identificador único del progreso';
COMMENT ON COLUMN trn_lesson_progress.lesson_id IS 'ID de la lección';
COMMENT ON COLUMN trn_lesson_progress.user_id IS 'ID del usuario';
COMMENT ON COLUMN trn_lesson_progress.started_at IS 'Fecha y hora de inicio de la lección';
COMMENT ON COLUMN trn_lesson_progress.completed_at IS 'Fecha y hora de finalización de la lección';
COMMENT ON COLUMN trn_lesson_progress.time_spent_minutes IS 'Tiempo dedicado a la lección en minutos';
COMMENT ON COLUMN trn_lesson_progress.is_completed IS 'Indica si la lección ha sido completada';
COMMENT ON COLUMN trn_lesson_progress.score IS 'Puntuación obtenida en la lección';
COMMENT ON COLUMN trn_lesson_progress.attempts IS 'Número de intentos realizados';
COMMENT ON COLUMN trn_lesson_progress.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN trn_lesson_progress.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de evaluaciones
CREATE TABLE trn_assessments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assessment_type VARCHAR(20) NOT NULL,
    passing_score DECIMAL(5,2) DEFAULT 70.00,
    max_attempts INTEGER DEFAULT 3,
    time_limit_minutes INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_assessments
COMMENT ON TABLE trn_assessments IS 'Tabla que almacena las evaluaciones de los cursos';
COMMENT ON COLUMN trn_assessments.id IS 'Identificador único de la evaluación';
COMMENT ON COLUMN trn_assessments.course_id IS 'ID del curso al que pertenece la evaluación';
COMMENT ON COLUMN trn_assessments.title IS 'Título de la evaluación';
COMMENT ON COLUMN trn_assessments.description IS 'Descripción de la evaluación';
COMMENT ON COLUMN trn_assessments.assessment_type IS 'Tipo de evaluación (QUIZ, EXAM, PRACTICAL, etc.)';
COMMENT ON COLUMN trn_assessments.passing_score IS 'Puntuación mínima para aprobar';
COMMENT ON COLUMN trn_assessments.max_attempts IS 'Número máximo de intentos permitidos';
COMMENT ON COLUMN trn_assessments.time_limit_minutes IS 'Límite de tiempo en minutos';
COMMENT ON COLUMN trn_assessments.is_active IS 'Indica si la evaluación está activa';
COMMENT ON COLUMN trn_assessments.created_at IS 'Fecha y hora de creación de la evaluación';
COMMENT ON COLUMN trn_assessments.updated_at IS 'Fecha y hora de la última actualización de la evaluación';

-- Tabla de preguntas de evaluación
CREATE TABLE trn_assessment_questions (
    id BIGSERIAL PRIMARY KEY,
    assessment_id BIGINT REFERENCES trn_assessments(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    options TEXT, -- JSON array para opciones múltiples
    correct_answer TEXT,
    points DECIMAL(5,2) DEFAULT 1.00,
    order_index INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_assessment_questions
COMMENT ON TABLE trn_assessment_questions IS 'Tabla que almacena las preguntas de cada evaluación';
COMMENT ON COLUMN trn_assessment_questions.id IS 'Identificador único de la pregunta';
COMMENT ON COLUMN trn_assessment_questions.assessment_id IS 'ID de la evaluación a la que pertenece';
COMMENT ON COLUMN trn_assessment_questions.question IS 'Texto de la pregunta';
COMMENT ON COLUMN trn_assessment_questions.question_type IS 'Tipo de pregunta (MULTIPLE_CHOICE, TRUE_FALSE, etc.)';
COMMENT ON COLUMN trn_assessment_questions.options IS 'Opciones de respuesta en formato JSON';
COMMENT ON COLUMN trn_assessment_questions.correct_answer IS 'Respuesta correcta';
COMMENT ON COLUMN trn_assessment_questions.points IS 'Puntos que vale la pregunta';
COMMENT ON COLUMN trn_assessment_questions.order_index IS 'Orden de la pregunta en la evaluación';
COMMENT ON COLUMN trn_assessment_questions.is_active IS 'Indica si la pregunta está activa';
COMMENT ON COLUMN trn_assessment_questions.created_at IS 'Fecha y hora de creación de la pregunta';
COMMENT ON COLUMN trn_assessment_questions.updated_at IS 'Fecha y hora de la última actualización de la pregunta';

-- Tabla de intentos de evaluación
CREATE TABLE trn_assessment_attempts (
    id BIGSERIAL PRIMARY KEY,
    assessment_id BIGINT REFERENCES trn_assessments(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    attempt_number INTEGER NOT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    time_spent_minutes INTEGER,
    score DECIMAL(5,2),
    is_passed BOOLEAN,
    answers TEXT, -- JSON con respuestas del usuario
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_assessment_attempts
COMMENT ON TABLE trn_assessment_attempts IS 'Tabla que registra los intentos de evaluación de los usuarios';
COMMENT ON COLUMN trn_assessment_attempts.id IS 'Identificador único del intento';
COMMENT ON COLUMN trn_assessment_attempts.assessment_id IS 'ID de la evaluación';
COMMENT ON COLUMN trn_assessment_attempts.user_id IS 'ID del usuario';
COMMENT ON COLUMN trn_assessment_attempts.attempt_number IS 'Número del intento';
COMMENT ON COLUMN trn_assessment_attempts.started_at IS 'Fecha y hora de inicio del intento';
COMMENT ON COLUMN trn_assessment_attempts.completed_at IS 'Fecha y hora de finalización del intento';
COMMENT ON COLUMN trn_assessment_attempts.time_spent_minutes IS 'Tiempo dedicado al intento en minutos';
COMMENT ON COLUMN trn_assessment_attempts.score IS 'Puntuación obtenida';
COMMENT ON COLUMN trn_assessment_attempts.is_passed IS 'Indica si el intento fue aprobado';
COMMENT ON COLUMN trn_assessment_attempts.answers IS 'Respuestas del usuario en formato JSON';
COMMENT ON COLUMN trn_assessment_attempts.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN trn_assessment_attempts.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de certificados
CREATE TABLE trn_certificates (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    certificate_number VARCHAR(100) UNIQUE NOT NULL,
    issued_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP,
    certificate_url VARCHAR(500),
    is_valid BOOLEAN DEFAULT true,
    revoked_reason TEXT,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla trn_certificates
COMMENT ON TABLE trn_certificates IS 'Tabla que almacena los certificados emitidos a los usuarios';
COMMENT ON COLUMN trn_certificates.id IS 'Identificador único del certificado';
COMMENT ON COLUMN trn_certificates.course_id IS 'ID del curso para el que se emitió el certificado';
COMMENT ON COLUMN trn_certificates.user_id IS 'ID del usuario que recibió el certificado';
COMMENT ON COLUMN trn_certificates.certificate_number IS 'Número único del certificado';
COMMENT ON COLUMN trn_certificates.issued_date IS 'Fecha de emisión del certificado';
COMMENT ON COLUMN trn_certificates.expiry_date IS 'Fecha de expiración del certificado';
COMMENT ON COLUMN trn_certificates.certificate_url IS 'URL del PDF del certificado';
COMMENT ON COLUMN trn_certificates.is_valid IS 'Indica si el certificado es válido';
COMMENT ON COLUMN trn_certificates.revoked_reason IS 'Razón de revocación del certificado';
COMMENT ON COLUMN trn_certificates.revoked_at IS 'Fecha de revocación del certificado';
COMMENT ON COLUMN trn_certificates.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN trn_certificates.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de relación curso-rol
CREATE TABLE trn_course_roles (
    course_id BIGINT REFERENCES trn_courses(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES cor_roles(id) ON DELETE CASCADE,
    PRIMARY KEY (course_id, role_id)
);

-- Comentarios de la tabla trn_course_roles
COMMENT ON TABLE trn_course_roles IS 'Tabla que relaciona cursos con roles requeridos';
COMMENT ON COLUMN trn_course_roles.course_id IS 'ID del curso';
COMMENT ON COLUMN trn_course_roles.role_id IS 'ID del rol requerido';
```

### Script de Creación de Índices

```sql
-- Índices para optimización
CREATE INDEX idx_trn_courses_code ON trn_courses(course_code);
CREATE INDEX idx_trn_courses_category ON trn_courses(category);
CREATE INDEX idx_trn_courses_difficulty ON trn_courses(difficulty_level);
CREATE INDEX idx_trn_courses_active ON trn_courses(is_active);
CREATE INDEX idx_trn_lessons_course_id ON trn_lessons(course_id);
CREATE INDEX idx_trn_lessons_number ON trn_lessons(lesson_number);
CREATE INDEX idx_trn_enrollments_course_id ON trn_course_enrollments(course_id);
CREATE INDEX idx_trn_enrollments_user_id ON trn_course_enrollments(user_id);
CREATE INDEX idx_trn_enrollments_status ON trn_course_enrollments(status);
CREATE INDEX idx_trn_progress_lesson_id ON trn_lesson_progress(lesson_id);
CREATE INDEX idx_trn_progress_user_id ON trn_lesson_progress(user_id);
CREATE INDEX idx_trn_progress_completed ON trn_lesson_progress(is_completed);
CREATE INDEX idx_trn_assessments_course_id ON trn_assessments(course_id);
CREATE INDEX idx_trn_assessments_type ON trn_assessments(assessment_type);
CREATE INDEX idx_trn_questions_assessment_id ON trn_assessment_questions(assessment_id);
CREATE INDEX idx_trn_attempts_assessment_id ON trn_assessment_attempts(assessment_id);
CREATE INDEX idx_trn_attempts_user_id ON trn_assessment_attempts(user_id);
CREATE INDEX idx_trn_certificates_course_id ON trn_certificates(course_id);
CREATE INDEX idx_trn_certificates_user_id ON trn_certificates(user_id);
CREATE INDEX idx_trn_certificates_number ON trn_certificates(certificate_number);
CREATE INDEX idx_trn_certificates_valid ON trn_certificates(is_valid);
```

### Script de Datos Demo

```sql
-- Datos demo para cursos
INSERT INTO trn_courses (title, description, course_code, category, difficulty_level, estimated_hours, is_mandatory, created_by) VALUES
('Fundamentos de CodeflowX', 'Curso introductorio a la plataforma CodeflowX', 'CFX_FUNDAMENTALS_001', 'SYSTEM_BASICS', 'BEGINNER', 8, true, 1),
('Gestión de Agentes IA', 'Aprende a crear y gestionar agentes de IA', 'AI_AGENTS_001', 'AI_FUNDAMENTALS', 'INTERMEDIATE', 12, false, 1),
('Sistema RAG Avanzado', 'Implementación avanzada de RAG en CodeflowX', 'RAG_ADVANCED_001', 'RAG_SYSTEM', 'ADVANCED', 16, false, 1),
('Gobierno y Cumplimiento', 'Políticas de gobierno y cumplimiento normativo', 'GOVERNANCE_001', 'GOVERNANCE', 'INTERMEDIATE', 10, true, 1),
('Desarrollo de Notebooks', 'Creación de notebooks interactivos para IA', 'NOTEBOOKS_001', 'NOTEBOOKS', 'INTERMEDIATE', 14, false, 1);

-- Datos demo para lecciones
INSERT INTO trn_lessons (course_id, title, description, lesson_number, content_type, estimated_minutes) VALUES
(1, 'Introducción a CodeflowX', 'Visión general de la plataforma', 1, 'VIDEO', 30),
(1, 'Navegación Básica', 'Cómo navegar por la interfaz', 2, 'INTERACTIVE', 45),
(1, 'Primer Proyecto', 'Crear tu primer proyecto', 3, 'PRACTICAL_EXERCISE', 60),
(2, 'Conceptos de Agentes IA', 'Fundamentos de los agentes de IA', 1, 'VIDEO', 40),
(2, 'Creación de Agentes', 'Paso a paso para crear agentes', 2, 'INTERACTIVE', 90),
(3, 'Arquitectura RAG', 'Componentes del sistema RAG', 1, 'TEXT', 60),
(3, 'Implementación Práctica', 'Ejercicios prácticos de RAG', 2, 'PRACTICAL_EXERCISE', 120),
(4, 'IA Act Europea', 'Marco normativo europeo', 1, 'DOCUMENT', 45),
(4, 'Políticas de Gobierno', 'Implementación de políticas', 2, 'INTERACTIVE', 75),
(5, 'Jupyter Kernel', 'Configuración del kernel', 1, 'VIDEO', 50),
(5, 'Celdas Interactivas', 'Trabajo con celdas de código', 2, 'INTERACTIVE', 80);

-- Datos demo para inscripciones
INSERT INTO trn_course_enrollments (course_id, user_id, status, progress_percentage) VALUES
(1, 1, 'COMPLETED', 100.00),
(1, 2, 'IN_PROGRESS', 75.00),
(2, 1, 'ENROLLED', 0.00),
(3, 3, 'IN_PROGRESS', 50.00),
(4, 1, 'COMPLETED', 100.00),
(5, 2, 'ENROLLED', 0.00);

-- Datos demo para evaluaciones
INSERT INTO trn_assessments (course_id, title, description, assessment_type, passing_score, max_attempts) VALUES
(1, 'Evaluación Final CodeflowX', 'Evaluación completa del curso', 'EXAM', 80.00, 2),
(2, 'Quiz Agentes IA', 'Cuestionario sobre conceptos básicos', 'QUIZ', 70.00, 3),
(3, 'Proyecto RAG', 'Implementación práctica de RAG', 'PROJECT', 85.00, 1),
(4, 'Test Cumplimiento', 'Evaluación de conocimientos normativos', 'EXAM', 90.00, 2),
(5, 'Notebook Final', 'Creación de notebook funcional', 'PROJECT', 80.00, 1);

-- Datos demo para preguntas de evaluación
INSERT INTO trn_assessment_questions (assessment_id, question, question_type, options, correct_answer, points, order_index) VALUES
(1, '¿Cuál es el objetivo principal de CodeflowX?', 'MULTIPLE_CHOICE', '["Plataforma de IA", "Sistema de gestión", "Herramienta de desarrollo", "Todas las anteriores"]', 'Todas las anteriores', 2.00, 1),
(1, '¿Qué significa RAG?', 'MULTIPLE_CHOICE', '["Retrieval Augmented Generation", "Random Access Generation", "Real-time AI Generation", "Remote Access Gateway"]', 'Retrieval Augmented Generation', 1.00, 2),
(2, 'Un agente de IA puede ser una herramienta', 'TRUE_FALSE', '["Verdadero", "Falso"]', 'Verdadero', 1.00, 1),
(2, '¿Qué framework se recomienda para agentes?', 'MULTIPLE_CHOICE', '["TensorFlow", "LangChain", "PyTorch", "Scikit-learn"]', 'LangChain', 2.00, 2),
(4, '¿Qué regula la IA Act Europea?', 'MULTIPLE_CHOICE', '["Solo modelos grandes", "Solo sistemas críticos", "Sistemas de IA de alto riesgo", "Todas las IA"]', 'Sistemas de IA de alto riesgo', 3.00, 1);

-- Datos demo para certificados
INSERT INTO trn_certificates (course_id, user_id, certificate_number, expiry_date) VALUES
(1, 1, 'CERT-CFX-001-2025', '2026-01-15 00:00:00'),
(4, 1, 'CERT-GOV-001-2025', '2026-01-15 00:00:00');

-- Datos demo para roles de curso
INSERT INTO trn_course_roles (course_id, role_id) VALUES
(1, 1), -- ADMIN puede acceder a fundamentos
(2, 2), -- DEVELOPER puede acceder a agentes IA
(3, 2), -- DEVELOPER puede acceder a RAG avanzado
(4, 1), -- ADMIN puede acceder a gobierno
(5, 2); -- DEVELOPER puede acceder a notebooks
```

## Servicios de User Training

### CourseService

```java
@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private AuditService auditService;

    public Course createCourse(CourseDto courseDto) {
        // Validar datos del curso
        validateCourseData(courseDto);

        // Generar código único del curso
        String courseCode = generateUniqueCourseCode(courseDto.getTitle());

        // Crear curso
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCourseCode(courseCode);
        course.setCategory(courseDto.getCategory());
        course.setDifficultyLevel(courseDto.getDifficultyLevel());
        course.setEstimatedHours(courseDto.getEstimatedHours());
        course.setIsMandatory(courseDto.getIsMandatory());
        course.setIsActive(true);
        course.setCreatedBy(getCurrentUserId());
        course.setCreatedAt(LocalDateTime.now());

        Course savedCourse = courseRepository.save(course);

        // Asignar roles requeridos si se especifican
        if (courseDto.getRequiredRoles() != null) {
            course.setRequiredRoles(courseDto.getRequiredRoles());
        }

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_COURSE",
            "COURSE",
            savedCourse.getId().toString()
        );

        return savedCourse;
    }

    public Lesson addLessonToCourse(Long courseId, LessonDto lessonDto) {
        // Validar que el curso existe
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // Añadir lección al curso
        Lesson lesson = lessonService.addLesson(courseId, lessonDto);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ADD_LESSON_TO_COURSE",
            "LESSON",
            lesson.getId().toString()
        );

        return lesson;
    }

    public CourseEnrollment enrollUserInCourse(Long courseId, Long userId) {
        // Validar que el curso existe
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // Validar que el usuario puede inscribirse
        validateUserEnrollment(courseId, userId);

        // Inscribir usuario en el curso
        CourseEnrollment enrollment = enrollmentService.enrollUser(courseId, userId);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "ENROLL_USER_IN_COURSE",
            "COURSE_ENROLLMENT",
            enrollment.getId().toString()
        );

        return enrollment;
    }

    private void validateCourseData(CourseDto courseDto) {
        if (courseDto.getTitle() == null || courseDto.getTitle().trim().isEmpty()) {
            throw new ValidationException("Course title is required");
        }

        if (courseDto.getCategory() == null) {
            throw new ValidationException("Course category is required");
        }

        if (courseDto.getDifficultyLevel() == null) {
            throw new ValidationException("Course difficulty level is required");
        }
    }

    private String generateUniqueCourseCode(String courseTitle) {
        // Generar código único basado en el título del curso
        String baseCode = courseTitle.toUpperCase().replaceAll("[^A-Z0-9]", "");
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        return baseCode + "_" + timestamp;
    }

    private void validateUserEnrollment(Long courseId, Long userId) {
        // Validar que el usuario no esté ya inscrito
        if (enrollmentService.isUserEnrolled(courseId, userId)) {
            throw new ValidationException("User is already enrolled in this course");
        }

        // Validar requisitos previos del curso
        // Implementar lógica de validación
    }

    private Long getCurrentUserId() {
        // Obtener ID del usuario actual
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        // Obtener username del usuario actual
        return "current_user"; // Placeholder
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class UserTrainingMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter coursesCreatedCounter;
    private final Counter lessonsCreatedCounter;
    private final Counter enrollmentsCounter;
    private final Counter assessmentsCompletedCounter;
    private final Counter certificatesIssuedCounter;
    private final Timer courseCompletionTimer;
    private final Timer assessmentCompletionTimer;

    public UserTrainingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.coursesCreatedCounter = Counter.builder("training.courses.created")
            .description("Total courses created")
            .register(meterRegistry);

        this.lessonsCreatedCounter = Counter.builder("training.lessons.created")
            .description("Total lessons created")
            .register(meterRegistry);

        this.enrollmentsCounter = Counter.builder("training.enrollments.total")
            .description("Total course enrollments")
            .register(meterRegistry);

        this.assessmentsCompletedCounter = Counter.builder("training.assessments.completed")
            .description("Total assessments completed")
            .register(meterRegistry);

        this.certificatesIssuedCounter = Counter.builder("training.certificates.issued")
            .description("Total certificates issued")
            .register(meterRegistry);

        this.courseCompletionTimer = Timer.builder("training.course.completion.time")
            .description("Course completion time")
            .register(meterRegistry);

        this.assessmentCompletionTimer = Timer.builder("training.assessment.completion.time")
            .description("Assessment completion time")
            .register(meterRegistry);
    }

    public void incrementCoursesCreated() {
        coursesCreatedCounter.increment();
    }

    public void incrementLessonsCreated() {
        lessonsCreatedCounter.increment();
    }

    public void incrementEnrollments() {
        enrollmentsCounter.increment();
    }

    public void incrementAssessmentsCompleted() {
        assessmentsCompletedCounter.increment();
    }

    public void incrementCertificatesIssued() {
        certificatesIssuedCounter.increment();
    }

    public Timer.Sample startCourseCompletionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startAssessmentCompletionTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-user-training.yml

```yaml
user-training:
  # Configuración de cursos
  courses:
    auto-code-generation: true
    code-prefix: "COURSE"
    max-title-length: 255
    default-difficulty: "INTERMEDIATE"
    auto-role-assignment: true

  # Configuración de lecciones
  lessons:
    auto-numbering: true
    max-content-size-mb: 50
    supported-formats: ["mp4", "pdf", "docx", "pptx", "mp3"]
    content-storage: "minio"

  # Configuración de evaluaciones
  assessments:
    auto-grading: true
    time-tracking: true
    attempt-limits: true
    plagiarism-detection: false
    max-questions-per-assessment: 100

  # Configuración de certificados
  certificates:
    auto-generation: true
    template-storage: "minio"
    validity-period-days: 365
    digital-signature: true
    revocation-support: true

  # Configuración de progreso
  progress:
    auto-tracking: true
    completion-threshold: 80
    time-tracking: true
    milestone-notifications: true

  # Configuración de notificaciones
  notifications:
    enrollment-confirmation: true
    completion-notification: true
    certificate-issuance: true
    reminder-emails: true
    reminder-frequency-days: 7
```

## Conclusión

El módulo de User Training proporciona un sistema completo de formación y capacitación, incluyendo:

- **Gestión de Cursos**: Creación, configuración y gestión del ciclo de vida de cursos
- **Lecciones Interactivas**: Contenido multimedia con seguimiento de progreso
- **Sistema de Evaluaciones**: Evaluaciones automáticas con múltiples tipos de preguntas
- **Gestión de Inscripciones**: Control de acceso y seguimiento de usuarios
- **Certificaciones**: Emisión automática de certificados con validez temporal
- **Seguimiento de Progreso**: Métricas detalladas de aprendizaje individual y grupal
- **Integración con Roles**: Acceso a cursos basado en roles del sistema
- **Reportes y Analytics**: Informes de formación por usuario, departamento y curso

El sistema está diseñado para ser escalable y flexible, permitiendo la creación de programas de formación personalizados según las necesidades de la organización.
