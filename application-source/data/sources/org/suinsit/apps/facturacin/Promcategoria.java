package org.suinsit.apps.facturacin;

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
import org.suinsit.apps.facturacin.Promcategoria;
import org.suinsit.apps.facturacin.Promproducto;
import org.suinsit.apps.partners.Ptrrcomision;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "PROMCATEGORIA" 
)
@Entidad (
	namespace = "facturacin",
	type = "TABLE",
	name = "PROMCATEGORIA",
	pk = "idxpromcategoria" 
)
public class Promcategoria implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxpromcategoria",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxpromcategoria;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "categoria",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "Categoría",
		type = "VARCHAR" 
	)
	private String categoria;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDPARENT0",
		referencedColumnName = "IDXPROMCATEGORIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Promcategoria idparent;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpromcategoria" 
	)
	private List<Promproducto> subpromproducto;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idpromcategoria" 
	)
	private List<Ptrrcomision> subptrrcomision; 

	public Promcategoria getIdparent() {
		if(this.idparent==null)this.idparent=new org.suinsit.apps.facturacin.Promcategoria();
		  return this.idparent; 
	}
	
	public List<Promproducto> getSubpromproducto() {
		if(this.subpromproducto==null)this.subpromproducto=new ArrayList<>(0);
		  return this.subpromproducto; 
	}
	
	public List<Ptrrcomision> getSubptrrcomision() {
		if(this.subptrrcomision==null)this.subptrrcomision=new ArrayList<>(0);
		  return this.subptrrcomision; 
	} 

}