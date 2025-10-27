package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.pmo.Pmoaprobacion;
import org.suinsit.apps.pmo.Pmoctrocostes;
import org.suinsit.apps.pmo.Pmodepartament;
import org.suinsit.apps.pmo.Pmoestado;
import org.suinsit.apps.pmo.Pmolineabase;
import org.suinsit.apps.pmo.Pmomactivos;
import org.suinsit.apps.pmo.Pmomcompra;
import org.suinsit.apps.pmo.Pmomincidencia;
import org.suinsit.apps.pmo.Pmomtask;
import org.suinsit.apps.pmo.Pmoobjetivo;
import org.suinsit.apps.pmo.Pmopartidapre;
import org.suinsit.apps.pmo.Pmopatrocinador;
import org.suinsit.apps.pmo.Pmoprioridad;
import org.suinsit.apps.pmo.Pmoriesgo;
import org.suinsit.apps.pmo.Pmorsuserproy;
import org.suinsit.apps.pmo.Pmortarifapro;
import org.suinsit.apps.pmo.Pmotipoproyect;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMPROJECT" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMPROJECT",
	labelMonitor = "CODPROYECTO",
	pk = "idxpmomproject" 
)
public class Pmomproject implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "acronimo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String acronimo;
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
		name = "avatar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object avatar;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "beneficioespera",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal beneficioespera;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codproyecto",
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
		name = "PMOMPROJECT_CODPROYECTO",
		prefix = "PR",
		mask = "000000",
		addYear = false 
	)
	private String codproyecto;
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
		label = "",
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
		label = "",
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "descripcionobj",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcionobj;
	@Column (
		name = "fechafin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechafin;
	@Id
	@Column (
		name = "idxpmomproject",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomproject;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "proyecto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String proyecto;
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
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tmmodif;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
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
		name = "IDPMOESTADO0",
		referencedColumnName = "IDXPMOESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoestado idpmoestado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOPRIORIDAD0",
		referencedColumnName = "IDXPMOPRIORIDAD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoprioridad idpmoprioridad;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOTIPOPROYECT0",
		referencedColumnName = "IDXPMOTIPOPROYECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmotipoproyect idpmotipoproyect;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOAPROBACION0",
		referencedColumnName = "IDXPMOAPROBACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoaprobacion idpmoaprobacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMODEPARTAMENT0",
		referencedColumnName = "IDXPMODEPARTAMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmodepartament idpmodepartament;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOOBJETIVO0",
		referencedColumnName = "IDXPMOOBJETIVO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoobjetivo idpmoobjetivo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOPATROCINADOR0",
		referencedColumnName = "IDXPMOPATROCINADOR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmopatrocinador idpmopatrocinador;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOCTROCOSTES0",
		referencedColumnName = "IDXPMOCTROCOSTES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmoctrocostes idpmoctrocostes;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMACTIVOS0",
		referencedColumnName = "IDXPMOMACTIVOS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomactivos idpmomactivos;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCLTEINTERNO0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idclteinterno;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmopartidapre> subpmopartidapre;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmorsuserproy> subpmorsuserproy;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmolineabase> subpmolineabase;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmortarifapro> subpmortarifapro;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmoriesgo> subpmoriesgo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmomincidencia> subpmomincidencia;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmomcompra> subpmomcompra;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Pmomtask> subpmomtask;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpmomproject" 
	)
	private List<Almproject> subalmproject; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Pmoestado getIdpmoestado() {
		if(this.idpmoestado==null)this.idpmoestado=new org.suinsit.apps.pmo.Pmoestado();
		  return this.idpmoestado; 
	}
	
	public Pmoprioridad getIdpmoprioridad() {
		if(this.idpmoprioridad==null)this.idpmoprioridad=new org.suinsit.apps.pmo.Pmoprioridad();
		  return this.idpmoprioridad; 
	}
	
	public Pmotipoproyect getIdpmotipoproyect() {
		if(this.idpmotipoproyect==null)this.idpmotipoproyect=new org.suinsit.apps.pmo.Pmotipoproyect();
		  return this.idpmotipoproyect; 
	}
	
	public Pmoaprobacion getIdpmoaprobacion() {
		if(this.idpmoaprobacion==null)this.idpmoaprobacion=new org.suinsit.apps.pmo.Pmoaprobacion();
		  return this.idpmoaprobacion; 
	}
	
	public Pmodepartament getIdpmodepartament() {
		if(this.idpmodepartament==null)this.idpmodepartament=new org.suinsit.apps.pmo.Pmodepartament();
		  return this.idpmodepartament; 
	}
	
	public Pmoobjetivo getIdpmoobjetivo() {
		if(this.idpmoobjetivo==null)this.idpmoobjetivo=new org.suinsit.apps.pmo.Pmoobjetivo();
		  return this.idpmoobjetivo; 
	}
	
	public Pmopatrocinador getIdpmopatrocinador() {
		if(this.idpmopatrocinador==null)this.idpmopatrocinador=new org.suinsit.apps.pmo.Pmopatrocinador();
		  return this.idpmopatrocinador; 
	}
	
	public Pmoctrocostes getIdpmoctrocostes() {
		if(this.idpmoctrocostes==null)this.idpmoctrocostes=new org.suinsit.apps.pmo.Pmoctrocostes();
		  return this.idpmoctrocostes; 
	}
	
	public Pmomactivos getIdpmomactivos() {
		if(this.idpmomactivos==null)this.idpmomactivos=new org.suinsit.apps.pmo.Pmomactivos();
		  return this.idpmomactivos; 
	}
	
	public Crmempresa getIdclteinterno() {
		if(this.idclteinterno==null)this.idclteinterno=new org.suinsit.apps.crm.Crmempresa();
		  return this.idclteinterno; 
	}
	
	public List<Pmopartidapre> getSubpmopartidapre() {
		if(this.subpmopartidapre==null)this.subpmopartidapre=new ArrayList<>(0);
		  return this.subpmopartidapre; 
	}
	
	public List<Pmorsuserproy> getSubpmorsuserproy() {
		if(this.subpmorsuserproy==null)this.subpmorsuserproy=new ArrayList<>(0);
		  return this.subpmorsuserproy; 
	}
	
	public List<Pmolineabase> getSubpmolineabase() {
		if(this.subpmolineabase==null)this.subpmolineabase=new ArrayList<>(0);
		  return this.subpmolineabase; 
	}
	
	public List<Pmortarifapro> getSubpmortarifapro() {
		if(this.subpmortarifapro==null)this.subpmortarifapro=new ArrayList<>(0);
		  return this.subpmortarifapro; 
	}
	
	public List<Pmoriesgo> getSubpmoriesgo() {
		if(this.subpmoriesgo==null)this.subpmoriesgo=new ArrayList<>(0);
		  return this.subpmoriesgo; 
	}
	
	public List<Pmomincidencia> getSubpmomincidencia() {
		if(this.subpmomincidencia==null)this.subpmomincidencia=new ArrayList<>(0);
		  return this.subpmomincidencia; 
	}
	
	public List<Pmomcompra> getSubpmomcompra() {
		if(this.subpmomcompra==null)this.subpmomcompra=new ArrayList<>(0);
		  return this.subpmomcompra; 
	}
	
	public List<Pmomtask> getSubpmomtask() {
		if(this.subpmomtask==null)this.subpmomtask=new ArrayList<>(0);
		  return this.subpmomtask; 
	}
	
	public List<Almproject> getSubalmproject() {
		if(this.subalmproject==null)this.subalmproject=new ArrayList<>(0);
		  return this.subalmproject; 
	} 

}