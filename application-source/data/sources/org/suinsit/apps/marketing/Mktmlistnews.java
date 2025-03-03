package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.marketing.Mktemailtemplate;
import org.suinsit.apps.marketing.Mktmnewsletter;
import org.suinsit.apps.marketing.Mktprovmail;
import org.suinsit.apps.marketing.Mktrlistcustomer;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTMLISTNEWS" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTMLISTNEWS",
	labelMonitor = "NOMBRE",
	pk = "idxmktmlistnews" 
)
public class Mktmlistnews implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "subscriptores",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer subscriptores;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String idproveedor;
	@Column (
		name = "activa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activa;
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
		name = "MKTMLISTNEWS_CODIGO",
		prefix = "",
		mask = "0000000000",
		addYear = false 
	)
	private String codigo;
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
		name = "idxmktmlistnews",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktmlistnews;
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
	private String nombre;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPROVMAIL0",
		referencedColumnName = "IDXMKTPROVMAIL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktprovmail idmktprovmail;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktmlistnews" 
	)
	private List<Mktrlistcustomer> submktrlistcustomer;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktmlistnews" 
	)
	private List<Mktmnewsletter> submktmnewsletter;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktmlistnews" 
	)
	private List<Mktemailtemplate> submktemailtemplate; 

	public Mktprovmail getIdmktprovmail() {
		if(this.idmktprovmail==null)this.idmktprovmail=new org.suinsit.apps.marketing.Mktprovmail();
		  return this.idmktprovmail; 
	}
	
	public List<Mktrlistcustomer> getSubmktrlistcustomer() {
		if(this.submktrlistcustomer==null)this.submktrlistcustomer=new ArrayList<>(0);
		  return this.submktrlistcustomer; 
	}
	
	public List<Mktmnewsletter> getSubmktmnewsletter() {
		if(this.submktmnewsletter==null)this.submktmnewsletter=new ArrayList<>(0);
		  return this.submktmnewsletter; 
	}
	
	public List<Mktemailtemplate> getSubmktemailtemplate() {
		if(this.submktemailtemplate==null)this.submktemailtemplate=new ArrayList<>(0);
		  return this.submktemailtemplate; 
	} 

}