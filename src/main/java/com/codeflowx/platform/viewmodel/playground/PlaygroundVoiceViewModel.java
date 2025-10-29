package com.codeflowx.platform.viewmodel.playground;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.playground.PlaygroundVoice;
import codeflowx.nocode.persist.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PlaygroundVoiceViewModel extends MasterPage {
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
    
    // TTS
    private String ttsText;
    private String ttsLanguage = "en-US";
    private String ttsVoice = "alloy";
    private String ttsFormat = "mp3";
    private String ttsAudioUrl;
    private Integer ttsDuration;
    private java.math.BigDecimal ttsCost;
    private boolean ttsContentViolation = false;
    private java.math.BigDecimal ttsToxicityScore = java.math.BigDecimal.ZERO;
    private boolean ttsPiiDetected = false;
    private boolean isGenerating = false;
    
    // STT
    private String sttAudioUrl;
    private String sttLanguage = "auto";
    private String sttTranscription;
    private String sttDetectedLanguage;
    private Integer sttDuration;
    private java.math.BigDecimal sttCost;
    private boolean sttContentViolation = false;
    private boolean sttPiiDetected = false;
    private java.math.BigDecimal sttToxicityScore = java.math.BigDecimal.ZERO;
    private boolean isTranscribing = false;
    
    // History
    private List<PlaygroundVoice> voicesList = new ArrayList<>();
    
    // Compliance
    private String complianceStatus = "PENDING";
    private String riskLevel = "LOW";
    private int totalViolations = 0;
    private java.math.BigDecimal totalCost = java.math.BigDecimal.ZERO;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        loadHistory();
    }
    
    @Command
    @NotifyChange("*")
    public void loadHistory() {
        try {
            PageParams params = PageParams.builder().maxRows(20).pageActual(1).build();
            PageResult<PlaygroundVoice> result = businessService.findAllEntity(PlaygroundVoice.class, params, new Criterias());
            if (result != null) {
                voicesList = result.getContent();
                calculateMetrics();
            }
        } catch (Exception e) {
            log.error("Error loading voice history", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void generateSpeech() {
        try {
            isGenerating = true;
            // TODO: Integración con leka-server para TTS
            Messagebox.show("TTS requires leka-server integration", "Info", Messagebox.OK, Messagebox.INFORMATION);
        } finally {
            isGenerating = false;
        }
    }
    
    @Command
    @NotifyChange("*")
    public void uploadAudio() {
        // TODO: Implementar subida de audio
        log.info("Upload audio file");
    }
    
    @Command
    @NotifyChange("*")
    public void transcribeAudio() {
        try {
            isTranscribing = true;
            // TODO: Integración con leka-server para STT
            Messagebox.show("STT requires leka-server integration", "Info", Messagebox.OK, Messagebox.INFORMATION);
        } finally {
            isTranscribing = false;
        }
    }
    
    @Command
    @NotifyChange("*")
    public void refreshHistory() {
        loadHistory();
    }
    
    @Command
    public void playAudio(@BindingParam("voice") PlaygroundVoice voice) {
        log.info("Play audio: {}", voice.getIdxplaygroundvoice());
    }
    
    @Command
    public void downloadAudio(@BindingParam("voice") PlaygroundVoice voice) {
        log.info("Download audio: {}", voice.getIdxplaygroundvoice());
    }
    
    private void calculateMetrics() {
        totalViolations = (int) voicesList.stream()
            .filter(v -> Boolean.TRUE.equals(v.getContentviol()))
            .count();
        
        totalCost = voicesList.stream()
            .map(v -> v.getVoicecost() != null ? v.getVoicecost() : java.math.BigDecimal.ZERO)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        riskLevel = totalViolations > 5 ? "HIGH" : totalViolations > 2 ? "MEDIUM" : "LOW";
        complianceStatus = totalViolations == 0 ? "COMPLIANT" : "NON_COMPLIANT";
    }
    
    public String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length) + "..." : text;
    }
    
    public String getComplianceColor(String status) {
        return "COMPLIANT".equals(status) ? "success" : "danger";
    }
    
    public String getRiskColor(String risk) {
        if (risk == null) return "secondary";
        switch (risk) {
            case "LOW": return "success";
            case "MEDIUM": return "warning";
            case "HIGH": return "danger";
            default: return "secondary";
        }
    }
    
    public String formatDate(Timestamp ts) {
        return ts != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "-";
    }
}
