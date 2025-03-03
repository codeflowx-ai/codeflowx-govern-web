package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.suinsit.apps.myalm.Almimportopenapi;
import org.suinsit.apps.myalm.Almtiposervicio;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ALMAPIMODEL" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMAPIMODEL",
	labelMonitor = "NOMBRE",
	pk = "idxalmapimodel" 
)
public class Almapimodel implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "activo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean activo;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "avatar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "BLOB" 
	)
	private Object avatar;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "clazzmapping",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String clazzmapping;
	@Id
	@Column (
		name = "idxalmapimodel",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmapimodel;
	@Column (
		name = "information",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String information;
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
	@Column (
		name = "openapi",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean openapi;
	@Column (
		name = "openevent",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean openevent;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "pathsrc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String pathsrc;
	@Column (
		name = "read",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean read;
	@Column (
		name = "store",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean store;
	@Column (
		name = "swagger",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean swagger;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",SWAGGER_V2,SWAGGER_V3,OPENAPI_V3" 
		},
		message = "solamente admite lo valores: ,SWAGGER_V2,SWAGGER_V3,OPENAPI_V3" 
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlapi",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlapi;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "urlweb",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String urlweb;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "version",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String version;
	@Column (
		name = "wsdl",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean wsdl;
	@Column (
		name = "yaml",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String yaml;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDALMTIPOSERVICIO0",
		referencedColumnName = "IDXALMTIPOSERVICIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Almtiposervicio idalmtiposervicio;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idalmapimodel" 
	)
	private List<Almimportopenapi> subalmimportopenapi; 

	public Almtiposervicio getIdalmtiposervicio() {
		if(this.idalmtiposervicio==null)this.idalmtiposervicio=new org.suinsit.apps.myalm.Almtiposervicio();
		  return this.idalmtiposervicio; 
	}
	
	public List<Almimportopenapi> getSubalmimportopenapi() {
		if(this.subalmimportopenapi==null)this.subalmimportopenapi=new ArrayList<>(0);
		  return this.subalmimportopenapi; 
	} 

}