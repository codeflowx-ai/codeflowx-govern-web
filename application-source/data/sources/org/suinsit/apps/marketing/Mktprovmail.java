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
import org.suinsit.apps.admin.Mservicio;
import org.suinsit.apps.marketing.Mktcontactprop;
import org.suinsit.apps.marketing.Mktemailtemplate;
import org.suinsit.apps.marketing.Mktmlistnews;
import org.suinsit.apps.marketing.Mktmnewsletter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTPROVMAIL" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTPROVMAIL",
	labelMonitor = "NOMBRE",
	pk = "idxmktprovmail" 
)
public class Mktprovmail implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clasejava",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clasejava;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "claveapi",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String claveapi;
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
	@Id
	@Column (
		name = "idxmktprovmail",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktprovmail;
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
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "password",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String password;
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
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMSERVICIO0",
		referencedColumnName = "IDXMSERVICIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mservicio idmservicio;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktprovmail" 
	)
	private List<Mktmlistnews> submktmlistnews;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktprovmail" 
	)
	private List<Mktmnewsletter> submktmnewsletter;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktprovmail" 
	)
	private List<Mktemailtemplate> submktemailtemplate;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktprovmail" 
	)
	private List<Mktcontactprop> submktcontactprop; 

	public Mservicio getIdmservicio() {
		if(this.idmservicio==null)this.idmservicio=new org.suinsit.apps.admin.Mservicio();
		  return this.idmservicio; 
	}
	
	public List<Mktmlistnews> getSubmktmlistnews() {
		if(this.submktmlistnews==null)this.submktmlistnews=new ArrayList<>(0);
		  return this.submktmlistnews; 
	}
	
	public List<Mktmnewsletter> getSubmktmnewsletter() {
		if(this.submktmnewsletter==null)this.submktmnewsletter=new ArrayList<>(0);
		  return this.submktmnewsletter; 
	}
	
	public List<Mktemailtemplate> getSubmktemailtemplate() {
		if(this.submktemailtemplate==null)this.submktemailtemplate=new ArrayList<>(0);
		  return this.submktemailtemplate; 
	}
	
	public List<Mktcontactprop> getSubmktcontactprop() {
		if(this.submktcontactprop==null)this.submktcontactprop=new ArrayList<>(0);
		  return this.submktcontactprop; 
	} 

}