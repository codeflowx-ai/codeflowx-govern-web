package org.suinsit.apps.subvenciones;

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
	name = "SUBRSUBVCONT" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "SUBRSUBVCONT",
	pk = "idxsubrsubvcont" 
)
public class Subrsubvcont implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsubrsubvcont",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsubrsubvcont;
	private boolean updatable; 

}