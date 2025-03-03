package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.seguros.Segmcompany;
import org.suinsit.apps.seguros.Segmpoliza;
import org.suinsit.apps.seguros.Segmramo;
import org.suinsit.apps.seguros.Segmtipoprod;
import org.suinsit.apps.seguros.Segmtiposeg;
import org.suinsit.apps.seguros.Segrprodgarantia;

@Entity
@Table (
	name = "SEGMPRODUCTO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmproducto" 
)
public class Segmproducto implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ACTIVO",
		nullable = true 
	)
	private boolean activo;
	@Column (
		name = "CODPRODUCTO",
		nullable = true 
	)
	@Sequence (
		name = "SEGMPRODUCTO_CODPRODUCTO",
		prefix = "",
		mask = "00000000",
		addYear = false 
	)
	private String codproducto;
	@Column (
		name = "COMISION",
		nullable = true 
	)
	private BigDecimal comision;
	@Column (
		name = "COMISIONCBRO",
		nullable = true 
	)
	private BigDecimal comisioncbro;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "DESCRIPPUBLI",
		nullable = true 
	)
	private String descrippubli;
	@Id
	@Column (
		name = "IDXSEGMPRODUCTO",
		nullable = true 
	)
	private Long idxsegmproducto;
	@Column (
		name = "IMPORTEFIJO",
		nullable = true 
	)
	private BigDecimal importefijo;
	@Column (
		name = "NOMBRE",
		nullable = false 
	)
	private String nombre;
	@Column (
		name = "REFCOMPANY",
		nullable = true 
	)
	private String refcompany;
	@Column (
		name = "TIPOCOMISION",
		nullable = true 
	)
	private String tipocomision;
	private boolean updatable;
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
		name = "IDSEGMRAMO0",
		referencedColumnName = "IDXSEGMRAMO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmramo idsegmramo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPOPROD0",
		referencedColumnName = "IDXSEGMTIPOPROD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtipoprod idsegmtipoprod;
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
	private List<Segmpoliza> subsegmpoliza;
	private List<Segrprodgarantia> subsegrprodgarantia; 

	public void setActivo(
		boolean var
	) {
		this.activo=var; 
	}
	
	public boolean isActivo() {
		return this.activo; 
	}
	
	public void setCodproducto(
		String var
	) {
		this.codproducto=var; 
	}
	
	public String getCodproducto() {
		return this.codproducto; 
	}
	
	public void setComision(
		BigDecimal var
	) {
		this.comision=var; 
	}
	
	public BigDecimal getComision() {
		return this.comision; 
	}
	
	public void setComisioncbro(
		BigDecimal var
	) {
		this.comisioncbro=var; 
	}
	
	public BigDecimal getComisioncbro() {
		return this.comisioncbro; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setDescrippubli(
		String var
	) {
		this.descrippubli=var; 
	}
	
	public String getDescrippubli() {
		return this.descrippubli; 
	}
	
	public void setIdxsegmproducto(
		Long var
	) {
		this.idxsegmproducto=var; 
	}
	
	public Long getIdxsegmproducto() {
		return this.idxsegmproducto; 
	}
	
	public void setImportefijo(
		BigDecimal var
	) {
		this.importefijo=var; 
	}
	
	public BigDecimal getImportefijo() {
		return this.importefijo; 
	}
	
	public void setNombre(
		String var
	) {
		this.nombre=var; 
	}
	
	public String getNombre() {
		return this.nombre; 
	}
	
	public void setRefcompany(
		String var
	) {
		this.refcompany=var; 
	}
	
	public String getRefcompany() {
		return this.refcompany; 
	}
	
	public void setTipocomision(
		String var
	) {
		this.tipocomision=var; 
	}
	
	public String getTipocomision() {
		return this.tipocomision; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
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
	
	public void setIdsegmramo(
		Segmramo var
	) {
		this.idsegmramo=var; 
	}
	
	public Segmramo getIdsegmramo() {
		if(this.idsegmramo==null)this.idsegmramo=new org.suinsit.apps.seguros.Segmramo();
		  return this.idsegmramo; 
	}
	
	public void setIdsegmtipoprod(
		Segmtipoprod var
	) {
		this.idsegmtipoprod=var; 
	}
	
	public Segmtipoprod getIdsegmtipoprod() {
		if(this.idsegmtipoprod==null)this.idsegmtipoprod=new org.suinsit.apps.seguros.Segmtipoprod();
		  return this.idsegmtipoprod; 
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
	
	public void setSubsegmpoliza(
		List<Segmpoliza> var
	) {
		this.subsegmpoliza=var; 
	}
	
	public List<Segmpoliza> getSubsegmpoliza() {
		if(this.subsegmpoliza==null)this.subsegmpoliza=new ArrayList<>(0);
		  return this.subsegmpoliza; 
	}
	
	public void setSubsegrprodgarantia(
		List<Segrprodgarantia> var
	) {
		this.subsegrprodgarantia=var; 
	}
	
	public List<Segrprodgarantia> getSubsegrprodgarantia() {
		if(this.subsegrprodgarantia==null)this.subsegrprodgarantia=new ArrayList<>(0);
		  return this.subsegrprodgarantia; 
	} 

}