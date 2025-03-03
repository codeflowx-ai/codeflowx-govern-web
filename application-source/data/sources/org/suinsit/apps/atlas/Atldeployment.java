package org.suinsit.apps.atlas;

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
import org.suinsit.apps.atlas.Atlrcompdeploy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ATLDEPLOYMENT" 
)
@Entidad (
	namespace = "atlas",
	type = "TABLE",
	name = "ATLDEPLOYMENT",
	labelMonitor = "NOMBRE",
	pk = "idxatldeployment" 
)
public class Atldeployment implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "infraestructura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean infraestructura;
	@Column (
		name = "application",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean application;
	@NotNull
	@NotBlank
	@Column (
		name = "deployment",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String deployment;
	@Id
	@Column (
		name = "idxatldeployment",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatldeployment;
	@Column (
		name = "informacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String informacion;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Service,Ingress,Deployment,LoadBalancer,ConfigMap,PersistentVolumeClaim,Secret" 
		},
		message = "solamente admite lo valores: ,Service,Ingress,Deployment,LoadBalancer,ConfigMap,PersistentVolumeClaim,Secret" 
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
		type = "ENUM_STRING" 
	)
	private String tipo;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idatldeployment" 
	)
	private List<Atlrcompdeploy> subatlrcompdeploy; 

	public List<Atlrcompdeploy> getSubatlrcompdeploy() {
		if(this.subatlrcompdeploy==null)this.subatlrcompdeploy=new ArrayList<>(0);
		  return this.subatlrcompdeploy; 
	} 

}