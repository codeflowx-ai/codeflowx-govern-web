package org.suinsit.apps.asesor4;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;

@Entity
@Table (
	name = "EXPEMP2" 
)
@Entidad (
	type = "TABLE",
	pk = "idx" 
)
public class Expemp2 implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CIUDAD",
		nullable = true 
	)
	private String ciudad;
	@Column (
		name = "COMPANYID",
		nullable = true 
	)
	private Integer companyid;
	@Column (
		name = "ESTADOREGIYN",
		nullable = true 
	)
	private String estadoregiyn;
	@Column (
		name = "FECHADECREACIYN",
		nullable = true 
	)
	private Timestamp fechadecreaciyn;
	@Column (
		name = "FECHADELAYLTIMAACTIVIDAD",
		nullable = true 
	)
	private Timestamp fechadelayltimaactividad;
	@Id
	@Column (
		name = "IDX",
		nullable = false 
	)
	private Long idx;
	@Column (
		name = "INDUSTRIA",
		nullable = true 
	)
	private String industria;
	@Column (
		name = "NOMBREDELAEMPRESA",
		nullable = true 
	)
	private String nombredelaempresa;
	@Column (
		name = "PAYSREGIYN",
		nullable = true 
	)
	private String paysregiyn;
	@Column (
		name = "PHONENUMBER",
		nullable = true 
	)
	private String phonenumber;
	@Column (
		name = "PROPIETARIODELREGISTRODEEMPRESA",
		nullable = true 
	)
	private String propietariodelregistrodeempresa;
	private boolean updatable; 

	public void setCiudad(
		String var
	) {
		this.ciudad=var; 
	}
	
	public String getCiudad() {
		return this.ciudad; 
	}
	
	public void setCompanyid(
		Integer var
	) {
		this.companyid=var; 
	}
	
	public Integer getCompanyid() {
		return this.companyid; 
	}
	
	public void setEstadoregiyn(
		String var
	) {
		this.estadoregiyn=var; 
	}
	
	public String getEstadoregiyn() {
		return this.estadoregiyn; 
	}
	
	public void setFechadecreaciyn(
		Timestamp var
	) {
		this.fechadecreaciyn=var; 
	}
	
	public Timestamp getFechadecreaciyn() {
		return this.fechadecreaciyn; 
	}
	
	public void setFechadelayltimaactividad(
		Timestamp var
	) {
		this.fechadelayltimaactividad=var; 
	}
	
	public Timestamp getFechadelayltimaactividad() {
		return this.fechadelayltimaactividad; 
	}
	
	public void setIdx(
		Long var
	) {
		this.idx=var; 
	}
	
	public Long getIdx() {
		return this.idx; 
	}
	
	public void setIndustria(
		String var
	) {
		this.industria=var; 
	}
	
	public String getIndustria() {
		return this.industria; 
	}
	
	public void setNombredelaempresa(
		String var
	) {
		this.nombredelaempresa=var; 
	}
	
	public String getNombredelaempresa() {
		return this.nombredelaempresa; 
	}
	
	public void setPaysregiyn(
		String var
	) {
		this.paysregiyn=var; 
	}
	
	public String getPaysregiyn() {
		return this.paysregiyn; 
	}
	
	public void setPhonenumber(
		String var
	) {
		this.phonenumber=var; 
	}
	
	public String getPhonenumber() {
		return this.phonenumber; 
	}
	
	public void setPropietariodelregistrodeempresa(
		String var
	) {
		this.propietariodelregistrodeempresa=var; 
	}
	
	public String getPropietariodelregistrodeempresa() {
		return this.propietariodelregistrodeempresa; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	} 

}