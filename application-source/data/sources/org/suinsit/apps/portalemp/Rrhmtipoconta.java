package org.suinsit.apps.portalemp;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.enartframework.nocode.annotacion.ValidEnum;
import org.suinsit.apps.portalemp.Rrhempleado;
import org.suinsit.apps.portalemp.Rrhhctescontrato;
import org.suinsit.apps.portalemp.Rrhhrempcontra;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "RRHMTIPOCONTA" 
)
@Entidad (
	namespace = "portalemp",
	type = "TABLE",
	name = "RRHMTIPOCONTA",
	labelMonitor = "",
	pk = "idxrrhmtipoconta" 
)
public class Rrhmtipoconta implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "horasanuales",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer horasanuales;
	@Column (
		name = "bonificado",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean bonificado;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "descripcion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String descripcion;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "diassemana",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal diassemana;
	@Column (
		name = "diasvacaciones",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "INTEGER" 
	)
	private Integer diasvacaciones;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "domingohora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal domingohora;
	@Column (
		name = "ficharmanual",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ficharmanual;
	@Column (
		name = "ficharsinturno",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean ficharsinturno;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "horassemana",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal horassemana;
	@Id
	@Column (
		name = "idxrrhmtipoconta",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxrrhmtipoconta;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "jueveshora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal jueveshora;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "luneshora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal luneshora;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "marteshora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal marteshora;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "miercoleshora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal miercoleshora;
	@Column (
		name = "nosumfestivo",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean nosumfestivo;
	@Column (
		name = "nosumhorasvacas",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean nosumhorasvacas;
	@Column (
		name = "notificaremail",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean notificaremail;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "percentbonif",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal percentbonif;
	@Column (
		name = "redondeofichaje",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean redondeofichaje;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "refgobierno",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String refgobierno;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "sabadohora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal sabadohora;
	@Column (
		name = "sobreturnos",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sobreturnos;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "tipocontrato",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String tipocontrato;
	@Size (
		min = 0,
		max = 150 
	)
	@ValidEnum (
		enums = {
			"" 
		},
		message = "solamente admite lo valores: " 
	)
	@Column (
		name = "tipodias",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "ENUM_STRING" 
	)
	private String tipodias;
	@Size (
		min = 0,
		max = 16 
	)
	@Column (
		name = "vierneshora",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DECIMAL" 
	)
	private BigDecimal vierneshora;
	private boolean updatable;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhmtipoconta" 
	)
	private List<Rrhhrempcontra> subrrhhrempcontra;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhmtipoconta" 
	)
	private List<Rrhhctescontrato> subrrhhctescontrato;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idrrhmtipoconta" 
	)
	private List<Rrhempleado> subrrhempleado; 

	public List<Rrhhrempcontra> getSubrrhhrempcontra() {
		if(this.subrrhhrempcontra==null)this.subrrhhrempcontra=new ArrayList<>(0);
		  return this.subrrhhrempcontra; 
	}
	
	public List<Rrhhctescontrato> getSubrrhhctescontrato() {
		if(this.subrrhhctescontrato==null)this.subrrhhctescontrato=new ArrayList<>(0);
		  return this.subrrhhctescontrato; 
	}
	
	public List<Rrhempleado> getSubrrhempleado() {
		if(this.subrrhempleado==null)this.subrrhempleado=new ArrayList<>(0);
		  return this.subrrhempleado; 
	} 

}