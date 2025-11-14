package nocode.services.entitys.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "aimprovement")
public class AIMImprovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxaimprovement")
    private Long idxaimprovement;

    @Column(name = "iduuid", nullable = false, length = 36, unique = true)
    private String iduuid;

    @Column(name = "aimtitle", nullable = false, length = 180)
    private String aimtitle;

    @Lob
    @Column(name = "aimdescription", nullable = false)
    private String aimdescription;

    @Column(name = "aimcategory", length = 50)
    private String aimcategory;

    @Column(name = "aimstatus", nullable = false, length = 20)
    private String aimstatus;

    @Column(name = "aimpriority", length = 20)
    private String aimpriority;

    @Column(name = "aimowner", length = 120)
    private String aimowner;

    @Column(name = "aimstartdate")
    private Timestamp aimstartdate;

    @Column(name = "aimduedate")
    private Timestamp aimduedate;

    @Column(name = "aimcompletiondate")
    private Timestamp aimcompletiondate;

    @Lob
    @Column(name = "aimexpectedbenefit")
    private String aimexpectedbenefit;

    @Lob
    @Column(name = "aimactualbenefit")
    private String aimactualbenefit;

    @Column(name = "aimcreatedat", nullable = false)
    private Timestamp aimcreatedat;

    @Column(name = "aimupdatedat")
    private Timestamp aimupdatedat;

    public Long getIdxaimprovement() {
        return idxaimprovement;
    }

    public void setIdxaimprovement(Long idxaimprovement) {
        this.idxaimprovement = idxaimprovement;
    }

    public String getIduuid() {
        return iduuid;
    }

    public void setIduuid(String iduuid) {
        this.iduuid = iduuid;
    }

    public String getAimtitle() {
        return aimtitle;
    }

    public void setAimtitle(String aimtitle) {
        this.aimtitle = aimtitle;
    }

    public String getAimdescription() {
        return aimdescription;
    }

    public void setAimdescription(String aimdescription) {
        this.aimdescription = aimdescription;
    }

    public String getAimcategory() {
        return aimcategory;
    }

    public void setAimcategory(String aimcategory) {
        this.aimcategory = aimcategory;
    }

    public String getAimstatus() {
        return aimstatus;
    }

    public void setAimstatus(String aimstatus) {
        this.aimstatus = aimstatus;
    }

    public String getAimpriority() {
        return aimpriority;
    }

    public void setAimpriority(String aimpriority) {
        this.aimpriority = aimpriority;
    }

    public String getAimowner() {
        return aimowner;
    }

    public void setAimowner(String aimowner) {
        this.aimowner = aimowner;
    }

    public Timestamp getAimstartdate() {
        return aimstartdate;
    }

    public void setAimstartdate(Timestamp aimstartdate) {
        this.aimstartdate = aimstartdate;
    }

    public Timestamp getAimduedate() {
        return aimduedate;
    }

    public void setAimduedate(Timestamp aimduedate) {
        this.aimduedate = aimduedate;
    }

    public Timestamp getAimcompletiondate() {
        return aimcompletiondate;
    }

    public void setAimcompletiondate(Timestamp aimcompletiondate) {
        this.aimcompletiondate = aimcompletiondate;
    }

    public String getAimexpectedbenefit() {
        return aimexpectedbenefit;
    }

    public void setAimexpectedbenefit(String aimexpectedbenefit) {
        this.aimexpectedbenefit = aimexpectedbenefit;
    }

    public String getAimactualbenefit() {
        return aimactualbenefit;
    }

    public void setAimactualbenefit(String aimactualbenefit) {
        this.aimactualbenefit = aimactualbenefit;
    }

    public Timestamp getAimcreatedat() {
        return aimcreatedat;
    }

    public void setAimcreatedat(Timestamp aimcreatedat) {
        this.aimcreatedat = aimcreatedat;
    }

    public Timestamp getAimupdatedat() {
        return aimupdatedat;
    }

    public void setAimupdatedat(Timestamp aimupdatedat) {
        this.aimupdatedat = aimupdatedat;
    }
}
