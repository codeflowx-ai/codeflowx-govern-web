package org.suinsit.apps.facturacin;

import java.io.Serializable;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Id;
import org.enartframework.nocode.annotacion.Entidad;

@Entidad (
	type = "QUERY",
	pk = "idxpromproducto" 
)
public class Qproproducto implements Serializable { 

	private static final long serialVersionUID = 1L;
	@Column (
		name = "AVATAR",
		nullable = true 
	)
	private Object avatar;
	@Column (
		name = "CODBARRAS",
		nullable = true 
	)
	private String codbarras;
	@Column (
		name = "DESCRIPCION",
		nullable = true 
	)
	private String descripcion;
	@Id
	@Column (
		name = "IDXPROMPRODUCTO",
		nullable = true 
	)
	private Long idxpromproducto;
	@Column (
		name = "NOTA",
		nullable = true 
	)
	private String nota;
	@Column (
		name = "PRECIOCOSTE",
		nullable = true 
	)
	private BigDecimal preciocoste;
	@Column (
		name = "PRECIOVENTA",
		nullable = false 
	)
	private BigDecimal precioventa;
	@Column (
		name = "PRODUCTO",
		nullable = false 
	)
	private String producto;
	@Column (
		name = "REFINTERNA",
		nullable = true 
	)
	private String refinterna;
	@Column (
		name = "STOCK",
		nullable = true 
	)
	private BigDecimal stock;
	@Column (
		name = "STOCKMIN",
		nullable = true 
	)
	private BigDecimal stockmin;
	@Column (
		name = "TIPOPROD",
		nullable = false 
	)
	private String tipoprod;
	@Column (
		name = "IDPROMCATEGORIA0",
		nullable = true 
	)
	private Long idpromcategoria0;
	@Column (
		name = "IDERPIMPUESTOS0",
		nullable = true 
	)
	private Long iderpimpuestos0;
	@Column (
		name = "IDPROVEEDOR0",
		nullable = true 
	)
	private Long idproveedor0;
	@Column (
		name = "TIPOIMPUESTO",
		nullable = true 
	)
	private String tipoimpuesto;
	@Column (
		name = "INCLUIDOPRECIO",
		nullable = true 
	)
	private boolean incluidoprecio;
	@Column (
		name = "IMPUESTO",
		nullable = false 
	)
	private String impuesto;
	@Column (
		name = "IVA",
		nullable = true 
	)
	private BigDecimal iva;
	@Column (
		name = "ACTIVADO",
		nullable = true 
	)
	private boolean activado;
	private boolean updatable; 

	public void setAvatar(
		Object var
	) {
		this.avatar=var; 
	}
	
	public Object getAvatar() {
		return this.avatar; 
	}
	
	public void setCodbarras(
		String var
	) {
		this.codbarras=var; 
	}
	
	public String getCodbarras() {
		return this.codbarras; 
	}
	
	public void setDescripcion(
		String var
	) {
		this.descripcion=var; 
	}
	
	public String getDescripcion() {
		return this.descripcion; 
	}
	
	public void setIdxpromproducto(
		Long var
	) {
		this.idxpromproducto=var; 
	}
	
	public Long getIdxpromproducto() {
		return this.idxpromproducto; 
	}
	
	public void setNota(
		String var
	) {
		this.nota=var; 
	}
	
	public String getNota() {
		return this.nota; 
	}
	
	public void setPreciocoste(
		BigDecimal var
	) {
		this.preciocoste=var; 
	}
	
	public BigDecimal getPreciocoste() {
		return this.preciocoste; 
	}
	
	public void setPrecioventa(
		BigDecimal var
	) {
		this.precioventa=var; 
	}
	
	public BigDecimal getPrecioventa() {
		return this.precioventa; 
	}
	
	public void setProducto(
		String var
	) {
		this.producto=var; 
	}
	
	public String getProducto() {
		return this.producto; 
	}
	
	public void setRefinterna(
		String var
	) {
		this.refinterna=var; 
	}
	
	public String getRefinterna() {
		return this.refinterna; 
	}
	
	public void setStock(
		BigDecimal var
	) {
		this.stock=var; 
	}
	
	public BigDecimal getStock() {
		return this.stock; 
	}
	
	public void setStockmin(
		BigDecimal var
	) {
		this.stockmin=var; 
	}
	
	public BigDecimal getStockmin() {
		return this.stockmin; 
	}
	
	public void setTipoprod(
		String var
	) {
		this.tipoprod=var; 
	}
	
	public String getTipoprod() {
		return this.tipoprod; 
	}
	
	public void setIdpromcategoria0(
		Long var
	) {
		this.idpromcategoria0=var; 
	}
	
	public Long getIdpromcategoria0() {
		return this.idpromcategoria0; 
	}
	
	public void setIderpimpuestos0(
		Long var
	) {
		this.iderpimpuestos0=var; 
	}
	
	public Long getIderpimpuestos0() {
		return this.iderpimpuestos0; 
	}
	
	public void setIdproveedor0(
		Long var
	) {
		this.idproveedor0=var; 
	}
	
	public Long getIdproveedor0() {
		return this.idproveedor0; 
	}
	
	public void setTipoimpuesto(
		String var
	) {
		this.tipoimpuesto=var; 
	}
	
	public String getTipoimpuesto() {
		return this.tipoimpuesto; 
	}
	
	public void setIncluidoprecio(
		boolean var
	) {
		this.incluidoprecio=var; 
	}
	
	public boolean isIncluidoprecio() {
		return this.incluidoprecio; 
	}
	
	public void setImpuesto(
		String var
	) {
		this.impuesto=var; 
	}
	
	public String getImpuesto() {
		return this.impuesto; 
	}
	
	public void setIva(
		BigDecimal var
	) {
		this.iva=var; 
	}
	
	public BigDecimal getIva() {
		return this.iva; 
	}
	
	public void setActivado(
		boolean var
	) {
		this.activado=var; 
	}
	
	public boolean isActivado() {
		return this.activado; 
	}
	
	public void setUpdatable(
		boolean var
	) {
		this.updatable=var; 
	}
	
	public boolean isUpdatable() {
		return this.updatable; 
	} 

}