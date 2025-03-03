package org.suinsit.apps.marketing;

import java.io.Serializable;
import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.marketing.Mktmpost;
import org.suinsit.apps.marketing.Mktrpublishpost;
import org.suinsit.apps.marketing.Mktrssagency;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "MKTRPOSTAGENCY" 
)
@Entidad (
	namespace = "marketing",
	type = "TABLE",
	name = "MKTRPOSTAGENCY",
	labelMonitor = "",
	pk = "idxmktrpostagency" 
)
public class Mktrpostagency implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxmktrpostagency",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxmktrpostagency;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTMPOST0",
		referencedColumnName = "IDXMKTMPOST",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktmpost idmktmpost;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMKTRSSAGENCY0",
		referencedColumnName = "IDXMKTRSSAGENCY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mktrssagency idmktrssagency;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idmktrpostagency" 
	)
	private List<Mktrpublishpost> submktrpublishpost; 

	public Mktmpost getIdmktmpost() {
		if(this.idmktmpost==null)this.idmktmpost=new org.suinsit.apps.marketing.Mktmpost();
		  return this.idmktmpost; 
	}
	
	public Mktrssagency getIdmktrssagency() {
		if(this.idmktrssagency==null)this.idmktrssagency=new org.suinsit.apps.marketing.Mktrssagency();
		  return this.idmktrssagency; 
	}
	
	public List<Mktrpublishpost> getSubmktrpublishpost() {
		if(this.submktrpublishpost==null)this.submktrpublishpost=new ArrayList<>(0);
		  return this.submktrpublishpost; 
	} 

}