package org.suinsit.apps.marketing;

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
import org.suinsit.apps.marketing.Mktpotenciales;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTCAMPAING" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTCAMPAING",
	labelMonitor = "CAMPAING",
	pk = "idxmktcampaing" 
)
public class Mktcampaing implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 10 
	)
	@Column (
		name = "code",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String code;
	@Column (
		name = "activa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "backcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String backcolor;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "campaing",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String campaing;
	@Id
	@Column (
		name = "idxmktcampaing",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktcampaing;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktcampaing" 
	)
	private List<Mktpotenciales> submktpotenciales; 

	public List<Mktpotenciales> getSubmktpotenciales() {
		if(this.submktpotenciales==null)this.submktpotenciales=new ArrayList<>(0);
		  return this.submktpotenciales; 
	} 

}