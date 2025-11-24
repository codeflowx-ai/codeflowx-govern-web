#!/usr/bin/env python3
"""
Script completo para migrar TODAS las referencias de BusinessService a Services
Incluye entidades relacionadas, funciones y procedures
"""

import os
import re
from pathlib import Path
from collections import defaultdict

# Mapeo completo de entidades a Services
ENTITY_TO_SERVICE = {
    # Governance
    "Policy": ("governance", "PolicyService"),
    "PolicyRule": ("governance", "PolicyRuleService"),
    "PolicyEvaluation": ("governance", "PolicyEvaluationService"),
    "ComplianceAssessment": ("governance", "ComplianceAssessmentService"),
    "PolicyViolation": ("governance", "PolicyViolationService"),
    "SecurityPolicy": ("governance", "SecurityPolicyService"),
    "SecurityThreat": ("governance", "SecurityThreatService"),
    "SecurityMetric": ("governance", "SecurityMetricService"),
    "GovernanceMetric": ("governance", "GovernanceMetricService"),
    "PolicyAuditLog": ("governance", "PolicyAuditLogService"),
    "PolicyChecklistItem": ("governance", "PolicyChecklistItemService"),
    "PolicyValidationConfig": ("governance", "PolicyValidationConfigService"),
    "ComplianceRequirement": ("governance", "ComplianceRequirementService"),
    "ComplianceFinding": ("governance", "ComplianceFindingService"),
    "ConformityDeclaration": ("governance", "ConformityDeclarationService"),
    "EthicsReview": ("governance", "EthicsReviewService"),
    "AIActTechnicalDocumentation": ("governance", "AIActTechnicalDocumentationService"),
    "AICCompetence": ("governance", "AICCompetenceService"),
    "AIObjective": ("governance", "AIObjectiveService"),
    "AITTrainingRecord": ("governance", "AITTrainingRecordService"),
    "DatasetQuality": ("governance", "DatasetQualityService"),
    
    # RAG
    "RagClientPolicy": ("rag", "RagClientPolicyService"),
    "RagDataSource": ("rag", "RagDataSourceService"),
    "RagDocument": ("rag", "RagDocumentService"),
    "RagVersion": ("rag", "RagVersionService"),
    "RagEvaluation": ("rag", "RagEvaluationService"),
    "RagChunk": ("rag", "RagChunkService"),
    "RagRollback": ("rag", "RagRollbackService"),
    "RagSystem": ("rag", "RagSystemService"),
    
    # Models
    "Model": ("models", "ModelService"),
    "ModelVersion": ("models", "ModelVersionService"),
    "ModelArtifact": ("models", "ModelArtifactService"),
    "ModelProvider": ("models", "ModelProviderService"),
    "ModelCatalog": ("models", "ModelCatalogService"),
    "ModelCapability": ("models", "ModelCapabilityService"),
    "ModelComparison": ("models", "ModelComparisonService"),
    "ModelDependency": ("models", "ModelDependencyService"),
    "ModelEndpoint": ("models", "ModelEndpointService"),
    "ModelLineage": ("models", "ModelLineageService"),
    "ModelRecommendation": ("models", "ModelRecommendationService"),
    "ModelStageTransition": ("models", "ModelStageTransitionService"),
    "ModelUsage": ("models", "ModelUsageService"),
    "ModelValidation": ("models", "ModelValidationService"),
    "ModelApproval": ("models", "ModelApprovalService"),
    "ModelAdaptationStrategy": ("models", "ModelAdaptationStrategyService"),
    "ProviderCredential": ("models", "ProviderCredentialService"),
    
    # Agents
    "Agent": ("agents", "AgentService"),
    "AgentVersion": ("agents", "AgentVersionService"),
    "AgentDeployment": ("agents", "AgentDeploymentService"),
    "AgentTool": ("agents", "AgentToolService"),
    "AgentWorkflow": ("agents", "AgentWorkflowService"),
    "AgentHealth": ("agents", "AgentHealthService"),
    "AgentMonitoring": ("agents", "AgentMonitoringService"),
    "AgentCollaboration": ("agents", "AgentCollaborationService"),
    "AgentDecision": ("agents", "AgentDecisionService"),
    "AgentGovernance": ("agents", "AgentGovernanceService"),
    "AgentCompliance": ("agents", "AgentComplianceService"),
    "AgentAlert": ("agents", "AgentAlertService"),
    "AgentInteraction": ("agents", "AgentInteractionService"),
    "AgentDomain": ("agents", "AgentDomainService"),
    "AgentApproval": ("agents", "AgentApprovalService"),
    "AgentBiasDetection": ("agents", "AgentBiasDetectionService"),
    "AgentCommunication": ("agents", "AgentCommunicationService"),
    "AgentEthicsAssessment": ("agents", "AgentEthicsAssessmentService"),
    "AgentExpertise": ("agents", "AgentExpertiseService"),
    "AgentRollback": ("agents", "AgentRollbackService"),
    "AgentTransparency": ("agents", "AgentTransparencyService"),
    "AgentWorkflowExecution": ("agents", "AgentWorkflowExecutionService"),
    
    # Training
    "Experiment": ("training", "ExperimentService"),
    "Run": ("training", "RunService"),
    "Checkpoint": ("training", "CheckpointService"),
    "Environment": ("training", "EnvironmentService"),
    "DatasetSource": ("training", "DatasetSourceService"),
    "Tag": ("training", "TagService"),
    "TrainingArtifact": ("training", "TrainingArtifactService"),
    "TrainingAlert": ("training", "TrainingAlertService"),
    "TrainingExecution": ("training", "TrainingExecutionService"),
    "TrainingInfrastructure": ("training", "TrainingInfrastructureService"),
    "TrainingGovernance": ("training", "TrainingGovernanceService"),
    "TrainingMetric": ("training", "TrainingMetricService"),
    "TrainingLog": ("training", "TrainingLogService"),
    "Param": ("training", "ParamService"),
    "MetricStream": ("training", "MetricStreamService"),
    "MetricSeries": ("training", "MetricSeriesService"),
    "HPOExperiment": ("training", "HPOExperimentService"),
    "HPOTrial": ("training", "HPOTrialService"),
    "ExperimentTemplate": ("training", "ExperimentTemplateService"),
    "ExperimentLineage": ("training", "ExperimentLineageService"),
    
    # Serving
    "ModelDeployment": ("serving", "ModelDeploymentService"),
    "ServingEndpoint": ("serving", "ServingEndpointService"),
    "ModelPrediction": ("serving", "ModelPredictionService"),
    "ModelMetrics": ("serving", "ModelMetricsService"),
    "DeploymentInstance": ("serving", "DeploymentInstanceService"),
    "DeploymentMetric": ("serving", "DeploymentMetricService"),
    "DeploymentLog": ("serving", "DeploymentLogService"),
    "ServingRequest": ("serving", "ServingRequestService"),
    
    # Prompts
    "Prompt": ("prompts", "PromptService"),
    "PromptVersion": ("prompts", "PromptVersionService"),
    "PromptValidation": ("prompts", "PromptValidationService"),
    
    # Projects
    "Project": ("projects", "ProjectService"),
    "ProjectVersion": ("projects", "ProjectVersionService"),
    "ProjectMember": ("projects", "ProjectMemberService"),
    "ProjectTask": ("projects", "ProjectTaskService"),
    "ProjectDocument": ("projects", "ProjectDocumentService"),
    "ProjectDomain": ("projects", "ProjectDomainService"),
    "ProjectRequirement": ("projects", "ProjectRequirementService"),
    "ProjectTechnology": ("projects", "ProjectTechnologyService"),
    "ProjectStack": ("projects", "ProjectStackService"),
    "ProjectToken": ("projects", "ProjectTokenService"),
    "ProjectTimeTracking": ("projects", "ProjectTimeTrackingService"),
    "ProjectROI": ("projects", "ProjectROIService"),
    "ProjectResourceConsumption": ("projects", "ProjectResourceConsumptionService"),
    "ProjectLicense": ("projects", "ProjectLicenseService"),
    "ProjectInvoice": ("projects", "ProjectInvoiceService"),
    "ProjectArtifact": ("projects", "ProjectArtifactService"),
    "ProjectBillingDetail": ("projects", "ProjectBillingDetailService"),
    "ProjectCostEstimator": ("projects", "ProjectCostEstimatorService"),
    
    # Infrastructure
    "CloudProvider": ("infrastructure", "CloudProviderService"),
    "CloudRegion": ("infrastructure", "CloudRegionService"),
    "CloudCredential": ("infrastructure", "CloudCredentialService"),
    "CloudResource": ("infrastructure", "CloudResourceService"),
    "KubernetesCluster": ("infrastructure", "KubernetesClusterService"),
    "KubernetesNode": ("infrastructure", "KubernetesNodeService"),
    "GpuInstance": ("infrastructure", "GpuInstanceService"),
    "InfrastructureTemplate": ("infrastructure", "InfrastructureTemplateService"),
    "InfrastructureMetric": ("infrastructure", "InfrastructureMetricService"),
    "InfrastructureCost": ("infrastructure", "InfrastructureCostService"),
    "InfrastructureAudit": ("infrastructure", "InfrastructureAuditService"),
    "ResourceQuota": ("infrastructure", "ResourceQuotaService"),
    
    # Monitoring
    "SystemMetric": ("monitoring", "SystemMetricService"),
    "SystemHealth": ("monitoring", "SystemHealthService"),
    "SystemAlert": ("monitoring", "SystemAlertService"),
    "AuditLog": ("monitoring", "AuditLogService"),
    "MonitoringMetric": ("monitoring", "MonitoringMetricService"),
    "MonitoringAlert": ("monitoring", "MonitoringAlertService"),
    
    # Notifications
    "Notification": ("notifications", "NotificationService"),
    "NotificationTemplate": ("notifications", "NotificationTemplateService"),
    "NotificationChannel": ("notifications", "NotificationChannelService"),
    "NotificationPreference": ("notifications", "NotificationPreferenceService"),
    "NotificationLog": ("notifications", "NotificationLogService"),
    
    # Platform
    "PlatformUpdate": ("platform", "PlatformUpdateService"),
    "PlatformNode": ("platform", "PlatformNodeService"),
    "PlatformLicense": ("platform", "PlatformLicenseService"),
    "UpdateSchedule": ("platform", "UpdateScheduleService"),
    "UpdateInstallation": ("platform", "UpdateInstallationService"),
    "RiskIndicator": ("platform", "RiskIndicatorService"),
    
    # Analytics
    "AnalyticsMetric": ("analytics", "AnalyticsMetricService"),
    "AnalyticsReport": ("analytics", "AnalyticsReportService"),
    
    # Providers
    "Provider": ("providers", "ProviderService"),
    
    # Integrations
    "ExternalPlatform": ("integrations", "ExternalPlatformService"),
}

