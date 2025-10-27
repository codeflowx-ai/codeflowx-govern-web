package org.suinsit.apps.expedientes;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.expedientes.Gexmepediente;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXRFIIRMAS" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXRFIIRMAS",
	labelMonitor = "FECHAFIRMA",
	pk = "idxgexrfiirmas" 
)
public class Gexrfiirmas implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "firma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object firma;
	@Column (
		name = "fechafirma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp fechafirma;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "otpcode",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String otpcode;
	@Id
	@Column (
		name = "idxgexrfiirmas",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexrfiirmas;
	private boolean updatable;
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
		name = "IDUSUARIOFIRMA0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idusuariofirma; 

	public Gexmepediente getIdgexmepediente() {
		if(this.idgexmepediente==null)this.idgexmepediente=new org.suinsit.apps.expedientes.Gexmepediente();
		  return this.idgexmepediente; 
	}
	
	public Ssousuario getIdusuariofirma() {
		if(this.idusuariofirma==null)this.idusuariofirma=new org.suinsit.apps.admin.Ssousuario();
		  return this.idusuariofirma; 
	} 

}