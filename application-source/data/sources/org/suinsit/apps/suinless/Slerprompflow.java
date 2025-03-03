package org.suinsit.apps.suinless;

import java.io.Serializable;
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
import org.suinsit.apps.suinless.Slespromp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLERPROMPFLOW" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLERPROMPFLOW",
	pk = "idxslerprompflow" 
)
public class Slerprompflow implements Serializable { 

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
		name = "evalprevius",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean evalprevius;
	@Id
	@Column (
		name = "idxslerprompflow",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslerprompflow;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "orden",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal orden;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESPROMP0",
		referencedColumnName = "IDXSLESPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slespromp idslespromp;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESPROMPCHILD0",
		referencedColumnName = "IDXSLESPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slespromp idslesprompchild; 

	public Slespromp getIdslespromp() {
		if(this.idslespromp==null)this.idslespromp=new org.suinsit.apps.suinless.Slespromp();
		  return this.idslespromp; 
	}
	
	public Slespromp getIdslesprompchild() {
		if(this.idslesprompchild==null)this.idslesprompchild=new org.suinsit.apps.suinless.Slespromp();
		  return this.idslesprompchild; 
	} 

}