package org.suinsit.apps.partners;

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
import org.suinsit.apps.partners.Nivel;
import org.suinsit.apps.partners.Partner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "TIPOPARTNER" 
)
@Entidad (
	namespace = "partners",
	type = "TABLE",
	name = "TIPOPARTNER",
	labelMonitor = "TIPOPARTNER",
	pk = "idxtipopartner" 
)
public class Tipopartner implements Serializable { 

	private static final long serialVersionUID = 1L;
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
		name = "idxtipopartner",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxtipopartner;
	@Column (
		name = "prescriptor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean prescriptor;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipopartner",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipopartner;
	private boolean updatable;
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
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idtipopartner" 
	)
	private List<Partner> subpartner; 

	public Nivel getIdnivel() {
		if(this.idnivel==null)this.idnivel=new org.suinsit.apps.partners.Nivel();
		  return this.idnivel; 
	}
	
	public List<Partner> getSubpartner() {
		if(this.subpartner==null)this.subpartner=new ArrayList<>(0);
		  return this.subpartner; 
	} 

}