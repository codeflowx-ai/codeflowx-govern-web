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

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.playground.PlaygroundSession;
import com.codeflowx.govern.entity.playground.PlaygroundVoice;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.playground.PlaygroundSessionService;
import com.codeflowx.govern.service.playground.PlaygroundVoiceService;

import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;

import lombok.extern.slf4j.Slf4j;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@Slf4j
public class PlaygroundVoiceViewModel extends BaseFront<PlaygroundVoiceViewModel> {

    @WireVariable
    private ModelService modelService;

    @WireVariable
    private PlaygroundSessionService playgroundSessionService;

    @WireVariable
    private PlaygroundVoiceService playgroundVoiceService;

    private PlaygroundSession currentSession;
    private List<PlaygroundVoice> voiceHistory = new ArrayList<>();

    // TTS
    private String textToConvert = "";
    private Model selectedTTSModel;
    private String selectedVoice = "alloy";
    private String selectedLanguage = "es";
    private BigDecimal speed = new BigDecimal("1.0");
    private String generatedAudioUrl;
    private Integer audioDuration;
    private BigDecimal audioCost;

    // STT
    private Model selectedSTTModel;
    private String selectedAudioFile;
    private String transcriptionLanguage;
    private String transcriptionText;
    private Integer processingTime;
    private BigDecimal transcriptionCost;

    private List<Model> availableModels = new ArrayList<>();
    private List<String> availableVoices = List.of("alloy", "echo", "fable", "onyx", "nova", "shimmer");
    private List<String> availableLanguages = List.of("es", "en", "fr", "de", "it", "pt");

    @Init(superclass = true)
    public void init() {
        logActivity("ACCESS", "PLAYGROUND_VOICE", null, "Usuario accedió a Voice Playground");
        loadAvailableModels();
        loadOrCreateSession();
        loadVoiceHistory();
    }

