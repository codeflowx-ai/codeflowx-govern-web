package com.codeflowx.platform.viewmodel.playground;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.playground.PlaygroundImage;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.platform.service.BaseFront;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaygroundImageViewModel extends BaseFront {

    private PlaygroundSession currentSession;
    private List<PlaygroundImage> generatedImages = new ArrayList<>();
    
    private String prompt = "";
    private String negativePrompt = "";
    private String style = "";
    private Model selectedModel;
    private String selectedSize = "1024x1024";
    private String selectedQuality = "STANDARD";
    private String selectedFormat = "PNG";
    
    private List<Model> availableModels = new ArrayList<>();
    private List<String> availableSizes = List.of("256x256", "512x512", "1024x1024", "1024x1792", "1792x1024");
    private List<String> availableQualities = List.of("STANDARD", "HD");
    private List<String> availableFormats = List.of("PNG", "JPEG", "WEBP");
    
    private Integer imageCount = 0;
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Init(superclass = true)
    public void init() {
        logActivity("PLAYGROUND_IMAGE", "ACCESS", "Usuario accedió a Image Playground");
        loadAvailableModels();
        loadOrCreateSession();
        loadGeneratedImages();
    }

    @Destroy
    public void destroy() {
        logActivity("PLAYGROUND_IMAGE", "LEAVE", "Usuario salió de Image Playground");
    }

    private void loadAvailableModels() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria("modeltype", Operation.EQUAL, "IMAGE", Evaluation.STRING);
            criterias.addCriteria("modelstatus", Operation.EQUAL, "ACTIVE", Evaluation.STRING);
            availableModels = getUXCriteriaManager().find(Model.class, criterias);
        } catch (Exception e) {
            log.error("Error loading models", e);
        }
    }

    private void loadOrCreateSession() {
        try {
            currentSession = new PlaygroundSession();
            currentSession.setSessionname("Image Session - " + new Timestamp(System.currentTimeMillis()));
            currentSession.setSessiontype("IMAGE");
            currentSession.setSessionstatus("ACTIVE");
            currentSession.setSessioncreatedby(getUserName());
            currentSession.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
            getUXCriteriaManager().save(currentSession);
        } catch (Exception e) {
            log.error("Error creating session", e);
        }
    }

    private void loadGeneratedImages() {
        try {
            if (currentSession != null) {
                generatedImages = currentSession.getSubplaygroundimages();
                updateStats();
            }
        } catch (Exception e) {
            log.error("Error loading images", e);
        }
    }

    @Command
    @NotifyChange({"generatedImages", "prompt", "imageCount", "totalCost"})
    public void generateImage() {
        if (selectedModel == null) {
            Messagebox.show("Por favor selecciona un modelo", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            PlaygroundImage image = new PlaygroundImage();
            image.setSession(currentSession);
            image.setImageprompt(prompt);
            image.setImagenegativeprompt(negativePrompt);
            image.setImagestyle(style);
            image.setImagesize(selectedSize);
            image.setImagequality(selectedQuality);
            image.setImageformat(selectedFormat);
            image.setImagestatus("GENERATING");
            image.setModel(selectedModel);
            image.setImagecreatedby(getUserName());
            image.setImagecreatedat(new Timestamp(System.currentTimeMillis()));
            
            // Simulate image generation (in production, call AI service)
            image.setImageurl("https://via.placeholder.com/1024x1024?text=Generated+Image");
            image.setImagestatus("COMPLETED");
            image.setImagegenerationtime(3500);
            image.setImagecost(new BigDecimal("0.04"));
            
            getUXCriteriaManager().save(image);
            loadGeneratedImages();
            prompt = "";
            negativePrompt = "";
            
            logActivity("PLAYGROUND_IMAGE", "GENERATE", "Imagen generada");
            Messagebox.show("Imagen generada correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            log.error("Error generating image", e);
            Messagebox.show("Error al generar imagen: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void downloadImage(PlaygroundImage image) {
        logActivity("PLAYGROUND_IMAGE", "DOWNLOAD", "Descargando imagen");
        // Download logic
    }

    @Command
    @NotifyChange({"generatedImages", "imageCount", "totalCost"})
    public void deleteImage(PlaygroundImage image) {
        try {
            getUXCriteriaManager().remove(image);
            loadGeneratedImages();
            logActivity("PLAYGROUND_IMAGE", "DELETE", "Imagen eliminada");
        } catch (Exception e) {
            log.error("Error deleting image", e);
        }
    }

    @Command
    @NotifyChange({"generatedImages", "imageCount", "totalCost"})
    public void newSession() {
        loadOrCreateSession();
        generatedImages.clear();
        prompt = "";
        updateStats();
    }

    private void updateStats() {
        imageCount = generatedImages.size();
        totalCost = generatedImages.stream()
            .filter(img -> img.getImagecost() != null)
            .map(PlaygroundImage::getImagecost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and Setters
    public List<PlaygroundImage> getGeneratedImages() {
        return generatedImages;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Model getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(Model selectedModel) {
        this.selectedModel = selectedModel;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getSelectedQuality() {
        return selectedQuality;
    }

    public void setSelectedQuality(String selectedQuality) {
        this.selectedQuality = selectedQuality;
    }

    public String getSelectedFormat() {
        return selectedFormat;
    }

    public void setSelectedFormat(String selectedFormat) {
        this.selectedFormat = selectedFormat;
    }

    public List<Model> getAvailableModels() {
        return availableModels;
    }

    public List<String> getAvailableSizes() {
        return availableSizes;
    }

    public List<String> getAvailableQualities() {
        return availableQualities;
    }

    public List<String> getAvailableFormats() {
        return availableFormats;
    }

    public Integer getImageCount() {
        return imageCount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}
