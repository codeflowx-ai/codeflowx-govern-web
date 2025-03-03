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
import org.suinsit.apps.pmo.Pmomtask;
import org.suinsit.apps.pmo.Pmomtipotask;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMESTADOTASK" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMESTADOTASK",
	labelMonitor = "",
	pk = "idxpmomestadotask" 
)
public class Pmomestadotask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "bgcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String bgcolor;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estadotask",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estadotask;
	@Id
	@Column (
		name = "idxpmomestadotask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomestadotask;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMTIPOTASK0",
		referencedColumnName = "IDXPMOMTIPOTASK",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomtipotask idpmomtipotask;
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
		mappedBy = "idpmomestadotask" 
	)
	private List<Pmomtask> subpmomtask; 

	public Pmomtipotask getIdpmomtipotask() {
		if(this.idpmomtipotask==null)this.idpmomtipotask=new org.suinsit.apps.pmo.Pmomtipotask();
		  return this.idpmomtipotask; 
	}
	
	public Bpmmproces getIdbpmmproces() {
		if(this.idbpmmproces==null)this.idbpmmproces=new org.suinsit.apps.bpmn.Bpmmproces();
		  return this.idbpmmproces; 
	}
	
	public List<Pmomtask> getSubpmomtask() {
		if(this.subpmomtask==null)this.subpmomtask=new ArrayList<>(0);
		  return this.subpmomtask; 
	} 

}