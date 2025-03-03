package org.suinsit.apps.mrp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.SequenceYear;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.mrp.Mrpestado;
import org.suinsit.apps.mrp.Mrpmsequence;
import org.suinsit.apps.mrp.Mrpmtipoperacion;
import org.suinsit.apps.mrp.Mrprproductos;
import org.suinsit.apps.portalemp.Rrhempleado;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPEXPEDICION" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPEXPEDICION",
	labelMonitor = "",
	pk = "idxmrpexpedicion" 
)
public class Mrpexpedicion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "documentoref",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String documentoref;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Antes posible,Productos finalizados" 
		},
		message = "solamente admite lo valores: ,Antes posible,Productos finalizados" 
	)
	@Column (
		name = "entregarcuando",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String entregarcuando;
	@Id
	@Column (
		name = "idxmrpexpedicion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrpexpedicion;
	@Column (
		name = "nota",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String nota;
	@Column (
		name = "prevista",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp prevista;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "referencia",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "AUTONUMERIC" 
	)
	@SequenceYear (
		autoCreateFromYear = false,
		autoResetYear = false,
		createManual = false,
		createFromFieldValue = "",
		fieldPrefix = "",
		foreing = true,
		fkTable = "false",
		fkAlias = "idmrpmsequence.sql",
		name = "MRPEXPEDICION_REFERENCIA",
		prefix = "",
		mask = "00000",
		addYear = false 
	)
	private String referencia;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMRPMSEQUENCE0",
		referencedColumnName = "IDXMRPMSEQUENCE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpmsequence idmrpmsequence;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMRPMTIPOPERACION0",
		referencedColumnName = "IDXMRPMTIPOPERACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpmtipoperacion idmrpmtipoperacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCLIENTREGA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idclientrega;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCOMPANY0",
		referencedColumnName = "IDXERPEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Erpempresa idcompany;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDRESPONSABLE0",
		referencedColumnName = "IDXRRHEMPLEADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Rrhempleado idresponsable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMRPESTADO0",
		referencedColumnName = "IDXMRPESTADO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpestado idmrpestado;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpexpedicion" 
	)
	private List<Mrprproductos> submrprproductos; 

	public Mrpmsequence getIdmrpmsequence() {
		if(this.idmrpmsequence==null)this.idmrpmsequence=new org.suinsit.apps.mrp.Mrpmsequence();
		  return this.idmrpmsequence; 
	}
	
	public Mrpmtipoperacion getIdmrpmtipoperacion() {
		if(this.idmrpmtipoperacion==null)this.idmrpmtipoperacion=new org.suinsit.apps.mrp.Mrpmtipoperacion();
		  return this.idmrpmtipoperacion; 
	}
	
	public Crmempresa getIdclientrega() {
		if(this.idclientrega==null)this.idclientrega=new org.suinsit.apps.crm.Crmempresa();
		  return this.idclientrega; 
	}
	
	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public Rrhempleado getIdresponsable() {
		if(this.idresponsable==null)this.idresponsable=new org.suinsit.apps.portalemp.Rrhempleado();
		  return this.idresponsable; 
	}
	
	public Mrpestado getIdmrpestado() {
		if(this.idmrpestado==null)this.idmrpestado=new org.suinsit.apps.mrp.Mrpestado();
		  return this.idmrpestado; 
	}
	
	public List<Mrprproductos> getSubmrprproductos() {
		if(this.submrprproductos==null)this.submrprproductos=new ArrayList<>(0);
		  return this.submrprproductos; 
	} 

}