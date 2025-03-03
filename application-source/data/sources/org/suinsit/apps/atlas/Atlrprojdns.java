package org.suinsit.apps.atlas;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.atlas.Atldomain;
import org.suinsit.apps.atlas.Atlproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLRPROJDNS" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLRPROJDNS",
	labelMonitor = "",
	pk = "idxatlrprojdns" 
)
public class Atlrprojdns implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "modificacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp modificacion;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "direccionurl",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String direccionurl;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dirip",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dirip;
	@Id
	@Column (
		name = "idxatlrprojdns",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlrprojdns;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ingress",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ingress;
	@Size (
		min = 0,
		max = 500 
	)
	@Column (
		name = "mensaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String mensaje;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subdomain",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String subdomain;
	private boolean updatable;
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
		name = "IDATLDOMAIN0",
		referencedColumnName = "IDXATLDOMAIN",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atldomain idatldomain; 

	public Atlproject getIdatlproject() {
		if(this.idatlproject==null)this.idatlproject=new org.suinsit.apps.atlas.Atlproject();
		  return this.idatlproject; 
	}
	
	public Atldomain getIdatldomain() {
		if(this.idatldomain==null)this.idatldomain=new org.suinsit.apps.atlas.Atldomain();
		  return this.idatldomain; 
	} 

}