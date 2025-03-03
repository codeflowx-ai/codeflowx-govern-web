package org.suinsit.apps.expedientes;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.expedientes.Gexmactuacion;
import org.suinsit.apps.expedientes.Gexmepediente;
import org.suinsit.apps.tramitacion.Trmtramite;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXRNOTA" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXRNOTA",
	labelMonitor = "",
	pk = "idxgexrnota" 
)
public class Gexrnota implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
	@NotNull
	@NotBlank
	@Column (
		name = "anotacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String anotacion;
	@Id
	@Column (
		name = "idxgexrnota",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexrnota;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMACTUACION0",
		referencedColumnName = "IDXGEXMACTUACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmactuacion idgexmactuacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDGEXMEPEDIENTE0",
		referencedColumnName = "IDXGEXMEPEDIENTE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmepediente idgexmepediente;
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
		name = "IDTRMTRAMITE0",
		referencedColumnName = "IDXTRMTRAMITE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Trmtramite idtrmtramite; 

	public Gexmactuacion getIdgexmactuacion() {
		if(this.idgexmactuacion==null)this.idgexmactuacion=new org.suinsit.apps.expedientes.Gexmactuacion();
		  return this.idgexmactuacion; 
	}
	
	public Gexmepediente getIdgexmepediente() {
		if(this.idgexmepediente==null)this.idgexmepediente=new org.suinsit.apps.expedientes.Gexmepediente();
		  return this.idgexmepediente; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Trmtramite getIdtrmtramite() {
		if(this.idtrmtramite==null)this.idtrmtramite=new org.suinsit.apps.tramitacion.Trmtramite();
		  return this.idtrmtramite; 
	} 

}