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
@Table(name = "icoiso42001control")
public class ICOISO42001Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxicoiso42001control")
    private Long idxicoiso42001control;

    @Column(name = "iduuid", nullable = false, length = 36, unique = true)
    private String iduuid;

    @Column(name = "icocontrolcode", nullable = false, length = 40, unique = true)
    private String icocontrolcode;

    @Column(name = "icocontrolclause", length = 40)
    private String icocontrolclause;

    @Column(name = "icocontroltitle", nullable = false, length = 200)
    private String icocontroltitle;

    @Lob
    @Column(name = "icocontroldescription", nullable = false)
    private String icocontroldescription;

    @Column(name = "icocontrolcategory", length = 60)
    private String icocontrolcategory;

    @Column(name = "icocontrolstatus", nullable = false, length = 20)
    private String icocontrolstatus;

    @Column(name = "icocontrolowner", length = 120)
    private String icocontrolowner;

    @Column(name = "icocontrollastreview")
    private Timestamp icocontrollastreview;

    @Column(name = "icocontrolnextreview")
    private Timestamp icocontrolnextreview;

    @Lob
    @Column(name = "icocontrolevidence")
    private String icocontrolevidence;

    @Column(name = "icocontrolriskimpact", length = 20)
    private String icocontrolriskimpact;

    @Column(name = "icocreatedat", nullable = false)
    private Timestamp icocreatedat;

    @Column(name = "icoupdatedat")
    private Timestamp icoupdatedat;

    public Long getIdxicoiso42001control() {
        return idxicoiso42001control;
    }

    public void setIdxicoiso42001control(Long idxicoiso42001control) {
        this.idxicoiso42001control = idxicoiso42001control;
    }

    public String getIduuid() {
        return iduuid;
    }

    public void setIduuid(String iduuid) {
        this.iduuid = iduuid;
    }

    public String getIcocontrolcode() {
        return icocontrolcode;
    }

    public void setIcocontrolcode(String icocontrolcode) {
        this.icocontrolcode = icocontrolcode;
    }

    public String getIcocontrolclause() {
        return icocontrolclause;
    }

    public void setIcocontrolclause(String icocontrolclause) {
        this.icocontrolclause = icocontrolclause;
    }

    public String getIcocontroltitle() {
        return icocontroltitle;
    }

    public void setIcocontroltitle(String icocontroltitle) {
        this.icocontroltitle = icocontroltitle;
    }

    public String getIcocontroldescription() {
        return icocontroldescription;
    }

    public void setIcocontroldescription(String icocontroldescription) {
        this.icocontroldescription = icocontroldescription;
    }

    public String getIcocontrolcategory() {
        return icocontrolcategory;
    }

    public void setIcocontrolcategory(String icocontrolcategory) {
        this.icocontrolcategory = icocontrolcategory;
    }

    public String getIcocontrolstatus() {
        return icocontrolstatus;
    }

    public void setIcocontrolstatus(String icocontrolstatus) {
        this.icocontrolstatus = icocontrolstatus;
    }

    public String getIcocontrolowner() {
        return icocontrolowner;
    }

    public void setIcocontrolowner(String icocontrolowner) {
        this.icocontrolowner = icocontrolowner;
    }

    public Timestamp getIcocontrollastreview() {
        return icocontrollastreview;
    }

    public void setIcocontrollastreview(Timestamp icocontrollastreview) {
        this.icocontrollastreview = icocontrollastreview;
    }

    public Timestamp getIcocontrolnextreview() {
        return icocontrolnextreview;
    }

    public void setIcocontrolnextreview(Timestamp icocontrolnextreview) {
        this.icocontrolnextreview = icocontrolnextreview;
    }

    public String getIcocontrolevidence() {
        return icocontrolevidence;
    }

    public void setIcocontrolevidence(String icocontrolevidence) {
        this.icocontrolevidence = icocontrolevidence;
    }

    public String getIcocontrolriskimpact() {
        return icocontrolriskimpact;
    }

    public void setIcocontrolriskimpact(String icocontrolriskimpact) {
        this.icocontrolriskimpact = icocontrolriskimpact;
    }

    public Timestamp getIcocreatedat() {
        return icocreatedat;
    }

    public void setIcocreatedat(Timestamp icocreatedat) {
        this.icocreatedat = icocreatedat;
    }

    public Timestamp getIcoupdatedat() {
        return icoupdatedat;
    }

    public void setIcoupdatedat(Timestamp icoupdatedat) {
        this.icoupdatedat = icoupdatedat;
    }
}
