package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.facturacin.Erpmctactble;
import org.suinsit.apps.facturacin.Erpmgrupoctactble;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPTIPOCTACBLE" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPTIPOCTACBLE",
	labelMonitor = "CUENTA",
	pk = "idxerptipoctacble" 
)
public class Erptipoctacble implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cuenta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cuenta;
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
	@Id
	@Column (
		name = "idxerptipoctacble",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerptipoctacble;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMGRUPOCTACTBLE0",
		referencedColumnName = "IDXERPMGRUPOCTACTBLE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmgrupoctactble iderpmgrupoctactble;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderptipoctacble" 
	)
	private List<Erpmctactble> suberpmctactble; 

	public Erpmgrupoctactble getIderpmgrupoctactble() {
		if(this.iderpmgrupoctactble==null)this.iderpmgrupoctactble=new org.suinsit.apps.facturacin.Erpmgrupoctactble();
		  return this.iderpmgrupoctactble; 
	}
	
	public List<Erpmctactble> getSuberpmctactble() {
		if(this.suberpmctactble==null)this.suberpmctactble=new ArrayList<>(0);
		  return this.suberpmctactble; 
	} 

}