package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.sql.Timestamp;
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
import org.suinsit.apps.portalemp.Rrhempleado;
import org.suinsit.apps.portalemp.Rrhhturno;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHAUSENCIA" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHAUSENCIA",
	pk = "idxrrhhausencia" 
)
public class Rrhhausencia implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "aprobada",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean aprobada;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String comentarios;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String couseralta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "dias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer dias;
	@Column (
		name = "faprovacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date faprovacion;
	@Column (
		name = "fin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fin;
	@Id
	@Column (
		name = "idxrrhhausencia",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhausencia;
	@NotNull
	@NotBlank
	@Column (
		name = "inicio",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date inicio;
	@Column (
		name = "iniciohora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp iniciohora;
	@Column (
		name = "numhoras",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer numhoras;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Vacaciones,Médica,Compensación,Otros" 
		},
		message = "solamente admite lo valores: ,Vacaciones,Médica,Compensación,Otros" 
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
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHEMPLEADO0",
		referencedColumnName = "IDXRRHEMPLEADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhempleado idrrhempleado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHTURNO0",
		referencedColumnName = "IDXRRHHTURNO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhturno idrrhhturno; 

	public Rrhempleado getIdrrhempleado() {
		if(this.idrrhempleado==null)this.idrrhempleado=new org.suinsit.apps.portalemp.Rrhempleado();
		  return this.idrrhempleado; 
	}
	
	public Rrhhturno getIdrrhhturno() {
		if(this.idrrhhturno==null)this.idrrhhturno=new org.suinsit.apps.portalemp.Rrhhturno();
		  return this.idrrhhturno; 
	} 

}