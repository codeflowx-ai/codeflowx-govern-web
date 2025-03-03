package org.suinsit.apps.subvenciones;

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
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.subvenciones.Kitestadosub;
import org.suinsit.apps.subvenciones.Kitrbonocat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "KITRBONOSUBSAN" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "KITRBONOSUBSAN",
	labelMonitor = "",
	pk = "idxkitrbonosubsan" 
)
public class Kitrbonosubsan implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "feccomunica",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date feccomunica;
	@Column (
		name = "fecsubsana",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "DATE" 
	)
	private Date fecsubsana;
	@Id
	@Column (
		name = "idxkitrbonosubsan",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxkitrbonosubsan;
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
		type = "SEQUENCE_PREFIX" 
	)
	@Sequence (
		name = "KITRBONOSUBSAN_REFERENCIA",
		prefix = "SUB",
		mask = "000000",
		addYear = true 
	)
	private String referencia;
	@Column (
		name = "subsanacion",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	private String subsanacion;
	private boolean updatable;
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
		name = "IDKITRBONOCAT0",
		referencedColumnName = "IDXKITRBONOCAT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Kitrbonocat idkitrbonocat; 

	public Kitestadosub getIdkitestadosub() {
		if(this.idkitestadosub==null)this.idkitestadosub=new org.suinsit.apps.subvenciones.Kitestadosub();
		  return this.idkitestadosub; 
	}
	
	public Kitrbonocat getIdkitrbonocat() {
		if(this.idkitrbonocat==null)this.idkitrbonocat=new org.suinsit.apps.subvenciones.Kitrbonocat();
		  return this.idkitrbonocat; 
	} 

}