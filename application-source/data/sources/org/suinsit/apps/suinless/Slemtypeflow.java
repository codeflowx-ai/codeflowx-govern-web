package org.suinsit.apps.suinless;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	name = "SLEMTYPEFLOW" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLEMTYPEFLOW",
	labelMonitor = "TIPO",
	pk = "idxslemtypeflow" 
)
public class Slemtypeflow implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Column (
		name = "chatbot",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean chatbot;
	@NotNull
	@NotBlank
	@Column (
		name = "execution",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean execution;
	@Id
	@Column (
		name = "idxslemtypeflow",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslemtypeflow;
	@NotNull
	@NotBlank
	@Column (
		name = "rag",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean rag;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipo",
		nullable = false 
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
		mappedBy = "idslemtypeflow" 
	)
	private List<Slespromp> subslespromp; 

	public List<Slespromp> getSubslespromp() {
		if(this.subslespromp==null)this.subslespromp=new ArrayList<>(0);
		  return this.subslespromp; 
	} 

}