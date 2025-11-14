package nocode.services.entitys.projects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "prjprojects")
public class PRJProject {

    @Id
    @Column(name = "idxproject")
    private Long idxproject;

    @Column(name = "prjaisystemtype", length = 50)
    private String prjaisystemtype;

    @Column(name = "prjailifecyclestage", length = 50)
    private String prjailifecyclestage;

    @Column(name = "prjairisklevel", length = 20)
    private String prjairisklevel;

    @Lob
    @Column(name = "prjaipurpose")
    private String prjaipurpose;

    @Lob
    @Column(name = "prjaiintendeduse")
    private String prjaiintendeduse;

    @Lob
    @Column(name = "prjaiusers")
    private String prjaiusers;

    @Lob
    @Column(name = "prjaicontrolsapplied")
    private String prjaicontrolsapplied;

    @Lob
    @Column(name = "prjaiperformancemetrics")
    private String prjaiperformancemetrics;

    @Column(name = "prjailastinventoryreview")
    private Timestamp prjailastinventoryreview;

    @Column(name = "prjainextinventoryreview")
    private Timestamp prjainextinventoryreview;

    public Long getIdxproject() {
        return idxproject;
    }

    public void setIdxproject(Long idxproject) {
        this.idxproject = idxproject;
    }

    public String getPrjaisystemtype() {
        return prjaisystemtype;
    }

    public void setPrjaisystemtype(String prjaisystemtype) {
        this.prjaisystemtype = prjaisystemtype;
    }

    public String getPrjailifecyclestage() {
        return prjailifecyclestage;
    }

    public void setPrjailifecyclestage(String prjailifecyclestage) {
        this.prjailifecyclestage = prjailifecyclestage;
    }

    public String getPrjairisklevel() {
        return prjairisklevel;
    }

    public void setPrjairisklevel(String prjairisklevel) {
        this.prjairisklevel = prjairisklevel;
    }

    public String getPrjaipurpose() {
        return prjaipurpose;
    }

    public void setPrjaipurpose(String prjaipurpose) {
        this.prjaipurpose = prjaipurpose;
    }

    public String getPrjaiintendeduse() {
        return prjaiintendeduse;
    }

    public void setPrjaiintendeduse(String prjaiintendeduse) {
        this.prjaiintendeduse = prjaiintendeduse;
    }

    public String getPrjaiusers() {
        return prjaiusers;
    }

    public void setPrjaiusers(String prjaiusers) {
        this.prjaiusers = prjaiusers;
    }

    public String getPrjaicontrolsapplied() {
        return prjaicontrolsapplied;
    }

    public void setPrjaicontrolsapplied(String prjaicontrolsapplied) {
        this.prjaicontrolsapplied = prjaicontrolsapplied;
    }

    public String getPrjaiperformancemetrics() {
        return prjaiperformancemetrics;
    }

    public void setPrjaiperformancemetrics(String prjaiperformancemetrics) {
        this.prjaiperformancemetrics = prjaiperformancemetrics;
    }

    public Timestamp getPrjailastinventoryreview() {
        return prjailastinventoryreview;
    }

    public void setPrjailastinventoryreview(Timestamp prjailastinventoryreview) {
        this.prjailastinventoryreview = prjailastinventoryreview;
    }

    public Timestamp getPrjainextinventoryreview() {
        return prjainextinventoryreview;
    }

    public void setPrjainextinventoryreview(Timestamp prjainextinventoryreview) {
        this.prjainextinventoryreview = prjainextinventoryreview;
    }
}
