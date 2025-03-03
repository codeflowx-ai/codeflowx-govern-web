package org.suinsit.apps.subvenciones;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.subvenciones.Kitrbonocat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CATKITDIGITAL" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "CATKITDIGITAL",
	labelMonitor = "CATEGORIA",
	pk = "idxcatkitdigital" 
)
public class Catkitdigital implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "categoria",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String categoria;
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
	@Id
	@Column (
		name = "idxcatkitdigital",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcatkitdigital;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importecat1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importecat1;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importecat2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importecat2;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importecat3",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importecat3;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "url",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String url;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idcatkitdigital" 
	)
	private List<Kitrbonocat> subkitrbonocat; 

	public List<Kitrbonocat> getSubkitrbonocat() {
		if(this.subkitrbonocat==null)this.subkitrbonocat=new ArrayList<>(0);
		  return this.subkitrbonocat; 
	} 

}