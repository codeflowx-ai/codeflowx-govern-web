package com.codeflowx.platform.viewmodel.playground;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.playground.PlaygroundImage;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundImageViewModel extends BaseFront<PlaygroundImageViewModel> {
    private static final long serialVersionUID = 1L;
    
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
        loadImages();
    }
    
    @Command
    @NotifyChange("*")
    public void loadImages() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            PageResult<PlaygroundImage> result = businessService.findAllEntity(
                PlaygroundImage.class, params, new Criterias());
            
            if (result != null && result.getContent() != null) {
                imagesList = result.getContent();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "PLAYGROUNDIMAGES", null, 
                    "Búsqueda: " + imagesList.size() + " imágenes");
            } else {
                imagesList = new ArrayList<>();
            }
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
            Messagebox.show("Image generation requires leka-server integration", 
                "Info", Messagebox.OK, Messagebox.INFORMATION);
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
            businessService.removeFromID(image);
            
            // Auditar eliminación
            logActivity("ELIMINAR", "PLAYGROUNDIMAGES", image.getIdxplaygroundimage(), 
                "Imagen eliminada");
            
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
    
    @Destroy
    public void destroy() {
        if (imagesList != null) { 
            imagesList.clear(); 
            imagesList = null; 
        }
        businessService = null;
    }
}
