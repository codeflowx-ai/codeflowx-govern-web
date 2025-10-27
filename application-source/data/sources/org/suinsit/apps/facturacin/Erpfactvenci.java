package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
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
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.facturacin.Erpmabnco;
import org.suinsit.apps.facturacin.Erpmestadocbo;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "ERPFACTVENCI" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "ERPFACTVENCI",
	pk = "idxerpfactvenci" 
)
public class Erpfactvenci implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "online",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean online;
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
	@Id
	@Column (
		name = "idxerpfactvenci",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxerpfactvenci;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPFACTURA0",
		referencedColumnName = "IDXERPFACTURA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpfactura iderpfactura;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMESTADOCBO0",
		referencedColumnName = "IDXERPMESTADOCBO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmestadocbo iderpmestadocbo;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDERPMABNCO0",
		referencedColumnName = "IDXERPMABNCO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpmabnco iderpmabnco; 

	public Erpfactura getIderpfactura() {
		if(this.iderpfactura==null)this.iderpfactura=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.iderpfactura; 
	}
	
	public Erpmestadocbo getIderpmestadocbo() {
		if(this.iderpmestadocbo==null)this.iderpmestadocbo=new org.suinsit.apps.facturacin.Erpmestadocbo();
		  return this.iderpmestadocbo; 
	}
	
	public Erpmabnco getIderpmabnco() {
		if(this.iderpmabnco==null)this.iderpmabnco=new org.suinsit.apps.facturacin.Erpmabnco();
		  return this.iderpmabnco; 
	} 

}