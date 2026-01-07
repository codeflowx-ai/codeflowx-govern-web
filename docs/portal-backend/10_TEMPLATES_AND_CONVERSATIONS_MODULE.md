## Módulo de Templates y Conversaciones (TMP)

## Descripción General

El módulo de **Templates y Conversaciones (TMP)** es el sistema centralizado para gestión de prompts, system prompts, templates de conversación y asistentes especializados. Este módulo unifica toda la funcionalidad de chat, generación de contenido (texto, imagen, audio) y gestión de conversaciones, permitiendo normalizar otros módulos como RAG y crear un sistema coherente.

### Características Principales

- **Gestión Centralizada de Prompts**: Templates reutilizables para diferentes tipos de contenido
- **System Prompts**: Configuración de personalidad y comportamiento de asistentes
- **Asistentes Especializados**: IA para texto, imagen, audio, video, código
- **Sistema de Conversaciones**: Almacenamiento y gestión de historiales de chat
- **Generación Multimodal**: Texto, imagen, audio, video, código
- **Integración con Agentes**: Conectividad con el módulo AGT
- **Gobernanza de Contenido**: Control de calidad y cumplimiento regulatorio

## Arquitectura del Sistema

### Componentes Principales

1.  **Prompt Template Manager**: Gestión de templates reutilizables
2.  **System Prompt Engine**: Configuración de personalidad de asistentes
3.  **Conversation Manager**: Gestión de conversaciones y historiales
4.  **Assistant Registry**: Catálogo de asistentes especializados
5.  **Content Generator**: Generación multimodal de contenido
6.  **Governance Integration**: Control de calidad y cumplimiento

### Integración con Otros Módulos

- **RAG Module**: Conversaciones con contexto de documentos
- **AGT Module**: Agentes que utilizan templates y conversaciones
- **Governance Module**: Control de calidad y cumplimiento
- **Model Management**: Modelos para generación de contenido

## Entidades del Sistema

### 1\. PromptTemplate

```java
@Entity
@Table(name = "tmp_prompt_templates")
public class PromptTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_type")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "template_content")
    private String templateContent; // Template con variables {{variable}}

    @Column(name = "variables")
    private String variables; // JSON array de variables disponibles

    @Column(name = "example_usage")
    private String exampleUsage;

    @Column(name = "version")
    private String version;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum TemplateType {
    TEXT_GENERATION, IMAGE_GENERATION, AUDIO_GENERATION, VIDEO_GENERATION,
    CODE_GENERATION, ANALYSIS, SUMMARIZATION, TRANSLATION, CUSTOM
}

public enum ApprovalStatus {
    DRAFT, REVIEW, APPROVED, REJECTED, ARCHIVED
}
```

### 2\. SystemPrompt

```java
@Entity
@Table(name = "tmp_system_prompts")
public class SystemPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prompt_name")
    private String promptName;

    @Column(name = "assistant_type")
    @Enumerated(EnumType.STRING)
    private AssistantType assistantType;

    @Column(name = "personality")
    private String personality; // JSON personality traits

    @Column(name = "behavior_rules")
    private String behaviorRules; // JSON behavior guidelines

    @Column(name = "knowledge_domains")
    private String knowledgeDomains; // JSON array of domains

    @Column(name = "capabilities")
    private String capabilities; // JSON array of capabilities

    @Column(name = "limitations")
    private String limitations; // JSON array of limitations

    @Column(name = "version")
    private String version;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum AssistantType {
    TEXT_ASSISTANT, IMAGE_ASSISTANT, AUDIO_ASSISTANT, VIDEO_ASSISTANT,
    CODE_ASSISTANT, ANALYSIS_ASSISTANT, RESEARCH_ASSISTANT, CUSTOM
}
```

### 3\. Assistant

```java
@Entity
@Table(name = "tmp_assistants")
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assistant_name")
    private String assistantName;

    @Column(name = "assistant_type")
    @Enumerated(EnumType.STRING)
    private AssistantType assistantType;

    @Column(name = "system_prompt_id")
    private Long systemPromptId;

    @Column(name = "description")
    private String description;

    @Column(name = "model_config")
    private String modelConfig; // JSON model configuration

    @Column(name = "capabilities")
    private String capabilities; // JSON array of capabilities

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_blocked")
    private Boolean isBlocked = false;

    @Column(name = "block_reason")
    private String blockReason;

    @Column(name = "blocked_by")
    private Long blockedBy;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 4\. Conversation

```java
@Entity
@Table(name = "tmp_conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private String conversationId; // UUID

    @Column(name = "assistant_id")
    private Long assistantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "project_id")
    private Long projectId; // Opcional, para conversaciones de proyecto

    @Column(name = "title")
    private String title;

    @Column(name = "conversation_type")
    @Enumerated(EnumType.STRING)
    private ConversationType conversationType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ConversationType {
    CHAT, ANALYSIS, GENERATION, REVIEW, SUPPORT, CUSTOM
}

public enum ConversationStatus {
    ACTIVE, PAUSED, COMPLETED, ARCHIVED
}
```

### 5\. ConversationMessage

```java
@Entity
@Table(name = "tmp_conversation_messages")
public class ConversationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "message_id")
    private String messageId; // UUID

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MessageRole role;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content")
    private String content; // Text content or JSON for other types

    @Column(name = "content_url")
    private String contentUrl; // URL to generated content (image, audio, etc.)

    @Column(name = "metadata")
    private String metadata; // JSON metadata

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Column(name = "model_used")
    private String modelUsed;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum MessageRole {
    USER, ASSISTANT, SYSTEM
}

