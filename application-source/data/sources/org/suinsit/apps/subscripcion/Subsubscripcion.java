package org.suinsit.apps.subscripcion;

import java.io.Serializable;
import java.lang.Integer;
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
import org.suinsit.apps.subscripcion.Subtiposub;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBSUBSCRIPCION" 
)
@Entidad (
	namespace = "subscripcion",
	type = "TABLE",
	name = "SUBSUBSCRIPCION",
	pk = "idxsubsubscripcion" 
)
public class Subsubscripcion implements Serializable { 

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
	@Column (
		name = "fecalta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecalta;
	@Column (
		name = "fecbaja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecbaja;
	@Id
	@Column (
		name = "idxsubsubscripcion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubsubscripcion;
	@Column (
		name = "projectlimits",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer projectlimits;
	@Column (
		name = "tokenlimits",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer tokenlimits;
	@Column (
		name = "userlimits",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer userlimits;
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

	public Subtiposub getIdsubtiposub() {
		if(this.idsubtiposub==null)this.idsubtiposub=new org.suinsit.apps.subscripcion.Subtiposub();
		  return this.idsubtiposub; 
	} 

}