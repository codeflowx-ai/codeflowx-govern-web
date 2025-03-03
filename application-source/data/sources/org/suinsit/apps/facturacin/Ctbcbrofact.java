package org.suinsit.apps.facturacin;

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
	name = "CTBCBROFACT" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "CTBCBROFACT" 
)
public class Ctbcbrofact implements Serializable { 

	private static final long serialVersionUID = 1L;
	private boolean updatable; 

}