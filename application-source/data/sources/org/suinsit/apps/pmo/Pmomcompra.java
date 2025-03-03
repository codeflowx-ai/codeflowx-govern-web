package org.suinsit.apps.pmo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
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
import org.suinsit.apps.pmo.Pmomproject;
import org.suinsit.apps.pmo.Pmopartidapre;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PMOMCOMPRA" 
)
@Entidad (
	namespace = "pmo",
	type = "TABLE",
	name = "PMOMCOMPRA",
	labelMonitor = "",
	pk = "idxpmomcompra" 
)
public class Pmomcompra implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "fecha",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecha;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Pedido,Pagado" 
		},
		message = "solamente admite lo valores: ,Pedido,Pagado" 
	)
	@Column (
		name = "estadogto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String estadogto;
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "importe",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal importe;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "compra",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String compra;
	@Id
	@Column (
		name = "idxpmomcompra",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpmomcompra;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOMPROJECT0",
		referencedColumnName = "IDXPMOMPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmomproject idpmomproject;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPMOPARTIDAPRE0",
		referencedColumnName = "IDXPMOPARTIDAPRE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Pmopartidapre idpmopartidapre; 

	public Pmomproject getIdpmomproject() {
		if(this.idpmomproject==null)this.idpmomproject=new org.suinsit.apps.pmo.Pmomproject();
		  return this.idpmomproject; 
	}
	
	public Pmopartidapre getIdpmopartidapre() {
		if(this.idpmopartidapre==null)this.idpmopartidapre=new org.suinsit.apps.pmo.Pmopartidapre();
		  return this.idpmopartidapre; 
	} 

}