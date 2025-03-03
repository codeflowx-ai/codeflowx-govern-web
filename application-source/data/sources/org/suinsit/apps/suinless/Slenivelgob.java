package org.suinsit.apps.suinless;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Sleestadogob;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLENIVELGOB" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLENIVELGOB",
	labelMonitor = "NIVEL",
	pk = "idxslenivelgob" 
)
public class Slenivelgob implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "actualizacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date actualizacion;
	@Column (
		name = "datavector",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean datavector;
	@Id
	@Column (
		name = "idxslenivelgob",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslenivelgob;
	@Column (
		name = "infopublic",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infopublic;
	@Column (
		name = "model",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean model;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nivel",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nivel;
	@Column (
		name = "personaldata",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean personaldata;
	@Column (
		name = "prompter",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean prompter;
	@Column (
		name = "proveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean proveedor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlnormativa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlnormativa;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLEESTADOGOB0",
		referencedColumnName = "IDXSLEESTADOGOB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sleestadogob idsleestadogob;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslenivelgob" 
	)
	private List<Slprovider> subslprovider; 

	public Sleestadogob getIdsleestadogob() {
		if(this.idsleestadogob==null)this.idsleestadogob=new org.suinsit.apps.suinless.Sleestadogob();
		  return this.idsleestadogob; 
	}
	
	public List<Slprovider> getSubslprovider() {
		if(this.subslprovider==null)this.subslprovider=new ArrayList<>(0);
		  return this.subslprovider; 
	} 

}