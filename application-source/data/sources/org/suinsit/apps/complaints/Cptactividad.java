package org.suinsit.apps.complaints;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.complaints.Cptcomplaint;
import org.suinsit.apps.complaints.Cpttipoactividad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTACTIVIDAD" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTACTIVIDAD",
	labelMonitor = "ACTIVIDAD",
	pk = "idxcptactividad" 
)
public class Cptactividad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "actividad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String actividad;
	@Column (
		name = "descripcionpriv",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcionpriv;
	@Column (
		name = "descripcionpub",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcionpub;
	@Column (
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "horas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal horas;
	@Id
	@Column (
		name = "idxcptactividad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcptactividad;
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
		name = "IDCPTCOMPLAINT0",
		referencedColumnName = "IDXCPTCOMPLAINT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cptcomplaint idcptcomplaint;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCPTTIPOACTIVIDAD0",
		referencedColumnName = "IDXCPTTIPOACTIVIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cpttipoactividad idcpttipoactividad; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Cptcomplaint getIdcptcomplaint() {
		if(this.idcptcomplaint==null)this.idcptcomplaint=new org.suinsit.apps.complaints.Cptcomplaint();
		  return this.idcptcomplaint; 
	}
	
	public Cpttipoactividad getIdcpttipoactividad() {
		if(this.idcpttipoactividad==null)this.idcpttipoactividad=new org.suinsit.apps.complaints.Cpttipoactividad();
		  return this.idcpttipoactividad; 
	} 

}