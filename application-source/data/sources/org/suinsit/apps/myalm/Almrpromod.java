package org.suinsit.apps.myalm;

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
	name = "ALMRPROMOD" 
)
@Entidad (
	namespace = "myalm",
	type = "TABLE",
	name = "ALMRPROMOD",
	labelMonitor = "",
	pk = "idxalmrpromod" 
)
public class Almrpromod implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxalmrpromod",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxalmrpromod;
	private boolean updatable; 

}