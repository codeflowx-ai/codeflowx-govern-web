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
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.suinless.Slesmodel;
import org.suinsit.apps.suinless.Sltemplatemeta;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLETIPOMODELO" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLETIPOMODELO",
	labelMonitor = "TIPOMODELO",
	pk = "idxsletipomodelo" 
)
public class Sletipomodelo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsletipomodelo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsletipomodelo;
	@Column (
		name = "infopublic",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String infopublic;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",NIVEL I,NIVEL II,NIVEL III" 
		},
		message = "solamente admite lo valores: ,NIVEL I,NIVEL II,NIVEL III" 
	)
	@Column (
		name = "nivel",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String nivel;
	@Column (
		name = "reglamento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String reglamento;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipomodelo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipomodelo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsletipomodelo" 
	)
	private List<Slesmodel> subslesmodel;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsletipomodelo" 
	)
	private List<Sltemplatemeta> subsltemplatemeta; 

	public List<Slesmodel> getSubslesmodel() {
		if(this.subslesmodel==null)this.subslesmodel=new ArrayList<>(0);
		  return this.subslesmodel; 
	}
	
	public List<Sltemplatemeta> getSubsltemplatemeta() {
		if(this.subsltemplatemeta==null)this.subsltemplatemeta=new ArrayList<>(0);
		  return this.subsltemplatemeta; 
	} 

}