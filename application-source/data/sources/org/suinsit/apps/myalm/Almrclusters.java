package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.suinsit.apps.myalm.Almproject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMRCLUSTERS" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRCLUSTERS",
	pk = "idxalmrclusters" 
)
public class Almrclusters implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "dominio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String dominio;
	@Id
	@Column (
		name = "idxalmrclusters",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrclusters;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "namespace",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String namespace;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "subdominio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String subdominio;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMPROJECT0",
		referencedColumnName = "IDXALMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almproject idalmproject;
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

	public Almproject getIdalmproject() {
		if(this.idalmproject==null)this.idalmproject=new org.suinsit.apps.myalm.Almproject();
		  return this.idalmproject; 
	}
	
	public Atlkubecloud getIdatlkubecloud() {
		if(this.idatlkubecloud==null)this.idatlkubecloud=new org.suinsit.apps.atlas.Atlkubecloud();
		  return this.idatlkubecloud; 
	} 

}