package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.math.BigDecimal;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.pmo.Pmomincidencia;
import org.suinsit.apps.pmo.Pmoriesgo;
import org.suinsit.apps.pmo.Pmorsuserproy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOUSUARIO" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOUSUARIO",
	labelMonitor = "",
	pk = "idxpmousuario" 
)
public class Pmousuario implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "externo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean externo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "facthora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Facturación Hora",
		type = "DECIMAL" 
	)
	private BigDecimal facthora;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "costehora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Coste Hora",
		type = "DECIMAL" 
	)
	private BigDecimal costehora;
	@Id
	@Column (
		name = "idxpmousuario",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmousuario;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmousuario" 
	)
	private List<Pmorsuserproy> subpmorsuserproy;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgestor" 
	)
	private List<Pmoriesgo> subpmoriesgo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idgestor" 
	)
	private List<Pmomincidencia> subpmomincidencia; 

	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public List<Pmorsuserproy> getSubpmorsuserproy() {
		if(this.subpmorsuserproy==null)this.subpmorsuserproy=new ArrayList<>(0);
		  return this.subpmorsuserproy; 
	}
	
	public List<Pmoriesgo> getSubpmoriesgo() {
		if(this.subpmoriesgo==null)this.subpmoriesgo=new ArrayList<>(0);
		  return this.subpmoriesgo; 
	}
	
	public List<Pmomincidencia> getSubpmomincidencia() {
		if(this.subpmomincidencia==null)this.subpmomincidencia=new ArrayList<>(0);
		  return this.subpmomincidencia; 
	} 

}