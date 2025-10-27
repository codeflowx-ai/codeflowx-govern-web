package org.suinsit.apps.facturacin;

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
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erpfactvenci;
import org.suinsit.apps.facturacli.Clifactura;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPMESTADOCBO" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPMESTADOCBO",
	pk = "idxerpmestadocbo" 
)
public class Erpmestadocbo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",bg-primary,bg-secondary,bg-success,bg-warning,bg-info,bg-light,bg-dark" 
		},
		message = "solamente admite lo valores: ,bg-primary,bg-secondary,bg-success,bg-warning,bg-info,bg-light,bg-dark" 
	)
	@Column (
		name = "bgcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String bgcolor;
	@Column (
		name = "cobparcial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean cobparcial;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estado",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estado;
	@Id
	@Column (
		name = "idxerpmestadocbo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpmestadocbo;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestadocbo" 
	)
	private List<Erpfactura> suberpfactura;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestadocbo" 
	)
	private List<Erpfactvenci> suberpfactvenci;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "iderpmestadocbo" 
	)
	private List<Clifactura> subclifactura; 

	public List<Erpfactura> getSuberpfactura() {
		if(this.suberpfactura==null)this.suberpfactura=new ArrayList<>(0);
		  return this.suberpfactura; 
	}
	
	public List<Erpfactvenci> getSuberpfactvenci() {
		if(this.suberpfactvenci==null)this.suberpfactvenci=new ArrayList<>(0);
		  return this.suberpfactvenci; 
	}
	
	public List<Clifactura> getSubclifactura() {
		if(this.subclifactura==null)this.subclifactura=new ArrayList<>(0);
		  return this.subclifactura; 
	} 

}