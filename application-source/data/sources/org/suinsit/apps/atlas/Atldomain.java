package org.suinsit.apps.atlas;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.atlas.Atlproveedor;
import org.suinsit.apps.atlas.Atlrprojdns;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.myalm.Almproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLDOMAIN" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLDOMAIN",
	labelMonitor = "DOMAIN",
	pk = "idxatldomain" 
)
public class Atldomain implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "contactadmin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String contactadmin;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "contactbilling",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String contactbilling;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "contacttech",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String contacttech;
	@Column (
		name = "creacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date creacion;
	@Column (
		name = "deleteexpire",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean deleteexpire;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "domain",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String domain;
	@Column (
		name = "expira",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date expira;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String idproveedor;
	@Id
	@Column (
		name = "idxatldomain",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatldomain;
	@Column (
		name = "locked",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean locked;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proveedor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "renuevaltype",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String renuevaltype;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "serviceid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String serviceid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "status",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String status;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLPROVEEDOR0",
		referencedColumnName = "IDXATLPROVEEDOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlproveedor idatlproveedor;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLPROJECT0",
		referencedColumnName = "IDXATLPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlproject idatlproject;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatldomain" 
	)
	private List<Atlrprojdns> subatlrprojdns;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatldomain" 
	)
	private List<Almproject> subalmproject; 

	public Atlproveedor getIdatlproveedor() {
		if(this.idatlproveedor==null)this.idatlproveedor=new org.suinsit.apps.atlas.Atlproveedor();
		  return this.idatlproveedor; 
	}
	
	public Atlproject getIdatlproject() {
		if(this.idatlproject==null)this.idatlproject=new org.suinsit.apps.atlas.Atlproject();
		  return this.idatlproject; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public List<Atlrprojdns> getSubatlrprojdns() {
		if(this.subatlrprojdns==null)this.subatlrprojdns=new ArrayList<>(0);
		  return this.subatlrprojdns; 
	}
	
	public List<Almproject> getSubalmproject() {
		if(this.subalmproject==null)this.subalmproject=new ArrayList<>(0);
		  return this.subalmproject; 
	} 

}