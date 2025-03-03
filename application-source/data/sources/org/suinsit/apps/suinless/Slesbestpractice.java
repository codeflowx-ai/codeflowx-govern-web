package org.suinsit.apps.suinless;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
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
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import org.suinsit.apps.suinless.Slesknowledgebase;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (
	name = "SLESBESTPRACTICE" 
)
@Entidad (
	namespace = "suinless",
	type = "TABLE",
	name = "SLESBESTPRACTICE",
	labelMonitor = "BEST_PRACTICE",
	pk = "idxslesbestpractice" 
)
public class Slesbestpractice implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Id
	@Column (
		name = "idxslesbestpractice",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = false,
		filter = true,
		type = "LONG" 
	)
	private Long idxslesbestpractice;
	@NotNull
	@NotBlank
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "title",
		nullable = false 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String title;
	@Column (
		name = "description",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "CLOB" 
	)
	private String description;
	@Size (
		min = 0,
		max = 100 
	)
	@Column (
		name = "category",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "VARCHAR" 
	)
	private String category;
	@Column (
		name = "usecase",
		nullable = true 
	)
	@Field (
		criteria = true,
		auditar = true,
		filter = true,
		type = "CLOB" 
	)
	private String usecase;
	@Column (
		name = "implementation",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String implementation;
	@Column (
		name = "metrics",
		nullable = true 
	)
	@Field (
		criteria = false,
		auditar = true,
		filter = false,
		type = "JSONB" 
	)
	private String metrics;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSLESKNOWLEDGEBASE",
		referencedColumnName = "IDXSLESKNOWLEDGEBASE",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Slesknowledgebase idslesknowledgebase; 

	public Slesknowledgebase getIdslesknowledgebase() {
		if(this.idslesknowledgebase==null)this.idslesknowledgebase=new org.suinsit.apps.suinless.Slesknowledgebase();
		  return this.idslesknowledgebase; 
	} 

}