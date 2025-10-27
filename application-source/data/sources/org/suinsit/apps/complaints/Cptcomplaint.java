package org.suinsit.apps.complaints;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Cipher;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.complaints.Cptactividad;
import org.suinsit.apps.complaints.Cptestado;
import org.suinsit.apps.complaints.Cptmessage;
import org.suinsit.apps.complaints.Cptreport;
import org.suinsit.apps.complaints.Cpttipodenucia;
import org.suinsit.apps.crm.Crmempresa;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTCOMPLAINT" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTCOMPLAINT",
	labelMonitor = "CASOREFERENCE",
	cipher = true,
	keyCipher = "xnmFf7VYL1Bgn2U1ixT4gxj12",
	fieldValueKeyCipher = "",
	pk = "idxcptcomplaint" 
)
public class Cptcomplaint implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "uuid",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String uuid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "alias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@Cipher
	private String alias;
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
	@Column (
		name = "anonimo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean anonimo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "asunto",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String asunto;
	@Column (
		name = "audio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object audio;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "casoreference",
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
		name = "CPTCOMPLAINT_CASOREFERENCE",
		prefix = "",
		mask = "0000000000",
		addYear = true 
	)
	private String casoreference;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clave",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String clave;
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
	@NotNull
	@NotBlank
	@Column (
		name = "descripcion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	@Cipher
	private String descripcion;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "email",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@Cipher
	private String email;
	@Column (
		name = "fechaincidencia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechaincidencia;
	@Column (
		name = "fecharesolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecharesolucion;
	@Id
	@Column (
		name = "idxcptcomplaint",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcptcomplaint;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@Cipher
	private String nombre;
	@Column (
		name = "resolucion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String resolucion;
	@Column (
		name = "shortdescr",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	@Cipher
	private String shortdescr;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "telefono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@Cipher
	private String telefono;
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
		name = "IDCPTTIPODENUCIA0",
		referencedColumnName = "IDXCPTTIPODENUCIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cpttipodenucia idcpttipodenucia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCPTESTADO0",
		referencedColumnName = "IDXCPTESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cptestado idcptestado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDDENUNCIANTE0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario iddenunciante;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCPTREPORT0",
		referencedColumnName = "IDXCPTREPORT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cptreport idcptreport;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcptcomplaint" 
	)
	private List<Cptmessage> subcptmessage;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcptcomplaint" 
	)
	private List<Cptactividad> subcptactividad; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	}
	
	public Cpttipodenucia getIdcpttipodenucia() {
		if(this.idcpttipodenucia==null)this.idcpttipodenucia=new org.suinsit.apps.complaints.Cpttipodenucia();
		  return this.idcpttipodenucia; 
	}
	
	public Cptestado getIdcptestado() {
		if(this.idcptestado==null)this.idcptestado=new org.suinsit.apps.complaints.Cptestado();
		  return this.idcptestado; 
	}
	
	public Ssousuario getIddenunciante() {
		if(this.iddenunciante==null)this.iddenunciante=new org.suinsit.apps.admin.Ssousuario();
		  return this.iddenunciante; 
	}
	
	public Cptreport getIdcptreport() {
		if(this.idcptreport==null)this.idcptreport=new org.suinsit.apps.complaints.Cptreport();
		  return this.idcptreport; 
	}
	
	public List<Cptmessage> getSubcptmessage() {
		if(this.subcptmessage==null)this.subcptmessage=new ArrayList<>(0);
		  return this.subcptmessage; 
	}
	
	public List<Cptactividad> getSubcptactividad() {
		if(this.subcptactividad==null)this.subcptactividad=new ArrayList<>(0);
		  return this.subcptactividad; 
	} 

}