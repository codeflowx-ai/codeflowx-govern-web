package org.suinsit.apps.franchise;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.franchise.Frcmfranchise;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "FRCTIPO" 
)
@Entidad (
	namespace = "franchise",
	type = "TABLE",
	name = "FRCTIPO",
	labelMonitor = "TIPO",
	pk = "idxfrctipo" 
)
public class Frctipo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "comercial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean comercial;
	@Id
	@Column (
		name = "idxfrctipo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxfrctipo;
	@Column (
		name = "master",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean master;
	@Column (
		name = "tecnica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean tecnica;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idfrctipo" 
	)
	private List<Frcmfranchise> subfrcmfranchise; 

	public List<Frcmfranchise> getSubfrcmfranchise() {
		if(this.subfrcmfranchise==null)this.subfrcmfranchise=new ArrayList<>(0);
		  return this.subfrcmfranchise; 
	} 

}