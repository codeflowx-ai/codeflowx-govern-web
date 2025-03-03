package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.ComSuinsitAppsSuinlessSleschatrecommender;
import org.suinsit.apps.suinless.Sleschatbot;
import org.suinsit.apps.suinless.Sleschatcontext;
import org.suinsit.apps.suinless.Sleschatinteraction;
import org.suinsit.apps.suinless.Sleschatordertracking;
import org.suinsit.apps.suinless.Sleschatproductrec;
import org.suinsit.apps.suinless.Sleschatsalesassist;
import org.suinsit.apps.suinless.Sleschemotional;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESCHATCONVERSATION" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATCONVERSATION",
	labelMonitor = "CHAT_CONVERSATION",
	pk = "idxsleschatconversation" 
)
public class Sleschatconversation implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "context",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String context;
	@Column (
		name = "endtime",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp endtime;
	@Id
	@Column (
		name = "idxsleschatconversation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatconversation;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "language",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String language;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "sessionid",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String sessionid;
	@Column (
		name = "starttime",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TIMESTAMP" 
	)
	private Timestamp starttime;
	@Column (
		name = "userinformation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String userinformation;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDXSLESCHATBOT",
		referencedColumnName = "IDXSLESCHATBOT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Sleschatbot idxsleschatbot;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<Sleschatcontext> subsleschatcontext;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<Sleschatinteraction> subsleschatinteraction;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<Sleschatordertracking> subsleschatordertracking;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<Sleschatproductrec> subsleschatproductrec;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<ComSuinsitAppsSuinlessSleschatrecommender> subsleschatrecommender;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<Sleschatsalesassist> subsleschatsalesassist;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatconversation" 
	)
	private List<Sleschemotional> subsleschemotional; 

	public Sleschatbot getIdxsleschatbot() {
		if(this.idxsleschatbot==null)this.idxsleschatbot=new org.suinsit.apps.suinless.Sleschatbot();
		  return this.idxsleschatbot; 
	}
	
	public List<Sleschatcontext> getSubsleschatcontext() {
		if(this.subsleschatcontext==null)this.subsleschatcontext=new ArrayList<>(0);
		  return this.subsleschatcontext; 
	}
	
	public List<Sleschatinteraction> getSubsleschatinteraction() {
		if(this.subsleschatinteraction==null)this.subsleschatinteraction=new ArrayList<>(0);
		  return this.subsleschatinteraction; 
	}
	
	public List<Sleschatordertracking> getSubsleschatordertracking() {
		if(this.subsleschatordertracking==null)this.subsleschatordertracking=new ArrayList<>(0);
		  return this.subsleschatordertracking; 
	}
	
	public List<Sleschatproductrec> getSubsleschatproductrec() {
		if(this.subsleschatproductrec==null)this.subsleschatproductrec=new ArrayList<>(0);
		  return this.subsleschatproductrec; 
	}
	
	public List<ComSuinsitAppsSuinlessSleschatrecommender> getSubsleschatrecommender() {
		if(this.subsleschatrecommender==null)this.subsleschatrecommender=new ArrayList<>(0);
		  return this.subsleschatrecommender; 
	}
	
	public List<Sleschatsalesassist> getSubsleschatsalesassist() {
		if(this.subsleschatsalesassist==null)this.subsleschatsalesassist=new ArrayList<>(0);
		  return this.subsleschatsalesassist; 
	}
	
	public List<Sleschemotional> getSubsleschemotional() {
		if(this.subsleschemotional==null)this.subsleschemotional=new ArrayList<>(0);
		  return this.subsleschemotional; 
	} 

}