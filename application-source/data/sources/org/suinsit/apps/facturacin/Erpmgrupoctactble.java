package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.facturacin.Erpmctactble;
import org.suinsit.apps.facturacin.Erptipoctacble;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPMGRUPOCTACTBLE" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMGRUPOCTACTBLE",
	labelMonitor = "",
	pk = "idxerpmgrupoctactble" 
)
public class Erpmgrupoctactble implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
	@Column (
		name = "grupo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer grupo;
	@Id
	@Column (
		name = "idxerpmgrupoctactble",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmgrupoctactble;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmgrupoctactble" 
	)
	private List<Erptipoctacble> suberptipoctacble;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmgrupoctactble" 
	)
	private List<Erpmctactble> suberpmctactble; 

	public List<Erptipoctacble> getSuberptipoctacble() {
		if(this.suberptipoctacble==null)this.suberptipoctacble=new ArrayList<>(0);
		  return this.suberptipoctacble; 
	}
	
	public List<Erpmctactble> getSuberpmctactble() {
		if(this.suberpmctactble==null)this.suberpmctactble=new ArrayList<>(0);
		  return this.suberpmctactble; 
	} 

}