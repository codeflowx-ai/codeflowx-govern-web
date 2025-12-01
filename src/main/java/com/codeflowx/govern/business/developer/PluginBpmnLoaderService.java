package com.codeflowx.govern.business.developer;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para cargar procesos BPMN desde plugins
 */
@Slf4j
@Service
public class PluginBpmnLoaderService {

    @Autowired(required = false)
    private RepositoryService repositoryService;

    /**
     * Carga y despliega proceso BPMN desde plugin
     */
    public Map<String, Object> loadBpmnProcess(Path bpmnPath, Map<String, Object> processDef) throws IOException {
        if (repositoryService == null) {
            log.warn("RepositoryService no disponible - procesos BPMN no se pueden cargar");
            return Map.of("status", "skipped", "reason", "RepositoryService no disponible");
        }

        String processId = (String) processDef.getOrDefault("process_id",
                bpmnPath.getFileName().toString().replace(".bpmn", "").replace(".bpmn20.xml", ""));
        String deploymentName = "plugin-" + processId;

        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(deploymentName)
                    .addInputStream(bpmnPath.getFileName().toString(), Files.newInputStream(bpmnPath))
                    .enableDuplicateFiltering()
                    .deploy();

            Map<String, Object> result = new HashMap<>();
            result.put("status", "loaded");
            result.put("deployment_id", deployment.getId());
            result.put("process_id", processId);
            result.put("deployment_name", deployment.getName());
            result.put("file", bpmnPath.toString());

            log.info("Proceso BPMN desplegado: {} (deploymentId: {})", processId, deployment.getId());
            return result;
        } catch (Exception e) {
            log.error("Error desplegando proceso BPMN: " + bpmnPath, e);
            throw new RuntimeException("Error desplegando proceso BPMN: " + e.getMessage(), e);
        }
    }
}
