package org.suinsit.apps.atlas;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.atlas.Atlkubecloud;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLBALANCER" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLBALANCER",
	labelMonitor = "BALANCER",
	pk = "idxatlbalancer" 
)
public class Atlbalancer implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "anchobanda",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer anchobanda;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "balancer",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String balancer;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "classingress",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String classingress;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String idproveedor;
	@Id
	@Column (
		name = "idxatlbalancer",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		label = "",
		type = "LONG" 
	)
	private Long idxatlbalancer;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importemes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importemes;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ingressdeploy",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ingressdeploy;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ippublica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String ippublica;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDATLKUBECLOUD0",
		referencedColumnName = "IDXATLKUBECLOUD",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Atlkubecloud idatlkubecloud; 

	public Atlkubecloud getIdatlkubecloud() {
		if(this.idatlkubecloud==null)this.idatlkubecloud=new org.suinsit.apps.atlas.Atlkubecloud();
		  return this.idatlkubecloud; 
	} 

}