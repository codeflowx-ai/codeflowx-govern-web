package org.suinsit.apps.myalm;

import java.io.Serializable;
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
	name = "ALMPRODUCT" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMPRODUCT",
	labelMonitor = "NAMEPRODUCT",
	pk = "idxalmproduct" 
)
public class Almproduct implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "descripction",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripction;
	@Id
	@Column (
		name = "idxalmproduct",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmproduct;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nameproduct",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nameproduct;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "pathbasesrc",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String pathbasesrc;
	private boolean updatable; 

}