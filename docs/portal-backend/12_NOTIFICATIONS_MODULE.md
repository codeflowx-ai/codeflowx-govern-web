# Módulo de Notificaciones (Notifications) - Portal Backend

## Descripción General

El módulo de Notifications gestiona todas las notificaciones del sistema, incluyendo notificaciones en tiempo real, emails, notificaciones push y alertas. Es fundamental para mantener informados a los usuarios sobre eventos importantes del sistema.

## Arquitectura del Sistema

### Concepto Clave

El sistema de notificaciones proporciona:

- **Notificaciones en tiempo real** mediante WebSockets
- **Emails automáticos** para eventos críticos
- **Notificaciones push** para dispositivos móviles
- **Alertas del sistema** para monitoreo
- **Plantillas personalizables** para diferentes tipos de notificaciones
- **Gestión de preferencias** por usuario
- **Colas de notificaciones** para procesamiento asíncrono

### Flujo de Notificaciones

```
Evento del Sistema → NotificationService → NotificationQueue
    ↓
Procesamiento Asíncrono:
    ↓
├── Notificación en tiempo real (WebSocket)
├── Email automático
├── Notificación push
└── Almacenamiento en base de datos
    ↓
Entrega al Usuario:
    ↓
├── Dashboard de notificaciones
├── Email recibido
├── Push notification
└── Historial de notificaciones
```

## Entidades del Sistema

### 1. Notification

```java
@Entity
@Table(name = "ntf_notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(nullable = false)
    private String title;

    @Column
    private String message;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private NotificationPriority priority;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;

    @Column(name = "source_module")
    private String sourceModule; // Módulo que generó la notificación

    @Column(name = "source_id")
    private String sourceId; // ID del recurso relacionado

    @Column(name = "action_url")
    private String actionUrl; // URL para acción directa

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt; // Para notificaciones programadas

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // Fecha de expiración

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationDelivery> deliveries = new ArrayList<>();
}

public enum NotificationType {
    INFO,               // Información general
    SUCCESS,            // Operación exitosa
    WARNING,            // Advertencia
    ERROR,              // Error del sistema
    ALERT,              // Alerta crítica
    REMINDER,           // Recordatorio
    UPDATE,             // Actualización
    MAINTENANCE,        // Mantenimiento
    SECURITY            // Seguridad
}

public enum NotificationPriority {
    LOW,                // Baja prioridad
    NORMAL,             // Prioridad normal
    HIGH,               // Alta prioridad
    URGENT,             // Urgente
    CRITICAL            // Crítico
}

public enum NotificationCategory {
    SYSTEM,             // Notificaciones del sistema
    USER,               // Notificaciones de usuario
    PROJECT,            // Notificaciones de proyecto
    TRAINING,           // Notificaciones de formación
    SECURITY,           // Notificaciones de seguridad
    MAINTENANCE,        // Notificaciones de mantenimiento
    ALERT,              // Alertas del sistema
    REMINDER            // Recordatorios
}
```

### 2. NotificationTemplate

```java
@Entity
@Table(name = "ntf_notification_templates")
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "template_code")
    private String templateCode; // Código único del template

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;

    @Column(name = "subject_template")
    private String subjectTemplate; // Template del asunto (para emails)

    @Column(name = "message_template")
    private String messageTemplate; // Template del mensaje

    @Column(name = "email_template")
    private String emailTemplate; // Template HTML para emails

    @Column(name = "push_template")
    private String pushTemplate; // Template para push notifications

    @Column(name = "variables")
    private String variables; // JSON con variables disponibles

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 3. NotificationDelivery

```java
@Entity
@Table(name = "ntf_notification_deliveries")
public class NotificationDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_id")
    private Long notificationId; // Referencia a notifications.id

    @Column(name = "delivery_channel")
    @Enumerated(EnumType.STRING)
    private DeliveryChannel channel;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

    @Column(name = "max_attempts")
    private Integer maxAttempts = 3;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "delivery_metadata")
    private String deliveryMetadata; // JSON con metadatos del canal

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum DeliveryChannel {
    IN_APP,             // Notificación en la aplicación
    EMAIL,              // Email
    PUSH,               // Push notification
    SMS,                // SMS
    WEBHOOK,            // Webhook externo
    SLACK,              // Slack
    TEAMS               // Microsoft Teams
}

