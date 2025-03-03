package org.suinsit.apps.myalm;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
	name = "ALMRGENFUENTES" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRGENFUENTES",
	labelMonitor = "",
	pk = "idxalmrgenfuentes" 
)
public class Almrgenfuentes implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "teimpodevelop",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal teimpodevelop;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "coeficiente",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal coeficiente;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "generados",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal generados;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "tiempo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal tiempo;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "source",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String source;
	@Id
	@Column (
		name = "idxalmrgenfuentes",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrgenfuentes;
	private boolean updatable; 

}