public enum ContentType {
    TEXT, IMAGE, AUDIO, VIDEO, CODE, JSON, CUSTOM
}
```

### 6\. ContentGeneration

```java
@Entity
@Table(name = "tmp_content_generations")
public class ContentGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_message_id")
    private Long conversationMessageId;

    @Column(name = "generation_type")
    @Enumerated(EnumType.STRING)
    private GenerationType generationType;

    @Column(name = "prompt_used")
    private String promptUsed;

    @Column(name = "model_used")
    private String modelUsed;

    @Column(name = "generation_config")
    private String generationConfig; // JSON configuration

    @Column(name = "output_url")
    private String outputUrl;

    @Column(name = "output_metadata")
    private String outputMetadata; // JSON metadata

    @Column(name = "quality_score")
    private Double qualityScore;

    @Column(name = "compliance_status")
    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

public enum GenerationType {
    TEXT, IMAGE, AUDIO, VIDEO, CODE, DOCUMENT, CUSTOM
}

public enum ComplianceStatus {
    COMPLIANT, NON_COMPLIANT, UNDER_REVIEW, FLAGGED
}
```

### 7\. ConversationAnalytics

```java
@Entity
@Table(name = "tmp_conversation_analytics")
public class ConversationAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

## Scripts SQL

### DROP TABLES

```plaintext
-- Eliminar tablas en orden inverso (por dependencias)
DROP TABLE IF EXISTS tmp_conversation_analytics CASCADE;
DROP TABLE IF EXISTS tmp_content_generations CASCADE;
DROP TABLE IF EXISTS tmp_conversation_messages CASCADE;
DROP TABLE IF EXISTS tmp_conversations CASCADE;
DROP TABLE IF EXISTS tmp_assistants CASCADE;
DROP TABLE IF EXISTS tmp_system_prompts CASCADE;
DROP TABLE IF EXISTS tmp_prompt_templates CASCADE;
```

### CREATE TABLES

```plaintext
-- Tabla de templates de prompts
CREATE TABLE tmp_prompt_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(255) NOT NULL,
    template_type VARCHAR(50) NOT NULL,
    category VARCHAR(100),
    description TEXT,
    template_content TEXT NOT NULL,
    variables TEXT, -- JSON array de variables
    example_usage TEXT,
    version VARCHAR(50) DEFAULT '1.0.0',
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    approved_by BIGINT REFERENCES cor_users(id),
    approval_status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de system prompts
CREATE TABLE tmp_system_prompts (
    id BIGSERIAL PRIMARY KEY,
    prompt_name VARCHAR(255) NOT NULL,
    assistant_type VARCHAR(50) NOT NULL,
    personality TEXT, -- JSON personality traits
    behavior_rules TEXT, -- JSON behavior guidelines
    knowledge_domains TEXT, -- JSON array of domains
    capabilities TEXT, -- JSON array of capabilities
    limitations TEXT, -- JSON array of limitations
    version VARCHAR(50) DEFAULT '1.0.0',
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    approved_by BIGINT REFERENCES cor_users(id),
    approval_status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de asistentes
CREATE TABLE tmp_assistants (
    id BIGSERIAL PRIMARY KEY,
    assistant_name VARCHAR(255) NOT NULL,
    assistant_type VARCHAR(50) NOT NULL,
    system_prompt_id BIGINT REFERENCES tmp_system_prompts(id),
    description TEXT,
    model_config TEXT, -- JSON model configuration
    capabilities TEXT, -- JSON array of capabilities
    is_active BOOLEAN DEFAULT true,
    is_blocked BOOLEAN DEFAULT false,
    block_reason TEXT,
    blocked_by BIGINT REFERENCES cor_users(id),
    blocked_at TIMESTAMP,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de conversaciones
CREATE TABLE tmp_conversations (
    id BIGSERIAL PRIMARY KEY,
    conversation_id VARCHAR(255) UNIQUE NOT NULL, -- UUID
    assistant_id BIGINT REFERENCES tmp_assistants(id),
    user_id BIGINT REFERENCES cor_users(id),
    project_id BIGINT REFERENCES prj_projects(id),
    title VARCHAR(255),
    conversation_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    metadata TEXT, -- JSON metadata
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de mensajes de conversación
CREATE TABLE tmp_conversation_messages (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT REFERENCES tmp_conversations(id) ON DELETE CASCADE,
    message_id VARCHAR(255) UNIQUE NOT NULL, -- UUID
    role VARCHAR(20) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    content TEXT,
    content_url VARCHAR(500),
    metadata TEXT, -- JSON metadata
    tokens_used INTEGER,
    processing_time_ms BIGINT,
    model_used VARCHAR(255),
    confidence_score DECIMAL(5,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de generación de contenido
CREATE TABLE tmp_content_generations (
    id BIGSERIAL PRIMARY KEY,
    conversation_message_id BIGINT REFERENCES tmp_conversation_messages(id) ON DELETE CASCADE,
    generation_type VARCHAR(50) NOT NULL,
    prompt_used TEXT,
    model_used VARCHAR(255),
    generation_config TEXT, -- JSON configuration
    output_url VARCHAR(500),
    output_metadata TEXT, -- JSON metadata
    quality_score DECIMAL(5,4),
    compliance_status VARCHAR(50) DEFAULT 'UNDER_REVIEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de analytics de conversaciones
CREATE TABLE tmp_conversation_analytics (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT REFERENCES tmp_conversations(id) ON DELETE CASCADE,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DECIMAL(15,6),
    metric_unit VARCHAR(50),
    timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### CREATE INDEXES

```plaintext
-- Índices para optimización
CREATE INDEX idx_tmp_prompt_templates_type ON tmp_prompt_templates(template_type);
CREATE INDEX idx_tmp_prompt_templates_category ON tmp_prompt_templates(category);
CREATE INDEX idx_tmp_prompt_templates_status ON tmp_prompt_templates(approval_status);
CREATE INDEX idx_tmp_prompt_templates_active ON tmp_prompt_templates(is_active);