public enum DeliveryStatus {
    PENDING,            // Pendiente de envío
    QUEUED,             // En cola
    SENDING,            // Enviando
    SENT,               // Enviado
    DELIVERED,          // Entregado
    FAILED,             // Fallido
    CANCELLED,          // Cancelado
    EXPIRED             // Expirado
}
```

### 4. NotificationPreference

```java
@Entity
@Table(name = "ntf_notification_preferences")
public class NotificationPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;

    @Column(name = "in_app_enabled")
    private Boolean inAppEnabled = true;

    @Column(name = "email_enabled")
    private Boolean emailEnabled = true;

    @Column(name = "push_enabled")
    private Boolean pushEnabled = false;

    @Column(name = "sms_enabled")
    private Boolean smsEnabled = false;

    @Column(name = "quiet_hours_start")
    private String quietHoursStart; // HH:mm formato

    @Column(name = "quiet_hours_end")
    private String quietHoursEnd; // HH:mm formato

    @Column(name = "timezone")
    private String timezone = "UTC";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 5. NotificationSubscription

```java
@Entity
@Table(name = "ntf_notification_subscriptions")
public class NotificationSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // Referencia a users.id

    @Column(name = "subscription_type")
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    @Column(name = "source_module")
    private String sourceModule;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum SubscriptionType {
    PROJECT_UPDATES,    // Actualizaciones de proyecto
    TRAINING_REMINDERS, // Recordatorios de formación
    SYSTEM_ALERTS,      // Alertas del sistema
    SECURITY_EVENTS,    // Eventos de seguridad
    MAINTENANCE_NOTICES, // Avisos de mantenimiento
    PERFORMANCE_ALERTS, // Alertas de rendimiento
    USER_ACTIVITY,      // Actividad de usuario
    CUSTOM             // Suscripción personalizada
}
```

## API Endpoints

### Notification Management

```
GET    /api/v1/notifications
GET    /api/v1/notifications/{id}
POST   /api/v1/notifications
PUT    /api/v1/notifications/{id}
DELETE /api/v1/notifications/{id}
GET    /api/v1/notifications/user/{userId}
GET    /api/v1/notifications/unread
POST   /api/v1/notifications/{id}/mark-read
POST   /api/v1/notifications/{id}/mark-unread
POST   /api/v1/notifications/{id}/archive
POST   /api/v1/notifications/bulk-mark-read
```

### Template Management

```
GET    /api/v1/notification-templates
GET    /api/v1/notification-templates/{id}
POST   /api/v1/notification-templates
PUT    /api/v1/notification-templates/{id}
DELETE /api/v1/notification-templates/{id}
GET    /api/v1/notification-templates/type/{type}
GET    /api/v1/notification-templates/category/{category}
POST   /api/v1/notification-templates/{id}/test
```

### Delivery Management

```
GET    /api/v1/notification-deliveries
GET    /api/v1/notification-deliveries/{id}
GET    /api/v1/notification-deliveries/notification/{notificationId}
GET    /api/v1/notification-deliveries/status/{status}
POST   /api/v1/notification-deliveries/{id}/retry
POST   /api/v1/notification-deliveries/{id}/cancel
```

### Preference Management

```
GET    /api/v1/notification-preferences/user/{userId}
PUT    /api/v1/notification-preferences/user/{userId}
POST   /api/v1/notification-preferences/user/{userId}/reset
GET    /api/v1/notification-preferences/user/{userId}/type/{type}
PUT    /api/v1/notification-preferences/user/{userId}/type/{type}
```

### Subscription Management

```
GET    /api/v1/notification-subscriptions/user/{userId}
POST   /api/v1/notification-subscriptions
PUT    /api/v1/notification-subscriptions/{id}
DELETE /api/v1/notification-subscriptions/{id}
POST   /api/v1/notification-subscriptions/{id}/activate
POST   /api/v1/notification-subscriptions/{id}/deactivate
```

### Real-time Notifications

```
GET    /api/v1/notifications/stream
WS     /ws/notifications/{userId}
POST   /api/v1/notifications/send-immediate
POST   /api/v1/notifications/schedule
```

## Scripts de Base de Datos

### Script de Creación de Tablas

