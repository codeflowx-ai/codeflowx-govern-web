package org.suinsit.apps.crm;

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
import org.suinsit.apps.crm.Crmroporcomp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CRMCOMPETIDOR" 
)
@Entidad (
	namespace = "crm",
	type = "TABLE",
	name = "CRMCOMPETIDOR",
	labelMonitor = "COMPETIDOR",
	pk = "idxcrmcompetidor" 
)
public class Crmcompetidor implements Serializable { 

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
		type = "BOOLEAN" 
	)
	private boolean baja;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "competidor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String competidor;
	@Column (
		name = "debilidades",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String debilidades;
	@Column (
		name = "fortalezas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String fortalezas;
	@Id
	@Column (
		name = "idxcrmcompetidor",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcrmcompetidor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "web",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String web;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcrmcompetidor" 
	)
	private List<Crmroporcomp> subcrmroporcomp; 

	public List<Crmroporcomp> getSubcrmroporcomp() {
		if(this.subcrmroporcomp==null)this.subcrmroporcomp=new ArrayList<>(0);
		  return this.subcrmroporcomp; 
	} 

}