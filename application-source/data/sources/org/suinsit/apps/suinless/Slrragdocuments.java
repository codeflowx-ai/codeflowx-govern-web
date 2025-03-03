package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slmragdocument;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLRRAGDOCUMENTS" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLRRAGDOCUMENTS",
	pk = "idxslrragdocuments" 
)
public class Slrragdocuments implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "indexed",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean indexed;
	@Column (
		name = "document",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BLOB" 
	)
	private Object document;
	@Id
	@Column (
		name = "idxslrragdocuments",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslrragdocuments;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLMRAGDOCUMENT0",
		referencedColumnName = "IDXSLMRAGDOCUMENT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slmragdocument idslmragdocument; 

	public Slmragdocument getIdslmragdocument() {
		if(this.idslmragdocument==null)this.idslmragdocument=new org.suinsit.apps.suinless.Slmragdocument();
		  return this.idslmragdocument; 
	} 

}