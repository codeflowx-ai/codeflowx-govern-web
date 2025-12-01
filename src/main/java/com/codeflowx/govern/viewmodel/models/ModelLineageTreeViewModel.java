package com.codeflowx.govern.viewmodel.models;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.enartframework.web.zk.page.MasterPage;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.business.models.ModelAdaptationBusinessService;
import com.codeflowx.govern.business.models.ModelAdaptationBusinessService.ModelLineageNode;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para visualizar el árbol de linaje (V_MODEL_LINEAGE_TREE)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelLineageTreeViewModel extends BaseFront<ModelLineageTreeViewModel>{

    private static final long serialVersionUID = 1L;

    @WireVariable
    private ModelAdaptationBusinessService adaptationBusinessService;

    private Long modelId;
    private List<LineageRow> lineageRows = new ArrayList<>();
    private boolean hasData = false;

    @Init
    public void init(@QueryParam("modelId") Long modelId) {
        this.modelId = modelId;
        if (modelId != null) {
            loadTree();
        }
    }

    @Command
    @NotifyChange({"lineageRows", "hasData"})
    public void loadTree() {
        if (modelId == null) {
            Messagebox.show("Selecciona un modelo para visualizar el linaje.", "Información", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        try {
            List<ModelLineageNode> nodes = adaptationBusinessService.getModelLineage(modelId);
            hasData = nodes != null && !nodes.isEmpty();
            if (hasData) {
                lineageRows = nodes.stream()
                    .map(node -> new LineageRow(node, buildIndent(node.getDepth())))
                    .collect(Collectors.toList());
            } else {
                lineageRows = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error cargando lineage", e);
            hasData = false;
            lineageRows = new ArrayList<>();
            Messagebox.show("No fue posible cargar el linaje: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private String buildIndent(Integer depth) {
        if (depth == null || depth <= 1) {
            return "";
        }
        return "└" + "─".repeat(Math.max(0, depth - 1)) + " ";
    }

    @Getter
    public static class LineageRow {
        private final ModelLineageNode node;
        private final String indent;

        LineageRow(ModelLineageNode node, String indent) {
            this.node = node;
            this.indent = indent;
        }

        public String getDisplayName() {
            String prefix = indent != null ? indent : "";
            return prefix + node.getModelName();
        }
    }
}
