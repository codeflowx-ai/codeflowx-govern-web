package org.suinsit.apps.mrp;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.mrp.Mrpexpedicion;
import org.suinsit.apps.mrp.Mrpmtipoperacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPMSEQUENCE" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPMSEQUENCE",
	labelMonitor = "secuencianame",
	pk = "idxmrpmsequence" 
)
public class Mrpmsequence implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namesql",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String namesql;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activo;
	@Id
	@Column (
		name = "idxmrpmsequence",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrpmsequence;
	@Column (
		name = "incremento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer incremento;
	@Column (
		name = "nextnumber",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer nextnumber;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "prefijo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String prefijo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "secuencianame",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String secuencianame;
	@Column (
		name = "size",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer size;
	@Column (
		name = "subsecuenciadate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean subsecuenciadate;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sufijo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String sufijo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCOMPANY0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa idcompany;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpmsequence" 
	)
	private List<Mrpmtipoperacion> submrpmtipoperacion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpmsequence" 
	)
	private List<Mrpexpedicion> submrpexpedicion; 

	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public List<Mrpmtipoperacion> getSubmrpmtipoperacion() {
		if(this.submrpmtipoperacion==null)this.submrpmtipoperacion=new ArrayList<>(0);
		  return this.submrpmtipoperacion; 
	}
	
	public List<Mrpexpedicion> getSubmrpexpedicion() {
		if(this.submrpexpedicion==null)this.submrpexpedicion=new ArrayList<>(0);
		  return this.submrpexpedicion; 
	} 

}