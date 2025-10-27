package org.suinsit.apps.notificaciones;

import java.io.Serializable;
import java.lang.Long;
import java.sql.Timestamp;
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
import org.suinsit.apps.notificaciones.Notmnotificacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "NOTRNOTIUSERS" 
)
@Entidad (
	namespace = "notificaciones",
	type = "TABLE",
	name = "NOTRNOTIUSERS",
	labelMonitor = "",
	pk = "idxnotrnotiusers" 
)
public class Notrnotiusers implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "fechaenvio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fechaenvio;
	@Id
	@Column (
		name = "idxnotrnotiusers",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnotrnotiusers;
	@Column (
		name = "lectura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp lectura;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDNOTMNOTIFICACION0",
		referencedColumnName = "IDXNOTMNOTIFICACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Notmnotificacion idnotmnotificacion;
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

	public Notmnotificacion getIdnotmnotificacion() {
		if(this.idnotmnotificacion==null)this.idnotmnotificacion=new org.suinsit.apps.notificaciones.Notmnotificacion();
		  return this.idnotmnotificacion; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}