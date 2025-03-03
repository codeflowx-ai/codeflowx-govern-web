package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Sltemplate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLRTEMPLATEMODELS" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLRTEMPLATEMODELS",
	pk = "idxslrtemplatemodels" 
)
public class Slrtemplatemodels implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslrtemplatemodels",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslrtemplatemodels;
	private boolean updatable;
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

	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	}
	
	public Sltemplate getIdsltemplate() {
		if(this.idsltemplate==null)this.idsltemplate=new org.suinsit.apps.suinless.Sltemplate();
		  return this.idsltemplate; 
	} 

}