    @Destroy
    public void destroy() {
        logActivity("LEAVE", "PLAYGROUND_VOICE", null, "Usuario salió de Voice Playground");
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
            currentSession.setSessionname("Voice Session - " + new Timestamp(System.currentTimeMillis()));
            currentSession.setSessiontype("VOICE");
            currentSession.setSessionstatus("ACTIVE");
            currentSession.setSessioncreatedby(getUserName());
            currentSession.setSessioncreatedat(new Timestamp(System.currentTimeMillis()));
            currentSession = playgroundSessionService.create(currentSession);
        } catch (GovernanceServiceException e) {
            log.error("Error creating session", e);
        }
    }

    private void loadVoiceHistory() {
        try {
            if (currentSession != null) {
                voiceHistory = currentSession.getSubplaygroundvoices();
            }
        } catch (Exception e) {
            log.error("Error loading voice history", e);
        }
    }

    @Command
    @NotifyChange({"generatedAudioUrl", "audioDuration", "audioCost", "voiceHistory"})
    public void generateSpeech() {
        if (selectedTTSModel == null) {
            Messagebox.show("Por favor selecciona un modelo", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            PlaygroundVoice voice = new PlaygroundVoice();
            voice.setSession(currentSession);
            voice.setVoicetype("TEXT_TO_SPEECH");
            voice.setVoicetext(textToConvert);
            voice.setVoicename(selectedVoice);
            voice.setVoicelanguage(selectedLanguage);
            voice.setVoicespeed(speed);
            voice.setVoiceformat("MP3");
            voice.setVoicestatus("PROCESSING");
            voice.setModel(selectedTTSModel);
            voice.setVoicecreatedby(getUserName());
            voice.setVoicecreatedat(new Timestamp(System.currentTimeMillis()));

            // Simulate audio generation
            voice.setVoiceaudiourl("https://example.com/audio/generated.mp3");
            voice.setVoicestatus("COMPLETED");
            voice.setVoiceprocessingtime(1200);
            voice.setVoiceduration(15);
            voice.setVoicecost(new BigDecimal("0.015"));

            voice = playgroundVoiceService.create(voice);

            generatedAudioUrl = voice.getVoiceaudiourl();
            audioDuration = voice.getVoiceduration();
            audioCost = voice.getVoicecost();

            loadVoiceHistory();
            logActivity("GENERATE_SPEECH", "PLAYGROUND_VOICE", null, "Audio generado");
            Messagebox.show("Audio generado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (GovernanceServiceException e) {
            log.error("Error generating speech", e);
            Messagebox.show("Error al generar audio: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"transcriptionText", "processingTime", "transcriptionCost", "voiceHistory"})
    public void transcribeAudio() {
        if (selectedSTTModel == null || selectedAudioFile == null) {
            Messagebox.show("Por favor selecciona un modelo y un archivo", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        try {
            PlaygroundVoice voice = new PlaygroundVoice();
            voice.setSession(currentSession);
            voice.setVoicetype("SPEECH_TO_TEXT");
            voice.setVoiceaudiourl(selectedAudioFile);
            voice.setVoicelanguage(transcriptionLanguage);
            voice.setVoicestatus("PROCESSING");
            voice.setModel(selectedSTTModel);
            voice.setVoicecreatedby(getUserName());
            voice.setVoicecreatedat(new Timestamp(System.currentTimeMillis()));

            // Simulate transcription
            voice.setVoicetext("Esta es una transcripción simulada del audio proporcionado.");
            voice.setVoicestatus("COMPLETED");
            voice.setVoiceprocessingtime(2500);
            voice.setVoicecost(new BigDecimal("0.006"));

            voice = playgroundVoiceService.create(voice);

            transcriptionText = voice.getVoicetext();
            processingTime = voice.getVoiceprocessingtime();
            transcriptionCost = voice.getVoicecost();

            loadVoiceHistory();
            logActivity("TRANSCRIBE", "PLAYGROUND_VOICE", null, "Audio transcrito");
            Messagebox.show("Audio transcrito correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        } catch (GovernanceServiceException e) {
            log.error("Error transcribing audio", e);
            Messagebox.show("Error al transcribir: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void selectAudioFile() {
        // File upload logic
        selectedAudioFile = "audio_sample.mp3";
    }

    @Command
    @NotifyChange({"selectedAudioFile"})
    public void clearAudioFile() {
        selectedAudioFile = null;
    }

    @Command
    public void downloadAudio() {
        logActivity("DOWNLOAD", "PLAYGROUND_VOICE", null, "Descargando audio");
    }

    @Command
    public void copyTranscription() {
        logActivity("COPY", "PLAYGROUND_VOICE", null, "Copiando transcripción");
    }

    @Command
    public void viewVoice(PlaygroundVoice voice) {
        logActivity("VIEW", "PLAYGROUND_VOICE", null, "Viendo detalles de conversión");
    }

    @Command
    @NotifyChange({"voiceHistory"})
    public void deleteVoice(PlaygroundVoice voice) {
        try {
            playgroundVoiceService.deleteById(voice.getIdxplaygroundvoice());
            loadVoiceHistory();
        } catch (GovernanceServiceException e) {
            log.error("Error deleting voice", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void newSession() {
        loadOrCreateSession();
        voiceHistory.clear();
        textToConvert = "";
        generatedAudioUrl = null;
        selectedAudioFile = null;
        transcriptionText = null;
    }

    // Getters and Setters
    public List<PlaygroundVoice> getVoiceHistory() {
        return voiceHistory;
    }

    public String getTextToConvert() {
        return textToConvert;
    }

    public void setTextToConvert(String textToConvert) {
        this.textToConvert = textToConvert;
    }

    public Model getSelectedTTSModel() {
        return selectedTTSModel;
    }

    public void setSelectedTTSModel(Model selectedTTSModel) {
        this.selectedTTSModel = selectedTTSModel;
    }

    public Model getSelectedSTTModel() {
        return selectedSTTModel;
    }

    public void setSelectedSTTModel(Model selectedSTTModel) {
        this.selectedSTTModel = selectedSTTModel;
    }

    public String getSelectedVoice() {
        return selectedVoice;
    }

    public void setSelectedVoice(String selectedVoice) {
        this.selectedVoice = selectedVoice;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public String getGeneratedAudioUrl() {
        return generatedAudioUrl;
    }

    public Integer getAudioDuration() {
        return audioDuration;
    }

    public BigDecimal getAudioCost() {
        return audioCost;
    }

    public String getSelectedAudioFile() {
        return selectedAudioFile;
    }

    public String getTranscriptionLanguage() {
        return transcriptionLanguage;
    }

    public void setTranscriptionLanguage(String transcriptionLanguage) {
        this.transcriptionLanguage = transcriptionLanguage;
    }

    public String getTranscriptionText() {
        return transcriptionText;
    }

    public Integer getProcessingTime() {
        return processingTime;
    }

    public BigDecimal getTranscriptionCost() {
        return transcriptionCost;
    }

    public List<Model> getAvailableModels() {
        return availableModels;
    }

    public List<String> getAvailableVoices() {
        return availableVoices;
    }

    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }
}
