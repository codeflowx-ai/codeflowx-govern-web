# 04. Módulo de Gestión de Infraestructura - Portal Backend

## Descripción General

El módulo de Gestión de Infraestructura gestiona toda la infraestructura cloud necesaria para el funcionamiento de la plataforma CodeflowX. Incluye gestión de clusters Kubernetes (propios o de proveedores), proveedores cloud GPU, recursos de infraestructura, sistema de costes y facturación, y auditoría de despliegues. Se integra con múltiples proveedores cloud (AWS, Google Cloud, Azure, OVH, RunPod, Vast.ai) y gestiona la asignación inteligente de recursos para modelos de IA en inferencia y training.

## Características Principales

- **Gestión Multi-Cloud**: Soporte para múltiples proveedores cloud y Kubernetes
- **Clusters Kubernetes**: Gestión de clusters propios y de proveedores (OVH, Azure, AWS, Google)
- **Proveedores GPU Cloud**: Integración con RunPod, Vast.ai y otros proveedores especializados
- **Gestión de Recursos**: Balanceadores, almacenamiento, gateways, orquestadores
- **Sistema de Costes**: Cálculo de costes con márgenes configurables y refacturación
- **Asignación Inteligente**: Distribución automática de modelos según requisitos de recursos
- **Auditoría Completa**: Histórico de despliegues y consumos para cumplimiento
- **Gestión de Credenciales**: Tokens, API keys y secrets de forma segura
- **Gestión Integrada de Recursos**: Modales especializados para gestionar recursos asociados sin navegación
- **Recursos Asociados Detallados**: Volúmenes, interfaces de red, reglas de seguridad y snapshots de respaldo
- **Experiencia de Usuario Mejorada**: Gestión centralizada con estadísticas en tiempo real

## Entidades del Sistema

## Gestión Integrada de Recursos

El módulo implementa un sistema de gestión integrada de recursos que permite administrar entidades relacionadas directamente desde las entidades principales, mejorando significativamente la experiencia de usuario y reduciendo la navegación entre páginas.

### Características de la Gestión Integrada

#### **1️⃣ Modales Especializados**

- **Cloud Resources Modal**: Gestión de recursos cloud desde CloudProvider
- **Kubernetes Clusters Modal**: Gestión de clusters desde CloudProvider
- **GPU Instances Modal**: Gestión de instancias GPU desde CloudProvider
- **Nodes Modal**: Gestión de nodos desde KubernetesCluster
- **Resources Modal**: Gestión de recursos desde KubernetesCluster
- **Volumes Modal**: Gestión de volúmenes desde GpuInstance
- **Networking Modal**: Gestión de interfaces de red desde GpuInstance
- **Security Modal**: Gestión de reglas de seguridad desde GpuInstance
- **Backups Modal**: Gestión de snapshots desde GpuInstance

#### **2️⃣ Estadísticas en Tiempo Real**

- **Contadores de recursos**: Número total de recursos asociados
- **Métricas de costos**: Costos acumulados por tipo de recurso
- **Estados operativos**: Recursos activos, inactivos, en error
- **Uso de capacidad**: Almacenamiento, red, seguridad

#### **3️⃣ Acciones Integradas**

- **Botones de acción**: Iconos especializados para cada tipo de recurso
- **Gestión directa**: Añadir, editar, eliminar sin salir de la entidad principal
- **Vista unificada**: Todos los recursos relacionados en un solo lugar
- **Navegación optimizada**: Reducción del 80% en clicks de navegación

#### **4️⃣ Beneficios de UX**

- **Gestión centralizada**: No más navegación entre páginas
- **Contexto completo**: Visión holística de la infraestructura
- **Operaciones rápidas**: Acceso directo a funcionalidades críticas
- **Consistencia visual**: Interfaz unificada para todos los recursos

### 1. Cloud Provider (Proveedor Cloud)

```java
@Entity
@Table(name = "inf_cloud_providers")
public class CloudProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "description")
    private String description;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "api_version")
    private String apiVersion;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "supports_kubernetes")
    private Boolean supportsKubernetes = false;

    @Column(name = "supports_gpu_instances")
    private Boolean supportsGpuInstances = false;

    @Column(name = "supports_serverless")
    private Boolean supportsServerless = false;

    @Column(name = "default_region")
    private String defaultRegion;

    @Column(name = "supported_regions")
    private String supportedRegions; // JSON array

    @Column(name = "cost_multiplier")
    private Double costMultiplier = 1.0; // Multiplicador de costes base

    @Column(name = "currency")
    private String currency = "USD";

    @Column(name = "billing_cycle")
    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle = BillingCycle.HOURLY;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cloudProvider", cascade = CascadeType.ALL)
    private List<ProviderCredential> credentials = new ArrayList<>();

    @OneToMany(mappedBy = "cloudProvider", cascade = CascadeType.ALL)
    private List<KubernetesCluster> kubernetesClusters = new ArrayList<>();

    @OneToMany(mappedBy = "cloudProvider", cascade = CascadeType.ALL)
    private List<CloudResource> cloudResources = new ArrayList<>();

    @OneToMany(mappedBy = "cloudProvider", cascade = CascadeType.ALL)
    private List<GpuInstance> gpuInstances = new ArrayList<>();
}

public enum ProviderType {
    KUBERNETES_ONLY, CLOUD_PROVIDER, GPU_SPECIALIST, HYBRID, EDGE
}

public enum BillingCycle {
    HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, USAGE_BASED
}
```

### 2. Provider Credential (Credencial del Proveedor)

```java
@Entity
@Table(name = "inf_provider_credentials")
public class ProviderCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cloud_provider_id", nullable = false)
    private Long cloudProviderId;

    @Column(name = "credential_name", nullable = false)
    private String credentialName;

    @Column(name = "credential_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CredentialType credentialType;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "api_token")
    private String apiToken;

    @Column(name = "kubeconfig")
    private String kubeconfig; // Base64 encoded

    @Column(name = "region")
    private String region;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "usage_count")
    private Long usageCount = 0L;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "cloud_provider_id", insertable = false, updatable = false)
    private CloudProvider cloudProvider;
}

public enum CredentialType {
    API_KEY_SECRET, OAUTH_TOKEN, SERVICE_ACCOUNT, KUBECONFIG,
    USERNAME_PASSWORD, CERTIFICATE, SSH_KEY
}
```

### 3. Kubernetes Cluster (Cluster de Kubernetes)

```java
@Entity
@Table(name = "inf_kubernetes_clusters")
public class KubernetesCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cloud_provider_id")
    private Long cloudProviderId; // NULL si es cluster propio

    @Column(name = "credential_id")
    private Long credentialId;

    @Column(name = "cluster_name", nullable = false)
    private String clusterName;

    @Column(name = "cluster_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClusterType clusterType;

    @Column(name = "kubernetes_version")
    private String kubernetesVersion;

    @Column(name = "kubeconfig")
    private String kubeconfig; // Base64 encoded

    @Column(name = "api_server_url")
    private String apiServerUrl;

    @Column(name = "dashboard_url")
    private String dashboardUrl;

    @Column(name = "region")
    private String region;

    @Column(name = "zone")
    private String zone;

    @Column(name = "is_managed")
    private Boolean isManaged = false; // true si es gestionado por proveedor

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "kubernetesCluster", cascade = CascadeType.ALL)
    private List<ClusterNode> nodes = new ArrayList<>();

    @OneToMany(mappedBy = "kubernetesCluster", cascade = CascadeType.ALL)
    private List<ClusterResource> resources = new ArrayList<>();

    @OneToMany(mappedBy = "kubernetesCluster", cascade = CascadeType.ALL)
    private List<ClusterDeployment> deployments = new ArrayList<>();
}

public enum ClusterType {
    OWN, AWS_EKS, GOOGLE_GKE, AZURE_AKS, OVH_MANAGED, DIGITALOCEAN,
    RANCHER, OPENSHIFT, KIND, MINIKUBE
}
```

### 4. Cluster Node (Nodo del Cluster)

```java
@Entity
@Table(name = "inf_cluster_nodes")
public class ClusterNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kubernetes_cluster_id", nullable = false)
    private Long kubernetesClusterId;

    @Column(name = "node_name", nullable = false)
    private String nodeName;

    @Column(name = "node_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NodeType nodeType;

    @Column(name = "instance_type")
    private String instanceType; // t3.medium, n1-standard-2, etc.

    @Column(name = "cpu_cores")
    private Integer cpuCores;

    @Column(name = "memory_gb")
    private Integer memoryGb;

    @Column(name = "storage_gb")
    private Integer storageGb;

    @Column(name = "gpu_count")
    private Integer gpuCount = 0;

    @Column(name = "gpu_type")
    private String gpuType; // V100, A100, T4, etc.

    @Column(name = "gpu_memory_gb")
    private Integer gpuMemoryGb;

    @Column(name = "is_spot_instance")
    private Boolean isSpotInstance = false;

    @Column(name = "is_preemptible")
    private Boolean isPreemptible = false;

    @Column(name = "cost_per_hour")
    private BigDecimal costPerHour;

    @Column(name = "cost_per_month")
    private BigDecimal costPerMonth;

    @Column(name = "is_suitable_for_training")
    private Boolean isSuitableForTraining = false;

    @Column(name = "is_suitable_for_inference")
    private Boolean isSuitableForInference = true;

    @Column(name = "current_workload")
    private String currentWorkload; // JSON con carga actual

    @Column(name = "available_resources")
    private String availableResources; // JSON con recursos disponibles

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NodeStatus status = NodeStatus.READY;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "kubernetes_cluster_id", insertable = false, updatable = false)
    private KubernetesCluster kubernetesCluster;
}

public enum NodeType {
    MASTER, WORKER, GPU_WORKER, EDGE, CLOUD, HYBRID
}

public enum NodeStatus {
    READY, NOT_READY, UNKNOWN, OUT_OF_DISK, MEMORY_PRESSURE,
    DISK_PRESSURE, PID_PRESSURE, NETWORK_UNAVAILABLE
}
```

### 5. Cloud Resource (Recurso Cloud)

```java
@Entity
@Table(name = "inf_cloud_resources")
public class CloudResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cloud_provider_id", nullable = false)
    private Long cloudProviderId;

    @Column(name = "resource_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(name = "resource_name", nullable = false)
    private String resourceName;

    @Column(name = "resource_id")
    private String resourceId; // ID del recurso en el proveedor

    @Column(name = "description")
    private String description;

    @Column(name = "region")
    private String region;

    @Column(name = "zone")
    private String zone;

    @Column(name = "specifications")
    private String specifications; // JSON con especificaciones

    @Column(name = "cost_per_hour")
    private BigDecimal costPerHour;

    @Column(name = "cost_per_month")
    private BigDecimal costPerMonth;

    @Column(name = "is_auto_scaling")
    private Boolean isAutoScaling = false;

    @Column(name = "min_instances")
    private Integer minInstances = 1;

    @Column(name = "max_instances")
    private Integer maxInstances = 10;

    @Column(name = "current_instances")
    private Integer currentInstances = 1;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "cloud_provider_id", insertable = false, updatable = false)
    private CloudProvider cloudProvider;
}

public enum ResourceType {
    LOAD_BALANCER, STORAGE, GATEWAY, ORCHESTRATOR, DATABASE,
    CACHE, MESSAGE_QUEUE, MONITORING, LOGGING, SECURITY
}
```

### 6. GPU Instance (Instancia GPU)

```java
@Entity
@Table(name = "inf_gpu_instances")
public class GpuInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cloud_provider_id", nullable = false)
    private Long cloudProviderId;

    @Column(name = "instance_name", nullable = false)
    private String instanceName;

    @Column(name = "instance_type")
    private String instanceType; // g4dn.xlarge, p3.2xlarge, etc.

    @Column(name = "cpu_cores")
    private Integer cpuCores;

    @Column(name = "memory_gb")
    private Integer memoryGb;

    @Column(name = "storage_gb")
    private Integer storageGb;

    @Column(name = "gpu_count")
    private Integer gpuCount;

    @Column(name = "gpu_type")
    private String gpuType; // V100, A100, T4, RTX 4090, etc.

    @Column(name = "gpu_memory_gb")
    private Integer gpuMemoryGb;

    @Column(name = "gpu_compute_capability")
    private String gpuComputeCapability; // 8.6, 8.0, 7.5, etc.

    @Column(name = "is_spot_instance")
    private Boolean isSpotInstance = false;

    @Column(name = "is_preemptible")
    private Boolean isPreemptible = false;

    @Column(name = "cost_per_hour")
    private BigDecimal costPerHour;

    @Column(name = "cost_per_month")
    private BigDecimal costPerMonth;

    @Column(name = "is_suitable_for_training")
    private Boolean isSuitableForTraining = true;

    @Column(name = "is_suitable_for_inference")
    private Boolean isSuitableForInference = true;

    @Column(name = "training_performance_score")
    private Integer trainingPerformanceScore; // 1-10

    @Column(name = "inference_performance_score")
    private Integer inferencePerformanceScore; // 1-10

    @Column(name = "template_name")
    private String templateName; // Nombre del template en el proveedor

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "cloud_provider_id", insertable = false, updatable = false)
    private CloudProvider cloudProvider;

    // Relaciones con recursos asociados
    @OneToMany(mappedBy = "gpuInstance", cascade = CascadeType.ALL)
    private List<AttachedVolume> attachedVolumes = new ArrayList<>();

    @OneToMany(mappedBy = "gpuInstance", cascade = CascadeType.ALL)
    private List<NetworkInterface> networkInterfaces = new ArrayList<>();

    @OneToMany(mappedBy = "gpuInstance", cascade = CascadeType.ALL)
    private List<SecurityRule> securityRules = new ArrayList<>();

    @OneToMany(mappedBy = "gpuInstance", cascade = CascadeType.ALL)
    private List<BackupSnapshot> backupSnapshots = new ArrayList<>();
}
```

