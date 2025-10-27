package org.suinsit.apps.citaprev;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.sql.Time;
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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.citaprev.Citmemploy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CITRUSUHORARIO" 
)
@Entidad (
	namespace = "citaprev",
	type = "TABLE",
	name = "CITRUSUHORARIO",
	labelMonitor = "dias",
	pk = "idxcrmrusuhorario" 
)
public class Citrusuhorario implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Column (
		name = "desde",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date desde;
	@NotNull
	@NotBlank
	@Column (
		name = "desdehora",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIME" 
	)
	private Time desdehora;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
		},
		message = "solamente admite lo valores: ,Lunes,Martes,Miércoles,Jueves,Viernes,Sabado,Domingo" 
	)
	@Column (
		name = "dias",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String dias;
	@NotNull
	@NotBlank
	@Column (
		name = "hasta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date hasta;
	@Column (
		name = "hastahora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIME" 
	)
	private Time hastahora;
	@Id
	@Column (
		name = "idxcrmrusuhorario",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmrusuhorario;
	@Column (
		name = "maxcitaparalell",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer maxcitaparalell;
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

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Citmemploy getIdcitmemploy() {
		if(this.idcitmemploy==null)this.idcitmemploy=new org.suinsit.apps.citaprev.Citmemploy();
		  return this.idcitmemploy; 
	} 

}