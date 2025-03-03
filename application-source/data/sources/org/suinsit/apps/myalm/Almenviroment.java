package org.suinsit.apps.myalm;

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
import org.suinsit.apps.myalm.Almcd;
import org.suinsit.apps.myalm.Almrdeploys;
import org.suinsit.apps.myalm.Almrprodscm;
import org.suinsit.apps.myalm.Almscm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMENVIROMENT" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMENVIROMENT",
	labelMonitor = "ENVIROMENT",
	pk = "idxalmenviroment" 
)
public class Almenviroment implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 50 
	)
	@Column (
		name = "prefix",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String prefix;
	@Column (
		name = "preproduction",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean preproduction;
	@Column (
		name = "development",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean development;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "enviroment",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String enviroment;
	@Id
	@Column (
		name = "idxalmenviroment",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmenviroment;
	@Column (
		name = "production",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean production;
	@Column (
		name = "testing",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean testing;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmenviroment" 
	)
	private List<Almcd> subalmcd;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmenviroment" 
	)
	private List<Almrdeploys> subalmrdeploys;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmenviroment" 
	)
	private List<Almrprodscm> subalmrprodscm;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmenviroment" 
	)
	private List<Almscm> subalmscm; 

	public List<Almcd> getSubalmcd() {
		if(this.subalmcd==null)this.subalmcd=new ArrayList<>(0);
		  return this.subalmcd; 
	}
	
	public List<Almrdeploys> getSubalmrdeploys() {
		if(this.subalmrdeploys==null)this.subalmrdeploys=new ArrayList<>(0);
		  return this.subalmrdeploys; 
	}
	
	public List<Almrprodscm> getSubalmrprodscm() {
		if(this.subalmrprodscm==null)this.subalmrprodscm=new ArrayList<>(0);
		  return this.subalmrprodscm; 
	}
	
	public List<Almscm> getSubalmscm() {
		if(this.subalmscm==null)this.subalmscm=new ArrayList<>(0);
		  return this.subalmscm; 
	} 

}