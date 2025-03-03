package org.suinsit.apps.marketing;

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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.marketing.Mktmrrss;
import org.suinsit.apps.marketing.Mktrpostagency;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTRSSAGENCY" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTRSSAGENCY",
	labelMonitor = "NOMBRE",
	pk = "idxmktrssagency" 
)
public class Mktrssagency implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
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
		name = "MKTRSSAGENCY_REFERENCIA",
		prefix = "",
		mask = "00000000",
		addYear = false 
	)
	private String referencia;
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentarios;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "secret",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String secret;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clave",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clave;
	@Id
	@Column (
		name = "idxmktrssagency",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktrssagency;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "emailcustomer",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Correo Cuenta",
		type = "VARCHAR" 
	)
	private String emailcustomer;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Nombre",
		type = "VARCHAR" 
	)
	private String nombre;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTMRRSS0",
		referencedColumnName = "IDXMKTMRRSS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmrrss idmktmrrss;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktrssagency" 
	)
	private List<Mktrpostagency> submktrpostagency; 

	public Mktmrrss getIdmktmrrss() {
		if(this.idmktmrrss==null)this.idmktmrrss=new org.suinsit.apps.marketing.Mktmrrss();
		  return this.idmktmrrss; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public List<Mktrpostagency> getSubmktrpostagency() {
		if(this.submktrpostagency==null)this.submktrpostagency=new ArrayList<>(0);
		  return this.submktrpostagency; 
	} 

}