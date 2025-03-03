package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.seguros.Segmtipoprov;

@Entity
@Table (
	name = "SEGMPROVEEDOR" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmproveedor" 
)
public class Segmproveedor implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "CODPROVEEDOR",
		nullable = false 
	)
	@Sequence (
		name = "SEGMPROVEEDOR_CODPROVEEDOR",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codproveedor;
	@Id
	@Column (
		name = "IDXSEGMPROVEEDOR",
		nullable = true 
	)
	private Long idxsegmproveedor;
	private boolean updatable;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPOPROV0",
		referencedColumnName = "IDXSEGMTIPOPROV",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtipoprov idsegmtipoprov; 

	public void setCodproveedor(
		String var
	) {
		this.codproveedor=var; 
	}
	
	public String getCodproveedor() {
		return this.codproveedor; 
	}
	
	public void setIdxsegmproveedor(
		Long var
	) {
		this.idxsegmproveedor=var; 
	}
	
	public Long getIdxsegmproveedor() {
		return this.idxsegmproveedor; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
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
	
	public void setIdsegmtipoprov(
		Segmtipoprov var
	) {
		this.idsegmtipoprov=var; 
	}
	
	public Segmtipoprov getIdsegmtipoprov() {
		if(this.idsegmtipoprov==null)this.idsegmtipoprov=new org.suinsit.apps.seguros.Segmtipoprov();
		  return this.idsegmtipoprov; 
	} 

}