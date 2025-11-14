package com.codeflowx.govern.viewmodel.compliance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.enartframework.web.zk.page.MasterPage;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.codeflowx.govern.faas.SectorMetamodelService;
import com.codeflowx.govern.faas.metamodel.FaasFrameworkCollection;
import com.codeflowx.govern.faas.metamodel.PolicyRegistry;
import com.codeflowx.govern.faas.metamodel.SectorCatalogue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@VariableResolver(DelegatingVariableResolver.class)
public class SectorDashboardViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private SectorMetamodelService metamodelService;

    private List<SectorSummary> sectors = new ArrayList<>();
    private SectorSummary selectedSector;
    private List<FaasFrameworkCollection.FaasFrameworkConfig> selectedFrameworks = List.of();
    private List<PolicyRegistry.PolicyDescriptor> selectedPolicies = List.of();

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadSectors();
    }

    private void loadSectors() {
        List<SectorSummary> summaries = metamodelService.listSectors().stream()
                .filter(definition -> "SECTOR".equalsIgnoreCase(definition.category()))
                .map(definition -> new SectorSummary(definition,
                        metamodelService.listSubsectors(definition.code())))
                .collect(Collectors.toCollection(ArrayList::new));

        this.sectors = List.copyOf(summaries);
        if (!this.sectors.isEmpty()) {
            this.selectedSector = this.sectors.get(0);
            refreshSelections();
        }
    }

    @Command
    @NotifyChange({ "selectedSector", "selectedFrameworks", "selectedPolicies" })
    public void onSectorSelected() {
        refreshSelections();
    }

    public void setSelectedSector(SectorSummary selectedSector) {
        this.selectedSector = selectedSector;
    }

    private void refreshSelections() {
        if (selectedSector == null) {
            this.selectedFrameworks = List.of();
            this.selectedPolicies = List.of();
            return;
        }

        this.selectedFrameworks = List.copyOf(
                metamodelService.listFrameworksBySector(selectedSector.getCode()));

        Map<String, List<PolicyRegistry.PolicyDescriptor>> policiesBySector = metamodelService
                .policyRegistry()
                .policies()
                .stream()
                .collect(Collectors.groupingBy(PolicyRegistry.PolicyDescriptor::sectorCode));

        this.selectedPolicies = policiesBySector
                .getOrDefault(selectedSector.getCode(), List.of())
                .stream()
                .sorted((left, right) -> left.policyCode().compareToIgnoreCase(right.policyCode()))
                .toList();

        log.debug("Sector seleccionado: {} ({} frameworks, {} policies)", selectedSector.getCode(),
                selectedFrameworks.size(), selectedPolicies.size());
    }

    @Getter
    public static class SectorSummary {
        private final SectorCatalogue.SectorDefinition definition;
        private final List<SectorCatalogue.SectorDefinition> subsectors;
        private final List<String> regulationRefs;
        private final List<String> standardRefs;
        private final List<String> prompts;

        public SectorSummary(SectorCatalogue.SectorDefinition definition,
                List<SectorCatalogue.SectorDefinition> subsectors) {
            this.definition = definition;
            this.subsectors = subsectors != null ? List.copyOf(subsectors) : List.of();
            var attributes = definition.attributes();
            this.regulationRefs = attributes != null && attributes.regulationRefs() != null
                    ? List.copyOf(attributes.regulationRefs())
                    : List.of();
            this.standardRefs = attributes != null && attributes.standardRefs() != null
                    ? List.copyOf(attributes.standardRefs())
                    : List.of();
            this.prompts = attributes != null && attributes.prompts() != null
                    ? List.copyOf(attributes.prompts())
                    : List.of();
        }

        public String getCode() {
            return definition.code();
        }

        public String getLabel() {
            return definition.label();
        }

        public String getRiskProfile() {
            return definition.riskProfile();
        }

        public List<String> getGeoScope() {
            return definition.geoScope() != null ? List.copyOf(definition.geoScope()) : List.of();
        }

        public String getRequirementsDoc() {
            return Optional.ofNullable(definition.attributes())
                    .map(SectorCatalogue.SectorAttributes::requirementsDoc)
                    .orElse(null);
        }

        public boolean hasSubsectors() {
            return !subsectors.isEmpty();
        }

        public List<String> getSubsectorLabels() {
            return subsectors.stream()
                    .map(SectorCatalogue.SectorDefinition::label)
                    .collect(Collectors.toUnmodifiableList());
        }

        public boolean hasPrompts() {
            return !prompts.isEmpty();
        }
    }
}
