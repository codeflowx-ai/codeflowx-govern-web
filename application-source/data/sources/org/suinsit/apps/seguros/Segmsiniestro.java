package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
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
import org.suinsit.apps.seguros.Segmperito;
import org.suinsit.apps.seguros.Segmpoliza;
import org.suinsit.apps.seguros.Segmproveedor;

@Entity
@Table (
	name = "SEGMSINIESTRO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmsiniestro" 
)
public class Segmsiniestro implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "APERTURA",
		nullable = true 
	)
	private Timestamp apertura;
	@Column (
		name = "CIERRE",
		nullable = true 
	)
	private Timestamp cierre;
	@Column (
		name = "CODSINIESTRO",
		nullable = false 
	)
	@Sequence (
		name = "SEGMSINIESTRO_CODSINIESTRO",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codsiniestro;
	@Column (
		name = "DANYOS",
		nullable = true 
	)
	private String danyos;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "DIRECCION",
		nullable = true 
	)
	private String direccion;
	@Column (
		name = "FECSINIESTRO",
		nullable = true 
	)
	private Timestamp fecsiniestro;
	@Id
	@Column (
		name = "IDXSEGMSINIESTRO",
		nullable = true 
	)
	private Long idxsegmsiniestro;
	@Column (
		name = "NOTASPERITO",
		nullable = true 
	)
	private String notasperito;
	@Column (
		name = "NOTASSERVICIO",
		nullable = true 
	)
	private String notasservicio;
	@Column (
		name = "POBLACION",
		nullable = true 
	)
	private String poblacion;
	@Column (
		name = "SINIESTROCIA",
		nullable = true 
	)
	private String siniestrocia;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPOLIZA0",
		referencedColumnName = "IDXSEGMPOLIZA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmpoliza idsegmpoliza;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPERITO0",
		referencedColumnName = "IDXSEGMPERITO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmperito idsegmperito;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPROVEEDOR0",
		referencedColumnName = "IDXSEGMPROVEEDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmproveedor idsegmproveedor; 

	public void setApertura(
		Timestamp var
	) {
		this.apertura=var; 
	}
	
	public Timestamp getApertura() {
		return this.apertura; 
	}
	
	public void setCierre(
		Timestamp var
	) {
		this.cierre=var; 
	}
	
	public Timestamp getCierre() {
		return this.cierre; 
	}
	
	public void setCodsiniestro(
		String var
	) {
		this.codsiniestro=var; 
	}
	
	public String getCodsiniestro() {
		return this.codsiniestro; 
	}
	
	public void setDanyos(
		String var
	) {
		this.danyos=var; 
	}
	
	public String getDanyos() {
		return this.danyos; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setDireccion(
		String var
	) {
		this.direccion=var; 
	}
	
	public String getDireccion() {
		return this.direccion; 
	}
	
	public void setFecsiniestro(
		Timestamp var
	) {
		this.fecsiniestro=var; 
	}
	
	public Timestamp getFecsiniestro() {
		return this.fecsiniestro; 
	}
	
	public void setIdxsegmsiniestro(
		Long var
	) {
		this.idxsegmsiniestro=var; 
	}
	
	public Long getIdxsegmsiniestro() {
		return this.idxsegmsiniestro; 
	}
	
	public void setNotasperito(
		String var
	) {
		this.notasperito=var; 
	}
	
	public String getNotasperito() {
		return this.notasperito; 
	}
	
	public void setNotasservicio(
		String var
	) {
		this.notasservicio=var; 
	}
	
	public String getNotasservicio() {
		return this.notasservicio; 
	}
	
	public void setPoblacion(
		String var
	) {
		this.poblacion=var; 
	}
	
	public String getPoblacion() {
		return this.poblacion; 
	}
	
	public void setSiniestrocia(
		String var
	) {
		this.siniestrocia=var; 
	}
	
	public String getSiniestrocia() {
		return this.siniestrocia; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmpoliza(
		Segmpoliza var
	) {
		this.idsegmpoliza=var; 
	}
	
	public Segmpoliza getIdsegmpoliza() {
		if(this.idsegmpoliza==null)this.idsegmpoliza=new org.suinsit.apps.seguros.Segmpoliza();
		  return this.idsegmpoliza; 
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
	
	public void setIdsegmperito(
		Segmperito var
	) {
		this.idsegmperito=var; 
	}
	
	public Segmperito getIdsegmperito() {
		if(this.idsegmperito==null)this.idsegmperito=new org.suinsit.apps.seguros.Segmperito();
		  return this.idsegmperito; 
	}
	
	public void setIdsegmproveedor(
		Segmproveedor var
	) {
		this.idsegmproveedor=var; 
	}
	
	public Segmproveedor getIdsegmproveedor() {
		if(this.idsegmproveedor==null)this.idsegmproveedor=new org.suinsit.apps.seguros.Segmproveedor();
		  return this.idsegmproveedor; 
	} 

}