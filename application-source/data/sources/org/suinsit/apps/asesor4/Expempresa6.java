package org.suinsit.apps.asesor4;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	name = "EXPEMPRESA6" 
)
@Entidad (
	namespace = "asesor4",
	type = "TABLE",
	name = "EXPEMPRESA6",
	pk = "idx" 
)
public class Expempresa6 implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "ciudad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String ciudad;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "companyid",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "DECIMAL" 
	)
	private BigDecimal companyid;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estadoregiyn",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String estadoregiyn;
	@Column (
		name = "fechadecreaciyn",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp fechadecreaciyn;
	@Column (
		name = "fechadelayltimaactividad",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp fechadelayltimaactividad;
	@Id
	@NotNull
	@NotBlank
	@Column (
		name = "idx",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idx;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "industria",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String industria;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombredelaempresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String nombredelaempresa;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "paysregiyn",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String paysregiyn;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "phonenumber",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String phonenumber;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "propietariodelregistrodeempresa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String propietariodelregistrodeempresa;
	private boolean updatable; 

}