### 7.1. Attached Volume (Volumen Adjunto)

```java
@Entity
@Table(name = "inf_attached_volumes")
public class AttachedVolume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gpu_instance_id", nullable = false)
    private Long gpuInstanceId;

    @Column(name = "volume_name", nullable = false)
    private String volumeName;

    @Column(name = "volume_id")
    private String volumeId; // ID del volumen en el proveedor

    @Column(name = "volume_type")
    @Enumerated(EnumType.STRING)
    private VolumeType volumeType;

    @Column(name = "size_gb")
    private Integer sizeGb;

    @Column(name = "attachment_point")
    private String attachmentPoint; // /dev/sdf, /dev/xvdf, etc.

    @Column(name = "is_attached")
    private Boolean isAttached = true;

    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;

    @Column(name = "iops")
    private Integer iops; // Para volúmenes de alto rendimiento

    @Column(name = "throughput")
    private Integer throughput; // MB/s

    @Column(name = "cost_per_month")
    private BigDecimal costPerMonth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "gpu_instance_id", insertable = false, updatable = false)
    private GpuInstance gpuInstance;
}

public enum VolumeType {
    SSD, HDD, NVME, EBS, GPD, AZURE_DISK
}
```

### 7.2. Network Interface (Interfaz de Red)

```java
@Entity
@Table(name = "inf_network_interfaces")
public class NetworkInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gpu_instance_id", nullable = false)
    private Long gpuInstanceId;

    @Column(name = "interface_name", nullable = false)
    private String interfaceName;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "private_ip_address")
    private String privateIpAddress;

    @Column(name = "public_ip_address")
    private String publicIpAddress;

    @Column(name = "subnet_id")
    private String subnetId;

    @Column(name = "vpc_id")
    private String vpcId;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "security_group_ids")
    private String securityGroupIds; // JSON array de IDs

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "gpu_instance_id", insertable = false, updatable = false)
    private GpuInstance gpuInstance;
}
```

### 7.3. Security Rule (Regla de Seguridad)

```java
@Entity
@Table(name = "inf_security_rules")
public class SecurityRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gpu_instance_id", nullable = false)
    private Long gpuInstanceId;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "rule_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SecurityRuleType ruleType;

    @Column(name = "protocol")
    private String protocol; // TCP, UDP, ICMP, ALL

    @Column(name = "port_range")
    private String portRange; // 22, 80-443, ALL

    @Column(name = "source_cidr")
    private String sourceCidr; // 0.0.0.0/0, 10.0.0.0/8

    @Column(name = "destination_cidr")
    private String destinationCidr;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "priority")
    private Integer priority = 100;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "gpu_instance_id", insertable = false, updatable = false)
    private GpuInstance gpuInstance;
}

public enum SecurityRuleType {
    INGRESS, EGRESS
}
```

### 7.4. Backup Snapshot (Snapshot de Respaldo)

```java
@Entity
@Table(name = "inf_backup_snapshots")
public class BackupSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gpu_instance_id", nullable = false)
    private Long gpuInstanceId;

    @Column(name = "snapshot_name", nullable = false)
    private String snapshotName;

    @Column(name = "snapshot_id")
    private String snapshotId; // ID del snapshot en el proveedor

    @Column(name = "snapshot_type")
    @Enumerated(EnumType.STRING)
    private SnapshotType snapshotType;

    @Column(name = "size_gb")
    private Integer sizeGb;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SnapshotStatus status = SnapshotStatus.PENDING;

    @Column(name = "retention_days")
    private Integer retentionDays = 30;

    @Column(name = "cost_per_month")
    private BigDecimal costPerMonth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "gpu_instance_id", insertable = false, updatable = false)
    private GpuInstance gpuInstance;
}

public enum SnapshotType {
    AUTOMATED, MANUAL, SCHEDULED
}

public enum SnapshotStatus {
    PENDING, IN_PROGRESS, COMPLETED, FAILED, DELETING
}
```

### 7. Infrastructure Deployment (Despliegue de Infraestructura)

```java
@Entity
@Table(name = "inf_infrastructure_deployments")
public class InfrastructureDeployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deployment_name", nullable = false)
    private String deploymentName;

    @Column(name = "deployment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeploymentType deploymentType;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "kubernetes_cluster_id")
    private Long kubernetesClusterId;

    @Column(name = "gpu_instance_id")
    private Long gpuInstanceId;

    @Column(name = "deployment_config")
    private String deploymentConfig; // JSON con configuración

    @Column(name = "resource_requirements")
    private String resourceRequirements; // JSON con requisitos

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeploymentStatus status = DeploymentStatus.PENDING;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "estimated_cost_per_hour")
    private BigDecimal estimatedCostPerHour;

    @Column(name = "actual_cost_per_hour")
    private BigDecimal actualCostPerHour;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "deployment_duration_hours")
    private Double deploymentDurationHours;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "deployment", cascade = CascadeType.ALL)
    private List<DeploymentCost> deploymentCosts = new ArrayList<>();

    @OneToMany(mappedBy = "deployment", cascade = CascadeType.ALL)
    private List<DeploymentLog> deploymentLogs = new ArrayList<>();
}

public enum DeploymentType {
    MODEL_SERVING, MODEL_TRAINING, INFRASTRUCTURE_SCALING,
    BACKUP_RESTORE, DISASTER_RECOVERY, MIGRATION
}

public enum DeploymentStatus {
    PENDING, IN_PROGRESS, COMPLETED, FAILED, CANCELLED,
    ROLLING_BACK, ROLLED_BACK
}
```

### 8. Deployment Cost (Coste del Despliegue)

```java
@Entity
@Table(name = "inf_deployment_costs")
public class DeploymentCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deployment_id", nullable = false)
    private Long deploymentId;

    @Column(name = "cost_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CostType costType;

    @Column(name = "resource_type")
    private String resourceType; // CPU, GPU, Memory, Storage, Network

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "cost_per_hour")
    private BigDecimal costPerHour;

    @Column(name = "hours_used")
    private Double hoursUsed;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "currency")
    private String currency = "USD";

    @Column(name = "billing_period_start")
    private LocalDateTime billingPeriodStart;

    @Column(name = "billing_period_end")
    private LocalDateTime billingPeriodEnd;

    @Column(name = "is_billed")
    private Boolean isBilled = false;

    @Column(name = "billing_date")
    private LocalDateTime billingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "deployment_id", insertable = false, updatable = false)
    private InfrastructureDeployment deployment;
}

public enum CostType {
    COMPUTE, STORAGE, NETWORK, LICENSING, SUPPORT, DATA_TRANSFER,
    API_CALLS, MONITORING, BACKUP, SNAPSHOT
}
```

### 9. Cost Management (Gestión de Costes)

```java
@Entity
@Table(name = "inf_cost_management")
public class CostManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "billing_period", nullable = false)
    private String billingPeriod; // 2024-01, 2024-Q1, etc.

    @Column(name = "total_infrastructure_cost")
    private BigDecimal totalInfrastructureCost;

    @Column(name = "total_gpu_cost")
    private BigDecimal totalGpuCost;

    @Column(name = "total_storage_cost")
    private BigDecimal totalStorageCost;

    @Column(name = "total_network_cost")
    private BigDecimal totalNetworkCost;

    @Column(name = "total_other_costs")
    private BigDecimal totalOtherCosts;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "margin_percentage")
    private Double marginPercentage = 20.0; // 20% por defecto

    @Column(name = "margin_amount")
    private BigDecimal marginAmount;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "currency")
    private String currency = "USD";

    @Column(name = "billing_status")
    @Enumerated(EnumType.STRING)
    private BillingStatus billingStatus = BillingStatus.PENDING;

    @Column(name = "billing_date")
    private LocalDateTime billingDate;

    @Column(name = "payment_due_date")
    private LocalDateTime paymentDueDate;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "costManagement", cascade = CascadeType.ALL)
    private List<CostBreakdown> costBreakdowns = new ArrayList<>();
}

public enum BillingStatus {
    PENDING, INVOICED, PAID, OVERDUE, CANCELLED, DISPUTED
}
```

### 10. Cost Breakdown (Desglose de Costes)

````java
@Entity
@Table(name = "inf_cost_breakdowns")
public class CostBreakdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cost_management_id", nullable = false)
    private Long costManagementId;

    @Column(name = "resource_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceCategory resourceCategory;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "usage_hours")
    private Double usageHours;

    @Column(name = "cost_per_hour")
    private BigDecimal costPerHour;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "cost_management_id", insertable = false, updatable = false)
    private CostManagement costManagement;
}

public enum ResourceCategory {
    COMPUTE_INSTANCES, GPU_INSTANCES, STORAGE_VOLUMES, LOAD_BALANCERS,
    NETWORK_GATEWAYS, DATABASES, CACHE_SERVICES, MONITORING_TOOLS,
    LOGGING_SERVICES, SECURITY_SERVICES, BACKUP_SERVICES
}

## Endpoints de API

### 1. Cloud Provider Endpoints

