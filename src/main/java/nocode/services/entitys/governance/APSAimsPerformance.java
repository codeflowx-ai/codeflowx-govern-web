package nocode.services.entitys.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "apsaimsperformance")
public class APSAimsPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxapsperformance")
    private Long idxapsperformance;

    @Column(name = "iduuid", nullable = false, length = 36, unique = true)
    private String iduuid;

    @Column(name = "apsmetricname", nullable = false, length = 150)
    private String apsmetricname;

    @Column(name = "apsmetriccategory", nullable = false, length = 50)
    private String apsmetriccategory;

    @Column(name = "apsmetricdefinition", columnDefinition = "TEXT")
    private String apsmetricdefinition;

    @Column(name = "apsmetricvalue", precision = 12, scale = 4)
    private BigDecimal apsmetricvalue;

    @Column(name = "apstargetvalue", precision = 12, scale = 4)
    private BigDecimal apstargetvalue;

    @Column(name = "apsvariance", precision = 12, scale = 4)
    private BigDecimal apsvariance;

    @Column(name = "apsstatus", nullable = false, length = 20)
    private String apsstatus;

    @Column(name = "apsdatasource", length = 100)
    private String apsdatasource;

    @Column(name = "apsperiodstart")
    private Timestamp apsperiodstart;

    @Column(name = "apsperiodend")
    private Timestamp apsperiodend;

    @Column(name = "apsnotes", columnDefinition = "TEXT")
    private String apsnotes;

    @Column(name = "apscreatedat", nullable = false)
    private Timestamp apscreatedat;

    @Column(name = "apsupdatedat")
    private Timestamp apsupdatedat;

    public Long getIdxapsperformance() {
        return idxapsperformance;
    }

    public void setIdxapsperformance(Long idxapsperformance) {
        this.idxapsperformance = idxapsperformance;
    }

    public String getIduuid() {
        return iduuid;
    }

    public void setIduuid(String iduuid) {
        this.iduuid = iduuid;
    }

    public String getApsmetricname() {
        return apsmetricname;
    }

    public void setApsmetricname(String apsmetricname) {
        this.apsmetricname = apsmetricname;
    }

    public String getApsmetriccategory() {
        return apsmetriccategory;
    }

    public void setApsmetriccategory(String apsmetriccategory) {
        this.apsmetriccategory = apsmetriccategory;
    }

    public String getApsmetricdefinition() {
        return apsmetricdefinition;
    }

    public void setApsmetricdefinition(String apsmetricdefinition) {
        this.apsmetricdefinition = apsmetricdefinition;
    }

    public BigDecimal getApsmetricvalue() {
        return apsmetricvalue;
    }

    public void setApsmetricvalue(BigDecimal apsmetricvalue) {
        this.apsmetricvalue = apsmetricvalue;
    }

    public BigDecimal getApstargetvalue() {
        return apstargetvalue;
    }

    public void setApstargetvalue(BigDecimal apstargetvalue) {
        this.apstargetvalue = apstargetvalue;
    }

    public BigDecimal getApsvariance() {
        return apsvariance;
    }

    public void setApsvariance(BigDecimal apsvariance) {
        this.apsvariance = apsvariance;
    }

    public String getApsstatus() {
        return apsstatus;
    }

    public void setApsstatus(String apsstatus) {
        this.apsstatus = apsstatus;
    }

    public String getApsdatasource() {
        return apsdatasource;
    }

    public void setApsdatasource(String apsdatasource) {
        this.apsdatasource = apsdatasource;
    }

    public Timestamp getApsperiodstart() {
        return apsperiodstart;
    }

    public void setApsperiodstart(Timestamp apsperiodstart) {
        this.apsperiodstart = apsperiodstart;
    }

    public Timestamp getApsperiodend() {
        return apsperiodend;
    }

    public void setApsperiodend(Timestamp apsperiodend) {
        this.apsperiodend = apsperiodend;
    }

    public String getApsnotes() {
        return apsnotes;
    }

    public void setApsnotes(String apsnotes) {
        this.apsnotes = apsnotes;
    }

    public Timestamp getApscreatedat() {
        return apscreatedat;
    }

    public void setApscreatedat(Timestamp apscreatedat) {
        this.apscreatedat = apscreatedat;
    }

    public Timestamp getApsupdatedat() {
        return apsupdatedat;
    }

    public void setApsupdatedat(Timestamp apsupdatedat) {
        this.apsupdatedat = apsupdatedat;
    }
}
