package org.suinsit.apps.admin;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MMODULO" 
)
@Entidad (
	namespace = "admin",
	type = "TABLE",
	name = "MMODULO" 
)
public class Mmodulo implements Serializable { 

	private static final long serialVersionUID = 1L;
	private boolean updatable; 

}