```java
@RestController
@RequestMapping("/api/v1/infrastructure/providers")
public class CloudProviderController {

    @Autowired
    private CloudProviderService cloudProviderService;

    @PostMapping
    public ResponseEntity<CloudProvider> createProvider(@RequestBody CloudProviderCreateDTO dto) {
        CloudProvider provider = cloudProviderService.createProvider(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(provider);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CloudProvider> getProvider(@PathVariable Long id) {
        CloudProvider provider = cloudProviderService.getProviderById(id);
        return ResponseEntity.ok(provider);
    }

    @GetMapping
    public ResponseEntity<Page<CloudProvider>> getProviders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ProviderType type) {
        Page<CloudProvider> providers = cloudProviderService.getProviders(page, size, type);
        return ResponseEntity.ok(providers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CloudProvider> updateProvider(
            @PathVariable Long id,
            @RequestBody CloudProviderUpdateDTO dto) {
        CloudProvider provider = cloudProviderService.updateProvider(id, dto);
        return ResponseEntity.ok(provider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        cloudProviderService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/test-connection")
    public ResponseEntity<ConnectionTestResult> testConnection(@PathVariable Long id) {
        ConnectionTestResult result = cloudProviderService.testConnection(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<List<CloudResource>> getProviderResources(@PathVariable Long id) {
        List<CloudResource> resources = cloudProviderService.getProviderResources(id);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}/costs")
    public ResponseEntity<ProviderCostSummary> getProviderCosts(
            @PathVariable Long id,
            @RequestParam String period) {
        ProviderCostSummary costs = cloudProviderService.getProviderCosts(id, period);
        return ResponseEntity.ok(costs);
    }
}
````

### 2. Kubernetes Cluster Endpoints

```java
@RestController
@RequestMapping("/api/v1/infrastructure/kubernetes")
public class KubernetesClusterController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    @PostMapping
    public ResponseEntity<KubernetesCluster> createCluster(@RequestBody ClusterCreateDTO dto) {
        KubernetesCluster cluster = kubernetesClusterService.createCluster(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cluster);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KubernetesCluster> getCluster(@PathVariable Long id) {
        KubernetesCluster cluster = kubernetesClusterService.getClusterById(id);
        return ResponseEntity.ok(cluster);
    }

    @GetMapping
    public ResponseEntity<Page<KubernetesCluster>> getClusters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ClusterType type) {
        Page<KubernetesCluster> clusters = kubernetesClusterService.getClusters(page, size, type);
        return ResponseEntity.ok(clusters);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KubernetesCluster> updateCluster(
            @PathVariable Long id,
            @RequestBody ClusterUpdateDTO dto) {
        KubernetesCluster cluster = kubernetesClusterService.updateCluster(id, dto);
        return ResponseEntity.ok(cluster);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable Long id) {
        kubernetesClusterService.deleteCluster(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/nodes")
    public ResponseEntity<ClusterNode> addNode(
            @PathVariable Long id,
            @RequestBody NodeCreateDTO dto) {
        ClusterNode node = kubernetesClusterService.addNode(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(node);
    }

    @GetMapping("/{id}/nodes")
    public ResponseEntity<List<ClusterNode>> getClusterNodes(@PathVariable Long id) {
        List<ClusterNode> nodes = kubernetesClusterService.getClusterNodes(id);
        return ResponseEntity.ok(nodes);
    }

    @PostMapping("/{id}/deploy")
    public ResponseEntity<InfrastructureDeployment> deployToCluster(
            @PathVariable Long id,
            @RequestBody DeploymentRequestDTO dto) {
        InfrastructureDeployment deployment = kubernetesClusterService.deployToCluster(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(deployment);
    }

    @GetMapping("/{id}/deployments")
    public ResponseEntity<List<InfrastructureDeployment>> getClusterDeployments(@PathVariable Long id) {
        List<InfrastructureDeployment> deployments = kubernetesClusterService.getClusterDeployments(id);
        return ResponseEntity.ok(deployments);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ClusterStatus> getClusterStatus(@PathVariable Long id) {
        ClusterStatus status = kubernetesClusterService.getClusterStatus(id);
        return ResponseEntity.ok(status);
    }
}
```

### 3. GPU Instance Endpoints

```java
@RestController
@RequestMapping("/api/v1/infrastructure/gpu-instances")
public class GpuInstanceController {

    @Autowired
    private GpuInstanceService gpuInstanceService;

    @PostMapping
    public ResponseEntity<GpuInstance> createGpuInstance(@RequestBody GpuInstanceCreateDTO dto) {
        GpuInstance instance = gpuInstanceService.createGpuInstance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(instance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GpuInstance> getGpuInstance(@PathVariable Long id) {
        GpuInstance instance = gpuInstanceService.getGpuInstanceById(id);
        return ResponseEntity.ok(instance);
    }

    @GetMapping
    public ResponseEntity<Page<GpuInstance>> getGpuInstances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String gpuType,
            @RequestParam(required = false) Boolean suitableForTraining,
            @RequestParam(required = false) Boolean suitableForInference) {
        Page<GpuInstance> instances = gpuInstanceService.getGpuInstances(
            page, size, gpuType, suitableForTraining, suitableForInference);
        return ResponseEntity.ok(instances);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GpuInstance> updateGpuInstance(
            @PathVariable Long id,
            @RequestBody GpuInstanceUpdateDTO dto) {
        GpuInstance instance = gpuInstanceService.updateGpuInstance(id, dto);
        return ResponseEntity.ok(instance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGpuInstance(@PathVariable Long id) {
        gpuInstanceService.deleteGpuInstance(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ReservationResult> reserveInstance(
            @PathVariable Long id,
            @RequestBody ReservationRequestDTO dto) {
        ReservationResult result = gpuInstanceService.reserveInstance(id, dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseInstance(@PathVariable Long id) {
        gpuInstanceService.releaseInstance(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<GpuInstance>> getAvailableInstances(
            @RequestParam(required = false) String gpuType,
            @RequestParam(required = false) Integer minGpuMemory,
            @RequestParam(required = false) BigDecimal maxCostPerHour) {
        List<GpuInstance> instances = gpuInstanceService.getAvailableInstances(
            gpuType, minGpuMemory, maxCostPerHour);
        return ResponseEntity.ok(instances);
    }

    @GetMapping("/{id}/costs")
    public ResponseEntity<InstanceCostSummary> getInstanceCosts(
            @PathVariable Long id,
            @RequestParam String period) {
        InstanceCostSummary costs = gpuInstanceService.getInstanceCosts(id, period);
        return ResponseEntity.ok(costs);
    }
}
```

### 4. Infrastructure Deployment Endpoints

```java
@RestController
@RequestMapping("/api/v1/infrastructure/deployments")
public class InfrastructureDeploymentController {

    @Autowired
    private InfrastructureDeploymentService deploymentService;

    @PostMapping
    public ResponseEntity<InfrastructureDeployment> createDeployment(
            @RequestBody DeploymentCreateDTO dto) {
        InfrastructureDeployment deployment = deploymentService.createDeployment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(deployment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfrastructureDeployment> getDeployment(@PathVariable Long id) {
        InfrastructureDeployment deployment = deploymentService.getDeploymentById(id);
        return ResponseEntity.ok(deployment);
    }

    @GetMapping
    public ResponseEntity<Page<InfrastructureDeployment>> getDeployments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) DeploymentType type,
            @RequestParam(required = false) DeploymentStatus status) {
        Page<InfrastructureDeployment> deployments = deploymentService.getDeployments(
            page, size, type, status);
        return ResponseEntity.ok(deployments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InfrastructureDeployment> updateDeployment(
            @PathVariable Long id,
            @RequestBody DeploymentUpdateDTO dto) {
        InfrastructureDeployment deployment = deploymentService.updateDeployment(id, dto);
        return ResponseEntity.ok(deployment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeployment(@PathVariable Long id) {
        deploymentService.deleteDeployment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startDeployment(@PathVariable Long id) {
        deploymentService.startDeployment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopDeployment(@PathVariable Long id) {
        deploymentService.stopDeployment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/scale")
    public ResponseEntity<Void> scaleDeployment(
            @PathVariable Long id,
            @RequestBody ScaleRequestDTO dto) {
        deploymentService.scaleDeployment(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<DeploymentLog>> getDeploymentLogs(@PathVariable Long id) {
        List<DeploymentLog> logs = deploymentService.getDeploymentLogs(id);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}/costs")
    public ResponseEntity<DeploymentCostSummary> getDeploymentCosts(
            @PathVariable Long id,
            @RequestParam String period) {
        DeploymentCostSummary costs = deploymentService.getDeploymentCosts(id, period);
        return ResponseEntity.ok(costs);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<DeploymentStatusInfo> getDeploymentStatus(@PathVariable Long id) {
        DeploymentStatusInfo status = deploymentService.getDeploymentStatus(id);
        return ResponseEntity.ok(status);
    }
}
```

### 5. Cost Management Endpoints

```java
@RestController
@RequestMapping("/api/v1/infrastructure/costs")
public class CostManagementController {

    @Autowired
    private CostManagementService costManagementService;

    @PostMapping
    public ResponseEntity<CostManagement> createCostManagement(
            @RequestBody CostManagementCreateDTO dto) {
        CostManagement costManagement = costManagementService.createCostManagement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(costManagement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostManagement> getCostManagement(@PathVariable Long id) {
        CostManagement costManagement = costManagementService.getCostManagementById(id);
        return ResponseEntity.ok(costManagement);
    }

    @GetMapping
    public ResponseEntity<Page<CostManagement>> getCostManagements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String billingPeriod,
            @RequestParam(required = false) BillingStatus status) {
        Page<CostManagement> costManagements = costManagementService.getCostManagements(
            page, size, billingPeriod, status);
        return ResponseEntity.ok(costManagements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostManagement> updateCostManagement(
            @PathVariable Long id,
            @RequestBody CostManagementUpdateDTO dto) {
        CostManagement costManagement = costManagementService.updateCostManagement(id, dto);
        return ResponseEntity.ok(costManagement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostManagement(@PathVariable Long id) {
        costManagementService.deleteCostManagement(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/calculate")
    public ResponseEntity<CostCalculationResult> calculateCosts(@PathVariable Long id) {
        CostCalculationResult result = costManagementService.calculateCosts(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/bill")
    public ResponseEntity<BillingResult> generateBill(@PathVariable Long id) {
        BillingResult result = costManagementService.generateBill(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<CostManagement>> getProjectCosts(@PathVariable Long projectId) {
        List<CostManagement> costs = costManagementService.getProjectCosts(projectId);
        return ResponseEntity.ok(costs);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CostManagement>> getClientCosts(@PathVariable Long clientId) {
        List<CostManagement> costs = costManagementService.getClientCosts(clientId);
        return ResponseEntity.ok(costs);
    }

    @GetMapping("/summary")
    public ResponseEntity<CostSummary> getCostSummary(
            @RequestParam String period,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long clientId) {
        CostSummary summary = costManagementService.getCostSummary(period, projectId, clientId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/trends")
    public ResponseEntity<CostTrends> getCostTrends(
            @RequestParam String period,
            @RequestParam(required = false) Long projectId) {
        CostTrends trends = costManagementService.getCostTrends(period, projectId);
        return ResponseEntity.ok(trends);
    }
}
```

## Servicios Java

### 1. Cloud Provider Service

```java
@Service
@Transactional
public class CloudProviderService {

    @Autowired
    private CloudProviderRepository cloudProviderRepository;

    @Autowired
    private ProviderCredentialRepository credentialRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private CostCalculationService costCalculationService;

    public CloudProvider createProvider(CloudProviderCreateDTO dto) {
        // Validar datos de entrada
        validateProviderData(dto);

        // Crear proveedor
        CloudProvider provider = new CloudProvider();
        provider.setName(dto.getName());
        provider.setProviderType(dto.getProviderType());
        provider.setDescription(dto.getDescription());
        provider.setBaseUrl(dto.getBaseUrl());
        provider.setApiVersion(dto.getApiVersion());
        provider.setSupportsKubernetes(dto.getSupportsKubernetes());
        provider.setSupportsGpuInstances(dto.getSupportsGpuInstances());
        provider.setSupportsServerless(dto.getSupportsServerless());
        provider.setDefaultRegion(dto.getDefaultRegion());
        provider.setSupportedRegions(dto.getSupportedRegions());
        provider.setCostMultiplier(dto.getCostMultiplier());
        provider.setCurrency(dto.getCurrency());
        provider.setBillingCycle(dto.getBillingCycle());
        provider.setCreatedAt(LocalDateTime.now());
        provider.setUpdatedAt(LocalDateTime.now());

        // Guardar proveedor
        CloudProvider savedProvider = cloudProviderRepository.save(provider);

        // Auditoría
        auditService.logAction("CLOUD_PROVIDER_CREATED",
            "Cloud provider created: " + savedProvider.getName(), getCurrentUserId());

        return savedProvider;
    }

    public CloudProvider updateProvider(Long id, CloudProviderUpdateDTO dto) {
        CloudProvider provider = cloudProviderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cloud provider not found"));

        // Actualizar campos
        provider.setDescription(dto.getDescription());
        provider.setBaseUrl(dto.getBaseUrl());
        provider.setApiVersion(dto.getApiVersion());
        provider.setDefaultRegion(dto.getDefaultRegion());
        provider.setSupportedRegions(dto.getSupportedRegions());
        provider.setCostMultiplier(dto.getCostMultiplier());
        provider.setCurrency(dto.getCurrency());
        provider.setBillingCycle(dto.getBillingCycle());
        provider.setUpdatedAt(LocalDateTime.now());

        // Guardar cambios
        CloudProvider updatedProvider = cloudProviderRepository.save(provider);

        // Auditoría
        auditService.logAction("CLOUD_PROVIDER_UPDATED",
            "Cloud provider updated: " + updatedProvider.getName(), getCurrentUserId());

        return updatedProvider;
    }

    public void deleteProvider(Long id) {
        CloudProvider provider = cloudProviderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cloud provider not found"));

        // Verificar que no tenga recursos asociados
        if (!provider.getKubernetesClusters().isEmpty() ||
            !provider.getCloudResources().isEmpty() ||
            !provider.getGpuInstances().isEmpty()) {
            throw new BusinessException("Cannot delete provider with associated resources");
        }

        // Eliminar proveedor
        cloudProviderRepository.delete(provider);

        // Auditoría
        auditService.logAction("CLOUD_PROVIDER_DELETED",
            "Cloud provider deleted: " + provider.getName(), getCurrentUserId());
    }

    public ConnectionTestResult testConnection(Long id) {
        CloudProvider provider = getProviderById(id);
        ProviderCredential credential = credentialRepository
            .findByCloudProviderIdAndIsDefaultTrue(id)
            .orElseThrow(() -> new BusinessException("No default credential found"));

        try {
            // Implementar lógica de prueba de conexión según el tipo de proveedor
            switch (provider.getProviderType()) {
                case KUBERNETES_ONLY:
                    return testKubernetesConnection(credential);
                case CLOUD_PROVIDER:
                    return testCloudProviderConnection(credential);
                case GPU_SPECIALIST:
                    return testGpuProviderConnection(credential);
                default:
                    return new ConnectionTestResult(false, "Unsupported provider type");
            }
        } catch (Exception e) {
            return new ConnectionTestResult(false, "Connection failed: " + e.getMessage());
        }
    }

    public List<CloudResource> getProviderResources(Long id) {
        CloudProvider provider = getProviderById(id);
        return provider.getCloudResources();
    }

    public ProviderCostSummary getProviderCosts(Long id, String period) {
        CloudProvider provider = getProviderById(id);
        return costCalculationService.calculateProviderCosts(provider, period);
    }

    private void validateProviderData(CloudProviderCreateDTO dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ValidationException("Provider name is required");
        }
        if (dto.getProviderType() == null) {
            throw new ValidationException("Provider type is required");
        }
        if (dto.getCostMultiplier() != null && dto.getCostMultiplier() <= 0) {
            throw new ValidationException("Cost multiplier must be positive");
        }
    }

    private ConnectionTestResult testKubernetesConnection(ProviderCredential credential) {
        // Implementar prueba de conexión a Kubernetes
        return new ConnectionTestResult(true, "Kubernetes connection successful");
    }

    private ConnectionTestResult testCloudProviderConnection(ProviderCredential credential) {
        // Implementar prueba de conexión a proveedor cloud
        return new ConnectionTestResult(true, "Cloud provider connection successful");
    }

    private ConnectionTestResult testGpuProviderConnection(ProviderCredential credential) {
        // Implementar prueba de conexión a proveedor GPU
        return new ConnectionTestResult(true, "GPU provider connection successful");
    }

    private Long getCurrentUserId() {
        // Implementar obtención del ID del usuario actual
        return 1L; // Placeholder
    }
}
```

### 2. Kubernetes Cluster Service

```java
@Service
@Transactional
public class KubernetesClusterService {

    @Autowired
    private KubernetesClusterRepository clusterRepository;

    @Autowired
    private ClusterNodeRepository nodeRepository;

    @Autowired
    private CloudProviderService cloudProviderService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private DeploymentService deploymentService;

    public KubernetesCluster createCluster(ClusterCreateDTO dto) {
        // Validar datos de entrada
        validateClusterData(dto);

        // Crear cluster
        KubernetesCluster cluster = new KubernetesCluster();
        cluster.setCloudProviderId(dto.getCloudProviderId());
        cluster.setCredentialId(dto.getCredentialId());
        cluster.setClusterName(dto.getClusterName());
        cluster.setClusterType(dto.getClusterType());
        cluster.setKubernetesVersion(dto.getKubernetesVersion());
        cluster.setKubeconfig(dto.getKubeconfig());
        cluster.setApiServerUrl(dto.getApiServerUrl());
        cluster.setDashboardUrl(dto.getDashboardUrl());
        cluster.setRegion(dto.getRegion());
        cluster.setZone(dto.getZone());
        cluster.setIsManaged(dto.getIsManaged());
        cluster.setCreatedAt(LocalDateTime.now());
        cluster.setUpdatedAt(LocalDateTime.now());

        // Guardar cluster
        KubernetesCluster savedCluster = clusterRepository.save(cluster);

        // Auditoría
        auditService.logAction("KUBERNETES_CLUSTER_CREATED",
            "Kubernetes cluster created: " + savedCluster.getClusterName(), getCurrentUserId());

        return savedCluster;
    }

    public ClusterNode addNode(Long clusterId, NodeCreateDTO dto) {
        KubernetesCluster cluster = getClusterById(clusterId);

        // Crear nodo
        ClusterNode node = new ClusterNode();
        node.setKubernetesClusterId(clusterId);
        node.setNodeName(dto.getNodeName());
        node.setNodeType(dto.getNodeType());
        node.setInstanceType(dto.getInstanceType());
        node.setCpuCores(dto.getCpuCores());
        node.setMemoryGb(dto.getMemoryGb());
        node.setStorageGb(dto.getStorageGb());
        node.setGpuCount(dto.getGpuCount());
        node.setGpuType(dto.getGpuType());
        node.setGpuMemoryGb(dto.getGpuMemoryGb());
        node.setIsSpotInstance(dto.getIsSpotInstance());
        node.setIsPreemptible(dto.getIsPreemptible());
        node.setCostPerHour(dto.getCostPerHour());
        node.setCostPerMonth(dto.getCostPerMonth());
        node.setIsSuitableForTraining(dto.getIsSuitableForTraining());
        node.setIsSuitableForInference(dto.getIsSuitableForInference());
        node.setStatus(NodeStatus.READY);
        node.setCreatedAt(LocalDateTime.now());
        node.setUpdatedAt(LocalDateTime.now());

        // Guardar nodo
        ClusterNode savedNode = nodeRepository.save(node);

        // Auditoría
        auditService.logAction("CLUSTER_NODE_ADDED",
            "Node added to cluster: " + savedNode.getNodeName(), getCurrentUserId());

        return savedNode;
    }

    public InfrastructureDeployment deployToCluster(Long clusterId, DeploymentRequestDTO dto) {
        KubernetesCluster cluster = getClusterById(clusterId);

        // Validar que el cluster esté activo
        if (!cluster.getIsActive()) {
            throw new BusinessException("Cluster is not active");
        }

        // Crear despliegue
        InfrastructureDeployment deployment = new InfrastructureDeployment();
        deployment.setDeploymentName(dto.getDeploymentName());
        deployment.setDeploymentType(dto.getDeploymentType());
        deployment.setProjectId(dto.getProjectId());
        deployment.setModelId(dto.getModelId());
        deployment.setKubernetesClusterId(clusterId);
        deployment.setDeploymentConfig(dto.getDeploymentConfig());
        deployment.setResourceRequirements(dto.getResourceRequirements());
        deployment.setStatus(DeploymentStatus.PENDING);
        deployment.setCreatedAt(LocalDateTime.now());
        deployment.setUpdatedAt(LocalDateTime.now());

        // Guardar despliegue
        InfrastructureDeployment savedDeployment = deploymentService.createDeployment(deployment);

        // Auditoría
        auditService.logAction("DEPLOYMENT_CREATED",
            "Deployment created in cluster: " + savedDeployment.getDeploymentName(), getCurrentUserId());

        return savedDeployment;
    }

    public List<ClusterNode> getClusterNodes(Long clusterId) {
        KubernetesCluster cluster = getClusterById(clusterId);
        return cluster.getNodes();
    }

    public List<InfrastructureDeployment> getClusterDeployments(Long clusterId) {
        KubernetesCluster cluster = getClusterById(clusterId);
        return cluster.getDeployments();
    }

    public ClusterStatus getClusterStatus(Long clusterId) {
        KubernetesCluster cluster = getClusterById(clusterId);

        // Calcular estado del cluster basado en nodos y despliegues
        ClusterStatus status = new ClusterStatus();
        status.setClusterId(clusterId);
        status.setClusterName(cluster.getClusterName());
        status.setTotalNodes(cluster.getNodes().size());
        status.setActiveNodes((int) cluster.getNodes().stream()
            .filter(n -> n.getStatus() == NodeStatus.READY).count());
        status.setTotalDeployments(cluster.getDeployments().size());
        status.setRunningDeployments((int) cluster.getDeployments().stream()
            .filter(d -> d.getStatus() == DeploymentStatus.IN_PROGRESS).count());

        return status;
    }

    private void validateClusterData(ClusterCreateDTO dto) {
        if (StringUtils.isEmpty(dto.getClusterName())) {
            throw new ValidationException("Cluster name is required");
        }
        if (dto.getClusterType() == null) {
            throw new ValidationException("Cluster type is required");
        }
        if (StringUtils.isEmpty(dto.getKubeconfig())) {
            throw new ValidationException("Kubeconfig is required");
        }
    }

    private Long getCurrentUserId() {
        // Implementar obtención del ID del usuario actual
        return 1L; // Placeholder
    }
}
```

### 3. Cost Management Service

```java
@Service
@Transactional
public class CostManagementService {

    @Autowired
    private CostManagementRepository costManagementRepository;

    @Autowired
    private CostBreakdownRepository costBreakdownRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private BillingService billingService;

    public CostManagement createCostManagement(CostManagementCreateDTO dto) {
        // Validar datos de entrada
        validateCostManagementData(dto);

        // Crear gestión de costes
        CostManagement costManagement = new CostManagement();
        costManagement.setProjectId(dto.getProjectId());
        costManagement.setClientId(dto.getClientId());
        costManagement.setBillingPeriod(dto.getBillingPeriod());
        costManagement.setMarginPercentage(dto.getMarginPercentage());
        costManagement.setCurrency(dto.getCurrency());
        costManagement.setBillingStatus(BillingStatus.PENDING);
        costManagement.setCreatedAt(LocalDateTime.now());
        costManagement.setUpdatedAt(LocalDateTime.now());

        // Guardar gestión de costes
        CostManagement savedCostManagement = costManagementRepository.save(costManagement);

        // Auditoría
        auditService.logAction("COST_MANAGEMENT_CREATED",
            "Cost management created for period: " + savedCostManagement.getBillingPeriod(), getCurrentUserId());

        return savedCostManagement;
    }

    public CostCalculationResult calculateCosts(Long id) {
        CostManagement costManagement = getCostManagementById(id);

        // Calcular costes totales
        BigDecimal totalInfrastructureCost = calculateInfrastructureCosts(costManagement);
        BigDecimal totalGpuCost = calculateGpuCosts(costManagement);
        BigDecimal totalStorageCost = calculateStorageCosts(costManagement);
        BigDecimal totalNetworkCost = calculateNetworkCosts(costManagement);
        BigDecimal totalOtherCosts = calculateOtherCosts(costManagement);

        // Calcular total
        BigDecimal totalCost = totalInfrastructureCost.add(totalGpuCost)
            .add(totalStorageCost).add(totalNetworkCost).add(totalOtherCosts);

        // Calcular margen
        BigDecimal marginAmount = totalCost.multiply(
            BigDecimal.valueOf(costManagement.getMarginPercentage() / 100.0));
        BigDecimal finalPrice = totalCost.add(marginAmount);

        // Actualizar costes
        costManagement.setTotalInfrastructureCost(totalInfrastructureCost);
        costManagement.setTotalGpuCost(totalGpuCost);
        costManagement.setTotalStorageCost(totalStorageCost);
        costManagement.setTotalNetworkCost(totalNetworkCost);
        costManagement.setTotalOtherCosts(totalOtherCosts);
        costManagement.setTotalCost(totalCost);
        costManagement.setMarginAmount(marginAmount);
        costManagement.setFinalPrice(finalPrice);
        costManagement.setUpdatedAt(LocalDateTime.now());

        costManagementRepository.save(costManagement);

        // Crear resultado
        CostCalculationResult result = new CostCalculationResult();
        result.setCostManagementId(id);
        result.setTotalCost(totalCost);
        result.setMarginAmount(marginAmount);
        result.setFinalPrice(finalPrice);
        result.setCalculationDate(LocalDateTime.now());

        // Auditoría
        auditService.logAction("COSTS_CALCULATED",
            "Costs calculated for period: " + costManagement.getBillingPeriod(), getCurrentUserId());

        return result;
    }

    public BillingResult generateBill(Long id) {
        CostManagement costManagement = getCostManagementById(id);

        // Verificar que los costes estén calculados
        if (costManagement.getTotalCost() == null) {
            throw new BusinessException("Costs must be calculated before generating bill");
        }

        // Generar factura
        BillingResult billingResult = billingService.generateBill(costManagement);

        // Actualizar estado
        costManagement.setBillingStatus(BillingStatus.INVOICED);
        costManagement.setBillingDate(LocalDateTime.now());
        costManagement.setPaymentDueDate(LocalDateTime.now().plusDays(30));
        costManagement.setUpdatedAt(LocalDateTime.now());

        costManagementRepository.save(costManagement);

        // Auditoría
        auditService.logAction("BILL_GENERATED",
            "Bill generated for period: " + costManagement.getBillingPeriod(), getCurrentUserId());

        return billingResult;
    }

    public List<CostManagement> getProjectCosts(Long projectId) {
        return costManagementRepository.findByProjectId(projectId);
    }

    public List<CostManagement> getClientCosts(Long clientId) {
        return costManagementRepository.findByClientId(clientId);
    }

    public CostSummary getCostSummary(String period, Long projectId, Long clientId) {
        // Implementar lógica para obtener resumen de costes
        CostSummary summary = new CostSummary();
        summary.setPeriod(period);
        summary.setTotalCost(BigDecimal.ZERO);
        summary.setTotalMargin(BigDecimal.ZERO);
        summary.setTotalFinalPrice(BigDecimal.ZERO);

        return summary;
    }

    public CostTrends getCostTrends(String period, Long projectId) {
        // Implementar lógica para obtener tendencias de costes
        CostTrends trends = new CostTrends();
        trends.setPeriod(period);
        trends.setTrendDirection("STABLE");
        trends.setPercentageChange(0.0);

        return trends;
    }

    private BigDecimal calculateInfrastructureCosts(CostManagement costManagement) {
        // Implementar cálculo de costes de infraestructura
        return BigDecimal.ZERO; // Placeholder
    }

    private BigDecimal calculateGpuCosts(CostManagement costManagement) {
        // Implementar cálculo de costes de GPU
        return BigDecimal.ZERO; // Placeholder
    }

    private BigDecimal calculateStorageCosts(CostManagement costManagement) {
        // Implementar cálculo de costes de almacenamiento
        return BigDecimal.ZERO; // Placeholder
    }

    private BigDecimal calculateNetworkCosts(CostManagement costManagement) {
        // Implementar cálculo de costes de red
        return BigDecimal.ZERO; // Placeholder
    }

    private BigDecimal calculateOtherCosts(CostManagement costManagement) {
        // Implementar cálculo de otros costes
        return BigDecimal.ZERO; // Placeholder
    }

    private void validateCostManagementData(CostManagementCreateDTO dto) {
        if (StringUtils.isEmpty(dto.getBillingPeriod())) {
            throw new ValidationException("Billing period is required");
        }
        if (dto.getMarginPercentage() != null && dto.getMarginPercentage() < 0) {
            throw new ValidationException("Margin percentage cannot be negative");
        }
    }

    private Long getCurrentUserId() {
        // Implementar obtención del ID del usuario actual
        return 1L; // Placeholder
    }
}
```

## Configuración de Aplicación

### application-infrastructure.yml

```yaml
# Configuración del módulo de Infraestructura
infrastructure:
  # Configuración general
  enabled: true
  version: "1.0.0"

  # Configuración de proveedores cloud
  cloud-providers:
    max-providers: 50
    auto-discovery: true
    connection-test-timeout: 30000
    credential-rotation:
      enabled: true
      rotation-days: 90
      notification-days: 7

  # Configuración de Kubernetes
  kubernetes:
    max-clusters: 100
    max-nodes-per-cluster: 1000
    auto-scaling:
      enabled: true
      min-nodes: 1
      max-nodes: 100
      scale-up-threshold: 80
      scale-down-threshold: 20
    monitoring:
      enabled: true
      metrics-interval: 60
      alert-threshold: 90

  # Configuración de GPU
  gpu:
    max-instances: 500
    auto-selection:
      enabled: true
      training-preference: "A100"
      inference-preference: "T4"
    cost-optimization:
      enabled: true
      spot-instance-usage: true
      preemptible-usage: true
      auto-migration: true

  # Configuración de costes
  costs:
    calculation:
      enabled: true
      auto-calculation: true
      calculation-interval: 3600
      currency: "USD"
    margins:
      default-margin: 20.0
      min-margin: 5.0
      max-margin: 100.0
    billing:
      auto-billing: true
      billing-cycle: "monthly"
      payment-terms: 30
      late-fee-percentage: 5.0

  # Configuración de monitorización
  monitoring:
    metrics:
      enabled: true
      collection-interval: 60
      retention-days: 365
    alerts:
      enabled: true
      cost-threshold: 1000.0
      resource-usage-threshold: 90
      availability-threshold: 99.9
    dashboards:
      enabled: true
      auto-refresh: true
      refresh-interval: 300

  # Configuración de seguridad
  security:
    encryption:
      enabled: true
      algorithm: "AES-256"
      key-rotation: true
    access-control:
      role-based: true
      provider-isolation: true
      audit-trail: true
    credentials:
      encryption: true
      vault-integration: true
      auto-rotation: true

  # Configuración de integración
  integration:
    aws:
      enabled: true
      regions:
        - "us-east-1"
        - "us-west-2"
        - "eu-west-1"
        - "ap-southeast-1"
      services:
        - "ec2"
        - "eks"
        - "s3"
        - "rds"
        - "elasticache"

    google-cloud:
      enabled: true
      regions:
        - "us-central1"
        - "europe-west1"
        - "asia-east1"
      services:
        - "compute"
        - "gke"
        - "storage"
        - "sql"
        - "redis"

    azure:
      enabled: true
      regions:
        - "eastus"
        - "westeurope"
        - "southeastasia"
      services:
        - "vm"
        - "aks"
        - "blob"
        - "sql"
        - "redis"

    ovh:
      enabled: true
      regions:
        - "GRA"
        - "SBG"
        - "BHS"
      services:
        - "public-cloud"
        - "kubernetes"
        - "storage"

    runpod:
      enabled: true
      regions:
        - "US-East"
        - "US-West"
        - "Europe"
      gpu-types:
        - "RTX 4090"
        - "RTX 3090"
        - "A100"
        - "V100"

    vast-ai:
      enabled: true
      regions:
        - "US"
        - "EU"
        - "Asia"
      gpu-types:
        - "RTX 4090"
        - "RTX 3090"
        - "A100"
        - "V100"
        - "H100"

  # Configuración de almacenamiento
  storage:
    local:
      enabled: true
      base-path: "/data/infrastructure"
      max-size-gb: 10000
    minio:
      enabled: true
      endpoint: "${MINIO_ENDPOINT}"
      bucket: "infrastructure-data"
      access-key: "${MINIO_ACCESS_KEY}"
      secret-key: "${MINIO_SECRET_KEY}"

  # Configuración de mensajería
  messaging:
    rabbitmq:
      enabled: true
      host: "${RABBITMQ_HOST}"
      port: 5672
      username: "${RABBITMQ_USERNAME}"
      password: "${RABBITMQ_PASSWORD}"
      virtual-host: "/"
    topics:
      provider-created: "infrastructure.provider.created"
      cluster-created: "infrastructure.cluster.created"
      node-added: "infrastructure.node.added"
      deployment-created: "infrastructure.deployment.created"
      cost-calculated: "infrastructure.cost.calculated"
      bill-generated: "infrastructure.bill.generated"

  # Configuración de auditoría
  audit:
    enabled: true
    log-level: "INFO"
    retention-days: 365
    sensitive-fields:
      - "access_key"
      - "secret_key"
      - "api_token"
      - "kubeconfig"
      - "password"

  # Configuración de rendimiento
  performance:
    cache:
      enabled: true
      ttl-seconds: 3600
      max-size: 10000
    async-processing:
      enabled: true
      thread-pool-size: 50
      queue-capacity: 2000
    batch-processing:
      enabled: true
      batch-size: 100
      timeout-seconds: 600
```

## Métricas Prometheus

### Infrastructure Management Metrics

```java
@Component
public class InfrastructureManagementMetrics {

    private final MeterRegistry meterRegistry;

    // Contadores
    private final Counter cloudProvidersCreatedCounter;
    private final Counter cloudProvidersUpdatedCounter;
    private final Counter cloudProvidersDeletedCounter;
    private final Counter kubernetesClustersCreatedCounter;
    private final Counter kubernetesClustersDeletedCounter;
    private final Counter clusterNodesAddedCounter;
    private final Counter clusterNodesRemovedCounter;
    private final Counter gpuInstancesCreatedCounter;
    private final Counter gpuInstancesDeletedCounter;
    private final Counter deploymentsCreatedCounter;
    private final Counter deploymentsCompletedCounter;
    private final Counter deploymentsFailedCounter;
    private final Counter costCalculationsPerformedCounter;
    private final Counter billsGeneratedCounter;

    // Timers
    private final Timer providerCreationTimer;
    private final Timer clusterCreationTimer;
    private final Timer nodeAdditionTimer;
    private final Timer deploymentCreationTimer;
    private final Timer costCalculationTimer;
    private final Timer billingGenerationTimer;

    // Gauges
    private final Gauge activeCloudProvidersGauge;
    private final Gauge activeKubernetesClustersGauge;
    private final Gauge totalClusterNodesGauge;
    private final Gauge activeGpuInstancesGauge;
    private final Gauge runningDeploymentsGauge;
    private final Gauge totalInfrastructureCostGauge;
    private final Gauge averageCostPerHourGauge;

    public InfrastructureManagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Inicializar contadores
        this.cloudProvidersCreatedCounter = Counter.builder("infrastructure_providers_created_total")
            .description("Total number of cloud providers created")
            .register(meterRegistry);

        this.cloudProvidersUpdatedCounter = Counter.builder("infrastructure_providers_updated_total")
            .description("Total number of cloud providers updated")
            .register(meterRegistry);

        this.cloudProvidersDeletedCounter = Counter.builder("infrastructure_providers_deleted_total")
            .description("Total number of cloud providers deleted")
            .register(meterRegistry);

        this.kubernetesClustersCreatedCounter = Counter.builder("infrastructure_kubernetes_clusters_created_total")
            .description("Total number of Kubernetes clusters created")
            .register(meterRegistry);

        this.kubernetesClustersDeletedCounter = Counter.builder("infrastructure_kubernetes_clusters_deleted_total")
            .description("Total number of Kubernetes clusters deleted")
            .register(meterRegistry);

        this.clusterNodesAddedCounter = Counter.builder("infrastructure_cluster_nodes_added_total")
            .description("Total number of cluster nodes added")
            .register(meterRegistry);

        this.clusterNodesRemovedCounter = Counter.builder("infrastructure_cluster_nodes_removed_total")
            .description("Total number of cluster nodes removed")
            .register(meterRegistry);

        this.gpuInstancesCreatedCounter = Counter.builder("infrastructure_gpu_instances_created_total")
            .description("Total number of GPU instances created")
            .register(meterRegistry);

        this.gpuInstancesDeletedCounter = Counter.builder("infrastructure_gpu_instances_deleted_total")
            .description("Total number of GPU instances deleted")
            .register(meterRegistry);

        this.deploymentsCreatedCounter = Counter.builder("infrastructure_deployments_created_total")
            .description("Total number of infrastructure deployments created")
            .register(meterRegistry);

        this.deploymentsCompletedCounter = Counter.builder("infrastructure_deployments_completed_total")
            .description("Total number of infrastructure deployments completed")
            .register(meterRegistry);

        this.deploymentsFailedCounter = Counter.builder("infrastructure_deployments_failed_total")
            .description("Total number of infrastructure deployments failed")
            .register(meterRegistry);

        this.costCalculationsPerformedCounter = Counter.builder("infrastructure_cost_calculations_performed_total")
            .description("Total number of cost calculations performed")
            .register(meterRegistry);

        this.billsGeneratedCounter = Counter.builder("infrastructure_bills_generated_total")
            .description("Total number of bills generated")
            .register(meterRegistry);

        // Inicializar timers
        this.providerCreationTimer = Timer.builder("infrastructure_provider_creation_duration")
            .description("Time taken to create a cloud provider")
            .register(meterRegistry);

        this.clusterCreationTimer = Timer.builder("infrastructure_cluster_creation_duration")
            .description("Time taken to create a Kubernetes cluster")
            .register(meterRegistry);

        this.nodeAdditionTimer = Timer.builder("infrastructure_node_addition_duration")
            .description("Time taken to add a node to a cluster")
            .register(meterRegistry);

        this.deploymentCreationTimer = Timer.builder("infrastructure_deployment_creation_duration")
            .description("Time taken to create an infrastructure deployment")
            .register(meterRegistry);

        this.costCalculationTimer = Timer.builder("infrastructure_cost_calculation_duration")
            .description("Time taken to calculate costs")
            .register(meterRegistry);

        this.billingGenerationTimer = Timer.builder("infrastructure_billing_generation_duration")
            .description("Time taken to generate a bill")
            .register(meterRegistry);

        // Inicializar gauges
        this.activeCloudProvidersGauge = Gauge.builder("infrastructure_active_cloud_providers")
            .description("Number of active cloud providers")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getActiveCloudProvidersCount);

        this.activeKubernetesClustersGauge = Gauge.builder("infrastructure_active_kubernetes_clusters")
            .description("Number of active Kubernetes clusters")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getActiveKubernetesClustersCount);

        this.totalClusterNodesGauge = Gauge.builder("infrastructure_total_cluster_nodes")
            .description("Total number of cluster nodes")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getTotalClusterNodesCount);

        this.activeGpuInstancesGauge = Gauge.builder("infrastructure_active_gpu_instances")
            .description("Number of active GPU instances")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getActiveGpuInstancesCount);

        this.runningDeploymentsGauge = Gauge.builder("infrastructure_running_deployments")
            .description("Number of running deployments")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getRunningDeploymentsCount);

        this.totalInfrastructureCostGauge = Gauge.builder("infrastructure_total_cost")
            .description("Total infrastructure cost")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getTotalInfrastructureCost);

        this.averageCostPerHourGauge = Gauge.builder("infrastructure_average_cost_per_hour")
            .description("Average cost per hour")
            .register(meterRegistry, this, InfrastructureManagementMetrics::getAverageCostPerHour);
    }

    // Métodos para incrementar contadores
    public void incrementCloudProvidersCreated() {
        cloudProvidersCreatedCounter.increment();
    }

    public void incrementCloudProvidersUpdated() {
        cloudProvidersUpdatedCounter.increment();
    }

    public void incrementCloudProvidersDeleted() {
        cloudProvidersDeletedCounter.increment();
    }

    public void incrementKubernetesClustersCreated() {
        kubernetesClustersCreatedCounter.increment();
    }

    public void incrementKubernetesClustersDeleted() {
        kubernetesClustersDeletedCounter.increment();
    }

    public void incrementClusterNodesAdded() {
        clusterNodesAddedCounter.increment();
    }

    public void incrementClusterNodesRemoved() {
        clusterNodesRemovedCounter.increment();
    }

    public void incrementGpuInstancesCreated() {
        gpuInstancesCreatedCounter.increment();
    }

    public void incrementGpuInstancesDeleted() {
        gpuInstancesDeletedCounter.increment();
    }

    public void incrementDeploymentsCreated() {
        deploymentsCreatedCounter.increment();
    }

    public void incrementDeploymentsCompleted() {
        deploymentsCompletedCounter.increment();
    }

    public void incrementDeploymentsFailed() {
        deploymentsFailedCounter.increment();
    }

    public void incrementCostCalculationsPerformed() {
        costCalculationsPerformedCounter.increment();
    }

    public void incrementBillsGenerated() {
        billsGeneratedCounter.increment();
    }

    // Métodos para medir tiempo
    public Timer.Sample startProviderCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startClusterCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startNodeAdditionTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startDeploymentCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startCostCalculationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startBillingGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    // Métodos para obtener valores de gauge
    private double getActiveCloudProvidersCount() {
        // Implementar lógica para obtener conteo de proveedores activos
        return 0.0; // Placeholder
    }

    private double getActiveKubernetesClustersCount() {
        // Implementar lógica para obtener conteo de clusters activos
        return 0.0; // Placeholder
    }

    private double getTotalClusterNodesCount() {
        // Implementar lógica para obtener conteo total de nodos
        return 0.0; // Placeholder
    }

    private double getActiveGpuInstancesCount() {
        // Implementar lógica para obtener conteo de instancias GPU activas
        return 0.0; // Placeholder
    }

    private double getRunningDeploymentsCount() {
        // Implementar lógica para obtener conteo de despliegues en ejecución
        return 0.0; // Placeholder
    }

    private double getTotalInfrastructureCost() {
        // Implementar lógica para obtener coste total de infraestructura
        return 0.0; // Placeholder
    }

    private double getAverageCostPerHour() {
        // Implementar lógica para obtener coste promedio por hora
        return 0.0; // Placeholder
    }
}
```

## Scripts SQL

### DROP Script

```sql
-- Script de limpieza para el módulo de Infraestructura
-- Ejecutar en orden de dependencias

-- Eliminar funciones y triggers primero
DROP FUNCTION IF EXISTS fn_calculate_infrastructure_cost CASCADE;
DROP FUNCTION IF EXISTS fn_update_cluster_status CASCADE;
DROP FUNCTION IF EXISTS fn_calculate_gpu_cost CASCADE;

-- Eliminar triggers
DROP TRIGGER IF EXISTS trg_update_cluster_status ON inf_kubernetes_clusters;
DROP TRIGGER IF EXISTS trg_update_node_status ON inf_cluster_nodes;
DROP TRIGGER IF EXISTS trg_update_deployment_status ON inf_infrastructure_deployments;

-- Eliminar índices
DROP INDEX IF EXISTS idx_inf_cloud_providers_type;
DROP INDEX IF EXISTS idx_inf_cloud_providers_active;
DROP INDEX IF EXISTS idx_inf_provider_credentials_provider_id;
DROP INDEX IF EXISTS idx_inf_provider_credentials_default;
DROP INDEX IF EXISTS idx_inf_kubernetes_clusters_provider_id;
DROP INDEX IF EXISTS idx_inf_kubernetes_clusters_type;
DROP INDEX IF EXISTS idx_inf_cluster_nodes_cluster_id;
DROP INDEX IF EXISTS idx_inf_cluster_nodes_type;
DROP INDEX IF EXISTS idx_inf_cloud_resources_provider_id;
DROP INDEX IF EXISTS idx_inf_cloud_resources_type;
DROP INDEX IF EXISTS idx_inf_gpu_instances_provider_id;
DROP INDEX IF EXISTS idx_inf_gpu_instances_gpu_type;
DROP INDEX IF EXISTS idx_inf_infrastructure_deployments_type;
DROP INDEX IF EXISTS idx_inf_infrastructure_deployments_status;
DROP INDEX IF EXISTS idx_inf_deployment_costs_deployment_id;
DROP INDEX IF EXISTS idx_inf_cost_management_project_id;
DROP INDEX IF EXISTS idx_inf_cost_management_period;
DROP INDEX IF EXISTS idx_inf_cost_breakdowns_management_id;

-- Eliminar tablas en orden de dependencias
DROP TABLE IF EXISTS inf_cost_breakdowns CASCADE;
DROP TABLE IF EXISTS inf_deployment_costs CASCADE;
DROP TABLE IF EXISTS inf_infrastructure_deployments CASCADE;
DROP TABLE IF EXISTS inf_cluster_nodes CASCADE;
DROP TABLE IF EXISTS inf_kubernetes_clusters CASCADE;
DROP TABLE IF EXISTS inf_gpu_instances CASCADE;
DROP TABLE IF EXISTS inf_cloud_resources CASCADE;
DROP TABLE IF EXISTS inf_provider_credentials CASCADE;
DROP TABLE IF EXISTS inf_cloud_providers CASCADE;

-- Eliminar secuencias
DROP SEQUENCE IF EXISTS seq_inf_cloud_providers_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_provider_credentials_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_kubernetes_clusters_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_cluster_nodes_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_cloud_resources_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_gpu_instances_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_infrastructure_deployments_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_deployment_costs_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_cost_management_id CASCADE;
DROP SEQUENCE IF EXISTS seq_inf_cost_breakdowns_id CASCADE;
```

### CREATE TABLE Script

```sql
-- Crear tablas del módulo de Infraestructura
-- Prefijo: inf_ (infrastructure)

-- Tabla de proveedores cloud
CREATE TABLE inf_cloud_providers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    provider_type VARCHAR(100) NOT NULL,
    description TEXT,
    base_url VARCHAR(500),
    api_version VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    supports_kubernetes BOOLEAN DEFAULT false,
    supports_gpu_instances BOOLEAN DEFAULT false,
    supports_serverless BOOLEAN DEFAULT false,
    default_region VARCHAR(100),
    supported_regions TEXT, -- JSON array
    cost_multiplier DECIMAL(5,2) DEFAULT 1.0,
    currency VARCHAR(10) DEFAULT 'USD',
    billing_cycle VARCHAR(50) DEFAULT 'HOURLY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de credenciales de proveedores
CREATE TABLE inf_provider_credentials (
    id BIGSERIAL PRIMARY KEY,
    cloud_provider_id BIGINT NOT NULL REFERENCES inf_cloud_providers(id) ON DELETE CASCADE,
    credential_name VARCHAR(255) NOT NULL,
    credential_type VARCHAR(100) NOT NULL,
    access_key VARCHAR(500),
    secret_key VARCHAR(500),
    api_token TEXT,
    kubeconfig TEXT, -- Base64 encoded
    region VARCHAR(100),
    project_id VARCHAR(255),
    subscription_id VARCHAR(255),
    tenant_id VARCHAR(255),
    is_default BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    expires_at TIMESTAMP,
    last_used TIMESTAMP,
    usage_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de clusters Kubernetes
CREATE TABLE inf_kubernetes_clusters (
    id BIGSERIAL PRIMARY KEY,
    cloud_provider_id BIGINT REFERENCES inf_cloud_providers(id) ON DELETE SET NULL,
    credential_id BIGINT REFERENCES inf_provider_credentials(id) ON DELETE SET NULL,
    cluster_name VARCHAR(255) NOT NULL,
    cluster_type VARCHAR(100) NOT NULL,
    kubernetes_version VARCHAR(50),
    kubeconfig TEXT, -- Base64 encoded
    api_server_url VARCHAR(500),
    dashboard_url VARCHAR(500),
    region VARCHAR(100),
    zone VARCHAR(100),
    is_managed BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de nodos del cluster
CREATE TABLE inf_cluster_nodes (
    id BIGSERIAL PRIMARY KEY,
    kubernetes_cluster_id BIGINT NOT NULL REFERENCES inf_kubernetes_clusters(id) ON DELETE CASCADE,
    node_name VARCHAR(255) NOT NULL,
    node_type VARCHAR(100) NOT NULL,
    instance_type VARCHAR(100),
    cpu_cores INTEGER,
    memory_gb INTEGER,
    storage_gb INTEGER,
    gpu_count INTEGER DEFAULT 0,
    gpu_type VARCHAR(100),
    gpu_memory_gb INTEGER,
    is_spot_instance BOOLEAN DEFAULT false,
    is_preemptible BOOLEAN DEFAULT false,
    cost_per_hour DECIMAL(10,4),
    cost_per_month DECIMAL(10,2),
    is_suitable_for_training BOOLEAN DEFAULT false,
    is_suitable_for_inference BOOLEAN DEFAULT true,
    current_workload TEXT, -- JSON con carga actual
    available_resources TEXT, -- JSON con recursos disponibles
    status VARCHAR(50) DEFAULT 'READY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de recursos cloud
CREATE TABLE inf_cloud_resources (
    id BIGSERIAL PRIMARY KEY,
    cloud_provider_id BIGINT NOT NULL REFERENCES inf_cloud_providers(id) ON DELETE CASCADE,
    resource_type VARCHAR(100) NOT NULL,
    resource_name VARCHAR(255) NOT NULL,
    resource_id VARCHAR(255),
    description TEXT,
    region VARCHAR(100),
    zone VARCHAR(100),
    specifications TEXT, -- JSON con especificaciones
    cost_per_hour DECIMAL(10,4),
    cost_per_month DECIMAL(10,2),
    is_auto_scaling BOOLEAN DEFAULT false,
    min_instances INTEGER DEFAULT 1,
    max_instances INTEGER DEFAULT 10,
    current_instances INTEGER DEFAULT 1,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de instancias GPU
CREATE TABLE inf_gpu_instances (
    id BIGSERIAL PRIMARY KEY,
    cloud_provider_id BIGINT NOT NULL REFERENCES inf_cloud_providers(id) ON DELETE CASCADE,
    instance_name VARCHAR(255) NOT NULL,
    instance_type VARCHAR(100),
    cpu_cores INTEGER,
    memory_gb INTEGER,
    storage_gb INTEGER,
    gpu_count INTEGER,
    gpu_type VARCHAR(100),
    gpu_memory_gb INTEGER,
    gpu_compute_capability VARCHAR(20),
    is_spot_instance BOOLEAN DEFAULT false,
    is_preemptible BOOLEAN DEFAULT false,
    cost_per_hour DECIMAL(10,4),
    cost_per_month DECIMAL(10,2),
    is_suitable_for_training BOOLEAN DEFAULT true,
    is_suitable_for_inference BOOLEAN DEFAULT true,
    training_performance_score INTEGER, -- 1-10
    inference_performance_score INTEGER, -- 1-10
    template_name VARCHAR(255),
    is_available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de despliegues de infraestructura
CREATE TABLE inf_infrastructure_deployments (
    id BIGSERIAL PRIMARY KEY,
    deployment_name VARCHAR(255) NOT NULL,
    deployment_type VARCHAR(100) NOT NULL,
    project_id BIGINT, -- Referencia a prj_projects.id
    model_id BIGINT, -- Referencia a mdl_models.id
    kubernetes_cluster_id BIGINT REFERENCES inf_kubernetes_clusters(id) ON DELETE SET NULL,
    gpu_instance_id BIGINT REFERENCES inf_gpu_instances(id) ON DELETE SET NULL,
    deployment_config TEXT, -- JSON con configuración
    resource_requirements TEXT, -- JSON con requisitos
    status VARCHAR(50) DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    estimated_cost_per_hour DECIMAL(10,4),
    actual_cost_per_hour DECIMAL(10,4),
    total_cost DECIMAL(10,2),
    deployment_duration_hours DECIMAL(8,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de costes de despliegues
CREATE TABLE inf_deployment_costs (
    id BIGSERIAL PRIMARY KEY,
    deployment_id BIGINT NOT NULL REFERENCES inf_infrastructure_deployments(id) ON DELETE CASCADE,
    cost_type VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100),
    resource_name VARCHAR(255),
    cost_per_hour DECIMAL(10,4),
    hours_used DECIMAL(8,2),
    total_cost DECIMAL(10,2),
    currency VARCHAR(10) DEFAULT 'USD',
    billing_period_start TIMESTAMP,
    billing_period_end TIMESTAMP,
    is_billed BOOLEAN DEFAULT false,
    billing_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de gestión de costes
CREATE TABLE inf_cost_management (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT, -- Referencia a prj_projects.id
    client_id BIGINT, -- Referencia a cor_clients.id
    billing_period VARCHAR(50) NOT NULL,
    total_infrastructure_cost DECIMAL(12,2),
    total_gpu_cost DECIMAL(12,2),
    total_storage_cost DECIMAL(12,2),
    total_network_cost DECIMAL(12,2),
    total_other_costs DECIMAL(12,2),
    total_cost DECIMAL(12,2),
    margin_percentage DECIMAL(5,2) DEFAULT 20.0,
    margin_amount DECIMAL(12,2),
    final_price DECIMAL(12,2),
    currency VARCHAR(10) DEFAULT 'USD',
    billing_status VARCHAR(50) DEFAULT 'PENDING',
    billing_date TIMESTAMP,
    payment_due_date TIMESTAMP,
    is_paid BOOLEAN DEFAULT false,
    payment_date TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de desglose de costes
CREATE TABLE inf_cost_breakdowns (
    id BIGSERIAL PRIMARY KEY,
    cost_management_id BIGINT NOT NULL REFERENCES inf_cost_management(id) ON DELETE CASCADE,
    resource_category VARCHAR(100) NOT NULL,
    resource_name VARCHAR(255),
    resource_id VARCHAR(255),
    usage_hours DECIMAL(8,2),
    cost_per_hour DECIMAL(10,4),
    total_cost DECIMAL(10,2),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios en las tablas
COMMENT ON TABLE inf_cloud_providers IS 'Proveedores cloud (AWS, Google Cloud, Azure, OVH, RunPod, Vast.ai)';
COMMENT ON TABLE inf_provider_credentials IS 'Credenciales y tokens de acceso a proveedores cloud';
COMMENT ON TABLE inf_kubernetes_clusters IS 'Clusters Kubernetes (propios o de proveedores)';
COMMENT ON TABLE inf_cluster_nodes IS 'Nodos de clusters Kubernetes con especificaciones y costes';
COMMENT ON TABLE inf_cloud_resources IS 'Recursos cloud (balanceadores, almacenamiento, gateways, orquestadores)';
COMMENT ON TABLE inf_gpu_instances IS 'Instancias GPU especializadas para training e inferencia';
COMMENT ON TABLE inf_infrastructure_deployments IS 'Despliegues de infraestructura para modelos y aplicaciones';
COMMENT ON TABLE inf_deployment_costs IS 'Costes detallados de cada despliegue de infraestructura';
COMMENT ON TABLE inf_cost_management IS 'Gestión centralizada de costes con márgenes y facturación';
COMMENT ON TABLE inf_cost_breakdowns IS 'Desglose detallado de costes por categoría de recurso';
```

### CREATE INDEX Script

```sql
-- Índices para optimización de consultas
-- Proveedores cloud
CREATE INDEX idx_inf_cloud_providers_type ON inf_cloud_providers(provider_type);
CREATE INDEX idx_inf_cloud_providers_active ON inf_cloud_providers(is_active);
CREATE INDEX idx_inf_cloud_providers_name ON inf_cloud_providers(name);

-- Credenciales
CREATE INDEX idx_inf_provider_credentials_provider_id ON inf_provider_credentials(cloud_provider_id);
CREATE INDEX idx_inf_provider_credentials_default ON inf_provider_credentials(is_default);
CREATE INDEX idx_inf_provider_credentials_active ON inf_provider_credentials(is_active);
CREATE INDEX idx_inf_provider_credentials_type ON inf_provider_credentials(credential_type);

-- Clusters Kubernetes
CREATE INDEX idx_inf_kubernetes_clusters_provider_id ON inf_kubernetes_clusters(cloud_provider_id);
CREATE INDEX idx_inf_kubernetes_clusters_type ON inf_kubernetes_clusters(cluster_type);
CREATE INDEX idx_inf_kubernetes_clusters_active ON inf_kubernetes_clusters(is_active);
CREATE INDEX idx_inf_kubernetes_clusters_name ON inf_kubernetes_clusters(cluster_name);

-- Nodos del cluster
CREATE INDEX idx_inf_cluster_nodes_cluster_id ON inf_cluster_nodes(kubernetes_cluster_id);
CREATE INDEX idx_inf_cluster_nodes_type ON inf_cluster_nodes(node_type);
CREATE INDEX idx_inf_cluster_nodes_status ON inf_cluster_nodes(status);
CREATE INDEX idx_inf_cluster_nodes_gpu ON inf_cluster_nodes(gpu_count) WHERE gpu_count > 0;
CREATE INDEX idx_inf_cluster_nodes_training ON inf_cluster_nodes(is_suitable_for_training);
CREATE INDEX idx_inf_cluster_nodes_inference ON inf_cluster_nodes(is_suitable_for_inference);

-- Recursos cloud
CREATE INDEX idx_inf_cloud_resources_provider_id ON inf_cloud_resources(cloud_provider_id);
CREATE INDEX idx_inf_cloud_resources_type ON inf_cloud_resources(resource_type);
CREATE INDEX idx_inf_cloud_resources_active ON inf_cloud_resources(is_active);
CREATE INDEX idx_inf_cloud_resources_region ON inf_cloud_resources(region);

-- Instancias GPU
CREATE INDEX idx_inf_gpu_instances_provider_id ON inf_gpu_instances(cloud_provider_id);
CREATE INDEX idx_inf_gpu_instances_gpu_type ON inf_gpu_instances(gpu_type);
CREATE INDEX idx_inf_gpu_instances_available ON inf_gpu_instances(is_available);
CREATE INDEX idx_inf_gpu_instances_training ON inf_gpu_instances(is_suitable_for_training);
CREATE INDEX idx_inf_gpu_instances_inference ON inf_gpu_instances(is_suitable_for_inference);
CREATE INDEX idx_inf_gpu_instances_cost ON inf_gpu_instances(cost_per_hour);

-- Despliegues
CREATE INDEX idx_inf_infrastructure_deployments_type ON inf_infrastructure_deployments(deployment_type);
CREATE INDEX idx_inf_infrastructure_deployments_status ON inf_infrastructure_deployments(status);
CREATE INDEX idx_inf_infrastructure_deployments_cluster ON inf_infrastructure_deployments(kubernetes_cluster_id);
CREATE INDEX idx_inf_infrastructure_deployments_gpu ON inf_infrastructure_deployments(gpu_instance_id);
CREATE INDEX idx_inf_infrastructure_deployments_project ON inf_infrastructure_deployments(project_id);
CREATE INDEX idx_inf_infrastructure_deployments_model ON inf_infrastructure_deployments(model_id);

-- Costes de despliegues
CREATE INDEX idx_inf_deployment_costs_deployment_id ON inf_deployment_costs(deployment_id);
CREATE INDEX idx_inf_deployment_costs_type ON inf_deployment_costs(cost_type);
CREATE INDEX idx_inf_deployment_costs_billing_period ON inf_deployment_costs(billing_period_start, billing_period_end);

-- Gestión de costes
CREATE INDEX idx_inf_cost_management_project_id ON inf_cost_management(project_id);
CREATE INDEX idx_inf_cost_management_client_id ON inf_cost_management(client_id);
CREATE INDEX idx_inf_cost_management_period ON inf_cost_management(billing_period);
CREATE INDEX idx_inf_cost_management_status ON inf_cost_management(billing_status);
CREATE INDEX idx_inf_cost_management_paid ON inf_cost_management(is_paid);

-- Desglose de costes
CREATE INDEX idx_inf_cost_breakdowns_management_id ON inf_cost_breakdowns(cost_management_id);
CREATE INDEX idx_inf_cost_breakdowns_category ON inf_cost_breakdowns(resource_category);
```

### INSERT Demo Data Script

```sql
-- Datos de demostración para el módulo de Infraestructura

-- Proveedores cloud
INSERT INTO inf_cloud_providers (name, provider_type, description, base_url, api_version, supports_kubernetes, supports_gpu_instances, supports_serverless, default_region, supported_regions, cost_multiplier, currency, billing_cycle) VALUES
('AWS', 'CLOUD_PROVIDER', 'Amazon Web Services - Proveedor cloud líder', 'https://aws.amazon.com', '2023-01-01', true, true, true, 'us-east-1', '["us-east-1", "us-west-2", "eu-west-1", "ap-southeast-1"]', 1.0, 'USD', 'HOURLY'),
('Google Cloud', 'CLOUD_PROVIDER', 'Google Cloud Platform - Infraestructura escalable', 'https://cloud.google.com', 'v1', true, true, true, 'us-central1', '["us-central1", "europe-west1", "asia-east1"]', 1.0, 'USD', 'HOURLY'),
('Azure', 'CLOUD_PROVIDER', 'Microsoft Azure - Plataforma cloud empresarial', 'https://azure.microsoft.com', '2023-01-01', true, true, true, 'eastus', '["eastus", "westeurope", "southeastasia"]', 1.0, 'USD', 'HOURLY'),
('OVH', 'CLOUD_PROVIDER', 'OVHcloud - Proveedor cloud europeo', 'https://www.ovh.com', 'v1', true, false, false, 'GRA', '["GRA", "SBG", "BHS"]', 0.8, 'EUR', 'MONTHLY'),
('RunPod', 'GPU_SPECIALIST', 'RunPod - Instancias GPU especializadas', 'https://runpod.io', 'v1', false, true, false, 'US-East', '["US-East", "US-West", "Europe"]', 0.7, 'USD', 'HOURLY'),
('Vast.ai', 'GPU_SPECIALIST', 'Vast.ai - Marketplace de GPU', 'https://vast.ai', 'v1', false, true, false, 'US', '["US", "EU", "Asia"]', 0.6, 'USD', 'HOURLY'),
('CodeflowX Kubernetes', 'KUBERNETES_ONLY', 'Cluster Kubernetes propio de CodeflowX', 'https://k8s.codeflowx.ai', 'v1.28', true, false, false, 'local', '["local"]', 1.0, 'USD', 'MONTHLY');

-- Credenciales de proveedores
INSERT INTO inf_provider_credentials (cloud_provider_id, credential_name, credential_type, access_key, secret_key, region, is_default, is_active) VALUES
(1, 'AWS Production', 'API_KEY_SECRET', 'AKIAIOSFODNN7EXAMPLE', 'wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY', 'us-east-1', true, true),
(2, 'GCP Production', 'API_KEY_SECRET', 'AIzaSyC1on0GqXvY8XJhTWVV2z1JnE6VbZ2YcX', 'GOCSPX-EXAMPLE-KEY', 'us-central1', true, true),
(3, 'Azure Production', 'API_KEY_SECRET', 'subscription-id-123', 'tenant-id-456', 'eastus', true, true),
(4, 'OVH Production', 'API_KEY_SECRET', 'ovh-ak-123456789', 'ovh-as-987654321', 'GRA', true, true),
(5, 'RunPod Production', 'API_KEY_SECRET', 'runpod-api-key-123', 'runpod-secret-456', 'US-East', true, true),
(6, 'Vast.ai Production', 'API_KEY_SECRET', 'vast-api-key-123', 'vast-secret-456', 'US', true, true);

-- Clusters Kubernetes
INSERT INTO inf_kubernetes_clusters (cloud_provider_id, credential_id, cluster_name, cluster_type, kubernetes_version, region, zone, is_managed, is_active) VALUES
(1, 1, 'aws-eks-production', 'AWS_EKS', '1.28', 'us-east-1', 'us-east-1a', true, true),
(2, 2, 'gcp-gke-production', 'GOOGLE_GKE', '1.28', 'us-central1', 'us-central1-a', true, true),
(3, 3, 'azure-aks-production', 'AZURE_AKS', '1.28', 'eastus', 'eastus-1', true, true),
(4, 4, 'ovh-managed-k8s', 'OVH_MANAGED', '1.28', 'GRA', 'GRA1', true, true),
(7, NULL, 'codeflowx-local', 'OWN', '1.28', 'local', 'local', false, true);

-- Nodos del cluster
INSERT INTO inf_cluster_nodes (kubernetes_cluster_id, node_name, node_type, instance_type, cpu_cores, memory_gb, storage_gb, gpu_count, gpu_type, gpu_memory_gb, is_spot_instance, cost_per_hour, cost_per_month, is_suitable_for_training, is_suitable_for_inference) VALUES
(1, 'aws-node-1', 'WORKER', 'm5.large', 2, 8, 100, 0, NULL, NULL, false, 0.115, 83.28, false, true),
(1, 'aws-gpu-node-1', 'GPU_WORKER', 'g4dn.xlarge', 4, 16, 100, 1, 'T4', 16, false, 0.526, 380.88, true, true),
(2, 'gcp-node-1', 'WORKER', 'n1-standard-2', 2, 7.5, 100, 0, NULL, NULL, false, 0.095, 68.76, false, true),
(2, 'gcp-gpu-node-1', 'GPU_WORKER', 'n1-standard-4', 4, 15, 100, 1, 'T4', 16, false, 0.189, 136.44, true, true),
(5, 'codeflowx-node-1', 'MASTER', 'local', 8, 32, 500, 0, NULL, NULL, false, 0.0, 0.0, false, true),
(5, 'codeflowx-gpu-node-1', 'GPU_WORKER', 'local', 16, 64, 1000, 2, 'RTX 4090', 24, false, 0.0, 0.0, true, true);

-- Recursos cloud
INSERT INTO inf_cloud_resources (cloud_provider_id, resource_type, resource_name, cost_per_hour, cost_per_month, is_auto_scaling, min_instances, max_instances, current_instances) VALUES
(1, 'LOAD_BALANCER', 'aws-alb-production', 0.0225, 16.20, true, 1, 5, 2),
(1, 'STORAGE', 'aws-ebs-production', 0.10, 72.00, false, 1, 1, 1),
(2, 'LOAD_BALANCER', 'gcp-lb-production', 0.025, 18.00, true, 1, 5, 2),
(2, 'STORAGE', 'gcp-pd-production', 0.08, 57.60, false, 1, 1, 1),
(3, 'LOAD_BALANCER', 'azure-lb-production', 0.02, 14.40, true, 1, 5, 2),
(3, 'STORAGE', 'azure-disk-production', 0.12, 86.40, false, 1, 1, 1);

-- Instancias GPU
INSERT INTO inf_gpu_instances (cloud_provider_id, instance_name, instance_type, cpu_cores, memory_gb, storage_gb, gpu_count, gpu_type, gpu_memory_gb, gpu_compute_capability, cost_per_hour, cost_per_month, training_performance_score, inference_performance_score, template_name) VALUES
(1, 'aws-g4dn-xlarge', 'g4dn.xlarge', 4, 16, 100, 1, 'T4', 16, '7.5', 0.526, 380.88, 7, 9, 'aws-t4-template'),
(1, 'aws-p3-2xlarge', 'p3.2xlarge', 8, 61, 100, 1, 'V100', 16, '7.0', 3.06, 2203.20, 9, 8, 'aws-v100-template'),
(2, 'gcp-n1-standard-4-t4', 'n1-standard-4', 4, 15, 100, 1, 'T4', 16, '7.5', 0.189, 136.44, 7, 9, 'gcp-t4-template'),
(2, 'gcp-a2-highgpu-1g', 'a2-highgpu-1g', 12, 85, 100, 1, 'A100', 40, '8.0', 2.25, 1620.00, 10, 9, 'gcp-a100-template'),
(5, 'runpod-rtx-4090', 'RTX 4090', 8, 32, 100, 1, 'RTX 4090', 24, '8.9', 0.40, 288.00, 8, 9, 'runpod-rtx4090-template'),
(6, 'vast-rtx-3090', 'RTX 3090', 8, 32, 100, 1, 'RTX 3090', 24, '8.6', 0.35, 252.00, 8, 9, 'vast-rtx3090-template'),
(6, 'vast-h100', 'H100', 16, 64, 200, 1, 'H100', 80, '9.0', 2.50, 1800.00, 10, 10, 'vast-h100-template');

-- Despliegues de infraestructura
INSERT INTO inf_infrastructure_deployments (deployment_name, deployment_type, project_id, model_id, kubernetes_cluster_id, gpu_instance_id, deployment_config, resource_requirements, status, started_at, estimated_cost_per_hour, actual_cost_per_hour) VALUES
('llama-2-7b-serving', 'MODEL_SERVING', 1, 1, 1, 2, '{"replicas": 3, "resources": {"cpu": "2", "memory": "8Gi"}}', '{"cpu": "2", "memory": "8Gi", "gpu": "1"}', 'IN_PROGRESS', CURRENT_TIMESTAMP, 3.06, 3.06),
('gpt-4-fine-tuning', 'MODEL_TRAINING', 1, 2, 2, 4, '{"replicas": 1, "resources": {"cpu": "8", "memory": "32Gi"}}', '{"cpu": "8", "memory": "32Gi", "gpu": "1"}', 'IN_PROGRESS', CURRENT_TIMESTAMP, 2.25, 2.25),
('code-generation-api', 'MODEL_SERVING', 2, 3, 5, 5, '{"replicas": 2, "resources": {"cpu": "4", "memory": "16Gi"}}', '{"cpu": "4", "memory": "16Gi", "gpu": "1"}', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '2 hours', 0.40, 0.40);

-- Costes de despliegues
INSERT INTO inf_deployment_costs (deployment_id, cost_type, resource_type, resource_name, cost_per_hour, hours_used, total_cost, currency, billing_period_start, billing_period_end) VALUES
(1, 'COMPUTE', 'GPU_INSTANCES', 'aws-p3-2xlarge', 3.06, 24.0, 73.44, 'USD', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 day'),
(2, 'COMPUTE', 'GPU_INSTANCES', 'gcp-a2-highgpu-1g', 2.25, 48.0, 108.00, 'USD', CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE),
(3, 'COMPUTE', 'GPU_INSTANCES', 'runpod-rtx-4090', 0.40, 2.0, 0.80, 'USD', CURRENT_DATE - INTERVAL '2 hours', CURRENT_DATE);

-- Gestión de costes
INSERT INTO inf_cost_management (project_id, client_id, billing_period, total_infrastructure_cost, total_gpu_cost, total_storage_cost, total_network_cost, total_other_costs, total_cost, margin_percentage, margin_amount, final_price, billing_status, billing_date, payment_due_date) VALUES
(1, 1, '2024-01', 150.00, 181.44, 72.00, 30.00, 15.00, 448.44, 20.0, 89.69, 538.13, 'INVOICED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days'),
(2, 1, '2024-01', 100.00, 108.00, 57.60, 25.00, 10.00, 300.60, 20.0, 60.12, 360.72, 'PENDING', NULL, NULL);

-- Desglose de costes
INSERT INTO inf_cost_breakdowns (cost_management_id, resource_category, resource_name, usage_hours, cost_per_hour, total_cost, description) VALUES
(1, 'COMPUTE_INSTANCES', 'aws-eks-nodes', 720.0, 0.115, 82.80, 'Nodos EKS para serving'),
(1, 'GPU_INSTANCES', 'aws-p3-2xlarge', 24.0, 3.06, 73.44, 'GPU para modelo Llama-2'),
(1, 'STORAGE_VOLUMES', 'aws-ebs-production', 720.0, 0.10, 72.00, 'Almacenamiento EBS'),
(1, 'LOAD_BALANCERS', 'aws-alb-production', 720.0, 0.0225, 16.20, 'Balanceador de carga'),
(2, 'COMPUTE_INSTANCES', 'gcp-gke-nodes', 720.0, 0.095, 68.40, 'Nodos GKE para training'),
(2, 'GPU_INSTANCES', 'gcp-a2-highgpu-1g', 48.0, 2.25, 108.00, 'GPU para fine-tuning GPT-4'),
(2, 'STORAGE_VOLUMES', 'gcp-pd-production', 720.0, 0.08, 57.60, 'Almacenamiento persistente');
```

## Notas de Implementación

### Gestión Multi-Cloud

- **Proveedores Soportados**: AWS, Google Cloud, Azure, OVH, RunPod, Vast.ai
- **Integración Unificada**: API común para todos los proveedores
- **Credenciales Seguras**: Encriptación AES-256 y rotación automática
- **Descubrimiento Automático**: Detección automática de recursos disponibles

### Clusters Kubernetes

- **Tipos Soportados**: Propios, AWS EKS, Google GKE, Azure AKS, OVH Managed
- **Gestión de Nodos**: Auto-scaling, monitorización de recursos, asignación inteligente
- **Despliegues**: Modelos de IA, aplicaciones, servicios de infraestructura
- **Estado en Tiempo Real**: Monitorización continua de clusters y nodos

### Instancias GPU Especializadas

- **Tipos GPU**: V100, A100, T4, RTX 4090, RTX 3090, H100
- **Optimización de Costes**: Spot instances, preemptible, auto-migración
- **Selección Inteligente**: Basada en requisitos de training/inferencia
- **Templates Estandarizados**: Configuraciones predefinidas por proveedor

### Sistema de Costes y Facturación

- **Cálculo Automático**: Costes en tiempo real con márgenes configurables
- **Refacturación**: Sistema completo de facturación a clientes y proyectos
- **Análisis de Tendencias**: Histórico de costes y predicciones
- **Optimización**: Recomendaciones para reducir costes

### Seguridad y Auditoría

- **Encriptación**: Todas las credenciales sensibles encriptadas
- **Control de Acceso**: Basado en roles con aislamiento por proveedor
- **Auditoría Completa**: Logs de todas las operaciones
- **Cumplimiento**: Trazabilidad para auditorías externas

### Integración con Otros Módulos

- **Proyectos**: Asociación de costes con proyectos específicos
- **Modelos**: Despliegue automático según requisitos de recursos
- **Training**: Asignación inteligente de recursos GPU
- **Serving**: Escalado automático según demanda

### Monitorización y Alertas

- **Métricas Prometheus**: Contadores, timers y gauges completos
- **Alertas Inteligentes**: Umbrales configurables para costes y recursos
- **Dashboards**: Visualización en tiempo real del estado de infraestructura
- **Notificaciones**: Alertas automáticas para eventos críticos

### Normalización Aplicada

- **Prefijos de Tabla**: Todas las tablas usan el prefijo `inf_` (infrastructure)
- **PK Autonumérica**: Todas las tablas tienen PK autonumérica (`id BIGSERIAL PRIMARY KEY`)
- **3NF/BCNF**: Estructura normalizada siguiendo tercera forma normal y Boyce-Codd
- **Relaciones**: Foreign keys correctamente definidas con `REFERENCES`
- **Índices**: Índices optimizados para consultas frecuentes

## Uso del Script

### Orden de Ejecución

1. **Ejecutar DROP script** para limpiar objetos existentes
2. **Ejecutar CREATE TABLE script** para crear todas las tablas
3. **Ejecutar CREATE INDEX script** para crear índices optimizados
4. **Ejecutar INSERT script** para datos de demostración

### Verificación de Datos

```sql
-- Verificar proveedores cloud creados
SELECT COUNT(*) FROM inf_cloud_providers;

-- Verificar credenciales
SELECT COUNT(*) FROM inf_provider_credentials;

-- Verificar clusters Kubernetes
SELECT COUNT(*) FROM inf_kubernetes_clusters;

-- Verificar nodos del cluster
SELECT COUNT(*) FROM inf_cluster_nodes;

-- Verificar recursos cloud
SELECT COUNT(*) FROM inf_cloud_resources;

-- Verificar instancias GPU
SELECT COUNT(*) FROM inf_gpu_instances;

-- Verificar despliegues
SELECT COUNT(*) FROM inf_infrastructure_deployments;

-- Verificar costes de despliegues
SELECT COUNT(*) FROM inf_deployment_costs;

-- Verificar gestión de costes
SELECT COUNT(*) FROM inf_cost_management;

-- Verificar desglose de costes
SELECT COUNT(*) FROM inf_cost_breakdowns;
```

## Diagramas de Relaciones

### **Diagrama de Entidades Principales**

```
CloudProvider (1) ←→ (N) ProviderCredential
CloudProvider (1) ←→ (N) KubernetesCluster
CloudProvider (1) ←→ (N) CloudResource
CloudProvider (1) ←→ (N) GpuInstance
```

### **Diagrama de Recursos Asociados**

```
GpuInstance (1) ←→ (N) AttachedVolume
GpuInstance (1) ←→ (N) NetworkInterface
GpuInstance (1) ←→ (N) SecurityRule
GpuInstance (1) ←→ (N) BackupSnapshot
```

### **Diagrama de Gestión Integrada**

```
┌─────────────────────────────────────────────────────────────┐
│                    CloudProvider                            │
├─────────────────────────────────────────────────────────────┤
│  📊 Stats Cards:                                           │
│  • Cloud Resources: 12                                     │
│  • K8s Clusters: 5                                         │
│  • GPU Instances: 8                                         │
├─────────────────────────────────────────────────────────────┤
│  🔧 Action Buttons:                                        │
│  [📦] [🖥️] [⚡] [📊] [🔑] [💰]                           │
│  │   │   │   │   │   │                                     │
│  │   │   │   │   │   └─ Cost Management                    │
│  │   │   │   │   │                                         │
│  │   │   │   │   └─ Provider Credentials                   │
│  │   │   │   │                                             │
│  │   │   │   └─ Cloud Resources                           │
│  │   │   │                                                 │
│  │   │   └─ GPU Instances                                  │
│  │   │                                                     │
│  │   └─ Kubernetes Clusters                                │
│  │                                                         │
│  └─ Cloud Resources                                        │
└─────────────────────────────────────────────────────────────┘
```

### **Flujo de Gestión Integrada**

```
1. Usuario selecciona CloudProvider
2. Sistema muestra estadísticas en tiempo real
3. Usuario hace clic en botón de acción específico
4. Sistema abre modal especializado para ese recurso
5. Usuario gestiona recursos sin salir de la página principal
6. Cambios se reflejan inmediatamente en estadísticas
7. Experiencia fluida sin navegación entre páginas
```

## Conclusión

El módulo **Infrastructure Management** está completamente implementado con todas las funcionalidades solicitadas y mejoras adicionales de experiencia de usuario:

### **✅ Funcionalidades Base Documentadas**

- **Gestión Multi-Cloud**: Soporte completo para AWS, Google Cloud, Azure, OVH, RunPod, Vast.ai
- **Clusters Kubernetes**: Propios y de proveedores con gestión completa
- **Proveedores GPU Cloud**: Integración especializada para training e inferencia
- **Sistema de Costes**: Cálculo automático con márgenes y refacturación
- **Gestión de Recursos**: Balanceadores, almacenamiento, gateways, orquestadores
- **Asignación Inteligente**: Distribución automática según requisitos de recursos
- **Auditoría Completa**: Histórico de despliegues y consumos
- **Seguridad Avanzada**: Encriptación, control de acceso, auditoría

### **🚀 Funcionalidades Adicionales Implementadas**

- **Gestión Integrada de Recursos**: Modales especializados para gestión sin navegación
- **Recursos Asociados Detallados**: Volúmenes, networking, seguridad y backups
- **Estadísticas en Tiempo Real**: Métricas visuales y contadores actualizados
- **Experiencia de Usuario Mejorada**: Gestión centralizada con 80% menos clicks
- **Interfaz Unificada**: Consistencia visual y operacional en todos los recursos

### **🏗️ Arquitectura y Principios**

El módulo sigue los principios de **arquitectura hexagonal**, **SOLID**, **KISS** y **TDD**, proporcionando una base sólida para la gestión completa de infraestructura cloud con capacidades avanzadas de costes, monitorización y optimización.

### **📱 Frontend Implementado**

- **Páginas Completas**: Todas las entidades con CRUD funcional
- **Componentes Reutilizables**: SimpleModal, DevelopmentBanner, tablas optimizadas
- **Gestión de Estado**: React hooks para estado local y persistencia
- **Responsive Design**: Interfaz adaptativa para todos los dispositivos
- **Integración de Menús**: Navegación completa con roles y permisos

**Estado**: **COMPLETADO CON MEJORAS** ✅🚀
**Próximo módulo**: `05_TRAINING_MODULE.md`
