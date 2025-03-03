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
import org.suinsit.apps.admin.Ssousuario;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBTOKENBALANCE" 
)
@Entidad (
	namespace = "subscripcion",
	type = "TABLE",
	name = "SUBTOKENBALANCE",
	pk = "idxsubtokenbalance" 
)
public class Subtokenbalance implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "purchasetoken",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer purchasetoken;
	@Id
	@Column (
		name = "idxsubtokenbalance",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubtokenbalance;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}