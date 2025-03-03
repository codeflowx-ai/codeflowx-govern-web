package org.suinsit.apps.complaints;

import java.io.Serializable;
import java.lang.Integer;
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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.Random;
import org.suinsit.apps.complaints.Cptcomplaint;
import org.suinsit.apps.crm.Crmempresa;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTREPORT" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTREPORT",
	labelMonitor = "",
	pk = "idxcptreport" 
)
public class Cptreport implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "contenido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String contenido;
	@Id
	@Column (
		name = "idxcptreport",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcptreport;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refform",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	@Random (
		lengh = 10,
		prefix = "null",
		type = "numeric" 
	)
	private String refform;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "titulo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String titulo;
	@Column (
		name = "visitas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer visitas;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcptreport" 
	)
	private List<Cptcomplaint> subcptcomplaint; 

	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public List<Cptcomplaint> getSubcptcomplaint() {
		if(this.subcptcomplaint==null)this.subcptcomplaint=new ArrayList<>(0);
		  return this.subcptcomplaint; 
	} 

}