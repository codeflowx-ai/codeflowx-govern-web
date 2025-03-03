package org.suinsit.apps.citaprev;

import java.io.Serializable;
import java.lang.Long;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.citaprev.Citmemploy;
import org.suinsit.apps.citaprev.Citmservice;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CITRUSUSERVICE" 
)
@Entidad (
	namespace = "citaprev",
	type = "TABLE",
	name = "CITRUSUSERVICE",
	labelMonitor = "",
	pk = "idxcitrususervice" 
)
public class Citrususervice implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcitrususervice",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcitrususervice;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "precio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal precio;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCITMSERVICE0",
		referencedColumnName = "IDXCITMSERVICE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Citmservice idcitmservice;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCITMEMPLOY0",
		referencedColumnName = "IDXCITMEMPLOY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Citmemploy idcitmemploy; 

	public Citmservice getIdcitmservice() {
		if(this.idcitmservice==null)this.idcitmservice=new org.suinsit.apps.citaprev.Citmservice();
		  return this.idcitmservice; 
	}
	
	public Citmemploy getIdcitmemploy() {
		if(this.idcitmemploy==null)this.idcitmemploy=new org.suinsit.apps.citaprev.Citmemploy();
		  return this.idcitmemploy; 
	} 

}