```sql
-- Tabla de notificaciones
CREATE TABLE ntf_notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    notification_type VARCHAR(20) NOT NULL,
    priority VARCHAR(20) DEFAULT 'NORMAL',
    category VARCHAR(30) NOT NULL,
    source_module VARCHAR(50),
    source_id VARCHAR(100),
    action_url VARCHAR(500),
    is_read BOOLEAN DEFAULT false,
    is_archived BOOLEAN DEFAULT false,
    scheduled_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla notifications
COMMENT ON TABLE ntf_notifications IS 'Tabla principal que almacena las notificaciones del sistema';
COMMENT ON COLUMN notifications.id IS 'Identificador único de la notificación';
COMMENT ON COLUMN notifications.user_id IS 'ID del usuario destinatario de la notificación';
COMMENT ON COLUMN notifications.title IS 'Título de la notificación';
COMMENT ON COLUMN notifications.message IS 'Mensaje detallado de la notificación';
COMMENT ON COLUMN notifications.notification_type IS 'Tipo de notificación (INFO, SUCCESS, WARNING, ERROR, etc.)';
COMMENT ON COLUMN notifications.priority IS 'Prioridad de la notificación (LOW, NORMAL, HIGH, URGENT, CRITICAL)';
COMMENT ON COLUMN notifications.category IS 'Categoría de la notificación (SYSTEM, USER, PROJECT, etc.)';
COMMENT ON COLUMN notifications.source_module IS 'Módulo del sistema que generó la notificación';
COMMENT ON COLUMN notifications.source_id IS 'ID del recurso relacionado con la notificación';
COMMENT ON COLUMN notifications.action_url IS 'URL para acción directa desde la notificación';
COMMENT ON COLUMN notifications.is_read IS 'Indica si la notificación ha sido leída';
COMMENT ON COLUMN notifications.is_archived IS 'Indica si la notificación ha sido archivada';
COMMENT ON COLUMN notifications.scheduled_at IS 'Fecha y hora programada para el envío';
COMMENT ON COLUMN notifications.expires_at IS 'Fecha y hora de expiración de la notificación';
COMMENT ON COLUMN notifications.created_at IS 'Fecha y hora de creación de la notificación';
COMMENT ON COLUMN notifications.updated_at IS 'Fecha y hora de la última actualización de la notificación';

-- Tabla de plantillas de notificación
CREATE TABLE ntf_notification_templates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    template_code VARCHAR(100) UNIQUE NOT NULL,
    notification_type VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,
    subject_template VARCHAR(500),
    message_template TEXT,
    email_template TEXT,
    push_template TEXT,
    variables TEXT, -- JSON con variables disponibles
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla notification_templates
COMMENT ON TABLE ntf_notification_templates IS 'Tabla que almacena las plantillas para diferentes tipos de notificaciones';
COMMENT ON COLUMN notification_templates.id IS 'Identificador único de la plantilla';
COMMENT ON COLUMN notification_templates.name IS 'Nombre de la plantilla';
COMMENT ON COLUMN notification_templates.description IS 'Descripción de la plantilla';
COMMENT ON COLUMN notification_templates.template_code IS 'Código único de la plantilla';
COMMENT ON COLUMN notification_templates.notification_type IS 'Tipo de notificación para el que se usa la plantilla';
COMMENT ON COLUMN notification_templates.category IS 'Categoría de la notificación';
COMMENT ON COLUMN notification_templates.subject_template IS 'Template del asunto para emails';
COMMENT ON COLUMN notification_templates.message_template IS 'Template del mensaje principal';
COMMENT ON COLUMN notification_templates.email_template IS 'Template HTML para emails';
COMMENT ON COLUMN notification_templates.push_template IS 'Template para push notifications';
COMMENT ON COLUMN notification_templates.variables IS 'Variables disponibles en formato JSON';
COMMENT ON COLUMN notification_templates.is_active IS 'Indica si la plantilla está activa';
COMMENT ON COLUMN notification_templates.created_at IS 'Fecha y hora de creación de la plantilla';
COMMENT ON COLUMN notification_templates.updated_at IS 'Fecha y hora de la última actualización de la plantilla';

-- Tabla de entregas de notificaciones
CREATE TABLE ntf_notification_deliveries (
    id BIGSERIAL PRIMARY KEY,
    notification_id BIGINT REFERENCES ntf_notifications(id) ON DELETE CASCADE,
    delivery_channel VARCHAR(20) NOT NULL,
    delivery_status VARCHAR(20) DEFAULT 'PENDING',
    attempt_count INTEGER DEFAULT 0,
    max_attempts INTEGER DEFAULT 3,
    scheduled_at TIMESTAMP,
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    error_message TEXT,
    delivery_metadata TEXT, -- JSON con metadatos del canal
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla notification_deliveries
COMMENT ON TABLE ntf_notification_deliveries IS 'Tabla que registra las entregas de notificaciones por diferentes canales';
COMMENT ON COLUMN notification_deliveries.id IS 'Identificador único de la entrega';
COMMENT ON COLUMN notification_deliveries.notification_id IS 'ID de la notificación';
COMMENT ON COLUMN notification_deliveries.delivery_channel IS 'Canal de entrega (IN_APP, EMAIL, PUSH, SMS, etc.)';
COMMENT ON COLUMN notification_deliveries.delivery_status IS 'Estado de la entrega (PENDING, QUEUED, SENDING, SENT, etc.)';
COMMENT ON COLUMN notification_deliveries.attempt_count IS 'Número de intentos de entrega realizados';
COMMENT ON COLUMN notification_deliveries.max_attempts IS 'Número máximo de intentos permitidos';
COMMENT ON COLUMN notification_deliveries.scheduled_at IS 'Fecha y hora programada para la entrega';
COMMENT ON COLUMN notification_deliveries.sent_at IS 'Fecha y hora de envío';
COMMENT ON COLUMN notification_deliveries.delivered_at IS 'Fecha y hora de entrega exitosa';
COMMENT ON COLUMN notification_deliveries.error_message IS 'Mensaje de error si la entrega falló';
COMMENT ON COLUMN notification_deliveries.delivery_metadata IS 'Metadatos específicos del canal en formato JSON';
COMMENT ON COLUMN notification_deliveries.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN notification_deliveries.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de preferencias de notificación
CREATE TABLE ntf_notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    notification_type VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,
    in_app_enabled BOOLEAN DEFAULT true,
    email_enabled BOOLEAN DEFAULT true,
    push_enabled BOOLEAN DEFAULT false,
    sms_enabled BOOLEAN DEFAULT false,
    quiet_hours_start VARCHAR(5), -- HH:mm formato
    quiet_hours_end VARCHAR(5), -- HH:mm formato
    timezone VARCHAR(50) DEFAULT 'UTC',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla notification_preferences
COMMENT ON TABLE ntf_notification_preferences IS 'Tabla que almacena las preferencias de notificación de cada usuario';
COMMENT ON COLUMN notification_preferences.id IS 'Identificador único de la preferencia';
COMMENT ON COLUMN notification_preferences.user_id IS 'ID del usuario';
COMMENT ON COLUMN notification_preferences.notification_type IS 'Tipo de notificación';
COMMENT ON COLUMN notification_preferences.category IS 'Categoría de la notificación';
COMMENT ON COLUMN notification_preferences.in_app_enabled IS 'Indica si las notificaciones en la app están habilitadas';
COMMENT ON COLUMN notification_preferences.email_enabled IS 'Indica si las notificaciones por email están habilitadas';
COMMENT ON COLUMN notification_preferences.push_enabled IS 'Indica si las notificaciones push están habilitadas';
COMMENT ON COLUMN notification_preferences.sms_enabled IS 'Indica si las notificaciones por SMS están habilitadas';
COMMENT ON COLUMN notification_preferences.quiet_hours_start IS 'Hora de inicio de las horas silenciosas (HH:mm)';
COMMENT ON COLUMN notification_preferences.quiet_hours_end IS 'Hora de fin de las horas silenciosas (HH:mm)';
COMMENT ON COLUMN notification_preferences.timezone IS 'Zona horaria del usuario';
COMMENT ON COLUMN notification_preferences.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN notification_preferences.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de suscripciones a notificaciones
CREATE TABLE ntf_notification_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    subscription_type VARCHAR(30) NOT NULL,
    source_module VARCHAR(50),
    source_id VARCHAR(100),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla notification_subscriptions
COMMENT ON TABLE ntf_notification_subscriptions IS 'Tabla que gestiona las suscripciones de usuarios a diferentes tipos de notificaciones';
COMMENT ON COLUMN notification_subscriptions.id IS 'Identificador único de la suscripción';
COMMENT ON COLUMN notification_subscriptions.user_id IS 'ID del usuario suscrito';
COMMENT ON COLUMN notification_subscriptions.subscription_type IS 'Tipo de suscripción (PROJECT_UPDATES, TRAINING_REMINDERS, etc.)';
COMMENT ON COLUMN notification_subscriptions.source_module IS 'Módulo fuente de las notificaciones';
COMMENT ON COLUMN notification_subscriptions.source_id IS 'ID del recurso fuente';
COMMENT ON COLUMN notification_subscriptions.is_active IS 'Indica si la suscripción está activa';
COMMENT ON COLUMN notification_subscriptions.created_at IS 'Fecha y hora de creación de la suscripción';
COMMENT ON COLUMN notification_subscriptions.updated_at IS 'Fecha y hora de la última actualización de la suscripción';

-- Índices para optimización
CREATE INDEX idx_ntf_notifications_user_id ON ntf_notifications(user_id);
CREATE INDEX idx_ntf_notifications_type ON ntf_notifications(notification_type);
CREATE INDEX idx_ntf_notifications_category ON ntf_notifications(category);
CREATE INDEX idx_ntf_notifications_priority ON ntf_notifications(priority);
CREATE INDEX idx_ntf_notifications_read ON ntf_notifications(is_read);
CREATE INDEX idx_ntf_notifications_archived ON ntf_notifications(is_archived);
CREATE INDEX idx_ntf_notifications_created_at ON ntf_notifications(created_at);
CREATE INDEX idx_ntf_notifications_scheduled_at ON ntf_notifications(scheduled_at);
CREATE INDEX idx_ntf_notifications_expires_at ON ntf_notifications(expires_at);
CREATE INDEX idx_ntf_templates_code ON ntf_notification_templates(template_code);
CREATE INDEX idx_ntf_templates_type ON ntf_notification_templates(notification_type);
CREATE INDEX idx_ntf_templates_category ON ntf_notification_templates(category);
CREATE INDEX idx_ntf_templates_active ON ntf_notification_templates(is_active);
CREATE INDEX idx_ntf_deliveries_notification_id ON ntf_notification_deliveries(notification_id);
CREATE INDEX idx_ntf_deliveries_channel ON ntf_notification_deliveries(delivery_channel);
CREATE INDEX idx_ntf_deliveries_status ON ntf_notification_deliveries(delivery_status);
CREATE INDEX idx_ntf_deliveries_scheduled_at ON ntf_notification_deliveries(scheduled_at);
CREATE INDEX idx_ntf_preferences_user_id ON ntf_notification_preferences(user_id);
CREATE INDEX idx_ntf_preferences_type ON ntf_notification_preferences(notification_type);
CREATE INDEX idx_ntf_preferences_category ON ntf_notification_preferences(category);
CREATE INDEX idx_ntf_subscriptions_user_id ON ntf_notification_subscriptions(user_id);
CREATE INDEX idx_ntf_subscriptions_type ON ntf_notification_subscriptions(subscription_type);
CREATE INDEX idx_ntf_subscriptions_active ON ntf_notification_subscriptions(is_active);
```

