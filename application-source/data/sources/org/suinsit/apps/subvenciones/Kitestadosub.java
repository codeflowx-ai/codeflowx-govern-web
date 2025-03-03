package org.suinsit.apps.subvenciones;

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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.bpmn.Bpmmproces;
import org.suinsit.apps.subvenciones.Kitrbonocat;
import org.suinsit.apps.subvenciones.Kitrbonosubsan;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "KITESTADOSUB" 
)
@Entidad (
	namespace = "subvenciones",
	type = "TABLE",
	name = "KITESTADOSUB",
	labelMonitor = "ESTADOSUB",
	pk = "idxkitestadosub" 
)
public class Kitestadosub implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "backcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String backcolor;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "estadosub",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String estadosub;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "icono",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String icono;
	@Id
	@Column (
		name = "idxkitestadosub",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxkitestadosub;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "textcolor",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String textcolor;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDBPMMPROCES0",
		referencedColumnName = "IDXBPMMPROCES",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Bpmmproces idbpmmproces;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idkitestadosub" 
	)
	private List<Kitrbonocat> subkitrbonocat;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idkitestadosub" 
	)
	private List<Kitrbonosubsan> subkitrbonosubsan; 

	public Bpmmproces getIdbpmmproces() {
		if(this.idbpmmproces==null)this.idbpmmproces=new org.suinsit.apps.bpmn.Bpmmproces();
		  return this.idbpmmproces; 
	}
	
	public List<Kitrbonocat> getSubkitrbonocat() {
		if(this.subkitrbonocat==null)this.subkitrbonocat=new ArrayList<>(0);
		  return this.subkitrbonocat; 
	}
	
	public List<Kitrbonosubsan> getSubkitrbonosubsan() {
		if(this.subkitrbonosubsan==null)this.subkitrbonosubsan=new ArrayList<>(0);
		  return this.subkitrbonosubsan; 
	} 

}