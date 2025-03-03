package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.math.BigDecimal;
import java.util.List;
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
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Sletipomodelo;
import org.suinsit.apps.suinless.Sltemplate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLTEMPLATEMETA" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLTEMPLATEMETA",
	pk = "idxsltemplatemeta" 
)
public class Sltemplatemeta implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 1535 
	)
	@Column (
		name = "requiredpermission",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "LIST_STRING" 
	)
	private List requiredpermission;
	@Column (
		name = "enablerag",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean enablerag;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "temperature",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal temperature;
	@Column (
		name = "maxtokens",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxtokens;
	@Id
	@Column (
		name = "idxsltemplatemeta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsltemplatemeta;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLETIPOMODELO0",
		referencedColumnName = "IDXSLETIPOMODELO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sletipomodelo idsletipomodelo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESMODEL0",
		referencedColumnName = "IDXSLESMODEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesmodel idslesmodel;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLTEMPLATE0",
		referencedColumnName = "IDXSLTEMPLATE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sltemplate idsltemplate; 

	public Sletipomodelo getIdsletipomodelo() {
		if(this.idsletipomodelo==null)this.idsletipomodelo=new org.suinsit.apps.suinless.Sletipomodelo();
		  return this.idsletipomodelo; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Sltemplate getIdsltemplate() {
		if(this.idsltemplate==null)this.idsltemplate=new org.suinsit.apps.suinless.Sltemplate();
		  return this.idsltemplate; 
	} 

}