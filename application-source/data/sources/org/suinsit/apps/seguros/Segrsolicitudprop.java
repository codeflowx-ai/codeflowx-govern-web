package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.seguros.Segmcompany;
import org.suinsit.apps.seguros.Segmproducto;
import org.suinsit.apps.seguros.Segmsolicitud;

@Entity
@Table (
	name = "SEGRSOLICITUDPROP" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegrsolicitudprop" 
)
public class Segrsolicitudprop implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "ACEPTADA",
		nullable = true 
	)
	private boolean aceptada;
	@Column (
		name = "FECVALIDEZ",
		nullable = true 
	)
	private Date fecvalidez;
	@Column (
		name = "PRIMATOTAL",
		nullable = true 
	)
	private BigDecimal primatotal;
	@Id
	@Column (
		name = "IDXSEGRSOLICITUDPROP",
		nullable = true 
	)
	private Long idxsegrsolicitudprop;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMSOLICITUD0",
		referencedColumnName = "IDXSEGMSOLICITUD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmsolicitud idsegmsolicitud;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMCOMPANY0",
		referencedColumnName = "IDXSEGMCOMPANY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmcompany idsegmcompany;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPRODUCTO0",
		referencedColumnName = "IDXSEGMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmproducto idsegmproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario; 

	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setAceptada(
		boolean var
	) {
		this.aceptada=var; 
	}
	
	public boolean isAceptada() {
		return this.aceptada; 
	}
	
	public void setFecvalidez(
		Date var
	) {
		this.fecvalidez=var; 
	}
	
	public Date getFecvalidez() {
		return this.fecvalidez; 
	}
	
	public void setPrimatotal(
		BigDecimal var
	) {
		this.primatotal=var; 
	}
	
	public BigDecimal getPrimatotal() {
		return this.primatotal; 
	}
	
	public void setIdxsegrsolicitudprop(
		Long var
	) {
		this.idxsegrsolicitudprop=var; 
	}
	
	public Long getIdxsegrsolicitudprop() {
		return this.idxsegrsolicitudprop; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmsolicitud(
		Segmsolicitud var
	) {
		this.idsegmsolicitud=var; 
	}
	
	public Segmsolicitud getIdsegmsolicitud() {
		if(this.idsegmsolicitud==null)this.idsegmsolicitud=new org.suinsit.apps.seguros.Segmsolicitud();
		  return this.idsegmsolicitud; 
	}
	
	public void setIdsegmcompany(
		Segmcompany var
	) {
		this.idsegmcompany=var; 
	}
	
	public Segmcompany getIdsegmcompany() {
		if(this.idsegmcompany==null)this.idsegmcompany=new org.suinsit.apps.seguros.Segmcompany();
		  return this.idsegmcompany; 
	}
	
	public void setIdsegmproducto(
		Segmproducto var
	) {
		this.idsegmproducto=var; 
	}
	
	public Segmproducto getIdsegmproducto() {
		if(this.idsegmproducto==null)this.idsegmproducto=new org.suinsit.apps.seguros.Segmproducto();
		  return this.idsegmproducto; 
	}
	
	public void setIdssousuario(
		Ssousuario var
	) {
		this.idssousuario=var; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}