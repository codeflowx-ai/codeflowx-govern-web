package org.suinsit.apps.gpdr;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PERSONALDATACATEGORY" 
)
@Entidad (
	namespace = "gpdr",
	type = "TABLE",
	name = "PERSONALDATACATEGORY",
	pk = "idxpersonaldatacategory" 
)
public class Personaldatacategory implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "retentionperiodmonths",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer retentionperiodmonths;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "legalbasis",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String legalbasis;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "processingpurpose",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String processingpurpose;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sensitivity",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String sensitivity;
	@Size (
		min = 0,
		max = 1000 
	)
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String description;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "category",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String category;
	@Id
	@Column (
		name = "idxpersonaldatacategory",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpersonaldatacategory;
	private boolean updatable; 

}