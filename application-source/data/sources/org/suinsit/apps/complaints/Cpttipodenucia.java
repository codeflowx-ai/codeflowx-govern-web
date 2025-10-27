package org.suinsit.apps.complaints;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
import org.suinsit.apps.complaints.Cptcomplaint;
import org.suinsit.apps.complaints.Cptrtipousers;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTTIPODENUCIA" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTTIPODENUCIA",
	labelMonitor = "TIPODENUNCIA",
	pk = "idxcpttipodenucia" 
)
public class Cpttipodenucia implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "administracion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean administracion;
	@Column (
		name = "descripcioninterna",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcioninterna;
	@Column (
		name = "descrippublica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descrippublica;
	@Column (
		name = "gobierno",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean gobierno;
	@Id
	@Column (
		name = "idxcpttipodenucia",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcpttipodenucia;
	@Column (
		name = "legal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean legal;
	@Column (
		name = "penal",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean penal;
	@Column (
		name = "procedimientos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String procedimientos;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipodenuncia",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipodenuncia;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcpttipodenucia" 
	)
	private List<Cptcomplaint> subcptcomplaint;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcpttipodenucia" 
	)
	private List<Cptrtipousers> subcptrtipousers; 

	public List<Cptcomplaint> getSubcptcomplaint() {
		if(this.subcptcomplaint==null)this.subcptcomplaint=new ArrayList<>(0);
		  return this.subcptcomplaint; 
	}
	
	public List<Cptrtipousers> getSubcptrtipousers() {
		if(this.subcptrtipousers==null)this.subcptrtipousers=new ArrayList<>(0);
		  return this.subcptrtipousers; 
	} 

}