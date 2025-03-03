package org.suinsit.apps.seguros;

import java.io.Serializable;
import java.lang.Long;
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
import org.suinsit.apps.crm.Crmempresa;
import org.suinsit.apps.seguros.Segmcompany;
import org.suinsit.apps.seguros.Segmpatrimonio;
import org.suinsit.apps.seguros.Segmproducto;
import org.suinsit.apps.seguros.Segmsiniestro;

@Entity
@Table (
	name = "SEGMPOLIZA" 
)
@Entidad (
	type = "TABLE",
	pk = "idxsegmpoliza" 
)
public class Segmpoliza implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "ALTA",
		nullable = false 
	)
	private Date alta;
	@Column (
		name = "CBROBCO",
		nullable = true 
	)
	private boolean cbrobco;
	@Column (
		name = "CBROCIA",
		nullable = true 
	)
	private boolean cbrocia;
	@Column (
		name = "CONTRATO",
		nullable = true 
	)
	@Sequence (
		name = "SEGMPOLIZA_CONTRATO",
		prefix = "INS",
		mask = "00000000",
		addYear = true 
	)
	private String contrato;
	@Column (
		name = "DESCINTERNA",
		nullable = true 
	)
	private String descinterna;
	@Column (
		name = "DESCPUBLICA",
		nullable = true 
	)
	private String descpublica;
	@Column (
		name = "EFECTORCBO",
		nullable = true 
	)
	private Timestamp efectorcbo;
	@Column (
		name = "FINEFECTORCB",
		nullable = true 
	)
	private Timestamp finefectorcb;
	@Id
	@Column (
		name = "IDXSEGMPOLIZA",
		nullable = true 
	)
	private Long idxsegmpoliza;
	@Column (
		name = "POLIZA",
		nullable = false 
	)
	private String poliza;
	@Column (
		name = "PRIMANETA",
		nullable = true 
	)
	private BigDecimal primaneta;
	@Column (
		name = "PRIMATOTAL",
		nullable = true 
	)
	private BigDecimal primatotal;
	@Column (
		name = "TIPOPOLIZA",
		nullable = false 
	)
	private String tipopoliza;
	private boolean updatable;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMCOMPANY0",
		referencedColumnName = "IDXSEGMCOMPANY",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmcompany idsegmcompany;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPRODUCTO0",
		referencedColumnName = "IDXSEGMPRODUCTO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmproducto idsegmproducto;
	@ManyToOne (
		fetch = javax.persistence.FetchType.LAZY 
	)
	@JoinColumn (
		name = "IDSEGMPATRIMONIO0",
		referencedColumnName = "IDXSEGMPATRIMONIO",
		nullable = true,
		insertable = true,
		updatable = true 
	)
	private Segmpatrimonio idsegmpatrimonio;
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
	private Crmempresa idcliente;
	private List<Segmsiniestro> subsegmsiniestro; 

	public void setAlta(
		Date var
	) {
		this.alta=var; 
	}
	
	public Date getAlta() {
		return this.alta; 
	}
	
	public void setCbrobco(
		boolean var
	) {
		this.cbrobco=var; 
	}
	
	public boolean isCbrobco() {
		return this.cbrobco; 
	}
	
	public void setCbrocia(
		boolean var
	) {
		this.cbrocia=var; 
	}
	
	public boolean isCbrocia() {
		return this.cbrocia; 
	}
	
	public void setContrato(
		String var
	) {
		this.contrato=var; 
	}
	
	public String getContrato() {
		return this.contrato; 
	}
	
	public void setDescinterna(
		String var
	) {
		this.descinterna=var; 
	}
	
	public String getDescinterna() {
		return this.descinterna; 
	}
	
	public void setDescpublica(
		String var
	) {
		this.descpublica=var; 
	}
	
	public String getDescpublica() {
		return this.descpublica; 
	}
	
	public void setEfectorcbo(
		Timestamp var
	) {
		this.efectorcbo=var; 
	}
	
	public Timestamp getEfectorcbo() {
		return this.efectorcbo; 
	}
	
	public void setFinefectorcb(
		Timestamp var
	) {
		this.finefectorcb=var; 
	}
	
	public Timestamp getFinefectorcb() {
		return this.finefectorcb; 
	}
	
	public void setIdxsegmpoliza(
		Long var
	) {
		this.idxsegmpoliza=var; 
	}
	
	public Long getIdxsegmpoliza() {
		return this.idxsegmpoliza; 
	}
	
	public void setPoliza(
		String var
	) {
		this.poliza=var; 
	}
	
	public String getPoliza() {
		return this.poliza; 
	}
	
	public void setPrimaneta(
		BigDecimal var
	) {
		this.primaneta=var; 
	}
	
	public BigDecimal getPrimaneta() {
		return this.primaneta; 
	}
	
	public void setPrimatotal(
		BigDecimal var
	) {
		this.primatotal=var; 
	}
	
	public BigDecimal getPrimatotal() {
		return this.primatotal; 
	}
	
	public void setTipopoliza(
		String var
	) {
		this.tipopoliza=var; 
	}
	
	public String getTipopoliza() {
		return this.tipopoliza; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	}
	
	public void setIdsegmcompany(
		Segmcompany var
	) {
		this.idsegmcompany=var; 
	}
	
	public Segmcompany getIdsegmcompany() {
		if(this.idsegmcompany==null)this.idsegmcompany=new org.suinsit.apps.seguros.Segmcompany();
		  return this.idsegmcompany; 
	}
	
	public void setIdsegmproducto(
		Segmproducto var
	) {
		this.idsegmproducto=var; 
	}
	
	public Segmproducto getIdsegmproducto() {
		if(this.idsegmproducto==null)this.idsegmproducto=new org.suinsit.apps.seguros.Segmproducto();
		  return this.idsegmproducto; 
	}
	
	public void setIdsegmpatrimonio(
		Segmpatrimonio var
	) {
		this.idsegmpatrimonio=var; 
	}
	
	public Segmpatrimonio getIdsegmpatrimonio() {
		if(this.idsegmpatrimonio==null)this.idsegmpatrimonio=new org.suinsit.apps.seguros.Segmpatrimonio();
		  return this.idsegmpatrimonio; 
	}
	
	public void setIdcliente(
		Crmempresa var
	) {
		this.idcliente=var; 
	}
	
	public Crmempresa getIdcliente() {
		if(this.idcliente==null)this.idcliente=new org.suinsit.apps.crm.Crmempresa();
		  return this.idcliente; 
	}
	
	public void setSubsegmsiniestro(
		List<Segmsiniestro> var
	) {
		this.subsegmsiniestro=var; 
	}
	
	public List<Segmsiniestro> getSubsegmsiniestro() {
		if(this.subsegmsiniestro==null)this.subsegmsiniestro=new ArrayList<>(0);
		  return this.subsegmsiniestro; 
	} 

}