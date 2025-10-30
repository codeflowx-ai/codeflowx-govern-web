package com.codeflowx.platform.viewmodel.domainingestion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.domainingestion.Domain;
import com.codeflowx.govern.entity.domainingestion.DomainDataSource;

import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class ConfigureWebScrapingViewModel extends BaseFront<ConfigureWebScrapingViewModel> {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private Domain selectedDomain;
    private DomainDataSource dataSource = new DomainDataSource();
    private List<Domain> domainsList = new ArrayList<>();
    private List<DomainDataSource> existingSources = new ArrayList<>();
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        dataSource.setDindstype("web");
        dataSource.setDindsenabled(true);
        
        loadDomains();
        loadExistingSources();
    }
    
    @Command
    @NotifyChange("*")
    public void loadDomains() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinstatus", "active"));
            PageResult<Domain> result = businessService.findAllEntity(Domain.class, 
                PageParams.builder().maxRows(1000).build(), criterias);
            if (result != null && result.getContent() != null) {
                domainsList = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar dominios", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadExistingSources() {
        try {
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dindstype", "web"));
            PageResult<DomainDataSource> result = businessService.findAllEntity(DomainDataSource.class, 
                PageParams.builder().maxRows(1000).build(), criterias);
            if (result != null && result.getContent() != null) {
                existingSources = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar fuentes", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void saveDataSource() {
        if (selectedDomain == null) {
            Messagebox.show("Seleccione un dominio", "Validación", Messagebox.OK, Messagebox.WARNING);
            return;
        }
        if (dataSource.getDindsname() == null || dataSource.getDindsname().trim().isEmpty()) {
            Messagebox.show("Ingrese un nombre", "Validación", Messagebox.OK, Messagebox.WARNING);
            return;
        }
        if (dataSource.getDindsurl() == null || dataSource.getDindsurl().trim().isEmpty()) {
            Messagebox.show("Ingrese una URL", "Validación", Messagebox.OK, Messagebox.WARNING);
            return;
        }
        
        try {
            dataSource.setDomain(selectedDomain);
            dataSource.setDindstype("web");
            businessService.save(dataSource);
            logActivity("CREAR", "DOMAIN_DATA_SOURCE", dataSource.getIdxdatasource(), 
                "Fuente Web Scraping creada: " + dataSource.getDindsname());
            Messagebox.show("Fuente Web Scraping creada exitosamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            
            dataSource = new DomainDataSource();
            dataSource.setDindstype("web");
            dataSource.setDindsenabled(true);
            selectedDomain = null;
            loadExistingSources();
        } catch (Exception e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void editSource(@BindingParam("source") DomainDataSource source) {
        selectedDomain = source.getDomain();
        dataSource = source;
        logActivity("EDITAR", "DOMAIN_DATA_SOURCE", source.getIdxdatasource(), "Edición: " + source.getDindsname());
    }
    
    @Command
    @NotifyChange("*")
    public void deleteSource(@BindingParam("source") DomainDataSource source) {
        Messagebox.show("¿Eliminar fuente: " + source.getDindsname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(source);
                        logActivity("ELIMINAR", "DOMAIN_DATA_SOURCE", source.getIdxdatasource(), 
                            "Fuente eliminada: " + source.getDindsname());
                        loadExistingSources();
                        Messagebox.show("Fuente eliminada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar", e);
                        Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    @Command
    public void cancel() {
        dataSource = new DomainDataSource();
        dataSource.setDindstype("web");
        dataSource.setDindsenabled(true);
        selectedDomain = null;
    }
    
    public String translateStatus(String status) {
        if (status == null) return "N/A";
        switch (status) {
            case "active": return "Activo";
            case "inactive": return "Inactivo";
            case "error": return "Error";
            default: return status;
        }
    }
    
    public String getStatusBadge(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "active": return "badge bg-success";
            case "inactive": return "badge bg-secondary";
            case "error": return "badge bg-danger";
            default: return "badge bg-secondary";
        }
    }
    
    @Destroy
    public void destroy() {
        if (domainsList != null) {
            domainsList.clear();
            domainsList = null;
        }
        if (existingSources != null) {
            existingSources.clear();
            existingSources = null;
        }
        dataSource = null;
        selectedDomain = null;
        businessService = null;
    }
}