CREATE INDEX idx_tmp_system_prompts_type ON tmp_system_prompts(assistant_type);
CREATE INDEX idx_tmp_system_prompts_status ON tmp_system_prompts(approval_status);
CREATE INDEX idx_tmp_system_prompts_active ON tmp_system_prompts(is_active);

CREATE INDEX idx_tmp_assistants_type ON tmp_assistants(assistant_type);
CREATE INDEX idx_tmp_assistants_system_prompt ON tmp_assistants(system_prompt_id);
CREATE INDEX idx_tmp_assistants_active ON tmp_assistants(is_active);
CREATE INDEX idx_tmp_assistants_blocked ON tmp_assistants(is_blocked);

CREATE INDEX idx_tmp_conversations_assistant ON tmp_conversations(assistant_id);
CREATE INDEX idx_tmp_conversations_user ON tmp_conversations(user_id);
CREATE INDEX idx_tmp_conversations_project ON tmp_conversations(project_id);
CREATE INDEX idx_tmp_conversations_type ON tmp_conversations(conversation_type);
CREATE INDEX idx_tmp_conversations_status ON tmp_conversations(status);
CREATE INDEX idx_tmp_conversations_conversation_id ON tmp_conversations(conversation_id);

CREATE INDEX idx_tmp_conversation_messages_conversation ON tmp_conversation_messages(conversation_id);
CREATE INDEX idx_tmp_conversation_messages_role ON tmp_conversation_messages(role);
CREATE INDEX idx_tmp_conversation_messages_type ON tmp_conversation_messages(content_type);
CREATE INDEX idx_tmp_conversation_messages_message_id ON tmp_conversation_messages(message_id);

CREATE INDEX idx_tmp_content_generations_message ON tmp_content_generations(conversation_message_id);
CREATE INDEX idx_tmp_content_generations_type ON tmp_content_generations(generation_type);
CREATE INDEX idx_tmp_content_generations_compliance ON tmp_content_generations(compliance_status);

CREATE INDEX idx_tmp_conversation_analytics_conversation ON tmp_conversation_analytics(conversation_id);
CREATE INDEX idx_tmp_conversation_analytics_timestamp ON tmp_conversation_analytics(timestamp);
```

### INSERT DEMO DATA

```plaintext
-- Insertar templates de prompts de demostración
INSERT INTO tmp_prompt_templates (template_name, template_type, category, description, template_content, variables, example_usage, version, is_active, created_by, approval_status) VALUES
('TextAnalysis', 'TEXT_GENERATION', 'Analysis', 'Template para análisis de texto', 'Analiza el siguiente texto: {{text}}. Proporciona un resumen y los puntos clave.', '["text"]', '{"text": "Este es un ejemplo de texto para analizar"}', '1.0.0', true, 1, 'APPROVED'),
('ImageDescription', 'IMAGE_GENERATION', 'Vision', 'Template para descripción de imágenes', 'Describe detalladamente la imagen: {{image_url}}. Enfócate en {{focus_area}}.', '["image_url", "focus_area"]', '{"image_url": "https://example.com/image.jpg", "focus_area": "objetos principales"}', '1.0.0', true, 1, 'APPROVED'),
('CodeReview', 'CODE_GENERATION', 'Development', 'Template para revisión de código', 'Revisa el siguiente código {{language}}: {{code}}. Identifica problemas y sugiere mejoras.', '["language", "code"]', '{"language": "Python", "code": "def example(): pass"}', '1.0.0', true, 1, 'APPROVED');

-- Insertar system prompts de demostración
INSERT INTO tmp_system_prompts (prompt_name, assistant_type, personality, behavior_rules, knowledge_domains, capabilities, limitations, version, is_active, created_by, approval_status) VALUES
('TextAssistant', 'TEXT_ASSISTANT', '{"tone": "professional", "style": "clear", "expertise": "high"}', '["be respectful", "be accurate", "be helpful"]', '["general knowledge", "writing", "analysis"]', '["text generation", "summarization", "translation"]', '["no medical advice", "no legal advice"]', '1.0.0', true, 1, 'APPROVED'),
('ImageAssistant', 'IMAGE_ASSISTANT', '{"tone": "creative", "style": "descriptive", "expertise": "visual"}', '["be creative", "be detailed", "be appropriate"]', '["art", "design", "visual analysis"]', '["image generation", "image analysis", "design suggestions"]', '["no inappropriate content", "no copyrighted material"]', '1.0.0', true, 1, 'APPROVED'),
('CodeAssistant', 'CODE_ASSISTANT', '{"tone": "technical", "style": "precise", "expertise": "development"}', '["be precise", "be efficient", "be secure"]', '["programming", "software development", "algorithms"]', '["code generation", "code review", "debugging"]', '["no malicious code", "no proprietary algorithms"]', '1.0.0', true, 1, 'APPROVED');

