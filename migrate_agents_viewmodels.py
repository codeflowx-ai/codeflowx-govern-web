#!/usr/bin/env python3
"""
Script para migrar ViewModels del módulo Agents de businessService a servicios dedicados
"""

import re
from pathlib import Path

# Mapeo de entidades a servicios
ENTITY_TO_SERVICE = {
    "AgentAlert": "AgentAlertService",
    "AgentApproval": "AgentApprovalService",
    "AgentBiasDetection": "AgentBiasDetectionService",
    "AgentCollaboration": "AgentCollaborationService",
    "AgentCommunication": "AgentCommunicationService",
    "AgentCompliance": "AgentComplianceService",
    "AgentDecision": "AgentDecisionService",
    "AgentDeployment": "AgentDeploymentService",
    "AgentDomain": "AgentDomainService",
    "AgentEthicsAssessment": "AgentEthicsAssessmentService",
    "AgentExpertise": "AgentExpertiseService",
    "AgentGovernance": "AgentGovernanceService",
    "AgentHealth": "AgentHealthService",
    "AgentInteraction": "AgentInteractionService",
    "AgentMonitoring": "AgentMonitoringService",
    "AgentRollback": "AgentRollbackService",
    "AgentTool": "AgentToolService",
    "AgentTransparency": "AgentTransparencyService",
    "AgentVersion": "AgentVersionService",
    "AgentWorkflow": "AgentWorkflowService",
    "AgentWorkflowExecution": "AgentWorkflowExecutionService",
    "Agent": "AgentService",
}

def migrate_viewmodel(file_path):
    """Migra un ViewModel individual"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        original_content = content
        entity_name = None
        service_name = None

        # Detectar entidad del ViewModel
        for entity, service in ENTITY_TO_SERVICE.items():
            if entity in file_path.name:
                entity_name = entity
                service_name = service
                break

        if not entity_name:
            return False, "No se encontró entidad conocida"

        # 1. Agregar imports
        if f"import com.codeflowx.govern.service.agents.{service_name};" not in content:
            # Buscar línea de import de BusinessService
            import_pattern = r'(import codeflowx\.nocode\.persist\.BusinessService;)'
            replacement = f'\\1\nimport com.codeflowx.govern.service.agents.{service_name};\nimport com.codeflowx.govern.service.exception.GovernanceServiceException;'
            content = re.sub(import_pattern, replacement, content)

        # 2. Agregar @WireVariable del servicio
        service_var_name = service_name[0].lower() + service_name[1:]  # AgentAlertService -> agentAlertService
        if f"private {service_name} {service_var_name};" not in content:
            # Buscar línea de @WireVariable BusinessService
            wire_pattern = r'(@WireVariable\s+private BusinessService businessService;)'
            replacement = f'\\1\n    \n    @WireVariable\n    private {service_name} {service_var_name};'
            content = re.sub(wire_pattern, replacement, content)

        # 3. Reemplazar findById
        find_by_id_pattern = rf'businessService\.findById\({entity_name}\.class,\s*(\w+)\)'
        replacement = rf'{service_var_name}.findById(\1)'
        content = re.sub(find_by_id_pattern, replacement, content)

        # 4. Reemplazar save -> create
        save_pattern = rf'businessService\.save\(current{entity_name}\)'
        replacement = rf'current{entity_name} = {service_var_name}.create(current{entity_name})'
        content = re.sub(save_pattern, replacement, content)

        # 5. Reemplazar update
        update_pattern = rf'businessService\.update\(current{entity_name}\)'
        replacement = rf'current{entity_name} = {service_var_name}.update(current{entity_name})'
        content = re.sub(update_pattern, replacement, content)

        # 6. Reemplazar findAllEntity
        find_all_pattern = rf'businessService\.findAllEntity\(\s*{entity_name}\.class,\s*([^,]+),\s*([^)]+)\)'
        replacement = rf'{service_var_name}.findAll(\1, \2)'
        content = re.sub(find_all_pattern, replacement, content)

        # 7. Reemplazar removeFromID -> deleteById
        remove_pattern = rf'businessService\.removeFromID\({entity_name}\.class,\s*(\w+)\)'
        replacement = rf'{service_var_name}.deleteById(\1)'
        content = re.sub(remove_pattern, replacement, content)

        # 8. Manejar excepciones - cambiar Exception por GovernanceServiceException donde corresponda
        # Esto es más complejo, mejor hacerlo manualmente

        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True, f"Migrado {entity_name} -> {service_name}"
        else:
            return False, "No se encontraron cambios necesarios"

    except Exception as e:
        return False, f"Error: {str(e)}"

if __name__ == "__main__":
    base_dir = Path("/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/platform/viewmodel/agents")

    viewmodels = list(base_dir.glob("*ViewModel.java"))

    print(f"Procesando {len(viewmodels)} ViewModels...\n")

    migrated = 0
    skipped = 0
    errors = 0

    for vm_file in sorted(viewmodels):
        success, message = migrate_viewmodel(vm_file)
        if success:
            print(f"✓ {vm_file.name} - {message}")
            migrated += 1
        else:
            if "Error" in message:
                print(f"✗ {vm_file.name} - {message}")
                errors += 1
            else:
                print(f"⊘ {vm_file.name} - {message}")
                skipped += 1

    print(f"\n{'='*60}")
    print(f"Resumen:")
    print(f"  Migrados: {migrated}")
    print(f"  Omitidos: {skipped}")
    print(f"  Errores: {errors}")
    print(f"{'='*60}")