def find_all_entities_in_file(file_path):
    """Encuentra todas las entidades usadas en el ViewModel"""
    entities_found = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # Buscar imports de entidades
        for entity, (package, service) in ENTITY_TO_SERVICE.items():
            if f"import com.codeflowx.govern.entity.{package}.{entity}" in content:
                entities_found.append((entity, package, service))
            elif f"entity.{package}.{entity}" in content or f"<{entity}>" in content:
                entities_found.append((entity, package, service))
                
        return entities_found
    except Exception as e:
        print(f"Error leyendo {file_path}: {e}")
        return []

def migrate_all_business_service_calls(file_path):
    """Migra TODAS las llamadas a businessService para usar Services"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        entities_found = find_all_entities_in_file(file_path)
        
        if not entities_found:
            return False, "No se encontraron entidades"
        
        # Agregar imports de todos los Services necesarios
        services_to_import = {}
        for entity, package, service in entities_found:
            service_import = f"import com.codeflowx.govern.service.{package}.{service};"
            if service_import not in content:
                services_to_import[service] = (package, service_import)
        
        # Agregar imports después del último import de entity
        for service, (package, service_import) in services_to_import.items():
            import_pattern = r"(import com\.codeflowx\.govern\.entity\.[^;]+;)"
            matches = list(re.finditer(import_pattern, content))
            if matches:
                last_match = matches[-1]
                insert_pos = last_match.end()
                content = content[:insert_pos] + "\n" + service_import + content[insert_pos:]
            else:
                # Agregar después de BusinessService import
                business_import_pattern = r"(import codeflowx\.nocode\.persist\.BusinessService;)"
                if re.search(business_import_pattern, content):
                    content = re.sub(
                        business_import_pattern,
                        rf"\1\n{service_import}",
                        content,
                        count=1
                    )
        
        # Para cada entidad encontrada, agregar @WireVariable del Service
        for entity, package, service in entities_found:
            service_var = service[0].lower() + service[1:]
            service_declaration = f"@WireVariable\n    private {service} {service_var};"
            
            # Verificar si ya existe
            if f"private {service} {service_var};" not in content:
                # Buscar dónde agregar (después de otros @WireVariable)
                wire_variable_pattern = r"(@WireVariable\s+private\s+\w+\s+\w+;)"
                matches = list(re.finditer(wire_variable_pattern, content))
                if matches:
                    last_match = matches[-1]
                    insert_pos = last_match.end()
                    content = content[:insert_pos] + "\n    " + service_declaration + content[insert_pos:]
                else:
                    # Agregar después de BusinessService si existe
                    business_service_pattern = r"(@WireVariable\s+private\s+BusinessService\s+businessService;)"
                    if re.search(business_service_pattern, content):
                        content = re.sub(
                            business_service_pattern,
                            rf"\1\n    {service_declaration}",
                            content
                        )
        
        # Reemplazar todas las llamadas a businessService para cada entidad
        for entity, package, service in entities_found:
            service_var = service[0].lower() + service[1:]
            
            # 1. businessService.findById(Entity.class, id) -> service.findById(id)
            pattern1 = rf"businessService\.findById\(\s*{entity}\.class,\s*(\w+)\)"
            content = re.sub(pattern1, rf"{service_var}.findById(\1)", content)
            
            # 2. businessService.save(entity) -> service.create(entity) o service.update(entity)
            # Esto requiere contexto, así que lo haremos más cuidadosamente
            pattern2 = rf"businessService\.save\(\s*(\w+)\)"
            def replace_save(match):
                var_name = match.group(1)
                # Si la variable tiene ID, es update, sino create
                # Por ahora, usaremos saveOrUpdate que maneja ambos casos
                return f"{var_name} = {service_var}.saveOrUpdate({var_name})"
            content = re.sub(pattern2, replace_save, content)
            
            # 3. businessService.update(entity) -> service.update(entity)
            pattern3 = rf"businessService\.update\(\s*(\w+)\)"
            content = re.sub(pattern3, rf"\1 = {service_var}.update(\1)", content)
            
            # 4. businessService.delete(entity) -> service.delete(entity)
            pattern4 = rf"businessService\.delete\(\s*(\w+)\)"
            content = re.sub(pattern4, rf"{service_var}.delete(\1)", content)
            
            # 5. businessService.removeFromID(Entity.class, id) -> service.deleteById(id)
            pattern5 = rf"businessService\.removeFromID\(\s*{entity}\.class,\s*(\w+)\)"
            content = re.sub(pattern5, rf"{service_var}.deleteById(\1)", content)
            
            # 6. businessService.findAllEntity(Entity.class, params, criterias) -> service.findAll(params, criterias)
            pattern6 = rf"businessService\.findAllEntity\(\s*{entity}\.class,\s*([^,]+),\s*([^)]+)\)"
            content = re.sub(pattern6, rf"{service_var}.findAll(\1, \2)", content)
            
            # 7. businessService.findAllEntity(Entity.class, params) -> service.findAll(params)
            pattern7 = rf"businessService\.findAllEntity\(\s*{entity}\.class,\s*([^)]+)\)"
            content = re.sub(pattern7, rf"{service_var}.findAll(\1)", content)
            
            # 8. businessService.findAllEntity(Entity.class, params, filters) -> service.findAll(params, buildCriteriasFromFilters(filters))
            # Este caso es más complejo, lo dejaremos para revisión manual
        
        # Limpiar initDao() si solo inicializa BusinessService
        init_dao_pattern = r"protected\s+void\s+initDao\(\)\s*\{[^}]*businessService\s*=\s*new\s+BusinessService[^}]*\}"
        if re.search(init_dao_pattern, content, re.DOTALL):
            content = re.sub(
                init_dao_pattern,
                "protected void initDao() {\n        // Ya no es necesario inicializar BusinessService manualmente\n        // Los Services se inyectan automáticamente mediante @WireVariable\n    }",
                content,
                flags=re.DOTALL
            )
        
        # Limpiar destroy() methods
        destroy_pattern = r"businessService\s*=\s*null;"
        if re.search(destroy_pattern, content):
            # Reemplazar con limpieza de todos los services
            services_vars = [service[0].lower() + service[1:] for _, _, service in entities_found]
            services_cleanup = "\n            ".join([f"{var} = null;" for var in services_vars])
            content = re.sub(
                destroy_pattern,
                services_cleanup,
                content
            )
        
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True, f"Migrado - {len(entities_found)} entidades"
        else:
            return False, "No se encontraron cambios necesarios"
            
    except Exception as e:
        return False, f"Error: {str(e)}"

def main():
    base_dir = Path("/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel")
    
    viewmodels = list(base_dir.rglob("*ViewModel.java"))
    
    print(f"Procesando {len(viewmodels)} ViewModels...\n")
    
    migrated = 0
    skipped = 0
    errors = 0
    
    for vm_file in sorted(viewmodels):
        rel_path = str(vm_file.relative_to(base_dir))
        success, message = migrate_all_business_service_calls(vm_file)
        if success:
            print(f"✓ {rel_path} - {message}")
            migrated += 1
        else:
            if "Error" in message:
                print(f"✗ {rel_path} - {message}")
                errors += 1
            else:
                print(f"⊘ {rel_path} - {message}")
                skipped += 1
    
    print(f"\n{'='*60}")
    print(f"Resumen:")
    print(f"  Migrados: {migrated}")
    print(f"  Omitidos: {skipped}")
    print(f"  Errores: {errors}")
    print(f"{'='*60}")

if __name__ == "__main__":
    main()

