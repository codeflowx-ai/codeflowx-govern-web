package org.suinsit.apps.mrp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.mrp.Mrpestado;
import org.suinsit.apps.mrp.Mrprproductos;
import org.suinsit.apps.portalemp.Rrhempleado;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPMRECEPCION" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPMRECEPCION",
	labelMonitor = "REFERENCIA",
	pk = "idxmrpminventario" 
)
public class Mrpmrecepcion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "documento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String documento;
	@Id
	@Column (
		name = "idxmrpminventario",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrpminventario;
	@Column (
		name = "notas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String notas;
	@Column (
		name = "prevista",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp prevista;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "MRPMRECEPCION_REFERENCIA",
		prefix = "MRPI",
		mask = "0000000000",
		addYear = true 
	)
	private String referencia;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDENVIADODE0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idenviadode;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRESPONSABLE0",
		referencedColumnName = "IDXRRHEMPLEADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhempleado idresponsable;
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
		name = "IDMRPESTADO0",
		referencedColumnName = "IDXMRPESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpestado idmrpestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpmrecepcion" 
	)
	private List<Mrprproductos> submrprproductos; 

	public Crmempresa getIdenviadode() {
		if(this.idenviadode==null)this.idenviadode=new org.suinsit.apps.crm.Crmempresa();
		  return this.idenviadode; 
	}
	
	public Rrhempleado getIdresponsable() {
		if(this.idresponsable==null)this.idresponsable=new org.suinsit.apps.portalemp.Rrhempleado();
		  return this.idresponsable; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Mrpestado getIdmrpestado() {
		if(this.idmrpestado==null)this.idmrpestado=new org.suinsit.apps.mrp.Mrpestado();
		  return this.idmrpestado; 
	}
	
	public List<Mrprproductos> getSubmrprproductos() {
		if(this.submrprproductos==null)this.submrprproductos=new ArrayList<>(0);
		  return this.submrprproductos; 
	} 

}