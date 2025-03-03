package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.bpmn.Bpmmproces;
import org.suinsit.apps.pmo.Pmomincidencia;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOESTADOINC" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOESTADOINC",
	labelMonitor = "",
	pk = "idxpmoestadoinc" 
)
public class Pmoestadoinc implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "backcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String backcolor;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estadoinc",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estadoinc;
	@Id
	@Column (
		name = "idxpmoestadoinc",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmoestadoinc;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDBPMMPROCES0",
		referencedColumnName = "IDXBPMMPROCES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Bpmmproces idbpmmproces;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoestadoinc" 
	)
	private List<Pmomincidencia> subpmomincidencia; 

	public Bpmmproces getIdbpmmproces() {
		if(this.idbpmmproces==null)this.idbpmmproces=new org.suinsit.apps.bpmn.Bpmmproces();
		  return this.idbpmmproces; 
	}
	
	public List<Pmomincidencia> getSubpmomincidencia() {
		if(this.subpmomincidencia==null)this.subpmomincidencia=new ArrayList<>(0);
		  return this.subpmomincidencia; 
	} 

}