-- Insertar asistentes de demostración
INSERT INTO tmp_assistants (assistant_name, assistant_type, system_prompt_id, description, model_config, capabilities, is_active, created_by) VALUES
('TextExpert', 'TEXT_ASSISTANT', 1, 'Asistente especializado en análisis y generación de texto', '{"model": "gpt-4", "temperature": 0.7, "max_tokens": 2000}', '["text_analysis", "summarization", "translation"]', true, 1),
('ImageCreator', 'IMAGE_ASSISTANT', 2, 'Asistente especializado en generación y análisis de imágenes', '{"model": "dall-e-3", "size": "1024x1024", "quality": "standard"}', '["image_generation", "image_analysis", "design"]', true, 1),
('CodeHelper', 'CODE_ASSISTANT', 3, 'Asistente especializado en desarrollo y revisión de código', '{"model": "gpt-4", "temperature": 0.3, "max_tokens": 4000}', '["code_generation", "code_review", "debugging"]', true, 1);
```

## API Endpoints

### Prompt Templates

```plaintext
GET    /api/v1/templates/prompts
GET    /api/v1/templates/prompts/{id}
POST   /api/v1/templates/prompts
PUT    /api/v1/templates/prompts/{id}
DELETE /api/v1/templates/prompts/{id}
GET    /api/v1/templates/prompts/type/{templateType}
GET    /api/v1/templates/prompts/category/{category}
POST   /api/v1/templates/prompts/{id}/approve
POST   /api/v1/templates/prompts/{id}/reject
GET    /api/v1/templates/prompts/search
```

### System Prompts

```plaintext
GET    /api/v1/templates/system-prompts
GET    /api/v1/templates/system-prompts/{id}
POST   /api/v1/templates/system-prompts
PUT    /api/v1/templates/system-prompts/{id}
DELETE /api/v1/templates/system-prompts/{id}
GET    /api/v1/templates/system-prompts/type/{assistantType}
POST   /api/v1/templates/system-prompts/{id}/approve
POST   /api/v1/templates/system-prompts/{id}/reject
GET    /api/v1/templates/system-prompts/search
```

### Assistants

```plaintext
GET    /api/v1/assistants
GET    /api/v1/assistants/{id}
POST   /api/v1/assistants
PUT    /api/v1/assistants/{id}
DELETE /api/v1/assistants/{id}
GET    /api/v1/assistants/type/{assistantType}
POST   /api/v1/assistants/{id}/block
POST   /api/v1/assistants/{id}/unblock
GET    /api/v1/assistants/search
GET    /api/v1/assistants/active
```

### Conversations

```plaintext
GET    /api/v1/conversations
GET    /api/v1/conversations/{id}
POST   /api/v1/conversations
PUT    /api/v1/conversations/{id}
DELETE /api/v1/conversations/{id}
GET    /api/v1/conversations/user/{userId}
GET    /api/v1/conversations/assistant/{assistantId}
GET    /api/v1/conversations/project/{projectId}
POST   /api/v1/conversations/{id}/start
POST   /api/v1/conversations/{id}/pause
POST   /api/v1/conversations/{id}/resume
POST   /api/v1/conversations/{id}/complete
```

### Conversation Messages

```plaintext
GET    /api/v1/conversations/{conversationId}/messages
GET    /api/v1/conversations/{conversationId}/messages/{id}
POST   /api/v1/conversations/{conversationId}/messages
PUT    /api/v1/conversations/{conversationId}/messages/{id}
DELETE /api/v1/conversations/{conversationId}/messages/{id}
GET    /api/v1/conversations/{conversationId}/messages/search
POST   /api/v1/conversations/{conversationId}/messages/stream
```

### Content Generation

```plaintext
POST   /api/v1/content/generate/text
POST   /api/v1/content/generate/image
POST   /api/v1/content/generate/audio
POST   /api/v1/content/generate/video
POST   /api/v1/content/generate/code
GET    /api/v1/content/generations/{id}
GET    /api/v1/content/generations/conversation/{conversationId}
POST   /api/v1/content/generations/{id}/review
```

### Analytics and Monitoring

```plaintext
GET    /api/v1/analytics/conversations/overview
GET    /api/v1/analytics/conversations/metrics
GET    /api/v1/analytics/assistants/performance
GET    /api/v1/analytics/templates/usage
GET    /api/v1/analytics/content/quality
GET    /api/v1/analytics/compliance/status
```

## Servicios Java

### PromptTemplateService

```java
@Service
public class PromptTemplateService {

    @Autowired
    private PromptTemplateRepository templateRepository;

    @Autowired
    private GovernanceService governanceService;

