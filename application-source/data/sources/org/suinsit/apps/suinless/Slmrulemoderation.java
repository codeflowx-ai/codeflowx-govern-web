package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slsecuritypromt;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLMRULEMODERATION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLMRULEMODERATION",
	labelMonitor = "nombre",
	pk = "idxslmrulemoderation" 
)
public class Slmrulemoderation implements Serializable { 

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
		name = "regla",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String regla;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "describe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String describe;
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
	@Id
	@Column (
		name = "idxslmrulemoderation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslmrulemoderation;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idslmrulemoderation" 
	)
	private List<Slsecuritypromt> subslsecuritypromt; 

	public List<Slsecuritypromt> getSubslsecuritypromt() {
		if(this.subslsecuritypromt==null)this.subslsecuritypromt=new ArrayList<>(0);
		  return this.subslsecuritypromt; 
	} 

}