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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.pmo.Pmoestries;
import org.suinsit.apps.pmo.Pmoimpacto;
import org.suinsit.apps.pmo.Pmomproba;
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmomtask;
import org.suinsit.apps.pmo.Pmotipriesgo;
import org.suinsit.apps.pmo.Pmousuario;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMORIESGO" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMORIESGO",
	labelMonitor = "",
	pk = "idxpmoriesgo" 
)
public class Pmoriesgo implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "idxpmoriesgo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmoriesgo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "riesgo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String riesgo;
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
		name = "IDGESTOR0",
		referencedColumnName = "IDXPMOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmousuario idgestor;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOESTRIES0",
		referencedColumnName = "IDXPMOESTRIES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoestries idpmoestries;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOTIPRIESGO0",
		referencedColumnName = "IDXPMOTIPRIESGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmotipriesgo idpmotipriesgo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOIMPACTO0",
		referencedColumnName = "IDXPMOIMPACTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoimpacto idpmoimpacto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMPROBA0",
		referencedColumnName = "IDXPMOMPROBA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomproba idpmomproba;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmoriesgo" 
	)
	private List<Pmomtask> subpmomtask; 

	public Pmomproject getIdpmomproject() {
		if(this.idpmomproject==null)this.idpmomproject=new org.suinsit.apps.pmo.Pmomproject();
		  return this.idpmomproject; 
	}
	
	public Pmousuario getIdgestor() {
		if(this.idgestor==null)this.idgestor=new org.suinsit.apps.pmo.Pmousuario();
		  return this.idgestor; 
	}
	
	public Pmoestries getIdpmoestries() {
		if(this.idpmoestries==null)this.idpmoestries=new org.suinsit.apps.pmo.Pmoestries();
		  return this.idpmoestries; 
	}
	
	public Pmotipriesgo getIdpmotipriesgo() {
		if(this.idpmotipriesgo==null)this.idpmotipriesgo=new org.suinsit.apps.pmo.Pmotipriesgo();
		  return this.idpmotipriesgo; 
	}
	
	public Pmoimpacto getIdpmoimpacto() {
		if(this.idpmoimpacto==null)this.idpmoimpacto=new org.suinsit.apps.pmo.Pmoimpacto();
		  return this.idpmoimpacto; 
	}
	
	public Pmomproba getIdpmomproba() {
		if(this.idpmomproba==null)this.idpmomproba=new org.suinsit.apps.pmo.Pmomproba();
		  return this.idpmomproba; 
	}
	
	public List<Pmomtask> getSubpmomtask() {
		if(this.subpmomtask==null)this.subpmomtask=new ArrayList<>(0);
		  return this.subpmomtask; 
	} 

}