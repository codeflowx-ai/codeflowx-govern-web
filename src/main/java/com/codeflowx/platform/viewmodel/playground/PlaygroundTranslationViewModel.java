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
import com.codeflowx.govern.entity.playground.PlaygroundTranslation;
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
public class PlaygroundTranslationViewModel extends BaseFront<PlaygroundTranslationViewModel> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private String sourceText;
    private String translatedText;
    private String sourceLanguage = "auto";
    private String targetLanguage = "es";
    private String translationModel = "gpt-4";
    private boolean isTranslating = false;
    
    private int sourceTextLength = 0;
    private int translatedTextLength = 0;
    private java.math.BigDecimal translationQuality = java.math.BigDecimal.ZERO;
    private java.math.BigDecimal sourceToxicityScore = java.math.BigDecimal.ZERO;
    
    private boolean showComplianceMonitor = false;
    private String complianceAlertType = "info";
    private boolean contentViolation = false;
    private boolean piiDetected = false;
    private java.math.BigDecimal toxicityScore = java.math.BigDecimal.ZERO;
    
    private List<PlaygroundTranslation> translationsList = new ArrayList<>();
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        loadTranslations();
    }
    
    @Command
    @NotifyChange("*")
    public void loadTranslations() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            PageResult<PlaygroundTranslation> result = businessService.findAllEntity(
                PlaygroundTranslation.class, params, new Criterias());
            
            if (result != null && result.getContent() != null) {
                translationsList = result.getContent();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "PLAYGROUNDTRANSLATIONS", null, 
                    "Búsqueda: " + translationsList.size() + " traducciones");
            } else {
                translationsList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error loading translations", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void translate() {
        if (sourceText == null || sourceText.trim().isEmpty()) {
            Messagebox.show("Please enter text to translate", "Validation", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            isTranslating = true;
            showComplianceMonitor = true;
            
            // TODO: Integración con leka-server para traducción
            translatedText = "[Translation result - requires leka-server integration]";
            translationQuality = new java.math.BigDecimal("0.95");
            translatedTextLength = translatedText.length();
            
            // Simular análisis de compliance
            contentViolation = false;
            piiDetected = false;
            toxicityScore = new java.math.BigDecimal("0.1");
            complianceAlertType = "success";
            
        } catch (Exception e) {
            log.error("Error translating", e);
            complianceAlertType = "danger";
        } finally {
            isTranslating = false;
        }
    }
    
    @Command
    @NotifyChange("*")
    public void swapLanguages() {
        String temp = sourceLanguage;
        sourceLanguage = targetLanguage;
        targetLanguage = temp;
        
        String tempText = sourceText;
        sourceText = translatedText;
        translatedText = tempText;
    }
    
    @Command
    public void viewTranslation(@BindingParam("translation") PlaygroundTranslation translation) {
        sourceText = translation.getSourcetext();
        translatedText = translation.getTranslatedtext();
        sourceLanguage = translation.getSourcelanguage();
        targetLanguage = translation.getTargetlanguage();
    }
    
    public String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length) + "..." : text;
    }
    
    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }
    
    @Destroy
    public void destroy() {
        if (translationsList != null) { 
            translationsList.clear(); 
            translationsList = null; 
        }
        businessService = null;
    }
}
