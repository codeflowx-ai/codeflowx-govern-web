package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.pmo.Pmomperfil;
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmomtask;
import org.suinsit.apps.pmo.Pmousuario;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMORSUSERPROY" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMORSUSERPROY",
	labelMonitor = "idpmousuario",
	pk = "idxpmorsuserproy" 
)
public class Pmorsuserproy implements Serializable { 

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
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date baja;
	@Id
	@Column (
		name = "idxpmorsuserproy",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmorsuserproy;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOUSUARIO0",
		referencedColumnName = "IDXPMOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmousuario idpmousuario;
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
		name = "IDPMOMPERFIL0",
		referencedColumnName = "IDXPMOMPERFIL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomperfil idpmomperfil;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idinformador" 
	)
	private List<Pmomtask> subpmomtask; 

	public Pmousuario getIdpmousuario() {
		if(this.idpmousuario==null)this.idpmousuario=new org.suinsit.apps.pmo.Pmousuario();
		  return this.idpmousuario; 
	}
	
	public Pmomproject getIdpmomproject() {
		if(this.idpmomproject==null)this.idpmomproject=new org.suinsit.apps.pmo.Pmomproject();
		  return this.idpmomproject; 
	}
	
	public Pmomperfil getIdpmomperfil() {
		if(this.idpmomperfil==null)this.idpmomperfil=new org.suinsit.apps.pmo.Pmomperfil();
		  return this.idpmomperfil; 
	}
	
	public List<Pmomtask> getSubpmomtask() {
		if(this.subpmomtask==null)this.subpmomtask=new ArrayList<>(0);
		  return this.subpmomtask; 
	} 

}