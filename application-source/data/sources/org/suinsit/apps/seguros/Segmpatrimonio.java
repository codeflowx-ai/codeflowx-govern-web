package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Sequence;
import org.suinsit.apps.admin.Mpais;
import org.suinsit.apps.admin.Mprovincia;
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.seguros.Segmbandera;
import org.suinsit.apps.seguros.Segmclasepatri;
import org.suinsit.apps.seguros.Segmnavegacion;
import org.suinsit.apps.seguros.Segmpatriamb;
import org.suinsit.apps.seguros.Segmpoliza;
import org.suinsit.apps.seguros.Segmregimenpatri;
import org.suinsit.apps.seguros.Segmtipopatr;
import org.suinsit.apps.seguros.Segmusopatr;
import org.suinsit.apps.seguros.Segmzonapatri;

@Entity
@Table (
	name = "SEGMPATRIMONIO" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmpatrimonio" 
)
public class Segmpatrimonio implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ALTA",
		nullable = true 
	)
	private Date alta;
	@Column (
		name = "ASCENSORES",
		nullable = true 
	)
	private Integer ascensores;
	@Column (
		name = "ATRAQUE",
		nullable = true 
	)
	private String atraque;
	@Column (
		name = "BAJA",
		nullable = true 
	)
	private Date baja;
	@Column (
		name = "BASTIDOR",
		nullable = true 
	)
	private String bastidor;
	@Column (
		name = "CALADO",
		nullable = true 
	)
	private BigDecimal calado;
	@Column (
		name = "CATASTRO",
		nullable = true 
	)
	private String catastro;
	@Column (
		name = "CILINDRADA",
		nullable = true 
	)
	private Integer cilindrada;
	@Column (
		name = "CODPATRIMONIO",
		nullable = false 
	)
	@Sequence (
		name = "SEGMPATRIMONIO_CODPATRIMONIO",
		prefix = "PTR",
		mask = "0000000000",
		addYear = true 
	)
	private String codpatrimonio;
	@Column (
		name = "COLOR",
		nullable = true 
	)
	private String color;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Column (
		name = "DIRECCION",
		nullable = true 
	)
	private String direccion;
	@Column (
		name = "ESLORA",
		nullable = true 
	)
	private BigDecimal eslora;
	@Column (
		name = "FABRICACION",
		nullable = true 
	)
	private Date fabricacion;
	@Column (
		name = "FECCADUCIDAD",
		nullable = true 
	)
	private Date feccaducidad;
	@Column (
		name = "FECDESGUACE",
		nullable = true 
	)
	private Date fecdesguace;
	@Column (
		name = "FECFIN",
		nullable = true 
	)
	private Timestamp fecfin;
	@Column (
		name = "FECINICIO",
		nullable = true 
	)
	private Timestamp fecinicio;
	@Column (
		name = "FECINSPECCION",
		nullable = true 
	)
	private Date fecinspeccion;
	@Column (
		name = "FECITV",
		nullable = true 
	)
	private Date fecitv;
	@Column (
		name = "GARAJES",
		nullable = true 
	)
	private Integer garajes;
	@Column (
		name = "IDENTIFICADOR",
		nullable = true 
	)
	private String identificador;
	@Id
	@Column (
		name = "IDXSEGMPATRIMONIO",
		nullable = true 
	)
	private Long idxsegmpatrimonio;
	@Column (
		name = "IMAGEN",
		nullable = true 
	)
	private Object imagen;
	@Column (
		name = "LOCALES",
		nullable = true 
	)
	private Integer locales;
	@Column (
		name = "MANGA",
		nullable = true 
	)
	private BigDecimal manga;
	@Column (
		name = "MARCA",
		nullable = true 
	)
	private String marca;
	@Column (
		name = "MATRICULA",
		nullable = true 
	)
	private String matricula;
	@Column (
		name = "MATRICULACION",
		nullable = true 
	)
	private Date matriculacion;
	@Column (
		name = "MODELO",
		nullable = true 
	)
	private String modelo;
	@Column (
		name = "NOMBRE",
		nullable = true 
	)
	private String nombre;
	@Column (
		name = "PASAJEROS",
		nullable = true 
	)
	private Integer pasajeros;
	@Column (
		name = "PESOMA",
		nullable = true 
	)
	private Integer pesoma;
	@Column (
		name = "PLANTAS",
		nullable = true 
	)
	private Integer plantas;
	@Column (
		name = "PLAZAS",
		nullable = true 
	)
	private Integer plazas;
	@Column (
		name = "POBLACION",
		nullable = true 
	)
	private String poblacion;
	@Column (
		name = "POTENCIA",
		nullable = true 
	)
	private Integer potencia;
	@Column (
		name = "PUERTAS",
		nullable = true 
	)
	private Integer puertas;
	@Column (
		name = "REMOLQUE",
		nullable = true 
	)
	private boolean remolque;
	@Column (
		name = "SUPERFICIE",
		nullable = true 
	)
	private Integer superficie;
	@Column (
		name = "SUPERFICIETOTAL",
		nullable = true 
	)
	private Integer superficietotal;
	@Column (
		name = "SUPERJARDIN",
		nullable = true 
	)
	private Integer superjardin;
	@Column (
		name = "TRIPULANTES",
		nullable = true 
	)
	private Integer tripulantes;
	@Column (
		name = "VALORACION",
		nullable = true 
	)
	private BigDecimal valoracion;
	@Column (
		name = "VIVIENDAS",
		nullable = true 
	)
	private Integer viviendas;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMTIPOPATR0",
		referencedColumnName = "IDXSEGMTIPOPATR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmtipopatr idsegmtipopatr;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMCLASEPATRI0",
		referencedColumnName = "IDXSEGMCLASEPATRI",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmclasepatri idsegmclasepatri;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMUSOPATR0",
		referencedColumnName = "IDXSEGMUSOPATR",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmusopatr idsegmusopatr;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPAIS0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mpais idmpais;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDMPROVINCIA0",
		referencedColumnName = "IDX",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Mprovincia idmprovincia;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPATRIAMB0",
		referencedColumnName = "IDXSEGMPATRIAMB",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmpatriamb idsegmpatriamb;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMREGIMENPATRI0",
		referencedColumnName = "IDXSEGMREGIMENPATRI",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmregimenpatri idsegmregimenpatri;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMZONAPATRI0",
		referencedColumnName = "IDXSEGMZONAPATRI",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmzonapatri idsegmzonapatri;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMNAVEGACION0",
		referencedColumnName = "IDXSEGMNAVEGACION",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmnavegacion idsegmnavegacion;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMBANDERA0",
		referencedColumnName = "IDXSEGMBANDERA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmbandera idsegmbandera;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDCRMEMPRESA0",
		referencedColumnName = "IDXCRMEMPRESA",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Crmempresa idcrmempresa;
	private List<Segmpoliza> subsegmpoliza; 

	public void setAlta(
		Date var
	) {
		this.alta=var; 
	}
	
	public Date getAlta() {
		return this.alta; 
	}
	
	public void setAscensores(
		Integer var
	) {
		this.ascensores=var; 
	}
	
	public Integer getAscensores() {
		return this.ascensores; 
	}
	
	public void setAtraque(
		String var
	) {
		this.atraque=var; 
	}
	
	public String getAtraque() {
		return this.atraque; 
	}
	
	public void setBaja(
		Date var
	) {
		this.baja=var; 
	}
	
	public Date getBaja() {
		return this.baja; 
	}
	
	public void setBastidor(
		String var
	) {
		this.bastidor=var; 
	}
	
	public String getBastidor() {
		return this.bastidor; 
	}
	
	public void setCalado(
		BigDecimal var
	) {
		this.calado=var; 
	}
	
	public BigDecimal getCalado() {
		return this.calado; 
	}
	
	public void setCatastro(
		String var
	) {
		this.catastro=var; 
	}
	
	public String getCatastro() {
		return this.catastro; 
	}
	
	public void setCilindrada(
		Integer var
	) {
		this.cilindrada=var; 
	}
	
	public Integer getCilindrada() {
		return this.cilindrada; 
	}
	
	public void setCodpatrimonio(
		String var
	) {
		this.codpatrimonio=var; 
	}
	
	public String getCodpatrimonio() {
		return this.codpatrimonio; 
	}
	
	public void setColor(
		String var
	) {
		this.color=var; 
	}
	
	public String getColor() {
		return this.color; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setDireccion(
		String var
	) {
		this.direccion=var; 
	}
	
	public String getDireccion() {
		return this.direccion; 
	}
	
	public void setEslora(
		BigDecimal var
	) {
		this.eslora=var; 
	}
	
	public BigDecimal getEslora() {
		return this.eslora; 
	}
	
	public void setFabricacion(
		Date var
	) {
		this.fabricacion=var; 
	}
	
	public Date getFabricacion() {
		return this.fabricacion; 
	}
	
	public void setFeccaducidad(
		Date var
	) {
		this.feccaducidad=var; 
	}
	
	public Date getFeccaducidad() {
		return this.feccaducidad; 
	}
	
	public void setFecdesguace(
		Date var
	) {
		this.fecdesguace=var; 
	}
	
	public Date getFecdesguace() {
		return this.fecdesguace; 
	}
	
	public void setFecfin(
		Timestamp var
	) {
		this.fecfin=var; 
	}
	
	public Timestamp getFecfin() {
		return this.fecfin; 
	}
	
	public void setFecinicio(
		Timestamp var
	) {
		this.fecinicio=var; 
	}
	
	public Timestamp getFecinicio() {
		return this.fecinicio; 
	}
	
	public void setFecinspeccion(
		Date var
	) {
		this.fecinspeccion=var; 
	}
	
	public Date getFecinspeccion() {
		return this.fecinspeccion; 
	}
	
	public void setFecitv(
		Date var
	) {
		this.fecitv=var; 
	}
	
	public Date getFecitv() {
		return this.fecitv; 
	}
	
	public void setGarajes(
		Integer var
	) {
		this.garajes=var; 
	}
	
	public Integer getGarajes() {
		return this.garajes; 
	}
	
	public void setIdentificador(
		String var
	) {
		this.identificador=var; 
	}
	
	public String getIdentificador() {
		return this.identificador; 
	}
	
	public void setIdxsegmpatrimonio(
		Long var
	) {
		this.idxsegmpatrimonio=var; 
	}
	
	public Long getIdxsegmpatrimonio() {
		return this.idxsegmpatrimonio; 
	}
	
	public void setImagen(
		Object var
	) {
		this.imagen=var; 
	}
	
	public Object getImagen() {
		return this.imagen; 
	}
	
	public void setLocales(
		Integer var
	) {
		this.locales=var; 
	}
	
	public Integer getLocales() {
		return this.locales; 
	}
	
	public void setManga(
		BigDecimal var
	) {
		this.manga=var; 
	}
	
	public BigDecimal getManga() {
		return this.manga; 
	}
	
	public void setMarca(
		String var
	) {
		this.marca=var; 
	}
	
	public String getMarca() {
		return this.marca; 
	}
	
	public void setMatricula(
		String var
	) {
		this.matricula=var; 
	}
	
	public String getMatricula() {
		return this.matricula; 
	}
	
	public void setMatriculacion(
		Date var
	) {
		this.matriculacion=var; 
	}
	
	public Date getMatriculacion() {
		return this.matriculacion; 
	}
	
	public void setModelo(
		String var
	) {
		this.modelo=var; 
	}
	
	public String getModelo() {
		return this.modelo; 
	}
	
	public void setNombre(
		String var
	) {
		this.nombre=var; 
	}
	
	public String getNombre() {
		return this.nombre; 
	}
	
	public void setPasajeros(
		Integer var
	) {
		this.pasajeros=var; 
	}
	
	public Integer getPasajeros() {
		return this.pasajeros; 
	}
	
	public void setPesoma(
		Integer var
	) {
		this.pesoma=var; 
	}
	
	public Integer getPesoma() {
		return this.pesoma; 
	}
	
	public void setPlantas(
		Integer var
	) {
		this.plantas=var; 
	}
	
	public Integer getPlantas() {
		return this.plantas; 
	}
	
	public void setPlazas(
		Integer var
	) {
		this.plazas=var; 
	}
	
	public Integer getPlazas() {
		return this.plazas; 
	}
	
	public void setPoblacion(
		String var
	) {
		this.poblacion=var; 
	}
	
	public String getPoblacion() {
		return this.poblacion; 
	}
	
	public void setPotencia(
		Integer var
	) {
		this.potencia=var; 
	}
	
	public Integer getPotencia() {
		return this.potencia; 
	}
	
	public void setPuertas(
		Integer var
	) {
		this.puertas=var; 
	}
	
	public Integer getPuertas() {
		return this.puertas; 
	}
	
	public void setRemolque(
		boolean var
	) {
		this.remolque=var; 
	}
	
	public boolean isRemolque() {
		return this.remolque; 
	}
	
	public void setSuperficie(
		Integer var
	) {
		this.superficie=var; 
	}
	
	public Integer getSuperficie() {
		return this.superficie; 
	}
	
	public void setSuperficietotal(
		Integer var
	) {
		this.superficietotal=var; 
	}
	
	public Integer getSuperficietotal() {
		return this.superficietotal; 
	}
	
	public void setSuperjardin(
		Integer var
	) {
		this.superjardin=var; 
	}
	
	public Integer getSuperjardin() {
		return this.superjardin; 
	}
	
	public void setTripulantes(
		Integer var
	) {
		this.tripulantes=var; 
	}
	
	public Integer getTripulantes() {
		return this.tripulantes; 
	}
	
	public void setValoracion(
		BigDecimal var
	) {
		this.valoracion=var; 
	}
	
	public BigDecimal getValoracion() {
		return this.valoracion; 
	}
	
	public void setViviendas(
		Integer var
	) {
		this.viviendas=var; 
	}
	
	public Integer getViviendas() {
		return this.viviendas; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmtipopatr(
		Segmtipopatr var
	) {
		this.idsegmtipopatr=var; 
	}
	
	public Segmtipopatr getIdsegmtipopatr() {
		if(this.idsegmtipopatr==null)this.idsegmtipopatr=new org.suinsit.apps.seguros.Segmtipopatr();
		  return this.idsegmtipopatr; 
	}
	
	public void setIdsegmclasepatri(
		Segmclasepatri var
	) {
		this.idsegmclasepatri=var; 
	}
	
	public Segmclasepatri getIdsegmclasepatri() {
		if(this.idsegmclasepatri==null)this.idsegmclasepatri=new org.suinsit.apps.seguros.Segmclasepatri();
		  return this.idsegmclasepatri; 
	}
	
	public void setIdsegmusopatr(
		Segmusopatr var
	) {
		this.idsegmusopatr=var; 
	}
	
	public Segmusopatr getIdsegmusopatr() {
		if(this.idsegmusopatr==null)this.idsegmusopatr=new org.suinsit.apps.seguros.Segmusopatr();
		  return this.idsegmusopatr; 
	}
	
	public void setIdmpais(
		Mpais var
	) {
		this.idmpais=var; 
	}
	
	public Mpais getIdmpais() {
		if(this.idmpais==null)this.idmpais=new org.suinsit.apps.admin.Mpais();
		  return this.idmpais; 
	}
	
	public void setIdmprovincia(
		Mprovincia var
	) {
		this.idmprovincia=var; 
	}
	
	public Mprovincia getIdmprovincia() {
		if(this.idmprovincia==null)this.idmprovincia=new org.suinsit.apps.admin.Mprovincia();
		  return this.idmprovincia; 
	}
	
	public void setIdsegmpatriamb(
		Segmpatriamb var
	) {
		this.idsegmpatriamb=var; 
	}
	
	public Segmpatriamb getIdsegmpatriamb() {
		if(this.idsegmpatriamb==null)this.idsegmpatriamb=new org.suinsit.apps.seguros.Segmpatriamb();
		  return this.idsegmpatriamb; 
	}
	
	public void setIdsegmregimenpatri(
		Segmregimenpatri var
	) {
		this.idsegmregimenpatri=var; 
	}
	
	public Segmregimenpatri getIdsegmregimenpatri() {
		if(this.idsegmregimenpatri==null)this.idsegmregimenpatri=new org.suinsit.apps.seguros.Segmregimenpatri();
		  return this.idsegmregimenpatri; 
	}
	
	public void setIdsegmzonapatri(
		Segmzonapatri var
	) {
		this.idsegmzonapatri=var; 
	}
	
	public Segmzonapatri getIdsegmzonapatri() {
		if(this.idsegmzonapatri==null)this.idsegmzonapatri=new org.suinsit.apps.seguros.Segmzonapatri();
		  return this.idsegmzonapatri; 
	}
	
	public void setIdsegmnavegacion(
		Segmnavegacion var
	) {
		this.idsegmnavegacion=var; 
	}
	
	public Segmnavegacion getIdsegmnavegacion() {
		if(this.idsegmnavegacion==null)this.idsegmnavegacion=new org.suinsit.apps.seguros.Segmnavegacion();
		  return this.idsegmnavegacion; 
	}
	
	public void setIdsegmbandera(
		Segmbandera var
	) {
		this.idsegmbandera=var; 
	}
	
	public Segmbandera getIdsegmbandera() {
		if(this.idsegmbandera==null)this.idsegmbandera=new org.suinsit.apps.seguros.Segmbandera();
		  return this.idsegmbandera; 
	}
	
	public void setIdcrmempresa(
		Crmempresa var
	) {
		this.idcrmempresa=var; 
	}
	
	public Crmempresa getIdcrmempresa() {
		if(this.idcrmempresa==null)this.idcrmempresa=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcrmempresa; 
	}
	
	public void setSubsegmpoliza(
		List<Segmpoliza> var
	) {
		this.subsegmpoliza=var; 
	}
	
	public List<Segmpoliza> getSubsegmpoliza() {
		if(this.subsegmpoliza==null)this.subsegmpoliza=new ArrayList<>(0);
		  return this.subsegmpoliza; 
	} 

}