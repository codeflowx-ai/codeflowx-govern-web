package com.codeflowx.platform.viewmodel.domainingestion;

import java.sql.Timestamp;
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
public class DomainManagementViewModel extends BaseFront<DomainManagementViewModel> {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    private PageParams pageParams;
    private PageResult<Domain> pageResult;
    
    private String searchText = "";
    private String filterIndustry = "";
    private String filterStatus = "";
    
    private List<Domain> domainsList = new ArrayList<>();
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadDomains();
    }
    
    @Command
    @NotifyChange("*")
    public void loadDomains() {
        try {
            Criterias criterias = buildCriterias();
            pageResult = businessService.findAllEntity(Domain.class, pageParams, criterias);
            
            if (pageResult != null && pageResult.getContent() != null) {
                domainsList = pageResult.getContent();
                logActivity("BUSCAR", "DOMAINS", null, "Gestión: " + domainsList.size() + " dominios");
            } else {
                domainsList = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error al cargar dominios", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "dindomainname", searchText));
        }
        if (filterIndustry != null && !filterIndustry.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinindustry", filterIndustry));
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "dinstatus", filterStatus));
        }
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void searchDomains() {
        pageParams.setPageActual(1);
        loadDomains();
    }
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadDomains();
    }
    
    @Command
    public void createNewDomain() {
        // TODO: Navegar a wizard
        log.info("Crear nuevo dominio");
    }
    
    @Command
    public void viewDomainDetail(@BindingParam("domain") Domain domain) {
        logActivity("VER", "DOMAINS", domain.getIdxdomain(), "Detalle: " + domain.getDindomainname());
        log.info("Ver detalle dominio: {}", domain.getDindomainname());
    }
    
    @Command
    public void editDomain(@BindingParam("domain") Domain domain) {
        logActivity("EDITAR", "DOMAINS", domain.getIdxdomain(), "Edición: " + domain.getDindomainname());
        log.info("Editar dominio: {}", domain.getDindomainname());
    }
    
    @Command
    @NotifyChange("*")
    public void deleteDomain(@BindingParam("domain") Domain domain) {
        Messagebox.show("¿Eliminar dominio: " + domain.getDindomainname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(domain);
                        logActivity("ELIMINAR", "DOMAINS", domain.getIdxdomain(), 
                            "Dominio eliminado: " + domain.getDindomainname());
                        loadDomains();
                        Messagebox.show("Dominio eliminado", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar", e);
                        Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    public String translateStatus(String status) {
        if (status == null) return "N/A";
        switch (status) {
            case "active": return "Activo";
            case "inactive": return "Inactivo";
            case "draft": return "Borrador";
            case "error": return "Error";
            default: return status;
        }
    }
    
    public String getStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "active": return "badge bg-success";
            case "inactive": return "badge bg-secondary";
            case "draft": return "badge bg-warning";
            case "error": return "badge bg-danger";
            default: return "badge bg-secondary";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (domainsList != null) {
            domainsList.clear();
            domainsList = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}

