package org.suinsit.apps.marketing;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.marketing.Mktpotenciales;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTREUNION" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTREUNION",
	labelMonitor = "tipo",
	pk = "idxmktreunion" 
)
public class Mktreunion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentarios;
	@Column (
		name = "fechahora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fechahora;
	@Id
	@Column (
		name = "idxmktreunion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktreunion;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",POSITIVO,NEGATIVO,NEUTRO" 
		},
		message = "solamente admite lo valores: ,POSITIVO,NEGATIVO,NEUTRO" 
	)
	@Column (
		name = "resultado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String resultado;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",DEMO,PRIMER CONTACTO,SEGUIMIENTO,OTROS" 
		},
		message = "solamente admite lo valores: ,DEMO,PRIMER CONTACTO,SEGUIMIENTO,OTROS" 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPOTENCIALES0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktpotenciales idmktpotenciales;
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

	public Mktpotenciales getIdmktpotenciales() {
		if(this.idmktpotenciales==null)this.idmktpotenciales=new org.suinsit.apps.marketing.Mktpotenciales();
		  return this.idmktpotenciales; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}