package org.suinsit.apps.document;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.document.Doccatalogo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "DOCTIPODOCUM" 
)
@Entidad (
	namespace = "document",
	type = "TABLE",
	name = "DOCTIPODOCUM",
	labelMonitor = "",
	pk = "idxdoctipodocum" 
)
public class Doctipodocum implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "baja",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Fecha de baja",
		type = "DATE" 
	)
	private Date baja;
	@Id
	@Column (
		name = "idxdoctipodocum",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxdoctipodocum;
	@Column (
		name = "subcarpeta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean subcarpeta;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipodocumento",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Tipo de documento",
		type = "VARCHAR" 
	)
	private String tipodocumento;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDDOCCATALOGO0",
		referencedColumnName = "IDXDOCCATALOGO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Doccatalogo iddoccatalogo; 

	public Doccatalogo getIddoccatalogo() {
		if(this.iddoccatalogo==null)this.iddoccatalogo=new org.suinsit.apps.document.Doccatalogo();
		  return this.iddoccatalogo; 
	} 

}