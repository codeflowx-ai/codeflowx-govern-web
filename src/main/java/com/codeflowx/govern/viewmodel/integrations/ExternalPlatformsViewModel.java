package com.codeflowx.govern.viewmodel.integrations;

import com.codeflowx.govern.business.integrations.AzureMLConnectorService;
import com.codeflowx.govern.business.integrations.AzureMLConnectorService.AzureDeploymentMetrics;
import com.codeflowx.govern.business.integrations.DataLakeCatalogService;
import com.codeflowx.govern.business.integrations.DatabricksConnectorService;
import com.codeflowx.govern.business.integrations.ExternalIntegrationBusinessService;
import com.codeflowx.govern.business.integrations.FabricConnectorService;
import com.codeflowx.govern.business.integrations.IbmWatsonxConnectorService;
import com.codeflowx.govern.business.integrations.JiraConnectorService;
import com.codeflowx.govern.business.integrations.PurviewConnectorService;
import com.codeflowx.govern.business.integrations.SageMakerConnectorService;
import com.codeflowx.govern.business.integrations.SageMakerConnectorService.SageMakerMetrics;
import com.codeflowx.govern.business.integrations.ServiceNowConnectorService;
import com.codeflowx.govern.business.integrations.SnowflakeConnectorService;
import com.codeflowx.govern.business.integrations.SnowflakeConnectorService.SnowflakeQualityResult;
import com.codeflowx.govern.business.integrations.SparkEvaluationService;
import com.codeflowx.govern.business.integrations.VertexAIConnectorService;
import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.service.integrations.ExternalPlatformService;
import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.context.support.GenericApplicationContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Clients;

