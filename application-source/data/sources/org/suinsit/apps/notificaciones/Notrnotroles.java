package org.suinsit.apps.notificaciones;

import java.io.Serializable;
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
import org.suinsit.apps.admin.Ssorol;
import org.suinsit.apps.notificaciones.Notmnotificacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "NOTRNOTROLES" 
)
@Entidad (
	namespace = "notificaciones",
	type = "TABLE",
	name = "NOTRNOTROLES",
	labelMonitor = "",
	pk = "idxnotrnotroles" 
)
public class Notrnotroles implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxnotrnotroles",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnotrnotroles;
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
		name = "IDSSOROL0",
		referencedColumnName = "IDXSSOROL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssorol idssorol; 

	public Notmnotificacion getIdnotmnotificacion() {
		if(this.idnotmnotificacion==null)this.idnotmnotificacion=new org.suinsit.apps.notificaciones.Notmnotificacion();
		  return this.idnotmnotificacion; 
	}
	
	public Ssorol getIdssorol() {
		if(this.idssorol==null)this.idssorol=new org.suinsit.apps.admin.Ssorol();
		  return this.idssorol; 
	} 

}