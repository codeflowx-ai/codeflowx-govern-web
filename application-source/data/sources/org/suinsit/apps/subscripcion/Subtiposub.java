package org.suinsit.apps.subscripcion;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
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
import org.enartframework.nocode.annotacion.AutoGenerate;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.subscripcion.Subrsubfun;
import org.suinsit.apps.subscripcion.Subrtipomodelo;
import org.suinsit.apps.subscripcion.Subsubscripcion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SUBTIPOSUB" 
)
@Entidad (
	namespace = "subscripcion",
	type = "TABLE",
	name = "SUBTIPOSUB",
	pk = "idxsubtiposub" 
)
public class Subtiposub implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ilimitedusers",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ilimitedusers;
	@Column (
		name = "ilimiteddocument",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ilimiteddocument;
	@Column (
		name = "ilimitedprojects",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ilimitedprojects;
	@Column (
		name = "ilimitedprojectia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ilimitedprojectia;
	@Column (
		name = "ilimitedmodels",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ilimitedmodels;
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
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "alias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String alias;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Column (
		name = "fecalta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecalta;
	@Column (
		name = "fecbaja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecbaja;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "identificador",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Identificador",
		type = "VARCHAR" 
	)
	@AutoGenerate (
		uuid = true 
	)
	private String identificador;
	@Id
	@Column (
		name = "idxsubtiposub",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubtiposub;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubtiposub" 
	)
	private List<Subrsubfun> subsubrsubfun;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubtiposub" 
	)
	private List<Subrtipomodelo> subsubrtipomodelo;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idsubtiposub" 
	)
	private List<Subsubscripcion> subsubsubscripcion; 

	public List<Subrsubfun> getSubsubrsubfun() {
		if(this.subsubrsubfun==null)this.subsubrsubfun=new ArrayList<>(0);
		  return this.subsubrsubfun; 
	}
	
	public List<Subrtipomodelo> getSubsubrtipomodelo() {
		if(this.subsubrtipomodelo==null)this.subsubrtipomodelo=new ArrayList<>(0);
		  return this.subsubrtipomodelo; 
	}
	
	public List<Subsubscripcion> getSubsubsubscripcion() {
		if(this.subsubsubscripcion==null)this.subsubsubscripcion=new ArrayList<>(0);
		  return this.subsubsubscripcion; 
	} 

}