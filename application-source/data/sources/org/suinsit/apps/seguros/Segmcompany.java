package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.seguros.Segmpoliza;
import org.suinsit.apps.seguros.Segmproducto;
import org.suinsit.apps.seguros.Segrciacontact;

@Entity
@Table (
	name = "SEGMCOMPANY" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmcompany" 
)
public class Segmcompany implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ACTIVO",
		nullable = false 
	)
	private boolean activo;
	@Column (
		name = "ALTA",
		nullable = false 
	)
	private Date alta;
	@Column (
		name = "CODCOMPANY",
		nullable = true 
	)
	@Sequence (
		name = "SEGMCOMPANY_CODCOMPANY",
		prefix = "",
		mask = "00000000",
		addYear = false 
	)
	private String codcompany;
	@Column (
		name = "CODMEDIADOR",
		nullable = false 
	)
	private String codmediador;
	@Column (
		name = "DIRECCION",
		nullable = true 
	)
	private String direccion;
	@Column (
		name = "EMAIL",
		nullable = true 
	)
	private String email;
	@Column (
		name = "FAX",
		nullable = true 
	)
	private String fax;
	@Column (
		name = "FECCONTRATO",
		nullable = false 
	)
	private Date feccontrato;
	@Id
	@Column (
		name = "IDXSEGMCOMPANY",
		nullable = true 
	)
	private Long idxsegmcompany;
	@Column (
		name = "LOGOTIPO",
		nullable = true 
	)
	private Object logotipo;
	@Column (
		name = "MARCACOM",
		nullable = true 
	)
	private String marcacom;
	@Column (
		name = "MOVIL",
		nullable = true 
	)
	private String movil;
	@Column (
		name = "NIFVAT",
		nullable = false 
	)
	private String nifvat;
	@Column (
		name = "NOMBRECIA",
		nullable = false 
	)
	private String nombrecia;
	@Column (
		name = "POBLACION",
		nullable = true 
	)
	private String poblacion;
	@Column (
		name = "RETENCION",
		nullable = true 
	)
	private BigDecimal retencion;
	@Column (
		name = "TELEFONO",
		nullable = true 
	)
	private String telefono;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPAIS0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais idmpais;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPROVINCIA0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mprovincia idmprovincia;
	private List<Segrciacontact> subsegrciacontact;
	private List<Segmproducto> subsegmproducto;
	private List<Segmpoliza> subsegmpoliza; 

	public void setActivo(
		boolean var
	) {
		this.activo=var; 
	}
	
	public boolean isActivo() {
		return this.activo; 
	}
	
	public void setAlta(
		Date var
	) {
		this.alta=var; 
	}
	
	public Date getAlta() {
		return this.alta; 
	}
	
	public void setCodcompany(
		String var
	) {
		this.codcompany=var; 
	}
	
	public String getCodcompany() {
		return this.codcompany; 
	}
	
	public void setCodmediador(
		String var
	) {
		this.codmediador=var; 
	}
	
	public String getCodmediador() {
		return this.codmediador; 
	}
	
	public void setDireccion(
		String var
	) {
		this.direccion=var; 
	}
	
	public String getDireccion() {
		return this.direccion; 
	}
	
	public void setEmail(
		String var
	) {
		this.email=var; 
	}
	
	public String getEmail() {
		return this.email; 
	}
	
	public void setFax(
		String var
	) {
		this.fax=var; 
	}
	
	public String getFax() {
		return this.fax; 
	}
	
	public void setFeccontrato(
		Date var
	) {
		this.feccontrato=var; 
	}
	
	public Date getFeccontrato() {
		return this.feccontrato; 
	}
	
	public void setIdxsegmcompany(
		Long var
	) {
		this.idxsegmcompany=var; 
	}
	
	public Long getIdxsegmcompany() {
		return this.idxsegmcompany; 
	}
	
	public void setLogotipo(
		Object var
	) {
		this.logotipo=var; 
	}
	
	public Object getLogotipo() {
		return this.logotipo; 
	}
	
	public void setMarcacom(
		String var
	) {
		this.marcacom=var; 
	}
	
	public String getMarcacom() {
		return this.marcacom; 
	}
	
	public void setMovil(
		String var
	) {
		this.movil=var; 
	}
	
	public String getMovil() {
		return this.movil; 
	}
	
	public void setNifvat(
		String var
	) {
		this.nifvat=var; 
	}
	
	public String getNifvat() {
		return this.nifvat; 
	}
	
	public void setNombrecia(
		String var
	) {
		this.nombrecia=var; 
	}
	
	public String getNombrecia() {
		return this.nombrecia; 
	}
	
	public void setPoblacion(
		String var
	) {
		this.poblacion=var; 
	}
	
	public String getPoblacion() {
		return this.poblacion; 
	}
	
	public void setRetencion(
		BigDecimal var
	) {
		this.retencion=var; 
	}
	
	public BigDecimal getRetencion() {
		return this.retencion; 
	}
	
	public void setTelefono(
		String var
	) {
		this.telefono=var; 
	}
	
	public String getTelefono() {
		return this.telefono; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdmpais(
		Mpais var
	) {
		this.idmpais=var; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public void setIdmprovincia(
		Mprovincia var
	) {
		this.idmprovincia=var; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public void setSubsegrciacontact(
		List<Segrciacontact> var
	) {
		this.subsegrciacontact=var; 
	}
	
	public List<Segrciacontact> getSubsegrciacontact() {
		if(this.subsegrciacontact==null)this.subsegrciacontact=new ArrayList<>(0);
		  return this.subsegrciacontact; 
	}
	
	public void setSubsegmproducto(
		List<Segmproducto> var
	) {
		this.subsegmproducto=var; 
	}
	
	public List<Segmproducto> getSubsegmproducto() {
		if(this.subsegmproducto==null)this.subsegmproducto=new ArrayList<>(0);
		  return this.subsegmproducto; 
	}
	
	public void setSubsegmpoliza(
		List<Segmpoliza> var
	) {
		this.subsegmpoliza=var; 
	}
	
	public List<Segmpoliza> getSubsegmpoliza() {
		if(this.subsegmpoliza==null)this.subsegmpoliza=new ArrayList<>(0);
		  return this.subsegmpoliza; 
	} 

}