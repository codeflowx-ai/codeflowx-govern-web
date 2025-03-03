package org.suinsit.apps.notificaciones;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.notificaciones.Notmprioridad;
import org.suinsit.apps.notificaciones.Notmtipo;
import org.suinsit.apps.notificaciones.Notrnotiusers;
import org.suinsit.apps.notificaciones.Notrnotroles;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "NOTMNOTIFICACION" 
)
@Entidad (
	namespace = "notificaciones",
	type = "TABLE",
	name = "NOTMNOTIFICACION",
	labelMonitor = "REFERENCIA",
	pk = "idxnotmnotificacion" 
)
public class Notmnotificacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "automatico",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean automatico;
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
		name = "enviartodos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean enviartodos;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "expresioncron",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String expresioncron;
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
		type = "TIMESTAMP" 
	)
	private Timestamp fecha;
	@Id
	@Column (
		name = "idxnotmnotificacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnotmnotificacion;
	@NotNull
	@NotBlank
	@Column (
		name = "mensaje",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String mensaje;
	@Column (
		name = "mostraraviso",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mostraraviso;
	@Column (
		name = "sendemail",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sendemail;
	@Column (
		name = "systems",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean systems;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
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
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	@Column (
		name = "validez",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date validez;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "asunto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Asunto",
		type = "VARCHAR" 
	)
	private String asunto;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "NOTMNOTIFICACION_REFERENCIA",
		prefix = "NOT",
		mask = "00000000",
		addYear = true 
	)
	private String referencia;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDNOTMTIPO0",
		referencedColumnName = "IDXNOTMTIPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Notmtipo idnotmtipo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDNOTMPRIORIDAD0",
		referencedColumnName = "IDXNOTMPRIORIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Notmprioridad idnotmprioridad;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnotmnotificacion" 
	)
	private List<Notrnotroles> subnotrnotroles;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnotmnotificacion" 
	)
	private List<Notrnotiusers> subnotrnotiusers; 

	public Notmtipo getIdnotmtipo() {
		if(this.idnotmtipo==null)this.idnotmtipo=new org.suinsit.apps.notificaciones.Notmtipo();
		  return this.idnotmtipo; 
	}
	
	public Notmprioridad getIdnotmprioridad() {
		if(this.idnotmprioridad==null)this.idnotmprioridad=new org.suinsit.apps.notificaciones.Notmprioridad();
		  return this.idnotmprioridad; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public List<Notrnotroles> getSubnotrnotroles() {
		if(this.subnotrnotroles==null)this.subnotrnotroles=new ArrayList<>(0);
		  return this.subnotrnotroles; 
	}
	
	public List<Notrnotiusers> getSubnotrnotiusers() {
		if(this.subnotrnotiusers==null)this.subnotrnotiusers=new ArrayList<>(0);
		  return this.subnotrnotiusers; 
	} 

}