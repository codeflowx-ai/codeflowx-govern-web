package org.suinsit.apps.arquitecturas;

import java.io.Serializable;
import java.lang.Integer;
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
import org.suinsit.apps.arquitecturas.Arqmpromp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ARQRPROMPFLOW" 
)
@Entidad (
	namespace = "arquitecturas",
	type = "TABLE",
	name = "ARQRPROMPFLOW",
	pk = "idxarqrprompflow" 
)
public class Arqrprompflow implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "asistente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean asistente;
	@Column (
		name = "datos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean datos;
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
		name = "documento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean documento;
	@Id
	@Column (
		name = "idxarqrprompflow",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxarqrprompflow;
	@Column (
		name = "order",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer order;
	@Column (
		name = "sendprevio",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sendprevio;
	@Column (
		name = "usuario",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean usuario;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARQMPROMP0",
		referencedColumnName = "IDXARQMPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqmpromp idarqmpromp;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDARPROMPCHILD0",
		referencedColumnName = "IDXARQMPROMP",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Arqmpromp idarprompchild; 

	public Arqmpromp getIdarqmpromp() {
		if(this.idarqmpromp==null)this.idarqmpromp=new org.suinsit.apps.arquitecturas.Arqmpromp();
		  return this.idarqmpromp; 
	}
	
	public Arqmpromp getIdarprompchild() {
		if(this.idarprompchild==null)this.idarprompchild=new org.suinsit.apps.arquitecturas.Arqmpromp();
		  return this.idarprompchild; 
	} 

}