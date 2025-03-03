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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.marketing.Mktrpotsegm;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTMSEGMENTO" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTMSEGMENTO",
	labelMonitor = "SEGMENTO",
	pk = "idxmktmsegmento" 
)
public class Mktmsegmento implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmktmsegmento",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktmsegmento;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "segmento",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String segmento;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktmsegmento" 
	)
	private List<Mktrpotsegm> submktrpotsegm; 

	public List<Mktrpotsegm> getSubmktrpotsegm() {
		if(this.submktrpotsegm==null)this.submktrpotsegm=new ArrayList<>(0);
		  return this.submktrpotsegm; 
	} 

}