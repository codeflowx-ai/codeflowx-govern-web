package org.suinsit.apps.mrp;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.facturacin.Erpempresa;
import org.suinsit.apps.mrp.Mrpexpedicion;
import org.suinsit.apps.mrp.Mrpmsequence;
import org.suinsit.apps.mrp.Mrpmtipoopera;
import org.suinsit.apps.mrp.Mrpmtipoperacion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MRPMTIPOPERACION" 
)
@Entidad (
	namespace = "mrp",
	type = "TABLE",
	name = "MRPMTIPOPERACION",
	labelMonitor = "TIPOOPERACION",
	pk = "idxmrpmtipoperacion" 
)
public class Mrpmtipoperacion implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "creaordenreparar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean creaordenreparar;
	@Column (
		name = "crearlote",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean crearlote;
	@Column (
		name = "detallaroperacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean detallaroperacion;
	@Column (
		name = "entregaparcial",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean entregaparcial;
	@Id
	@Column (
		name = "idxmrpmtipoperacion",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmrpmtipoperacion;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "prefijo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String prefijo;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			",Confirmar,Manual,antes fecha" 
		},
		message = "solamente admite lo valores: ,Confirmar,Manual,antes fecha" 
	)
	@Column (
		name = "reservar",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String reservar;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipooperacion",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipooperacion;
	@Column (
		name = "uselote",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean uselote;
	private boolean updatable;
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
		name = "IDMRPRETORNO0",
		referencedColumnName = "IDXMRPMTIPOPERACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpmtipoperacion idmrpretorno;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMRPMTIPOOPERA0",
		referencedColumnName = "IDXMRPMTIPOOPERA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mrpmtipoopera idmrpmtipoopera;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmrpmtipoperacion" 
	)
	private List<Mrpexpedicion> submrpexpedicion; 

	public Erpempresa getIdcompany() {
		if(this.idcompany==null)this.idcompany=new org.suinsit.apps.facturacin.Erpempresa();
		  return this.idcompany; 
	}
	
	public Mrpmsequence getIdmrpmsequence() {
		if(this.idmrpmsequence==null)this.idmrpmsequence=new org.suinsit.apps.mrp.Mrpmsequence();
		  return this.idmrpmsequence; 
	}
	
	public Mrpmtipoperacion getIdmrpretorno() {
		if(this.idmrpretorno==null)this.idmrpretorno=new org.suinsit.apps.mrp.Mrpmtipoperacion();
		  return this.idmrpretorno; 
	}
	
	public Mrpmtipoopera getIdmrpmtipoopera() {
		if(this.idmrpmtipoopera==null)this.idmrpmtipoopera=new org.suinsit.apps.mrp.Mrpmtipoopera();
		  return this.idmrpmtipoopera; 
	}
	
	public List<Mrpexpedicion> getSubmrpexpedicion() {
		if(this.submrpexpedicion==null)this.submrpexpedicion=new ArrayList<>(0);
		  return this.submrpexpedicion; 
	} 

}