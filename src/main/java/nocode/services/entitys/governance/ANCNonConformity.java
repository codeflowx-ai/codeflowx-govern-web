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
@Table(name = "ancnonconformity")
public class ANCNonConformity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxancnonconformity")
    private Long idxancnonconformity;

    @Column(name = "iduuid", nullable = false, length = 36, unique = true)
    private String iduuid;

    @Column(name = "anctitle", nullable = false, length = 180)
    private String anctitle;

    @Lob
    @Column(name = "ancdescription", nullable = false)
    private String ancdescription;

    @Column(name = "ancseverity", nullable = false, length = 20)
    private String ancseverity;

    @Column(name = "ancstatus", nullable = false, length = 20)
    private String ancstatus;

    @Column(name = "ancdetectiondate")
    private Timestamp ancdetectiondate;

    @Column(name = "ancresponsible", length = 120)
    private String ancresponsible;

    @Lob
    @Column(name = "ancrootcause")
    private String ancrootcause;

    @Lob
    @Column(name = "anccontainmentactions")
    private String anccontainmentactions;

    @Lob
    @Column(name = "anccorrectiveactions")
    private String anccorrectiveactions;

    @Column(name = "ancduedate")
    private Timestamp ancduedate;

    @Column(name = "ancclosuredate")
    private Timestamp ancclosuredate;

    @Column(name = "ancevidenceurl", length = 500)
    private String ancevidenceurl;

    @Column(name = "anccreatedat", nullable = false)
    private Timestamp anccreatedat;

    @Column(name = "ancupdatedat")
    private Timestamp ancupdatedat;

    public Long getIdxancnonconformity() {
        return idxancnonconformity;
    }

    public void setIdxancnonconformity(Long idxancnonconformity) {
        this.idxancnonconformity = idxancnonconformity;
    }

    public String getIduuid() {
        return iduuid;
    }

    public void setIduuid(String iduuid) {
        this.iduuid = iduuid;
    }

    public String getAnctitle() {
        return anctitle;
    }

    public void setAnctitle(String anctitle) {
        this.anctitle = anctitle;
    }

    public String getAncdescription() {
        return ancdescription;
    }

    public void setAncdescription(String ancdescription) {
        this.ancdescription = ancdescription;
    }

    public String getAncseverity() {
        return ancseverity;
    }

    public void setAncseverity(String ancseverity) {
        this.ancseverity = ancseverity;
    }

    public String getAncstatus() {
        return ancstatus;
    }

    public void setAncstatus(String ancstatus) {
        this.ancstatus = ancstatus;
    }

    public Timestamp getAncdetectiondate() {
        return ancdetectiondate;
    }

    public void setAncdetectiondate(Timestamp ancdetectiondate) {
        this.ancdetectiondate = ancdetectiondate;
    }

    public String getAncresponsible() {
        return ancresponsible;
    }

    public void setAncresponsible(String ancresponsible) {
        this.ancresponsible = ancresponsible;
    }

    public String getAncrootcause() {
        return ancrootcause;
    }

    public void setAncrootcause(String ancrootcause) {
        this.ancrootcause = ancrootcause;
    }

    public String getAnccontainmentactions() {
        return anccontainmentactions;
    }

    public void setAnccontainmentactions(String anccontainmentactions) {
        this.anccontainmentactions = anccontainmentactions;
    }

    public String getAnccorrectiveactions() {
        return anccorrectiveactions;
    }

    public void setAnccorrectiveactions(String anccorrectiveactions) {
        this.anccorrectiveactions = anccorrectiveactions;
    }

    public Timestamp getAncduedate() {
        return ancduedate;
    }

    public void setAncduedate(Timestamp ancduedate) {
        this.ancduedate = ancduedate;
    }

    public Timestamp getAncclosuredate() {
        return ancclosuredate;
    }

    public void setAncclosuredate(Timestamp ancclosuredate) {
        this.ancclosuredate = ancclosuredate;
    }

    public String getAncevidenceurl() {
        return ancevidenceurl;
    }

    public void setAncevidenceurl(String ancevidenceurl) {
        this.ancevidenceurl = ancevidenceurl;
    }

    public Timestamp getAnccreatedat() {
        return anccreatedat;
    }

    public void setAnccreatedat(Timestamp anccreatedat) {
        this.anccreatedat = anccreatedat;
    }

    public Timestamp getAncupdatedat() {
        return ancupdatedat;
    }

    public void setAncupdatedat(Timestamp ancupdatedat) {
        this.ancupdatedat = ancupdatedat;
    }
}
