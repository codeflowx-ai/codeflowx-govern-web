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

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.govern.entity.playground.PlaygroundTranslation;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.playground.PlaygroundSessionService;
import com.codeflowx.govern.service.playground.PlaygroundTranslationService;

import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;

import lombok.extern.slf4j.Slf4j;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@Slf4j
public class PlaygroundTranslationViewModel extends BaseFront<PlaygroundTranslationViewModel> {

    @WireVariable
    private ModelService modelService;

    @WireVariable
    private PlaygroundSessionService playgroundSessionService;

    @WireVariable
    private PlaygroundTranslationService playgroundTranslationService;

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
        logActivity("ACCESS", "PLAYGROUND_TRANSLATION", null, "Usuario accedió a Translation Playground");
        loadAvailableModels();
        loadOrCreateSession();
        loadTranslations();
    }

    @Destroy
    public void destroy() {
        logActivity("LEAVE", "PLAYGROUND_TRANSLATION", null, "Usuario salió de Translation Playground");
    }

    private void loadAvailableModels() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria("modelstatus", Operation.EQUAL, "ACTIVE", Evaluation.STRING);
            availableModels = modelService.findAll(criterias);
        } catch (GovernanceServiceException e) {
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
            currentSession = playgroundSessionService.create(currentSession);
        } catch (GovernanceServiceException e) {
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

            translation = playgroundTranslationService.create(translation);

            targetText = translation.getTranslationtargettext();
            confidence = translation.getTranslationconfidence();

            loadTranslations();
            logActivity("TRANSLATE", "PLAYGROUND_TRANSLATION", null, "Texto traducido");
        } catch (GovernanceServiceException e) {
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

        logActivity("SWAP_LANGUAGES", "PLAYGROUND_TRANSLATION", null, "Idiomas intercambiados");
    }

    @Command
    public void transcribeSource() {
        logActivity("TRANSCRIBE_SOURCE", "PLAYGROUND_TRANSLATION", null, "Transcribiendo fuente");
    }

    @Command
    public void speakTarget() {
        logActivity("SPEAK_TARGET", "PLAYGROUND_TRANSLATION", null, "Reproduciendo traducción");
    }

    @Command
    public void copyTranslation() {
        logActivity("COPY", "PLAYGROUND_TRANSLATION", null, "Copiando traducción");
    }

    @Command
    @NotifyChange({"sourceText", "targetText", "sourceLanguage", "targetLanguage"})
    public void reuseTranslation(PlaygroundTranslation translation) {
        sourceText = translation.getTranslationsourcetext();
        targetText = translation.getTranslationtargettext();
        sourceLanguage = translation.getTranslationsourcelanguage();
        targetLanguage = translation.getTranslationtargetlanguage();
        logActivity("REUSE", "PLAYGROUND_TRANSLATION", null, "Re-usando traducción");
    }

    @Command
    @NotifyChange({"filteredTranslations", "translationHistory"})
    public void deleteTranslation(PlaygroundTranslation translation) {
        try {
            playgroundTranslationService.deleteById(translation.getIdxplaygroundtranslation());
            loadTranslations();
            logActivity("DELETE", "PLAYGROUND_TRANSLATION", null, "Traducción eliminada");
        } catch (GovernanceServiceException e) {
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
        logActivity("PAGE_CHANGE", "PLAYGROUND_TRANSLATION", null, "Cambio a página: " + activePage);
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
