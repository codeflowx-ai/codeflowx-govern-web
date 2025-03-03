package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.seguros.Segmtiposeg;
import org.suinsit.apps.seguros.Segrsolicitudprop;

@Entity
@Table (
	name = "SEGMSOLICITUD" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmsolicitud" 
)
public class Segmsolicitud implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ALARMACONECTADA",
		nullable = true 
	)
	private boolean alarmaconectada;
	@Column (
		name = "ALTA",
		nullable = false 
	)
	private Timestamp alta;
	@Column (
		name = "BAJAENFERMEDAD",
		nullable = true 
	)
	private boolean bajaenfermedad;
	@Column (
		name = "CAPITALACCIDENTE",
		nullable = true 
	)
	private BigDecimal capitalaccidente;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "FECHAEFECTO",
		nullable = true 
	)
	private Date fechaefecto;
	@Column (
		name = "FECHAPROPUESTA",
		nullable = true 
	)
	private Date fechapropuesta;
	@Column (
		name = "FECITV",
		nullable = true 
	)
	private Date fecitv;
	@Column (
		name = "FECMATRICULA",
		nullable = true 
	)
	private Date fecmatricula;
	@Column (
		name = "HOSPITALIZACION",
		nullable = true 
	)
	private boolean hospitalizacion;
	@Id
	@Column (
		name = "IDXSEGMSOLICITUD",
		nullable = true 
	)
	private Long idxsegmsolicitud;
	@Column (
		name = "MARCA",
		nullable = true 
	)
	private String marca;
	@Column (
		name = "MATRICULA",
		nullable = true 
	)
	private String matricula;
	@Column (
		name = "MODELO",
		nullable = true 
	)
	private String modelo;
	@Column (
		name = "PLAZAS",
		nullable = true 
	)
	private BigDecimal plazas;
	@Column (
		name = "SOLICITUD",
		nullable = false 
	)
	@Sequence (
		name = "SEGMSOLICITUD_SOLICITUD",
		prefix = "SOL",
		mask = "00000000",
		addYear = true 
	)
	private String solicitud;
	@Column (
		name = "SUPERFICIE",
		nullable = true 
	)
	private Integer superficie;
	@Column (
		name = "VALORINMUEBLE",
		nullable = true 
	)
	private BigDecimal valorinmueble;
	@Column (
		name = "VALORMOBILIARIO",
		nullable = true 
	)
	private BigDecimal valormobiliario;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPOSEG0",
		referencedColumnName = "IDXSEGMTIPOSEG",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtiposeg idsegmtiposeg;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
	private List<Segrsolicitudprop> subsegrsolicitudprop; 

	public void setAlarmaconectada(
		boolean var
	) {
		this.alarmaconectada=var; 
	}
	
	public boolean isAlarmaconectada() {
		return this.alarmaconectada; 
	}
	
	public void setAlta(
		Timestamp var
	) {
		this.alta=var; 
	}
	
	public Timestamp getAlta() {
		return this.alta; 
	}
	
	public void setBajaenfermedad(
		boolean var
	) {
		this.bajaenfermedad=var; 
	}
	
	public boolean isBajaenfermedad() {
		return this.bajaenfermedad; 
	}
	
	public void setCapitalaccidente(
		BigDecimal var
	) {
		this.capitalaccidente=var; 
	}
	
	public BigDecimal getCapitalaccidente() {
		return this.capitalaccidente; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setFechaefecto(
		Date var
	) {
		this.fechaefecto=var; 
	}
	
	public Date getFechaefecto() {
		return this.fechaefecto; 
	}
	
	public void setFechapropuesta(
		Date var
	) {
		this.fechapropuesta=var; 
	}
	
	public Date getFechapropuesta() {
		return this.fechapropuesta; 
	}
	
	public void setFecitv(
		Date var
	) {
		this.fecitv=var; 
	}
	
	public Date getFecitv() {
		return this.fecitv; 
	}
	
	public void setFecmatricula(
		Date var
	) {
		this.fecmatricula=var; 
	}
	
	public Date getFecmatricula() {
		return this.fecmatricula; 
	}
	
	public void setHospitalizacion(
		boolean var
	) {
		this.hospitalizacion=var; 
	}
	
	public boolean isHospitalizacion() {
		return this.hospitalizacion; 
	}
	
	public void setIdxsegmsolicitud(
		Long var
	) {
		this.idxsegmsolicitud=var; 
	}
	
	public Long getIdxsegmsolicitud() {
		return this.idxsegmsolicitud; 
	}
	
	public void setMarca(
		String var
	) {
		this.marca=var; 
	}
	
	public String getMarca() {
		return this.marca; 
	}
	
	public void setMatricula(
		String var
	) {
		this.matricula=var; 
	}
	
	public String getMatricula() {
		return this.matricula; 
	}
	
	public void setModelo(
		String var
	) {
		this.modelo=var; 
	}
	
	public String getModelo() {
		return this.modelo; 
	}
	
	public void setPlazas(
		BigDecimal var
	) {
		this.plazas=var; 
	}
	
	public BigDecimal getPlazas() {
		return this.plazas; 
	}
	
	public void setSolicitud(
		String var
	) {
		this.solicitud=var; 
	}
	
	public String getSolicitud() {
		return this.solicitud; 
	}
	
	public void setSuperficie(
		Integer var
	) {
		this.superficie=var; 
	}
	
	public Integer getSuperficie() {
		return this.superficie; 
	}
	
	public void setValorinmueble(
		BigDecimal var
	) {
		this.valorinmueble=var; 
	}
	
	public BigDecimal getValorinmueble() {
		return this.valorinmueble; 
	}
	
	public void setValormobiliario(
		BigDecimal var
	) {
		this.valormobiliario=var; 
	}
	
	public BigDecimal getValormobiliario() {
		return this.valormobiliario; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmtiposeg(
		Segmtiposeg var
	) {
		this.idsegmtiposeg=var; 
	}
	
	public Segmtiposeg getIdsegmtiposeg() {
		if(this.idsegmtiposeg==null)this.idsegmtiposeg=new org.suinsit.apps.seguros.Segmtiposeg();
		  return this.idsegmtiposeg; 
	}
	
	public void setIdcrmempresa(
		Crmempresa var
	) {
		this.idcrmempresa=var; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public void setSubsegrsolicitudprop(
		List<Segrsolicitudprop> var
	) {
		this.subsegrsolicitudprop=var; 
	}
	
	public List<Segrsolicitudprop> getSubsegrsolicitudprop() {
		if(this.subsegrsolicitudprop==null)this.subsegrsolicitudprop=new ArrayList<>(0);
		  return this.subsegrsolicitudprop; 
	} 

}