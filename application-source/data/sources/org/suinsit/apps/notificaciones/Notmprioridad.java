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
	name = "NOTMPRIORIDAD" 
)
@Entidad (
	namespace = "notificaciones",
	type = "TABLE",
	name = "NOTMPRIORIDAD",
	labelMonitor = "Color",
	pk = "idxnotmprioridad" 
)
public class Notmprioridad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "color",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String color;
	@Id
	@Column (
		name = "idxnotmprioridad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxnotmprioridad;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "prioridad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String prioridad;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idnotmprioridad" 
	)
	private List<Notmnotificacion> subnotmnotificacion; 

	public List<Notmnotificacion> getSubnotmnotificacion() {
		if(this.subnotmnotificacion==null)this.subnotmnotificacion=new ArrayList<>(0);
		  return this.subnotmnotificacion; 
	} 

}