    public PromptTemplate createTemplate(PromptTemplateCreateRequest request) {
        PromptTemplate template = new PromptTemplate();
        template.setTemplateName(request.getTemplateName());
        template.setTemplateType(request.getTemplateType());
        template.setCategory(request.getCategory());
        template.setDescription(request.getDescription());
        template.setTemplateContent(request.getTemplateContent());
        template.setVariables(request.getVariables());
        template.setExampleUsage(request.getExampleUsage());
        template.setCreatedBy(getCurrentUserId());
        template.setApprovalStatus(ApprovalStatus.DRAFT);

        return templateRepository.save(template);
    }

    public PromptTemplate approveTemplate(Long templateId, String approvalNotes) {
        PromptTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -&gt; new TemplateNotFoundException("Template not found"));

        template.setApprovalStatus(ApprovalStatus.APPROVED);
        template.setApprovedBy(getCurrentUserId());
        template.setUpdatedAt(LocalDateTime.now());

        // Registrar en governance
        governanceService.logApproval("PROMPT_TEMPLATE", templateId, approvalNotes);

        return templateRepository.save(template);
    }

    public String renderTemplate(Long templateId, Map<string, object=""> variables) {
        PromptTemplate template = templateRepository.findById(templateId)
            .orElseThrow(() -&gt; new TemplateNotFoundException("Template not found"));

        if (template.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new TemplateNotApprovedException("Template not approved");
        }

        return renderTemplateContent(template.getTemplateContent(), variables);
    }

    private String renderTemplateContent(String content, Map<string, object=""> variables) {
        String result = content;
        for (Map.Entry<string, object=""> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}",
                String.valueOf(entry.getValue()));
        }
        return result;
    }
}
```

### AssistantService

```java
@Service
public class AssistantService {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private SystemPromptService systemPromptService;

    @Autowired
    private ContentGenerationService contentGenerationService;

    public Assistant createAssistant(AssistantCreateRequest request) {
        Assistant assistant = new Assistant();
        assistant.setAssistantName(request.getAssistantName());
        assistant.setAssistantType(request.getAssistantType());
        assistant.setSystemPromptId(request.getSystemPromptId());
        assistant.setDescription(request.getDescription());
        assistant.setModelConfig(request.getModelConfig());
        assistant.setCapabilities(request.getCapabilities());
        assistant.setCreatedBy(getCurrentUserId());

        return assistantRepository.save(assistant);
    }

    public Assistant blockAssistant(Long assistantId, String reason) {
        Assistant assistant = assistantRepository.findById(assistantId)
            .orElseThrow(() -&gt; new AssistantNotFoundException("Assistant not found"));

        assistant.setIsBlocked(true);
        assistant.setBlockReason(reason);
        assistant.setBlockedBy(getCurrentUserId());
        assistant.setBlockedAt(LocalDateTime.now());

        return assistantRepository.save(assistant);
    }

