package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.pmo.Pmomestadotask;
import org.suinsit.apps.pmo.Pmomincidencia;
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmomversion;
import org.suinsit.apps.pmo.Pmoriesgo;
import org.suinsit.apps.pmo.Pmorsuserproy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMTASK" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMTASK",
	labelMonitor = "CODIGO",
	pk = "idxpmomtask" 
)
public class Pmomtask implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigo",
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
		name = "PMOMTASK_CODIGO",
		prefix = "TSL",
		mask = "0000000000",
		addYear = false 
	)
	private String codigo;
	@Column (
		name = "finprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date finprev;
	@Column (
		name = "horasprev",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer horasprev;
	@Column (
		name = "horasreal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer horasreal;
	@Id
	@Column (
		name = "idxpmomtask",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "LONG" 
	)
	private Long idxpmomtask;
	@Column (
		name = "inicio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date inicio;
	@Column (
		name = "resumen",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String resumen;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tarea",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tarea;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMPROJECT0",
		referencedColumnName = "IDXPMOMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomproject idpmomproject;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMESTADOTASK0",
		referencedColumnName = "IDXPMOMESTADOTASK",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomestadotask idpmomestadotask;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMINCIDENCIA0",
		referencedColumnName = "IDXPMOMINCIDENCIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomincidencia idpmomincidencia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMVERSION0",
		referencedColumnName = "IDXPMOMVERSION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomversion idpmomversion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMORIESGO0",
		referencedColumnName = "IDXPMORIESGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoriesgo idpmoriesgo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDINFORMADOR0",
		referencedColumnName = "IDXPMORSUSERPROY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmorsuserproy idinformador;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRESPONSABLE0",
		referencedColumnName = "IDXPMORSUSERPROY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmorsuserproy idresponsable; 

	public Pmomproject getIdpmomproject() {
		if(this.idpmomproject==null)this.idpmomproject=new org.suinsit.apps.pmo.Pmomproject();
		  return this.idpmomproject; 
	}
	
	public Pmomestadotask getIdpmomestadotask() {
		if(this.idpmomestadotask==null)this.idpmomestadotask=new org.suinsit.apps.pmo.Pmomestadotask();
		  return this.idpmomestadotask; 
	}
	
	public Pmomincidencia getIdpmomincidencia() {
		if(this.idpmomincidencia==null)this.idpmomincidencia=new org.suinsit.apps.pmo.Pmomincidencia();
		  return this.idpmomincidencia; 
	}
	
	public Pmomversion getIdpmomversion() {
		if(this.idpmomversion==null)this.idpmomversion=new org.suinsit.apps.pmo.Pmomversion();
		  return this.idpmomversion; 
	}
	
	public Pmoriesgo getIdpmoriesgo() {
		if(this.idpmoriesgo==null)this.idpmoriesgo=new org.suinsit.apps.pmo.Pmoriesgo();
		  return this.idpmoriesgo; 
	}
	
	public Pmorsuserproy getIdinformador() {
		if(this.idinformador==null)this.idinformador=new org.suinsit.apps.pmo.Pmorsuserproy();
		  return this.idinformador; 
	}
	
	public Pmorsuserproy getIdresponsable() {
		if(this.idresponsable==null)this.idresponsable=new org.suinsit.apps.pmo.Pmorsuserproy();
		  return this.idresponsable; 
	} 

}