package org.suinsit.apps.partners;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.arquitecturas.Arqmarchitecture;
import org.suinsit.apps.arquitecturas.Arqmarketplace;
import org.suinsit.apps.arquitecturas.Arqsolucion;
import org.suinsit.apps.atlas.Atlproject;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpcomercial;
import org.suinsit.apps.franchise.Frcmfranchise;
import org.suinsit.apps.myalm.Almproject;
import org.suinsit.apps.partners.Nivel;
import org.suinsit.apps.partners.Ptrrapel;
import org.suinsit.apps.partners.Ptrrcomision;
import org.suinsit.apps.partners.Ptruserpartner;
import org.suinsit.apps.partners.Tipopartner;
import org.suinsit.apps.subvenciones.Subsolictudes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PARTNER" 
)
@Entidad (
	namespace = "partners",
	type = "TABLE",
	name = "PARTNER",
	labelMonitor = "Partner",
	pk = "idxpartner" 
)
public class Partner implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		type = "DATE" 
	)
	private Date alta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "PARTNER_CODIGO",
		prefix = "",
		mask = "00000",
		addYear = true 
	)
	private String codigo;
	@Column (
		name = "contrato",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date contrato;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "correo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String correo;
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
		name = "idxpartner",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpartner;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "partner",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String partner;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "precent",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal precent;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
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
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlweb",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlweb;
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
		name = "IDERPCOMERCIAL0",
		referencedColumnName = "IDXERPCOMERCIAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpcomercial iderpcomercial;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDNIVEL0",
		referencedColumnName = "IDXNIVEL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Nivel idnivel;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDTIPOPARTNER0",
		referencedColumnName = "IDXTIPOPARTNER",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Tipopartner idtipopartner;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDFRCMFRANCHISE0",
		referencedColumnName = "IDXFRCMFRANCHISE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Frcmfranchise idfrcmfranchise;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Subsolictudes> subsubsolictudes;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Atlproject> subatlproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Ptrrcomision> subptrrcomision;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Ptrrapel> subptrrapel;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Ptruserpartner> subptruserpartner;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Arqmarchitecture> subarqmarchitecture;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Arqmarketplace> subarqmarketplace;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Arqsolucion> subarqsolucion;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Almproject> subalmproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpartner" 
	)
	private List<Crmempresa> subcrmempresa; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public Erpcomercial getIderpcomercial() {
		if(this.iderpcomercial==null)this.iderpcomercial=new org.suinsit.apps.facturacin.Erpcomercial();
		  return this.iderpcomercial; 
	}
	
	public Nivel getIdnivel() {
		if(this.idnivel==null)this.idnivel=new org.suinsit.apps.partners.Nivel();
		  return this.idnivel; 
	}
	
	public Tipopartner getIdtipopartner() {
		if(this.idtipopartner==null)this.idtipopartner=new org.suinsit.apps.partners.Tipopartner();
		  return this.idtipopartner; 
	}
	
	public Frcmfranchise getIdfrcmfranchise() {
		if(this.idfrcmfranchise==null)this.idfrcmfranchise=new org.suinsit.apps.franchise.Frcmfranchise();
		  return this.idfrcmfranchise; 
	}
	
	public List<Subsolictudes> getSubsubsolictudes() {
		if(this.subsubsolictudes==null)this.subsubsolictudes=new ArrayList<>(0);
		  return this.subsubsolictudes; 
	}
	
	public List<Atlproject> getSubatlproject() {
		if(this.subatlproject==null)this.subatlproject=new ArrayList<>(0);
		  return this.subatlproject; 
	}
	
	public List<Ptrrcomision> getSubptrrcomision() {
		if(this.subptrrcomision==null)this.subptrrcomision=new ArrayList<>(0);
		  return this.subptrrcomision; 
	}
	
	public List<Ptrrapel> getSubptrrapel() {
		if(this.subptrrapel==null)this.subptrrapel=new ArrayList<>(0);
		  return this.subptrrapel; 
	}
	
	public List<Ptruserpartner> getSubptruserpartner() {
		if(this.subptruserpartner==null)this.subptruserpartner=new ArrayList<>(0);
		  return this.subptruserpartner; 
	}
	
	public List<Arqmarchitecture> getSubarqmarchitecture() {
		if(this.subarqmarchitecture==null)this.subarqmarchitecture=new ArrayList<>(0);
		  return this.subarqmarchitecture; 
	}
	
	public List<Arqmarketplace> getSubarqmarketplace() {
		if(this.subarqmarketplace==null)this.subarqmarketplace=new ArrayList<>(0);
		  return this.subarqmarketplace; 
	}
	
	public List<Arqsolucion> getSubarqsolucion() {
		if(this.subarqsolucion==null)this.subarqsolucion=new ArrayList<>(0);
		  return this.subarqsolucion; 
	}
	
	public List<Almproject> getSubalmproject() {
		if(this.subalmproject==null)this.subalmproject=new ArrayList<>(0);
		  return this.subalmproject; 
	}
	
	public List<Crmempresa> getSubcrmempresa() {
		if(this.subcrmempresa==null)this.subcrmempresa=new ArrayList<>(0);
		  return this.subcrmempresa; 
	} 

}