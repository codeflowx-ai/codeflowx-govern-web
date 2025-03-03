package org.suinsit.apps.subvenciones;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.suinsit.apps.subvenciones.Kitrbonocat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "KITRHORASCAT" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "KITRHORASCAT",
	labelMonitor = "concepto",
	pk = "idxkitrhorascat" 
)
public class Kitrhorascat implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",JUSTIFICACION,SUBSANACION,ASISTENCIA,OTROS" 
		},
		message = "solamente admite lo valores: ,JUSTIFICACION,SUBSANACION,ASISTENCIA,OTROS" 
	)
	@Column (
		name = "motivo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String motivo;
	@NotNull
	@NotBlank
	@Column (
		name = "fecha",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "horas",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal horas;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "concepto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String concepto;
	@Id
	@Column (
		name = "idxkitrhorascat",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxkitrhorascat;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDKITRBONOCAT0",
		referencedColumnName = "IDXKITRBONOCAT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitrbonocat idkitrbonocat;
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

	public Kitrbonocat getIdkitrbonocat() {
		if(this.idkitrbonocat==null)this.idkitrbonocat=new org.suinsit.apps.subvenciones.Kitrbonocat();
		  return this.idkitrbonocat; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}