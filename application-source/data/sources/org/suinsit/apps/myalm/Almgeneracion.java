package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
	name = "ALMGENERACION" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMGENERACION",
	labelMonitor = "",
	pk = "idxalmgeneracion" 
)
public class Almgeneracion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 500 
	)
	@Column (
		name = "branch",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String branch;
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentarios;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "ctesuinsit",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal ctesuinsit;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cteestimate",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cteestimate;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "costedevelop",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal costedevelop;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "corrector",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal corrector;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "tarifahora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal tarifahora;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "hrestantes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal hrestantes;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "hestimadas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal hestimadas;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "efectividad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal efectividad;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "fuentes",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal fuentes;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "errores",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal errores;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "lineas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal lineas;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "ficheros",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal ficheros;
	@Column (
		name = "generacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp generacion;
	@Id
	@Column (
		name = "idxalmgeneracion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmgeneracion;
	private boolean updatable; 

}