    public ContentGenerationResponse generateContent(Long assistantId,
                                                   String prompt,
                                                   ContentType contentType) {
        Assistant assistant = assistantRepository.findById(assistantId)
            .orElseThrow(() -&gt; new AssistantNotFoundException("Assistant not found"));

        if (assistant.getIsBlocked()) {
            throw new AssistantBlockedException("Assistant is blocked: " + assistant.getBlockReason());
        }

        return contentGenerationService.generateContent(assistant, prompt, contentType);
    }
}
```

### ConversationService

```java
@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationMessageRepository messageRepository;

    @Autowired
    private AssistantService assistantService;

    public Conversation startConversation(ConversationStartRequest request) {
        Conversation conversation = new Conversation();
        conversation.setConversationId(UUID.randomUUID().toString());
        conversation.setAssistantId(request.getAssistantId());
        conversation.setUserId(getCurrentUserId());
        conversation.setProjectId(request.getProjectId());
        conversation.setTitle(request.getTitle());
        conversation.setConversationType(request.getConversationType());
        conversation.setStatus(ConversationStatus.ACTIVE);

        return conversationRepository.save(conversation);
    }

    public ConversationMessage addMessage(Long conversationId, MessageCreateRequest request) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -&gt; new ConversationNotFoundException("Conversation not found"));

        ConversationMessage message = new ConversationMessage();
        message.setConversationId(conversationId);
        message.setMessageId(UUID.randomUUID().toString());
        message.setRole(request.getRole());
        message.setContentType(request.getContentType());
        message.setContent(request.getContent());

        ConversationMessage savedMessage = messageRepository.save(message);

        // Actualizar última actividad
        conversation.setLastActivity(LocalDateTime.now());
        conversationRepository.save(conversation);

        return savedMessage;
    }

    public List<conversationmessage> getConversationHistory(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
```

## Métricas y Monitoreo

### Prometheus Metrics

```java
@Component
public class TemplatesAndConversationsMetrics {

    private final Counter templatesCreatedCounter;
    private final Counter templatesApprovedCounter;
    private final Counter assistantsCreatedCounter;
    private final Counter conversationsStartedCounter;
    private final Counter messagesSentCounter;
    private final Counter contentGeneratedCounter;
    private final Timer templateRenderingTimer;
    private final Timer contentGenerationTimer;

    public TemplatesAndConversationsMetrics(MeterRegistry meterRegistry) {
        this.templatesCreatedCounter = Counter.builder("tmp_templates_created_total")
            .description("Total prompt templates created")
            .register(meterRegistry);

        this.templatesApprovedCounter = Counter.builder("tmp_templates_approved_total")
            .description("Total prompt templates approved")
            .register(meterRegistry);

        this.assistantsCreatedCounter = Counter.builder("tmp_assistants_created_total")
            .description("Total assistants created")
            .register(meterRegistry);

        this.conversationsStartedCounter = Counter.builder("tmp_conversations_started_total")
            .description("Total conversations started")
            .register(meterRegistry);

        this.messagesSentCounter = Counter.builder("tmp_messages_sent_total")
            .description("Total messages sent")
            .register(meterRegistry);

        this.contentGeneratedCounter = Counter.builder("tmp_content_generated_total")
            .description("Total content generated")
            .register(meterRegistry);

        this.templateRenderingTimer = Timer.builder("tmp_template_rendering_duration_seconds")
            .description("Template rendering duration")
            .register(meterRegistry);

        this.contentGenerationTimer = Timer.builder("tmp_content_generation_duration_seconds")
            .description("Content generation duration")
            .register(meterRegistry);
    }

    public void incrementTemplatesCreated() {
        templatesCreatedCounter.increment();
    }

    public void incrementTemplatesApproved() {
        templatesApprovedCounter.increment();
    }

    public void incrementAssistantsCreated() {
        assistantsCreatedCounter.increment();
    }

    public void incrementConversationsStarted() {
        conversationsStartedCounter.increment();
    }

    public void incrementMessagesSent() {
        messagesSentCounter.increment();
    }

    public void incrementContentGenerated() {
        contentGeneratedCounter.increment();
    }

    public Timer.Sample startTemplateRenderingTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startContentGenerationTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-templates.yml

```plaintext
tmp:
  templates:
    auto-versioning: true
    approval-workflow: true
    template-caching: true
    max-template-size: 10000

  system-prompts:
    auto-approval: false
    governance-integration: true
    compliance-checking: true

  assistants:
    auto-creation: false
    model-selection: automatic
    capability-validation: true
    block-notification: true

  conversations:
    max-conversation-length: 1000
    message-retention-days: 365
    auto-archiving: true
    analytics-tracking: true

  content-generation:
    quality-threshold: 0.8
    compliance-checking: true
    auto-moderation: true
    output-validation: true

  monitoring:
    real-time-metrics: true
    conversation-analytics: true
    content-quality-tracking: true
    compliance-monitoring: true

content-generation:
  text:
    max-length: 4000
    quality-check: true
    plagiarism-detection: true

  image:
    max-resolution: "4096x4096"
    content-moderation: true
    style-validation: true

  audio:
    max-duration: 300
    quality-check: true
    format-validation: true

  video:
    max-duration: 60
    content-moderation: true
    resolution-validation: true

  code:
    security-scanning: true
    quality-analysis: true
    license-checking: true
```

## Características Innovadoras

### 1\. **Sistema Unificado de Conversaciones**

- **Centralización**: Todas las conversaciones se gestionan desde un solo módulo
- **Reutilización**: Los templates y system prompts se pueden usar en diferentes contextos
- **Normalización**: Elimina duplicación entre módulos RAG y otros

### 2\. **Gestión de Prompts Inteligente**

- **Templates Variables**: Sistema de variables para prompts dinámicos
- **Aprobación Workflow**: Sistema de aprobación para templates y system prompts
- **Versionado**: Control de versiones de prompts y templates

### 3\. **Asistentes Especializados**

- **Tipos Específicos**: Asistentes para texto, imagen, audio, video, código
- **Configuración de Modelos**: Cada asistente puede usar diferentes modelos
- **Sistema de Bloqueo**: Control de acceso y bloqueo de asistentes

### 4\. **Generación Multimodal**

- **Múltiples Formatos**: Texto, imagen, audio, video, código
- **Control de Calidad**: Validación automática de contenido generado
- **Cumplimiento**: Verificación de cumplimiento regulatorio

### 5\. **Integración con Governance**

- **Aprobación de Contenido**: Sistema de aprobación para templates y prompts
- **Monitoreo de Cumplimiento**: Seguimiento de cumplimiento regulatorio
- **Auditoría**: Registro completo de todas las acciones

## Pendiente

### **Microservicio Enrutador para Chats y Playgrounds**

Todos los chats y playgrounds, incluso los publicados en API, se comunican a través de un **microservicio Spring Boot** que actúa como enrutador para los serving endpoints. Este microservicio es responsable de:

- **Persistir conversaciones** en el módulo TMP
- **Actualizar métricas de consumo de tokens**
- **Enrutar requests** a los serving endpoints apropiados
- **Gestionar autenticación y autorización**
- **Implementar rate limiting y throttling**
- **Monitorear performance** de los endpoints

#### **Arquitectura del Microservicio Enrutador**

```plaintext
┌─────────────────┐    ┌─────────────────────┐    ┌─────────────────┐
│   Frontend      │    │   TMP Router        │    │   Serving       │
│   (Chat/Play)   │◄──►│   Microservice      │◄──►│   Endpoints     │
│                 │    │   (Spring Boot)     │    │   (Leka-Server) │
└─────────────────┘    └─────────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   TMP Module    │
                       │   (Database)    │
                       │   - Conversations│
                       │   - Messages    │
                       │   - Analytics   │
                       └─────────────────┘
```

#### **Componentes del Microservicio Enrutador**

##### 1. **ChatRouterController**

```java
@RestController
@RequestMapping("/api/v1/chat-router")
public class ChatRouterController {

    @Autowired
    private ChatRoutingService routingService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private TokenMetricsService tokenMetricsService;

    @PostMapping("/chat/{assistantId}")
    public ResponseEntity<ChatResponse> routeChat(
            @PathVariable Long assistantId,
            @RequestBody ChatRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Validar autenticación
        UserContext userContext = validateAndExtractUser(authHeader);

        // Crear o recuperar conversación
        Conversation conversation = conversationService.getOrCreateConversation(
            assistantId, userContext.getUserId(), request.getProjectId());

        // Enrutar al serving endpoint apropiado
        ServingEndpoint endpoint = routingService.determineServingEndpoint(assistantId);

        // Ejecutar request en el endpoint
        ChatResponse response = executeChatRequest(endpoint, request, conversation);

        // Persistir mensaje en TMP
        conversationService.addMessage(conversation.getId(), request, response);

        // Actualizar métricas de tokens
        tokenMetricsService.recordTokenUsage(conversation.getId(), response.getTokensUsed());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/playground/{playgroundType}")
    public ResponseEntity<PlaygroundResponse> routePlayground(
            @PathVariable String playgroundType,
            @RequestBody PlaygroundRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Validar autenticación
        UserContext userContext = validateAndExtractUser(authHeader);

        // Determinar endpoint del playground
        ServingEndpoint endpoint = routingService.determinePlaygroundEndpoint(playgroundType);

        // Ejecutar request
        PlaygroundResponse response = executePlaygroundRequest(endpoint, request);

        // Persistir en analytics (opcional para playgrounds)
        if (request.isPersistAnalytics()) {
            conversationService.persistPlaygroundAnalytics(playgroundType, request, response, userContext);
        }

        // Actualizar métricas de tokens
        tokenMetricsService.recordTokenUsage(null, response.getTokensUsed());

        return ResponseEntity.ok(response);
    }
}
```

##### 2. **ChatRoutingService**

```java
@Service
public class ChatRoutingService {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private ServingEndpointRepository endpointRepository;

    @Autowired
    private LoadBalancerService loadBalancerService;

    public ServingEndpoint determineServingEndpoint(Long assistantId) {
        // Obtener configuración del asistente
        Assistant assistant = assistantRepository.findById(assistantId)
            .orElseThrow(() -> new AssistantNotFoundException("Assistant not found"));

        // Determinar tipo de modelo requerido
        String modelType = extractModelType(assistant.getModelConfig());

        // Buscar endpoints disponibles para ese tipo
        List<ServingEndpoint> availableEndpoints = endpointRepository
            .findByModelTypeAndStatus(modelType, "ACTIVE");

        // Aplicar load balancing
        return loadBalancerService.selectOptimalEndpoint(availableEndpoints);
    }

    public ServingEndpoint determinePlaygroundEndpoint(String playgroundType) {
        // Mapear tipo de playground a tipo de modelo
        String modelType = mapPlaygroundToModelType(playgroundType);

        // Buscar endpoints disponibles
        List<ServingEndpoint> availableEndpoints = endpointRepository
            .findByModelTypeAndStatus(modelType, "ACTIVE");

        // Seleccionar endpoint óptimo
        return loadBalancerService.selectOptimalEndpoint(availableEndpoints);
    }

    private String extractModelType(String modelConfig) {
        // Parsear JSON de configuración del modelo
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode config = mapper.readTree(modelConfig);
            return config.get("model_type").asText();
        } catch (Exception e) {
            throw new ConfigurationException("Invalid model configuration");
        }
    }

    private String mapPlaygroundToModelType(String playgroundType) {
        switch (playgroundType.toLowerCase()) {
            case "text": return "TEXT_GENERATION";
            case "image": return "IMAGE_GENERATION";
            case "audio": return "AUDIO_GENERATION";
            case "video": return "VIDEO_GENERATION";
            case "code": return "CODE_GENERATION";
            default: return "TEXT_GENERATION";
        }
    }
}
```

##### 3. **ServingEndpoint Entity**

```java
@Entity
@Table(name = "tmp_serving_endpoints")
public class ServingEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "endpoint_name")
    private String endpointName;

    @Column(name = "endpoint_url")
    private String endpointUrl;

    @Column(name = "model_type")
    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "endpoint_status")
    @Enumerated(EnumType.STRING)
    private EndpointStatus status;

    @Column(name = "health_check_url")
    private String healthCheckUrl;

    @Column(name = "last_health_check")
    private LocalDateTime lastHealthCheck;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    @Column(name = "max_concurrent_requests")
    private Integer maxConcurrentRequests;

    @Column(name = "current_load")
    private Integer currentLoad;

    @Column(name = "rate_limit_per_minute")
    private Integer rateLimitPerMinute;

    @Column(name = "endpoint_config")
    private String endpointConfig; // JSON configuration

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ModelType {
    TEXT_GENERATION, IMAGE_GENERATION, AUDIO_GENERATION,
    VIDEO_GENERATION, CODE_GENERATION, MULTIMODAL
}

public enum EndpointStatus {
    ACTIVE, INACTIVE, MAINTENANCE, ERROR, OVERLOADED
}
```

##### 4. **LoadBalancerService**

```java
@Service
public class LoadBalancerService {

    public ServingEndpoint selectOptimalEndpoint(List<ServingEndpoint> endpoints) {
        if (endpoints.isEmpty()) {
            throw new NoEndpointAvailableException("No serving endpoints available");
        }

        // Filtrar endpoints saludables
        List<ServingEndpoint> healthyEndpoints = endpoints.stream()
            .filter(e -> e.getStatus() == EndpointStatus.ACTIVE)
            .filter(e -> isEndpointHealthy(e))
            .collect(Collectors.toList());

        if (healthyEndpoints.isEmpty()) {
            throw new NoEndpointAvailableException("No healthy serving endpoints available");
        }

        // Aplicar estrategia de load balancing
        return applyLoadBalancingStrategy(healthyEndpoints);
    }

    private ServingEndpoint applyLoadBalancingStrategy(List<ServingEndpoint> endpoints) {
        // Estrategia: Round Robin con consideración de carga
        return endpoints.stream()
            .min(Comparator.comparing(ServingEndpoint::getCurrentLoad)
                .thenComparing(ServingEndpoint::getResponseTimeMs))
            .orElse(endpoints.get(0));
    }

    private boolean isEndpointHealthy(ServingEndpoint endpoint) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                endpoint.getHealthCheckUrl(), String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}
```

##### 5. **TokenMetricsService**

```java
@Service
public class TokenMetricsService {

    @Autowired
    private TokenUsageRepository tokenUsageRepository;

    @Autowired
    private PrometheusMetricsService metricsService;

    public void recordTokenUsage(Long conversationId, Integer tokensUsed) {
        // Persistir uso de tokens
        TokenUsage usage = new TokenUsage();
        usage.setConversationId(conversationId);
        usage.setTokensUsed(tokensUsed);
        usage.setTimestamp(LocalDateTime.now());
        usage.setUserId(getCurrentUserId());

        tokenUsageRepository.save(usage);

        // Actualizar métricas Prometheus
        metricsService.incrementTokenUsage(tokensUsed);

        // Actualizar métricas por usuario
        if (conversationId != null) {
            metricsService.recordUserTokenUsage(getCurrentUserId(), tokensUsed);
        }
    }

    public TokenUsageStatistics getUserTokenStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        List<TokenUsage> usages = tokenUsageRepository
            .findByUserIdAndTimestampBetween(userId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        return TokenUsageStatistics.builder()
            .totalTokens(usages.stream().mapToInt(TokenUsage::getTokensUsed).sum())
            .totalRequests(usages.size())
            .averageTokensPerRequest(usages.stream()
                .mapToInt(TokenUsage::getTokensUsed)
                .average()
                .orElse(0.0))
            .build();
    }
}
```

##### 6. **Configuración del Microservicio**

```yaml
# application-router.yml
tmp-router:
  serving-endpoints:
    discovery:
      enabled: true
      refresh-interval: 30s
      health-check-interval: 10s

    load-balancing:
      strategy: round-robin-with-load
      health-check-timeout: 5s
      max-retries: 3

    rate-limiting:
      enabled: true
      default-limit: 100
      per-user-limit: 1000
      window-size: 60s

    monitoring:
      response-time-threshold: 5000ms
      error-rate-threshold: 0.05
      load-threshold: 0.8

  chat:
    max-conversation-length: 1000
    message-retention-days: 365
    auto-archiving: true

  playground:
    analytics-persistence: true
    token-tracking: true
    performance-monitoring: true

  security:
    jwt-validation: true
    rate-limiting: true
    user-context-validation: true
```

#### **Flujo de Funcionamiento**

1. **Request llega** al microservicio enrutador
2. **Validación** de autenticación y autorización
3. **Determinación** del serving endpoint apropiado
4. **Load balancing** para seleccionar endpoint óptimo
5. **Ejecución** del request en el endpoint seleccionado
6. **Persistencia** de conversación en módulo TMP
7. **Actualización** de métricas de tokens
8. **Respuesta** al cliente

#### **Beneficios de esta Arquitectura**

- **Centralización**: Un solo punto de entrada para todos los chats y playgrounds
- **Escalabilidad**: Fácil añadir nuevos serving endpoints
- **Monitoreo**: Métricas centralizadas de uso y performance
- **Persistencia**: Todas las conversaciones se guardan en TMP
- **Load Balancing**: Distribución inteligente de carga
- **Health Checking**: Monitoreo continuo de endpoints
- **Rate Limiting**: Control de uso por usuario y endpoint

### **Integración con Leka-Server**

El microservicio enrutador se integra con leka-server para:

- **Descubrimiento automático** de serving endpoints
- **Health checking** de endpoints
- **Load balancing** inteligente
- **Métricas de performance** en tiempo real
- **Fallback** a endpoints alternativos en caso de fallo

### **Métricas y Monitoreo**

- **Token Usage**: Consumo de tokens por usuario y conversación
- **Response Time**: Tiempo de respuesta de cada endpoint
- **Error Rate**: Tasa de errores por endpoint
- **Load Distribution**: Distribución de carga entre endpoints
- **User Activity**: Actividad de usuarios y conversaciones
- **Endpoint Health**: Estado de salud de cada endpoint