/**
 * ViewModel para la gestión de plataformas externas enterprise.
 */
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class ExternalPlatformsViewModel extends MasterPage implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final List<String> SUPPORTED_PLATFORMS = List.of(
        "DATABRICKS",
        "SNOWFLAKE",
        "AZURE_ML",
        "SAGEMAKER",
        "S3",
        "AZURE_BLOB",
        "GCS",
        "SPARK",
        "PURVIEW",
        "FABRIC",
        "VERTEX_AI",
        "IBM_WATSONX",
        "SERVICENOW",
        "JIRA"
    );

    private static final List<String> DATASET_PLATFORMS = List.of(
        "SNOWFLAKE",
        "S3",
        "AZURE_BLOB",
        "GCS",
        "PURVIEW",
        "FABRIC",
        "SERVICENOW",
        "JIRA"
    );

    @WireVariable
    private ExternalIntegrationBusinessService integrationService;

    @WireVariable
    private DatabricksConnectorService databricksConnectorService;

    @WireVariable
    private SnowflakeConnectorService snowflakeConnectorService;

    @WireVariable
    private AzureMLConnectorService azureMlConnectorService;

    @WireVariable
    private SageMakerConnectorService sageMakerConnectorService;

    @WireVariable
    private DataLakeCatalogService dataLakeCatalogService;

    @WireVariable
    private PurviewConnectorService purviewConnectorService;

    @WireVariable
    private FabricConnectorService fabricConnectorService;

    @WireVariable
    private VertexAIConnectorService vertexAIConnectorService;

    @WireVariable
    private IbmWatsonxConnectorService ibmWatsonxConnectorService;

    @WireVariable
    private ServiceNowConnectorService serviceNowConnectorService;

    @WireVariable
    private JiraConnectorService jiraConnectorService;
    @WireVariable
    private ExternalPlatformService externalPlatformService;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<ExternalPlatformIntegration> platforms = new ArrayList<>();
    private List<ExternalModel> externalModels = new ArrayList<>();
    private List<ExternalDataset> externalDatasets = new ArrayList<>();

    private ExternalPlatformIntegration selectedPlatform;
    private ExternalPlatformIntegration platformForm;

    private boolean addModalVisible;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        loadPlatforms();
    }

    @Command
    @NotifyChange({"platforms", "externalModels", "externalDatasets", "selectedPlatform"})
    public void refresh() {
        loadPlatforms();
        Clients.showNotification("Integraciones sincronizadas", Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 2000);
    }

    @Command
    @NotifyChange("addModalVisible")
    public void openAddModal() {
        platformForm = new ExternalPlatformIntegration();
        platformForm.setEplsyncenabled(Boolean.TRUE);
        platformForm.setEplsyncfrequencyhours(1);
        platformForm.setEplsyncstatus("PENDING");
        addModalVisible = true;
    }

    @Command
    @NotifyChange({"addModalVisible", "platforms"})
    public void savePlatform() {
        if (platformForm == null) {
            return;
        }
        if (!SUPPORTED_PLATFORMS.contains(StringUtils.defaultString(platformForm.getEplplatformtype()).toUpperCase())) {
            Clients.showNotification("Tipo de plataforma no soportado", Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            return;
        }
        sanitizePlatform(platformForm);
        integrationService.savePlatform(platformForm);
        addModalVisible = false;
        loadPlatforms();
        Clients.showNotification("Plataforma registrada", Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 2000);
    }

    @Command
    @NotifyChange("addModalVisible")
    public void cancelAdd() {
        addModalVisible = false;
    }

    @Command
    @NotifyChange({"selectedPlatform", "externalModels", "externalDatasets"})
    public void selectPlatform(@BindingParam("platform") ExternalPlatformIntegration platform) {
        selectedPlatform = platform;
        reloadCollections();
    }

    @Command
    @NotifyChange({"platforms", "externalModels", "externalDatasets"})
    public void syncNow(@BindingParam("platform") ExternalPlatformIntegration platform) {
        if (platform == null) {
            return;
        }
        String type = StringUtils.upperCase(platform.getEplplatformtype());
        try {
            switch (type) {
                case "DATABRICKS" -> databricksConnectorService.syncModelsFromDatabricks(platform.getIdxexternalplatform());
                case "VERTEX_AI" -> vertexAIConnectorService.syncModels(platform.getIdxexternalplatform());
                case "IBM_WATSONX" -> ibmWatsonxConnectorService.syncModels(platform.getIdxexternalplatform());
                case "SNOWFLAKE" -> snowflakeConnectorService.catalogDatasets(platform.getIdxexternalplatform());
                case "S3" -> dataLakeCatalogService.catalogS3(platform.getIdxexternalplatform());
                case "AZURE_BLOB" -> dataLakeCatalogService.catalogAzureBlob(platform.getIdxexternalplatform());
                case "GCS" -> dataLakeCatalogService.catalogGcs(platform.getIdxexternalplatform());
                case "AZURE_ML" -> azureMlConnectorService.listDeployments(platform.getIdxexternalplatform());
                case "SAGEMAKER" -> sageMakerConnectorService.listEndpoints(platform.getIdxexternalplatform());
                case "PURVIEW" -> purviewConnectorService.syncCatalog(platform.getIdxexternalplatform());
                case "FABRIC" -> fabricConnectorService.syncFabricAssets(platform.getIdxexternalplatform());
                case "SERVICENOW" -> serviceNowConnectorService.syncTickets(platform.getIdxexternalplatform());
                case "JIRA" -> jiraConnectorService.syncIssues(platform.getIdxexternalplatform());
                default -> {
                    Clients.showNotification("Conector aún no implementado", Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
                    return;
                }
            }
            loadPlatforms();
            Clients.showNotification("Sincronización iniciada", Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 2000);
        } catch (Exception ex) {
            log.error("Error sincronizando plataforma {}", platform.getEplplatformname(), ex);
            Clients.showNotification("Error: " + ex.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000);
        }
    }

    @Command
    public void testConnection(@BindingParam("platform") ExternalPlatformIntegration platform) {
        if (platform == null) {
            return;
        }
        boolean ok;
        switch (StringUtils.upperCase(platform.getEplplatformtype())) {
            case "DATABRICKS" -> ok = databricksConnectorService.testConnection(platform);
            case "VERTEX_AI" -> ok = vertexAIConnectorService.testConnection(platform);
            case "IBM_WATSONX" -> ok = ibmWatsonxConnectorService.testConnection(platform);
            case "SNOWFLAKE" -> ok = snowflakeConnectorService.testConnection(platform);
            case "PURVIEW" -> ok = purviewConnectorService.testConnection(platform);
            case "FABRIC" -> ok = fabricConnectorService.testConnection(platform);
            case "SERVICENOW" -> ok = serviceNowConnectorService.testConnection(platform);
            case "JIRA" -> ok = jiraConnectorService.testConnection(platform);
            case "SAGEMAKER", "S3", "AZURE_BLOB", "GCS", "AZURE_ML", "SPARK" -> ok = true;
            default -> ok = false;
        }
        Clients.showNotification(ok ? "Conexión OK" : "Conexión pendiente", ok ? Clients.NOTIFICATION_TYPE_INFO : Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
    }

    @Command
    @NotifyChange("externalDatasets")
    public void evaluateDataset(@BindingParam("dataset") ExternalDataset dataset) {
        if (dataset == null) {
            return;
        }
        try {
            String type = dataset.getPlatform() != null
                ? StringUtils.upperCase(dataset.getPlatform().getEplplatformtype())
                : "";
            if (!"SNOWFLAKE".equals(type)) {
                Clients.showNotification("Evaluación automática no soportada para " + type,
                    Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
                return;
            }
            SnowflakeQualityResult result = snowflakeConnectorService.evaluateDatasetQuality(dataset.getIdxexternaldataset(), 0);
            Clients.showNotification(
                String.format(Locale.ROOT, "Completeness %.2f%% · Duplicados %.2f%%", result.completenessPct(), result.duplicatesPct()),
                Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 3000);
            reloadCollections();
        } catch (Exception ex) {
            log.error("Error evaluando dataset {}", dataset.getExdexternalid(), ex);
            Clients.showNotification("Error evaluando dataset: " + ex.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000);
        }
    }

    @Command
    public void monitorModel(@BindingParam("model") ExternalModel model) {
        if (selectedPlatform == null || model == null) {
            return;
        }
        String type = StringUtils.upperCase(selectedPlatform.getEplplatformtype());
        try {
            switch (type) {
                case "AZURE_ML" -> monitorAzureModel(model);
                case "SAGEMAKER" -> monitorSageMakerModel(model);
                default -> Clients.showNotification("Monitoreo no disponible para " + type, Clients.NOTIFICATION_TYPE_WARNING, null, "middle_center", 3000);
            }
        } catch (Exception ex) {
            log.error("Error monitoreando modelo externo {}", model.getExmexternalname(), ex);
            Clients.showNotification("Error monitoreando modelo: " + ex.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 4000);
        }
    }

    public boolean isDatasetSectionVisible() {
        return selectedPlatform != null && isDatasetPlatform(selectedPlatform);
    }

    private void monitorAzureModel(ExternalModel model) {
        JsonNode metadata = parseJson(model.getExmmetadata());
        String endpointName = metadata.path("endpoint_name").asText();
        String deploymentName = metadata.path("deployment_name").asText(model.getExmexternalname());
        if (StringUtils.isAnyBlank(endpointName, deploymentName)) {
            throw new IllegalStateException("Metadata Azure ML sin endpoint/deployment");
        }
        AzureDeploymentMetrics metrics = azureMlConnectorService.monitorDeployment(
            selectedPlatform.getIdxexternalplatform(),
            endpointName,
            deploymentName,
            7
        );
        Clients.showNotification(
            String.format(Locale.ROOT, "Latency P95 %.2f ms · RPS %.2f", metrics.latencyP95(), metrics.requestsPerSecondAvg()),
            Clients.NOTIFICATION_TYPE_INFO,
            null,
            "top_center",
            3000
        );
    }

    private void monitorSageMakerModel(ExternalModel model) {
        String endpointName = model.getExmexternalname();
        SageMakerMetrics metrics = sageMakerConnectorService.monitorEndpoint(
            selectedPlatform.getIdxexternalplatform(),
            endpointName,
            24
        );
        Clients.showNotification(
            String.format(Locale.ROOT, "Latency Avg %.2f ms · Max %.2f ms · Calls %.0f", metrics.averageLatencyMs(), metrics.maximumLatencyMs(), metrics.totalInvocations()),
            Clients.NOTIFICATION_TYPE_INFO,
            null,
            "top_center",
            3000
        );
    }

    public List<String> getPlatformTypes() {
        return SUPPORTED_PLATFORMS;
    }

    private void loadPlatforms() {
        platforms = integrationService.findAllPlatforms();
        if (selectedPlatform != null) {
            selectedPlatform = platforms.stream()
                .filter(p -> Objects.equals(p.getIdxexternalplatform(), selectedPlatform.getIdxexternalplatform()))
                .findFirst()
                .orElse(null);
        }
        reloadCollections();
    }

    private void reloadCollections() {
        if (selectedPlatform == null) {
            externalModels = new ArrayList<>();
            externalDatasets = new ArrayList<>();
            return;
        }
        externalModels = integrationService.findModelsByPlatform(selectedPlatform.getIdxexternalplatform());
        if (isDatasetPlatform(selectedPlatform)) {
            externalDatasets = integrationService.findDatasetsByPlatform(selectedPlatform.getIdxexternalplatform());
        } else {
            externalDatasets = new ArrayList<>();
        }
    }

    private boolean isDatasetPlatform(ExternalPlatformIntegration platform) {
        return DATASET_PLATFORMS.contains(StringUtils.upperCase(platform.getEplplatformtype()));
    }

    private void sanitizePlatform(ExternalPlatformIntegration platform) {
        platform.setEplplatformtype(StringUtils.upperCase(platform.getEplplatformtype()));
        platform.setEplplatformname(StringUtils.trimToNull(platform.getEplplatformname()));
        platform.setEplhosturl(StringUtils.trimToNull(platform.getEplhosturl()));
        platform.setEplauthenticationtype(StringUtils.upperCase(StringUtils.trimToNull(platform.getEplauthenticationtype())));
        platform.setEplapitoken(StringUtils.trimToNull(platform.getEplapitoken()));
        platform.setEplclientid(StringUtils.trimToNull(platform.getEplclientid()));
        platform.setEplclientsecret(StringUtils.trimToNull(platform.getEplclientsecret()));
        platform.setEpltenantid(StringUtils.trimToNull(platform.getEpltenantid()));
        platform.setEplupdatedat(new Timestamp(System.currentTimeMillis()));
    }

    private JsonNode parseJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON inválido en metadata modelo externo: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }
}
