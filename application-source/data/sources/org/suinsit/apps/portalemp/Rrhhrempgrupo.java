package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Long;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.portalemp.Rrhempleado;
import org.suinsit.apps.portalemp.Rrhhgrupo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHHREMPGRUPO" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHHREMPGRUPO",
	pk = "idxrrhhrempgrupo" 
)
public class Rrhhrempgrupo implements Serializable { 

	private static final long serialVersionUID = 1L;
	@NotNull
	@NotBlank
	@Column (
		name = "inicio",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date inicio;
	@Id
	@Column (
		name = "idxrrhhrempgrupo",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhhrempgrupo;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHEMPLEADO0",
		referencedColumnName = "IDXRRHEMPLEADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhempleado idrrhempleado;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRRHHGRUPO0",
		referencedColumnName = "IDXRRHHGRUPO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhhgrupo idrrhhgrupo; 

	public Rrhempleado getIdrrhempleado() {
		if(this.idrrhempleado==null)this.idrrhempleado=new org.suinsit.apps.portalemp.Rrhempleado();
		  return this.idrrhempleado; 
	}
	
	public Rrhhgrupo getIdrrhhgrupo() {
		if(this.idrrhhgrupo==null)this.idrrhhgrupo=new org.suinsit.apps.portalemp.Rrhhgrupo();
		  return this.idrrhhgrupo; 
	} 

}