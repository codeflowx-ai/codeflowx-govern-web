package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
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
import org.suinsit.apps.marketing.Mktrpostagency;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTRPUBLISHPOST" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTRPUBLISHPOST",
	labelMonitor = "",
	pk = "idxmktrpublishpost" 
)
public class Mktrpublishpost implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "idxmktrpublishpost",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktrpublishpost;
	@Column (
		name = "likes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer likes;
	@Column (
		name = "publicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp publicacion;
	@Column (
		name = "reenvio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer reenvio;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refpublicacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refpublicacion;
	@Column (
		name = "visitors",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer visitors;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTRPOSTAGENCY0",
		referencedColumnName = "IDXMKTRPOSTAGENCY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktrpostagency idmktrpostagency; 

	public Mktrpostagency getIdmktrpostagency() {
		if(this.idmktrpostagency==null)this.idmktrpostagency=new org.suinsit.apps.marketing.Mktrpostagency();
		  return this.idmktrpostagency; 
	} 

}