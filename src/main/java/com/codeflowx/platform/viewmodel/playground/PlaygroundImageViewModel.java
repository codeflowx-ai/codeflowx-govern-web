package com.codeflowx.platform.viewmodel.playground;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.playground.*;
import codeflowx.nocode.persist.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundImageViewModel extends MasterPage {
    private static final long serialVersionUID = 1L;
    
    @WireVariable
    private BusinessService businessService;
    @WireVariable
    public Environment environment;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((javax.sql.DataSource) environment.getProperty("APPLICATION_DS", javax.sql.DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {}
    
    private String imagePrompt;
    private String selectedModel = "dall-e-3";
    private String imageSize = "1024x1024";
    private String imageStyle = "natural";
    private boolean isGenerating = false;
    
    private List<PlaygroundImage> imagesList = new ArrayList<>();
    private String complianceStatus = "PENDING";
    private boolean nsfwDetected = false;
    private boolean contentViolation = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        loadImages();
    }
    
    @Command
    @NotifyChange("*")
    public void loadImages() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            PageResult<PlaygroundImage> result = businessService.findAllEntity(PlaygroundImage.class, params, new Criterias());
            if (result != null) imagesList = result.getContent();
        } catch (Exception e) {
            log.error("Error loading images", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void generateImage() {
        try {
            isGenerating = true;
            // TODO: Integración con leka-server
            Messagebox.show("Image generation requires leka-server integration", "Info", Messagebox.OK, Messagebox.INFORMATION);
        } finally {
            isGenerating = false;
        }
    }
    
    @Command
    public void downloadImage(@BindingParam("image") PlaygroundImage image) {
        log.info("Download image: {}", image.getIdxplaygroundimage());
    }
    
    @Command
    @NotifyChange("*")
    public void deleteImage(@BindingParam("image") PlaygroundImage image) {
        try {
            businessService.deleteEntity(image);
            loadImages();
        } catch (Exception e) {
            log.error("Error deleting image", e);
        }
    }
    
    public String getComplianceColor(String status) {
        return "COMPLIANT".equals(status) ? "success" : "warning";
    }
    
    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }
}
