package org.suinsit.apps.subscripcion;

import java.io.Serializable;
import java.lang.Integer;
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
import org.suinsit.apps.subscripcion.Subtiposub;
import org.suinsit.apps.suinless.Slesmodel;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBRTIPOMODELO" 
)
@Entidad (
	namespace = "subscripcion",
	type = "TABLE",
	name = "SUBRTIPOMODELO",
	pk = "idxsubrtipomodelo" 
)
public class Subrtipomodelo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "paidused",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean paidused;
	@Column (
		name = "maxminutes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxminutes;
	@Column (
		name = "maxdoc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxdoc;
	@Column (
		name = "maximagen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maximagen;
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
		name = "idxsubrtipomodelo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubrtipomodelo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBTIPOSUB0",
		referencedColumnName = "IDXSUBTIPOSUB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subtiposub idsubtiposub;
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

	public Subtiposub getIdsubtiposub() {
		if(this.idsubtiposub==null)this.idsubtiposub=new org.suinsit.apps.subscripcion.Subtiposub();
		  return this.idsubtiposub; 
	}
	
	public Slesmodel getIdslesmodel() {
		if(this.idslesmodel==null)this.idslesmodel=new org.suinsit.apps.suinless.Slesmodel();
		  return this.idslesmodel; 
	} 

}