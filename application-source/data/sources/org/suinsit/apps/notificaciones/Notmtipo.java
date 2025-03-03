package org.suinsit.apps.notificaciones;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.notificaciones.Notmnotificacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "NOTMTIPO" 
)
@Entidad (
	namespace = "notificaciones",
	type = "TABLE",
	name = "NOTMTIPO",
	labelMonitor = "TIPO",
	pk = "idxnotmtipo" 
)
public class Notmtipo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "plantilla",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String plantilla;
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
	@Id
	@Column (
		name = "idxnotmtipo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnotmtipo;
	@Size (
		min = 0,
		max = 100 
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
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnotmtipo" 
	)
	private List<Notmnotificacion> subnotmnotificacion; 

	public List<Notmnotificacion> getSubnotmnotificacion() {
		if(this.subnotmnotificacion==null)this.subnotmnotificacion=new ArrayList<>(0);
		  return this.subnotmnotificacion; 
	} 

}