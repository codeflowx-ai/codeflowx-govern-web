package org.suinsit.apps.subvenciones;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
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
import org.suinsit.apps.facturacin.Erpfactura;
import org.suinsit.apps.subvenciones.Agentedig;
import org.suinsit.apps.subvenciones.Catkitdigital;
import org.suinsit.apps.subvenciones.Kitdigital;
import org.suinsit.apps.subvenciones.Kitestadojust;
import org.suinsit.apps.subvenciones.Kitestadosub;
import org.suinsit.apps.subvenciones.Kitrbonocatprod;
import org.suinsit.apps.subvenciones.Kitrbonosubsan;
import org.suinsit.apps.subvenciones.Kitrhorascat;
import org.suinsit.apps.subvenciones.Subsolictudes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "KITRBONOCAT" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "KITRBONOCAT",
	labelMonitor = "",
	pk = "idxkitrbonocat" 
)
public class Kitrbonocat implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "aceptajust1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date aceptajust1;
	@Column (
		name = "aceptajust2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date aceptajust2;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cbro1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cbro1;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "cbro2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal cbro2;
	@Column (
		name = "comentarios",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String comentarios;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "couseralta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String couseralta;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "cousermodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "VARCHAR" 
	)
	private String cousermodif;
	@Column (
		name = "fecavisosub",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecavisosub;
	@Column (
		name = "feccbro1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccbro1;
	@Column (
		name = "feccbro2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccbro2;
	@Column (
		name = "fechadesa",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechadesa;
	@Column (
		name = "fechafirma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechafirma;
	@Column (
		name = "fechafirmared",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechafirmared;
	@Column (
		name = "fechaprod",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fechaprod;
	@Column (
		name = "feclimitefirma",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feclimitefirma;
	@Column (
		name = "fecpagiva",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecpagiva;
	@Column (
		name = "fecsubsanado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecsubsanado;
	@Id
	@Column (
		name = "idxkitrbonocat",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxkitrbonocat;
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
		name = "numacuerdo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String numacuerdo;
	@Column (
		name = "pagadoiva",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean pagadoiva;
	@Column (
		name = "tmalta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmalta;
	@Column (
		name = "tmmodif",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp tmmodif;
	@Column (
		name = "unidades",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer unidades;
	@Column (
		name = "vtojustifica1",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date vtojustifica1;
	@Column (
		name = "vtojustifica2",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date vtojustifica2;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCATKITDIGITAL0",
		referencedColumnName = "IDXCATKITDIGITAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Catkitdigital idcatkitdigital;
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
		name = "IDAGENTEDIG0",
		referencedColumnName = "IDXAGENTEDIG",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Agentedig idagentedig;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDKITESTADOJUST0",
		referencedColumnName = "IDXKITESTADOJUST",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitestadojust idkitestadojust;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDKITESTADOSUB0",
		referencedColumnName = "IDXKITESTADOSUB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitestadosub idkitestadosub;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSUBSOLICTUDES0",
		referencedColumnName = "IDXSUBSOLICTUDES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Subsolictudes idsubsolictudes;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDKITDIGITAL0",
		referencedColumnName = "IDXKITDIGITAL",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitdigital idkitdigital;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idkitrbonocat" 
	)
	private List<Kitrbonocatprod> subkitrbonocatprod;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idkitrbonocat" 
	)
	private List<Kitrhorascat> subkitrhorascat;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idkitrbonocat" 
	)
	private List<Kitrbonosubsan> subkitrbonosubsan; 

	public Catkitdigital getIdcatkitdigital() {
		if(this.idcatkitdigital==null)this.idcatkitdigital=new org.suinsit.apps.subvenciones.Catkitdigital();
		  return this.idcatkitdigital; 
	}
	
	public Erpfactura getIderpfactura() {
		if(this.iderpfactura==null)this.iderpfactura=new org.suinsit.apps.facturacin.Erpfactura();
		  return this.iderpfactura; 
	}
	
	public Agentedig getIdagentedig() {
		if(this.idagentedig==null)this.idagentedig=new org.suinsit.apps.subvenciones.Agentedig();
		  return this.idagentedig; 
	}
	
	public Kitestadojust getIdkitestadojust() {
		if(this.idkitestadojust==null)this.idkitestadojust=new org.suinsit.apps.subvenciones.Kitestadojust();
		  return this.idkitestadojust; 
	}
	
	public Kitestadosub getIdkitestadosub() {
		if(this.idkitestadosub==null)this.idkitestadosub=new org.suinsit.apps.subvenciones.Kitestadosub();
		  return this.idkitestadosub; 
	}
	
	public Subsolictudes getIdsubsolictudes() {
		if(this.idsubsolictudes==null)this.idsubsolictudes=new org.suinsit.apps.subvenciones.Subsolictudes();
		  return this.idsubsolictudes; 
	}
	
	public Kitdigital getIdkitdigital() {
		if(this.idkitdigital==null)this.idkitdigital=new org.suinsit.apps.subvenciones.Kitdigital();
		  return this.idkitdigital; 
	}
	
	public List<Kitrbonocatprod> getSubkitrbonocatprod() {
		if(this.subkitrbonocatprod==null)this.subkitrbonocatprod=new ArrayList<>(0);
		  return this.subkitrbonocatprod; 
	}
	
	public List<Kitrhorascat> getSubkitrhorascat() {
		if(this.subkitrhorascat==null)this.subkitrhorascat=new ArrayList<>(0);
		  return this.subkitrhorascat; 
	}
	
	public List<Kitrbonosubsan> getSubkitrbonosubsan() {
		if(this.subkitrbonosubsan==null)this.subkitrbonosubsan=new ArrayList<>(0);
		  return this.subkitrbonosubsan; 
	} 

}