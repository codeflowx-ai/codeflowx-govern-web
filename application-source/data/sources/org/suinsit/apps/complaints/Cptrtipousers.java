package org.suinsit.apps.complaints;

import java.io.Serializable;
import java.lang.Long;
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
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.complaints.Cpttipodenucia;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTRTIPOUSERS" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTRTIPOUSERS",
	labelMonitor = "",
	pk = "idxcptrtipousers" 
)
public class Cptrtipousers implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxcptrtipousers",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcptrtipousers;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCPTTIPODENUCIA0",
		referencedColumnName = "IDXCPTTIPODENUCIA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cpttipodenucia idcpttipodenucia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSSOUSUARIO0",
		referencedColumnName = "IDXSSOUSUARIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ssousuario idssousuario; 

	public Cpttipodenucia getIdcpttipodenucia() {
		if(this.idcpttipodenucia==null)this.idcpttipodenucia=new org.suinsit.apps.complaints.Cpttipodenucia();
		  return this.idcpttipodenucia; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}