## Servicios de Notifications

### NotificationService

```java
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTemplateService templateService;

    @Autowired
    private NotificationDeliveryService deliveryService;

    @Autowired
    private NotificationPreferenceService preferenceService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PushNotificationService pushService;

    @Autowired
    private AuditService auditService;

    public Notification createNotification(NotificationDto notificationDto) {
        // Validar datos de la notificación
        validateNotificationData(notificationDto);

        // Crear notificación
        Notification notification = new Notification();
        notification.setUserId(notificationDto.getUserId());
        notification.setTitle(notificationDto.getTitle());
        notification.setMessage(notificationDto.getMessage());
        notification.setType(notificationDto.getType());
        notification.setPriority(notificationDto.getPriority());
        notification.setCategory(notificationDto.getCategory());
        notification.setSourceModule(notificationDto.getSourceModule());
        notification.setSourceId(notificationDto.getSourceId());
        notification.setActionUrl(notificationDto.getActionUrl());
        notification.setScheduledAt(notificationDto.getScheduledAt());
        notification.setExpiresAt(notificationDto.getExpiresAt());
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);

        // Procesar entregas según preferencias del usuario
        processNotificationDeliveries(savedNotification);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_NOTIFICATION",
            "NOTIFICATION",
            savedNotification.getId().toString()
        );

        return savedNotification;
    }

    public Notification createNotificationFromTemplate(String templateCode,
                                                   Long userId,
                                                   Map<String, Object> variables) {
        // Obtener plantilla
        NotificationTemplate template = templateService.getTemplateByCode(templateCode);
        if (template == null) {
            throw new TemplateNotFoundException("Template not found: " + templateCode);
        }

        // Procesar variables en la plantilla
        String title = processTemplate(template.getSubjectTemplate(), variables);
        String message = processTemplate(template.getMessageTemplate(), variables);

        // Crear notificación
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUserId(userId);
        notificationDto.setTitle(title);
        notificationDto.setMessage(message);
        notificationDto.setType(template.getType());
        notificationDto.setCategory(template.getCategory());
        notificationDto.setPriority(NotificationPriority.NORMAL);

        return createNotification(notificationDto);
    }

    public void sendImmediateNotification(NotificationDto notificationDto) {
        // Crear notificación
        Notification notification = createNotification(notificationDto);

        // Enviar inmediatamente
        sendNotificationImmediately(notification);
    }

    public void scheduleNotification(NotificationDto notificationDto,
                                  LocalDateTime scheduledAt) {
        notificationDto.setScheduledAt(scheduledAt);
        createNotification(notificationDto);

        // Programar envío
        scheduleNotificationDelivery(notificationDto, scheduledAt);
    }

    private void processNotificationDeliveries(Notification notification) {
        // Obtener preferencias del usuario
        NotificationPreference preference = preferenceService.getUserPreference(
            notification.getUserId(),
            notification.getType(),
            notification.getCategory()
        );

        if (preference == null) {
            // Usar preferencias por defecto
            preference = preferenceService.getDefaultPreference(notification.getType(), notification.getCategory());
        }

        // Crear entregas según preferencias
        if (preference.getInAppEnabled()) {
            deliveryService.createDelivery(notification.getId(), DeliveryChannel.IN_APP);
        }

        if (preference.getEmailEnabled()) {
            deliveryService.createDelivery(notification.getId(), DeliveryChannel.EMAIL);
        }

        if (preference.getPushEnabled()) {
            deliveryService.createDelivery(notification.getId(), DeliveryChannel.PUSH);
        }

        if (preference.getSmsEnabled()) {
            deliveryService.createDelivery(notification.getId(), DeliveryChannel.SMS);
        }
    }

    private void sendNotificationImmediately(Notification notification) {
        // Enviar notificación en tiempo real
        webSocketService.sendNotification(notification.getUserId(), notification);

        // Procesar entregas inmediatas
        deliveryService.processImmediateDeliveries(notification.getId());
    }

    private void scheduleNotificationDelivery(NotificationDto notificationDto, LocalDateTime scheduledAt) {
        // Programar envío para fecha futura
        deliveryService.scheduleDelivery(notificationDto, scheduledAt);
    }

    private String processTemplate(String template, Map<String, Object> variables) {
        // Procesar variables en la plantilla
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}",
                                  String.valueOf(entry.getValue()));
        }
        return result;
    }

    private void validateNotificationData(NotificationDto notificationDto) {
        if (notificationDto.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }

        if (notificationDto.getTitle() == null || notificationDto.getTitle().trim().isEmpty()) {
            throw new ValidationException("Notification title is required");
        }

        if (notificationDto.getType() == null) {
            throw new ValidationException("Notification type is required");
        }

        if (notificationDto.getCategory() == null) {
            throw new ValidationException("Notification category is required");
        }
    }

    private String getCurrentUsername() {
        // Obtener username del usuario actual
        return "system"; // Placeholder
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class NotificationMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter notificationsCreatedCounter;
    private final Counter notificationsDeliveredCounter;
    private final Counter notificationsFailedCounter;
    private final Counter emailsSentCounter;
    private final Counter pushNotificationsSentCounter;
    private final Timer notificationProcessingTimer;
    private final Timer deliveryTimer;

    public NotificationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.notificationsCreatedCounter = Counter.builder("notifications.created")
            .description("Total notifications created")
            .register(meterRegistry);

        this.notificationsDeliveredCounter = Counter.builder("notifications.delivered")
            .description("Total notifications delivered")
            .register(meterRegistry);

        this.notificationsFailedCounter = Counter.builder("notifications.failed")
            .description("Total notifications failed")
            .register(meterRegistry);

        this.emailsSentCounter = Counter.builder("notifications.emails.sent")
            .description("Total emails sent")
            .register(meterRegistry);

        this.pushNotificationsSentCounter = Counter.builder("notifications.push.sent")
            .description("Total push notifications sent")
            .register(meterRegistry);

        this.notificationProcessingTimer = Timer.builder("notifications.processing.time")
            .description("Notification processing time")
            .register(meterRegistry);

        this.deliveryTimer = Timer.builder("notifications.delivery.time")
            .description("Notification delivery time")
            .register(meterRegistry);
    }

    public void incrementNotificationsCreated() {
        notificationsCreatedCounter.increment();
    }

    public void incrementNotificationsDelivered() {
        notificationsDeliveredCounter.increment();
    }

    public void incrementNotificationsFailed() {
        notificationsFailedCounter.increment();
    }

    public void incrementEmailsSent() {
        emailsSentCounter.increment();
    }

    public void incrementPushNotificationsSent() {
        pushNotificationsSentCounter.increment();
    }

    public Timer.Sample startNotificationProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startDeliveryTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-notifications.yml

```yaml
notifications:
  # Configuración general
  general:
    max-retry-attempts: 3
    retry-delay-seconds: 300
    batch-size: 100
    async-processing: true

  # Configuración de WebSockets
  websocket:
    enabled: true
    endpoint: "/ws/notifications"
    max-connections: 1000
    heartbeat-interval: 30

  # Configuración de emails
  email:
    enabled: true
    smtp-host: "localhost"
    smtp-port: 587
    smtp-username: "${EMAIL_USERNAME}"
    smtp-password: "${EMAIL_PASSWORD}"
    from-address: "noreply@codeflowx.com"
    max-retries: 3
    retry-delay-minutes: 5

  # Configuración de push notifications
  push:
    enabled: false
    fcm-server-key: "${FCM_SERVER_KEY}"
    apns-bundle-id: "${APNS_BUNDLE_ID}"
    apns-key-id: "${APNS_KEY_ID}"
    apns-team-id: "${APNS_TEAM_ID}"

  # Configuración de SMS
  sms:
    enabled: false
    provider: "twilio"
    twilio-account-sid: "${TWILIO_ACCOUNT_SID}"
    twilio-auth-token: "${TWILIO_AUTH_TOKEN}"
    twilio-phone-number: "${TWILIO_PHONE_NUMBER}"

  # Configuración de colas
  queue:
    enabled: true
    broker: "rabbitmq"
    queue-name: "notifications"
    exchange-name: "notifications.exchange"
    routing-key: "notifications"

  # Configuración de plantillas
  templates:
    storage: "database"
    cache-enabled: true
    cache-ttl-seconds: 3600

  # Configuración de preferencias
  preferences:
    default-in-app: true
    default-email: true
    default-push: false
    default-sms: false
    quiet-hours-enabled: true
    default-quiet-start: "22:00"
    default-quiet-end: "08:00"
```

## Conclusión

El módulo de Notifications proporciona un sistema completo de gestión de notificaciones, incluyendo:

- **Notificaciones en Tiempo Real**: WebSockets para notificaciones instantáneas
- **Plantillas Personalizables**: Sistema de templates con variables dinámicas
- **Múltiples Canales**: Email, push, SMS, in-app y webhooks
- **Gestión de Preferencias**: Control granular por usuario y tipo de notificación
- **Suscripciones Inteligentes**: Sistema de suscripciones por módulo y recurso
- **Entrega Asíncrona**: Colas para procesamiento escalable
- **Horarios Silenciosos**: Respeto de preferencias de tiempo del usuario
- **Métricas y Monitoreo**: Prometheus para seguimiento del rendimiento
- **Auditoría Completa**: Registro de todas las acciones del sistema

El sistema está diseñado para ser altamente escalable y configurable, permitiendo la integración con diferentes proveedores de servicios y adaptándose a las necesidades específicas de cada organización.
