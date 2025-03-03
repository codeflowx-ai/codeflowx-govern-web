package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Integer;
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
import org.suinsit.apps.rag.Ragproyecto;
import org.suinsit.apps.suinless.ComSuinsitAppsSuinlessSleschatscenario;
import org.suinsit.apps.suinless.Slesadvancedbehavior;
import org.suinsit.apps.suinless.Sleschatbusinessintegration;
import org.suinsit.apps.suinless.Sleschatbusinesskpi;
import org.suinsit.apps.suinless.Sleschatcognitive;
import org.suinsit.apps.suinless.Sleschatconversation;
import org.suinsit.apps.suinless.Sleschatcrmintegration;
import org.suinsit.apps.suinless.Sleschaterpintegration;
import org.suinsit.apps.suinless.Sleschatfeedbackadv;
import org.suinsit.apps.suinless.Sleschatflowtemplate;
import org.suinsit.apps.suinless.Sleschatlearning;
import org.suinsit.apps.suinless.Sleschatmetrics;
import org.suinsit.apps.suinless.Sleschatpattern;
import org.suinsit.apps.suinless.Sleschatpersonalization;
import org.suinsit.apps.suinless.Sleschatpromptopt;
import org.suinsit.apps.suinless.Slescomplexdialogue;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESCHATBOT" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESCHATBOT",
	labelMonitor = "CHATBOT",
	pk = "idxsleschatbot" 
)
public class Sleschatbot implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxsleschatbot",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxsleschatbot;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "chatbotname",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String chatbotname;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "accesstoken",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String accesstoken;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "businessarea",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String businessarea;
	@Column (
		name = "languages",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "TEXT_ARRAY" 
	)
	private List languages;
	@Column (
		name = "humanhandoffthreshold",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "INTEGER" 
	)
	private Integer humanhandoffthreshold;
	@Column (
		name = "configuration",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String configuration;
	@Column (
		name = "embedconfig",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String embedconfig;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESRAGPROJECT",
		referencedColumnName = "IDXSLESRAGPROJECT",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Ragproyecto idslesragproject;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Slesadvancedbehavior> subslesadvancedbehavior;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatbusinessintegration> subsleschatbusinessintegration;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatbusinesskpi> subsleschatbusinesskpi;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatcognitive> subsleschatcognitive;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatconversation> subsleschatconversation;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatcrmintegration> subsleschatcrmintegration;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschaterpintegration> subsleschaterpintegration;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatfeedbackadv> subsleschatfeedbackadv;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatflowtemplate> subsleschatflowtemplate;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatlearning> subsleschatlearning;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatmetrics> subsleschatmetrics;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatpattern> subsleschatpattern;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatpersonalization> subsleschatpersonalization;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Sleschatpromptopt> subsleschatpromptopt;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<ComSuinsitAppsSuinlessSleschatscenario> subsleschatscenario;
	@OneToMany (
		cascade = javax.persistence.CascadeType.ALL,
		mappedBy = "idxsleschatbot" 
	)
	private List<Slescomplexdialogue> subslescomplexdialogue; 

	public Ragproyecto getIdslesragproject() {
		if(this.idslesragproject==null)this.idslesragproject=new org.suinsit.apps.rag.Ragproyecto();
		  return this.idslesragproject; 
	}
	
	public List<Slesadvancedbehavior> getSubslesadvancedbehavior() {
		if(this.subslesadvancedbehavior==null)this.subslesadvancedbehavior=new ArrayList<>(0);
		  return this.subslesadvancedbehavior; 
	}
	
	public List<Sleschatbusinessintegration> getSubsleschatbusinessintegration() {
		if(this.subsleschatbusinessintegration==null)this.subsleschatbusinessintegration=new ArrayList<>(0);
		  return this.subsleschatbusinessintegration; 
	}
	
	public List<Sleschatbusinesskpi> getSubsleschatbusinesskpi() {
		if(this.subsleschatbusinesskpi==null)this.subsleschatbusinesskpi=new ArrayList<>(0);
		  return this.subsleschatbusinesskpi; 
	}
	
	public List<Sleschatcognitive> getSubsleschatcognitive() {
		if(this.subsleschatcognitive==null)this.subsleschatcognitive=new ArrayList<>(0);
		  return this.subsleschatcognitive; 
	}
	
	public List<Sleschatconversation> getSubsleschatconversation() {
		if(this.subsleschatconversation==null)this.subsleschatconversation=new ArrayList<>(0);
		  return this.subsleschatconversation; 
	}
	
	public List<Sleschatcrmintegration> getSubsleschatcrmintegration() {
		if(this.subsleschatcrmintegration==null)this.subsleschatcrmintegration=new ArrayList<>(0);
		  return this.subsleschatcrmintegration; 
	}
	
	public List<Sleschaterpintegration> getSubsleschaterpintegration() {
		if(this.subsleschaterpintegration==null)this.subsleschaterpintegration=new ArrayList<>(0);
		  return this.subsleschaterpintegration; 
	}
	
	public List<Sleschatfeedbackadv> getSubsleschatfeedbackadv() {
		if(this.subsleschatfeedbackadv==null)this.subsleschatfeedbackadv=new ArrayList<>(0);
		  return this.subsleschatfeedbackadv; 
	}
	
	public List<Sleschatflowtemplate> getSubsleschatflowtemplate() {
		if(this.subsleschatflowtemplate==null)this.subsleschatflowtemplate=new ArrayList<>(0);
		  return this.subsleschatflowtemplate; 
	}
	
	public List<Sleschatlearning> getSubsleschatlearning() {
		if(this.subsleschatlearning==null)this.subsleschatlearning=new ArrayList<>(0);
		  return this.subsleschatlearning; 
	}
	
	public List<Sleschatmetrics> getSubsleschatmetrics() {
		if(this.subsleschatmetrics==null)this.subsleschatmetrics=new ArrayList<>(0);
		  return this.subsleschatmetrics; 
	}
	
	public List<Sleschatpattern> getSubsleschatpattern() {
		if(this.subsleschatpattern==null)this.subsleschatpattern=new ArrayList<>(0);
		  return this.subsleschatpattern; 
	}
	
	public List<Sleschatpersonalization> getSubsleschatpersonalization() {
		if(this.subsleschatpersonalization==null)this.subsleschatpersonalization=new ArrayList<>(0);
		  return this.subsleschatpersonalization; 
	}
	
	public List<Sleschatpromptopt> getSubsleschatpromptopt() {
		if(this.subsleschatpromptopt==null)this.subsleschatpromptopt=new ArrayList<>(0);
		  return this.subsleschatpromptopt; 
	}
	
	public List<ComSuinsitAppsSuinlessSleschatscenario> getSubsleschatscenario() {
		if(this.subsleschatscenario==null)this.subsleschatscenario=new ArrayList<>(0);
		  return this.subsleschatscenario; 
	}
	
	public List<Slescomplexdialogue> getSubslescomplexdialogue() {
		if(this.subslescomplexdialogue==null)this.subslescomplexdialogue=new ArrayList<>(0);
		  return this.subslescomplexdialogue; 
	} 

}