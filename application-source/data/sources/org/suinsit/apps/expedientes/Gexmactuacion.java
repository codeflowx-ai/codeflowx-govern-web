package org.suinsit.apps.expedientes;

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
import org.suinsit.apps.expedientes.Gexmepediente;
import org.suinsit.apps.expedientes.Gexmtipoact;
import org.suinsit.apps.expedientes.Gexractuadoc;
import org.suinsit.apps.expedientes.Gexrnota;
import org.suinsit.apps.tramitacion.Trmtramite;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "GEXMACTUACION" 
)
@Entidad (
	namespace = "expedientes",
	type = "TABLE",
	name = "GEXMACTUACION",
	labelMonitor = "Actuacion",
	pk = "idxgexmactuacion" 
)
public class Gexmactuacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "actuacion",
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
		name = "GEXMACTUACION_ACTUACION",
		prefix = "ACT",
		mask = "00000",
		addYear = false 
	)
	private String actuacion;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
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
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Id
	@Column (
		name = "idxgexmactuacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxgexmactuacion;
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
		name = "visiblecli",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean visiblecli;
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
		name = "IDGEXMTIPOACT0",
		referencedColumnName = "IDXGEXMTIPOACT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Gexmtipoact idgexmtipoact;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmactuacion" 
	)
	private List<Gexractuadoc> subgexractuadoc;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgexmactuacion" 
	)
	private List<Gexrnota> subgexrnota; 

	public Gexmepediente getIdgexmepediente() {
		if(this.idgexmepediente==null)this.idgexmepediente=new org.suinsit.apps.expedientes.Gexmepediente();
		  return this.idgexmepediente; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Gexmtipoact getIdgexmtipoact() {
		if(this.idgexmtipoact==null)this.idgexmtipoact=new org.suinsit.apps.expedientes.Gexmtipoact();
		  return this.idgexmtipoact; 
	}
	
	public Trmtramite getIdtrmtramite() {
		if(this.idtrmtramite==null)this.idtrmtramite=new org.suinsit.apps.tramitacion.Trmtramite();
		  return this.idtrmtramite; 
	}
	
	public List<Gexractuadoc> getSubgexractuadoc() {
		if(this.subgexractuadoc==null)this.subgexractuadoc=new ArrayList<>(0);
		  return this.subgexractuadoc; 
	}
	
	public List<Gexrnota> getSubgexrnota() {
		if(this.subgexrnota==null)this.subgexrnota=new ArrayList<>(0);
		  return this.subgexrnota; 
	} 

}