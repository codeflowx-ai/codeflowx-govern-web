package org.suinsit.apps.admin;

import java.io.Serializable;
import java.lang.Integer;
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
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.contratos.Crtmcontrato;
import org.suinsit.apps.contratos.Crtmtipo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MPERIDICIDAD" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MPERIDICIDAD",
	labelMonitor = "PERIDODO",
	pk = "idxmperidicidad" 
)
public class Mperidicidad implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "diavto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer diavto;
	@Column (
		name = "mesnatural",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean mesnatural;
	@NotNull
	@NotBlank
	@Column (
		name = "alta",
		nullable = false 
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
		name = "anonatural",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean anonatural;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "codigo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String codigo;
	@Id
	@Column (
		name = "idxmperidicidad",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmperidicidad;
	@Column (
		name = "peridicidad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer peridicidad;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "periodo",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String periodo;
	@Column (
		name = "semestrenatural",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean semestrenatural;
	@Column (
		name = "trimestrenatural",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean trimestrenatural;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmperidicidad" 
	)
	private List<Crtmcontrato> subcrtmcontrato;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmperidicidad" 
	)
	private List<Crtmtipo> subcrtmtipo; 

	public List<Crtmcontrato> getSubcrtmcontrato() {
		if(this.subcrtmcontrato==null)this.subcrtmcontrato=new ArrayList<>(0);
		  return this.subcrtmcontrato; 
	}
	
	public List<Crtmtipo> getSubcrtmtipo() {
		if(this.subcrtmtipo==null)this.subcrtmtipo=new ArrayList<>(0);
		  return this.subcrtmtipo; 
	} 

}