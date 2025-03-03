package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Sleestadogob;
import org.suinsit.apps.suinless.Slenivelgob;
import org.suinsit.apps.suinless.Sletipoprovgob;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLPROVIDER" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLPROVIDER",
	labelMonitor = "PROVIDER",
	pk = "idxslprovider" 
)
public class Slprovider implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "token",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String token;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "userkey",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String userkey;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "accesskey",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String accesskey;
	@Column (
		name = "avatar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object avatar;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clazz",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String clazz;
	@Column (
		name = "comunity",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean comunity;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "endpoint",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String endpoint;
	@Id
	@Column (
		name = "idxslprovider",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslprovider;
	@Column (
		name = "infohome",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "CLOB" 
	)
	private String infohome;
	@Column (
		name = "opensource",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean opensource;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "provider",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String provider;
	@Column (
		name = "showhome",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BOOLEAN" 
	)
	private boolean showhome;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlweb",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String urlweb;
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
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLENIVELGOB0",
		referencedColumnName = "IDXSLENIVELGOB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slenivelgob idslenivelgob;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLETIPOPROVGOB0",
		referencedColumnName = "IDXSLETIPOPROVGOB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sletipoprovgob idsletipoprovgob; 

	public Sleestadogob getIdsleestadogob() {
		if(this.idsleestadogob==null)this.idsleestadogob=new org.suinsit.apps.suinless.Sleestadogob();
		  return this.idsleestadogob; 
	}
	
	public Slenivelgob getIdslenivelgob() {
		if(this.idslenivelgob==null)this.idslenivelgob=new org.suinsit.apps.suinless.Slenivelgob();
		  return this.idslenivelgob; 
	}
	
	public Sletipoprovgob getIdsletipoprovgob() {
		if(this.idsletipoprovgob==null)this.idsletipoprovgob=new org.suinsit.apps.suinless.Sletipoprovgob();
		  return this.idsletipoprovgob; 
	} 

}