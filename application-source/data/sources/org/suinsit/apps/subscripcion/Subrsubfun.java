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
import org.suinsit.apps.subscripcion.Subfuncionalidad;
import org.suinsit.apps.subscripcion.Subtiposub;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBRSUBFUN" 
)
@Entidad (
	namespace = "subscripcion",
	type = "TABLE",
	name = "SUBRSUBFUN",
	pk = "idxsubrsubfun" 
)
public class Subrsubfun implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "opcional",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean opcional;
	@Column (
		name = "limite",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer limite;
	@Id
	@Column (
		name = "idxsubrsubfun",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubrsubfun;
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
		name = "IDSUBFUNCIONALIDAD0",
		referencedColumnName = "IDXSUBFUNCIONALIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subfuncionalidad idsubfuncionalidad; 

	public Subtiposub getIdsubtiposub() {
		if(this.idsubtiposub==null)this.idsubtiposub=new org.suinsit.apps.subscripcion.Subtiposub();
		  return this.idsubtiposub; 
	}
	
	public Subfuncionalidad getIdsubfuncionalidad() {
		if(this.idsubfuncionalidad==null)this.idsubfuncionalidad=new org.suinsit.apps.subscripcion.Subfuncionalidad();
		  return this.idsubfuncionalidad; 
	} 

}