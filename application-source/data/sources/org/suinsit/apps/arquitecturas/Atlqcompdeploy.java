package org.suinsit.apps.arquitecturas;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;

@Getter
@Setter
@NoArgsConstructor
@Entidad (
	namespace = "arquitecturas",
	type = "QUERY",
	name = "ATLQCOMPDEPLOY" 
)
public class Atlqcompdeploy implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "componente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "VARCHAR" 
	)
	private String componente;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "{}",
		type = "CLOB" 
	)
	private String descripcion;
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
		label = "{}",
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
		label = "{}",
		type = "ENUM_STRING" 
	)
	private String tipo;
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
		label = "{}",
		type = "CLOB" 
	)
	private String deployment;
	@Column (
		name = "idxatlcomponent",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxatlcomponent;
	private boolean updatable; 

}