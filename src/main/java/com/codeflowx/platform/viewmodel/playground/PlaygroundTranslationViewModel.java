package com.codeflowx.platform.viewmodel.playground;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.govern.entity.playground.PlaygroundTranslation;
import com.codeflowx.platform.service.BaseFront;
import com.codeflowx.platform.service.BaseFront.Criteria;
import com.codeflowx.platform.service.BaseFront.Criterias;
import com.codeflowx.platform.service.BaseFront.Evaluation;
import com.codeflowx.platform.service.BaseFront.Operation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaygroundTranslationViewModel extends BaseFront {

    private PlaygroundSession currentSession;
    private List<PlaygroundTranslation> allTranslations = new ArrayList<>();
    private List<PlaygroundTranslation> filteredTranslations = new ArrayList<>();
    
    private String sourceText = "";
    private String targetText = "";
    private String sourceLanguage = "es";
    private String targetLanguage = "en";
    private BigDecimal confidence = BigDecimal.ZERO;
    private Model selectedModel;
    
    private String searchTerm = "";
    private int activePage = 0;
    private int pageSize = 10;
    
    private List<Model> availableModels = new ArrayList<>();
    private List<String> availableLanguages = List.of("es", "en", "fr", "de", "it", "pt", "zh", "ja", "ru", "ar");

    @Init(superclass = true)
    public void init() {
        logActivity("PLAYGROUND_TRANSLATION", "ACCESS", "Usuario accedió a Translation Playground");
        loadAvailableModels();
        loadOrCreateSession();
        loadTranslations();
    }

    @Destroy
    public void destroy() {
        logActivity("PLAYGROUND_TRANSLATION", "LEAVE", "Usuario salió de Translation Playground");
    }

    private void loadAvailableModels() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria("modelstatus", Operation.EQUAL, "ACTIVE", Evaluation.STRING);
            availableModels = getUXCriteriaManager().find(Model.class, criterias);
        } catch (Exception e) {
            log.error("Error loading models", e);
        }
    }

    private void loadOrCreateSession() {
        try {
            currentSession = new PlaygroundSession();
            currentSession.setSessionname("Translation Session - " + new Timestamp(System.currentTimeMillis()));
            currentSession.setSessiontype("TRANSLATION");
            currentSession.setSessionstatus("ACTIVE");
            currentSession.setSessioncreatedby(getUserName());
            currentSession.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
            getUXCriteriaManager().save(currentSession);
        } catch (Exception e) {
            log.error("Error creating session", e);
        }
    }

    private void loadTranslations() {
        try {
            if (currentSession != null) {
                allTranslations = currentSession.getSubplaygroundtranslations();
                applyFilters();
            }
        } catch (Exception e) {
            log.error("Error loading translations", e);
        }
    }

    @Command
    @NotifyChange({"targetText", "confidence", "filteredTranslations", "translationHistory"})
    public void translate() {
        if (selectedModel == null) {
            Messagebox.show("Por favor selecciona un modelo", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        if (StringUtils.isBlank(sourceText)) {
            Messagebox.show("Por favor ingresa el texto a traducir", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        try {
            PlaygroundTranslation translation = new PlaygroundTranslation();
            translation.setSession(currentSession);
            translation.setTranslationsourcetext(sourceText);
            translation.setTranslationsourcelanguage(sourceLanguage);
            translation.setTranslationtargetlanguage(targetLanguage);
            translation.setTranslationstatus("TRANSLATING");
            translation.setModel(selectedModel);
            translation.setTranslationcreatedby(getUserName());
            translation.setTranslationcreatedat(new Timestamp(System.currentTimeMillis()));
            
            // Simulate translation
            translation.setTranslationtargettext("This is a simulated translation of the source text.");
            translation.setTranslationstatus("COMPLETED");
            translation.setTranslationconfidence(new BigDecimal("95.5"));
            translation.setTranslationprocessingtime(850);
            translation.setTranslationcost(new BigDecimal("0.002"));
            translation.setTranslationmethod("NEURAL");
            
            getUXCriteriaManager().save(translation);
            
            targetText = translation.getTranslationtargettext();
            confidence = translation.getTranslationconfidence();
            
            loadTranslations();
            logActivity("PLAYGROUND_TRANSLATION", "TRANSLATE", "Texto traducido");
        } catch (Exception e) {
            log.error("Error translating", e);
            Messagebox.show("Error al traducir: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"sourceText", "targetText", "sourceLanguage", "targetLanguage"})
    public void swapLanguages() {
        String tempLang = sourceLanguage;
        sourceLanguage = targetLanguage;
        targetLanguage = tempLang;
        
        String tempText = sourceText;
        sourceText = targetText;
        targetText = tempText;
        
        logActivity("PLAYGROUND_TRANSLATION", "SWAP_LANGUAGES", "Idiomas intercambiados");
    }

    @Command
    public void transcribeSource() {
        logActivity("PLAYGROUND_TRANSLATION", "TRANSCRIBE_SOURCE", "Transcribiendo fuente");
    }

    @Command
    public void speakTarget() {
        logActivity("PLAYGROUND_TRANSLATION", "SPEAK_TARGET", "Reproduciendo traducción");
    }

    @Command
    public void copyTranslation() {
        logActivity("PLAYGROUND_TRANSLATION", "COPY", "Copiando traducción");
    }

    @Command
    @NotifyChange({"sourceText", "targetText", "sourceLanguage", "targetLanguage"})
    public void reuseTranslation(PlaygroundTranslation translation) {
        sourceText = translation.getTranslationsourcetext();
        targetText = translation.getTranslationtargettext();
        sourceLanguage = translation.getTranslationsourcelanguage();
        targetLanguage = translation.getTranslationtargetlanguage();
        logActivity("PLAYGROUND_TRANSLATION", "REUSE", "Re-usando traducción");
    }

    @Command
    @NotifyChange({"filteredTranslations", "translationHistory"})
    public void deleteTranslation(PlaygroundTranslation translation) {
        try {
            getUXCriteriaManager().remove(translation);
            loadTranslations();
            logActivity("PLAYGROUND_TRANSLATION", "DELETE", "Traducción eliminada");
        } catch (Exception e) {
            log.error("Error deleting translation", e);
        }
    }

    @Command
    @NotifyChange({"filteredTranslations", "translationHistory"})
    public void search() {
        applyFilters();
    }

    @Command
    @NotifyChange({"translationHistory"})
    public void changePage() {
        logActivity("PLAYGROUND_TRANSLATION", "PAGE_CHANGE", "Cambio a página: " + activePage);
    }

    @Command
    @NotifyChange("*")
    public void newSession() {
        loadOrCreateSession();
        allTranslations.clear();
        filteredTranslations.clear();
        sourceText = "";
        targetText = "";
    }

    private void applyFilters() {
        filteredTranslations = allTranslations.stream()
            .filter(t -> {
                if (StringUtils.isNotBlank(searchTerm)) {
                    return t.getTranslationsourcetext().toLowerCase().contains(searchTerm.toLowerCase()) ||
                           t.getTranslationtargettext().toLowerCase().contains(searchTerm.toLowerCase());
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    // Getters and Setters
    public List<PlaygroundTranslation> getTranslationHistory() {
        int start = activePage * pageSize;
        int end = Math.min(start + pageSize, filteredTranslations.size());
        return start < filteredTranslations.size() ? 
               filteredTranslations.subList(start, end) : new ArrayList<>();
    }

    public int getTotalSize() {
        return filteredTranslations.size();
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTargetText() {
        return targetText;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public Model getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(Model selectedModel) {
        this.selectedModel = selectedModel;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<Model> getAvailableModels() {
        return availableModels;
    }

    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }
}
