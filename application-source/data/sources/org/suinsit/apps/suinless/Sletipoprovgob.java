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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.suinless.Slprovider;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLETIPOPROVGOB" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLETIPOPROVGOB",
	labelMonitor = "TIPO",
	pk = "idxsletipoprovgob" 
)
public class Sletipoprovgob implements Serializable { 

	private static final long serialVersionUID = 1L;
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
	@Id
	@Column (
		name = "idxsletipoprovgob",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsletipoprovgob;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsletipoprovgob" 
	)
	private List<Slprovider> subslprovider; 

	public List<Slprovider> getSubslprovider() {
		if(this.subslprovider==null)this.subslprovider=new ArrayList<>(0);
		  return this.subslprovider; 
	} 

}