package org.suinsit.apps.marketing;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.marketing.Mktmlistnews;
import org.suinsit.apps.marketing.Mktprovmail;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTEMAILTEMPLATE" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTEMAILTEMPLATE",
	labelMonitor = "nombre",
	pk = "idxmktemailtemplate" 
)
public class Mktemailtemplate implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date alta;
	@Column (
		name = "descripcin",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcin;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Transacción,Bpmn,Marketing" 
		},
		message = "solamente admite lo valores: ,Transacción,Bpmn,Marketing" 
	)
	@Column (
		name = "tipo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipo;
	@Column (
		name = "contenido",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String contenido;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "idproveedor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String idproveedor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "nombre",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String nombre;
	@Id
	@Column (
		name = "idxmktemailtemplate",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktemailtemplate;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTPROVMAIL0",
		referencedColumnName = "IDXMKTPROVMAIL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktprovmail idmktprovmail;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTMLISTNEWS0",
		referencedColumnName = "IDXMKTMLISTNEWS",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmlistnews idmktmlistnews; 

	public Mktprovmail getIdmktprovmail() {
		if(this.idmktprovmail==null)this.idmktprovmail=new org.suinsit.apps.marketing.Mktprovmail();
		  return this.idmktprovmail; 
	}
	
	public Mktmlistnews getIdmktmlistnews() {
		if(this.idmktmlistnews==null)this.idmktmlistnews=new org.suinsit.apps.marketing.Mktmlistnews();
		  return this.idmktmlistnews; 
	} 

}