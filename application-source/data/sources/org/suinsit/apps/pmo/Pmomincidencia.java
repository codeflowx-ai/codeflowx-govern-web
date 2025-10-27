package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.pmo.Pmoestadoinc;
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmomtask;
import org.suinsit.apps.pmo.Pmomtipoinc;
import org.suinsit.apps.pmo.Pmousuario;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMINCIDENCIA" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMINCIDENCIA",
	labelMonitor = "",
	pk = "idxpmomincidencia" 
)
public class Pmomincidencia implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		label = "INC",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "PMOMINCIDENCIA_CODIGO",
		prefix = "",
		mask = "00000000",
		addYear = false 
	)
	private String codigo;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "coste",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal coste;
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
	@Column (
		name = "horas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer horas;
	@Id
	@Column (
		name = "idxpmomincidencia",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "LONG" 
	)
	private Long idxpmomincidencia;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "inicidencia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String inicidencia;
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
		name = "IDPMOMTIPOINC0",
		referencedColumnName = "IDXPMOMTIPOINC",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomtipoinc idpmomtipoinc;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOESTADOINC0",
		referencedColumnName = "IDXPMOESTADOINC",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoestadoinc idpmoestadoinc;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomincidencia" 
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
	
	public Pmomtipoinc getIdpmomtipoinc() {
		if(this.idpmomtipoinc==null)this.idpmomtipoinc=new org.suinsit.apps.pmo.Pmomtipoinc();
		  return this.idpmomtipoinc; 
	}
	
	public Pmoestadoinc getIdpmoestadoinc() {
		if(this.idpmoestadoinc==null)this.idpmoestadoinc=new org.suinsit.apps.pmo.Pmoestadoinc();
		  return this.idpmoestadoinc; 
	}
	
	public List<Pmomtask> getSubpmomtask() {
		if(this.subpmomtask==null)this.subpmomtask=new ArrayList<>(0);
		  return this.subpmomtask; 
	} 

}