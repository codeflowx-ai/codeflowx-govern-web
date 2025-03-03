package org.suinsit.apps.complaints;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Cipher;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.admin.Ssousuario;
import org.suinsit.apps.complaints.Cptcomplaint;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "CPTMESSAGE" 
)
@Entidad (
	namespace = "complaints",
	type = "TABLE",
	name = "CPTMESSAGE",
	labelMonitor = "",
	cipher = true,
	keyCipher = "ZEgseT5onKUbUqGpRROk4QEWi",
	fieldValueKeyCipher = "",
	pk = "idxcptmessage" 
)
public class Cptmessage implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "asunto",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "VARCHAR" 
	)
	private String asunto;
	@Column (
		name = "denunciante",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean denunciante;
	@Column (
		name = "alta",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp alta;
	@Id
	@Column (
		name = "idxcptmessage",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxcptmessage;
	@Column (
		name = "lectura",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "TIMESTAMP" 
	)
	private Timestamp lectura;
	@NotNull
	@NotBlank
	@Column (
		name = "mensaje",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "CLOB" 
	)
	@Cipher
	private String mensaje;
	@Column (
		name = "sendemail",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = false,
		filter = true,
		label = "",
		type = "BOOLEAN" 
	)
	private boolean sendemail;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCPTCOMPLAINT0",
		referencedColumnName = "IDXCPTCOMPLAINT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Cptcomplaint idcptcomplaint;
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

	public Cptcomplaint getIdcptcomplaint() {
		if(this.idcptcomplaint==null)this.idcptcomplaint=new org.suinsit.apps.complaints.Cptcomplaint();
		  return this.idcptcomplaint; 
	}
	
	public Ssousuario getIdssousuario() {
		if(this.idssousuario==null)this.idssousuario=new org.suinsit.apps.admin.Ssousuario();
		  return this.idssousuario; 
	} 

}