package org.suinsit.apps.citaprev;

import java.io.Serializable;
import java.lang.Long;
import java.sql.Date;
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
import org.suinsit.apps.citaprev.Citmemploy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CITRUSUDIALIBRE" 
)
@Entidad (
	namespace = "citaprev",
	type = "TABLE",
	name = "CITRUSUDIALIBRE",
	labelMonitor = "",
	pk = "idxcitrusudialibre" 
)
public class Citrusudialibre implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@Id
	@Column (
		name = "idxcitrusudialibre",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcitrusudialibre;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCITMEMPLOY0",
		referencedColumnName = "IDXCITMEMPLOY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Citmemploy idcitmemploy; 

	public Citmemploy getIdcitmemploy() {
		if(this.idcitmemploy==null)this.idcitmemploy=new org.suinsit.apps.citaprev.Citmemploy();
		  return this.idcitmemploy; 
	} 

}