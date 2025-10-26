package com.codeflowx.govern.viewmodel.core;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.suinsit.nocode.web.MasterBeanUI;
import codeflowx.nocode.persist.*;
import com.codeflowx.govern.entity.core.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel extends MasterBeanUI {
    
    @WireVariable
    private BusinessService businessService;
    @Autowired
    protected IEntityLocal dao;
    @WireVariable
    public Environment environment;
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }

    
    private PageResult<Menu> pageResult;
    private PageParams pageParams;
    private String searchTerm = "";
    private Menu selectedMenu;
    private boolean showDialog = false;
    private boolean isEditing = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        doAfterCompose(view);
        initDao();
        
        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadData();
    }
    
    @Command
    @NotifyChange("pageResult")
    public void loadData() {
        try {
            log.debug("Cargando menús - Página: {}, SearchTerm: '{}'", 
                     pageParams.getPageActual(), searchTerm);
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String sql = "SELECT * FROM CORMENUS WHERE UPPER(MENUNAME) LIKE :search ORDER BY MENUORDER";
                Map<String, Object> params = new HashMap<>();
                params.put("search", "%" + searchTerm.toUpperCase() + "%");
                pageResult = businessService.findByParams(Menu.class, sql, params, pageParams);
            } else {
                String sqlAll = "SELECT * FROM CORMENUS ORDER BY MENUORDER";
                pageResult = businessService.findByParams(Menu.class, sqlAll, null, pageParams);
            }
            log.debug("Menús cargados: {}", pageResult != null && pageResult.getContent() != null ? pageResult.getContent().size() : 0);
        } catch (Exception e) {
            log.error("Error cargando menús", e);
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void search() {
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void clearSearch() {
        searchTerm = "";
        pageParams.setPageActual(1);
        loadData();
    }
    
    @Command
    @NotifyChange("pageResult")
    public void nextPage() {
        if (pageResult != null && pageResult.getNextRow() > 0) {
            pageParams.setRowActual(pageResult.getNextRow());
            pageParams.setPageActual(pageParams.getPageActual() + 1);
            loadData();
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void previousPage() {
        if (pageParams.getPageActual() > 1) {
            pageParams.setPageActual(pageParams.getPageActual() - 1);
            pageParams.setRowActual((pageParams.getPageActual() - 1) * pageParams.getMaxRows());
            loadData();
        }
    }
    
    @Command
    @NotifyChange({"selectedMenu", "showDialog", "isEditing"})
    public void newMenu() {
        selectedMenu = new Menu();
        isEditing = false;
        showDialog = true;
    }
    
    @Command
    @NotifyChange({"selectedMenu", "showDialog", "isEditing"})
    public void editMenu(@BindingParam("item") Menu menu) {
        try {
            selectedMenu = businessService.findById(Menu.class, menu.getIdxmenu());
            isEditing = true;
            showDialog = true;
        } catch (Exception e) {
            log.error("Error cargando menú para edición", e);
        }
    }
    
    @Command
    @NotifyChange({"pageResult", "showDialog", "selectedMenu"})
    public void saveMenu() {
        try {
            if (selectedMenu != null) {
                log.debug("Guardando menú: {}", selectedMenu.getMenuname());
                businessService.save(selectedMenu);
                log.info("Menú guardado exitosamente: ID={}", selectedMenu.getIdxmenu());
                showDialog = false;
                selectedMenu = null;
                loadData();
            }
        } catch (Exception e) {
            log.error("Error guardando menú", e);
        }
    }
    
    @Command
    @NotifyChange("pageResult")
    public void deleteMenu(@BindingParam("item") Menu menu) {
        try {
            if (menu != null) {
                log.debug("Eliminando menú: ID={}", menu.getIdxmenu());
                businessService.removeFromID(menu);
                log.info("Menú eliminado exitosamente: ID={}", menu.getIdxmenu());
                loadData();
            }
        } catch (Exception e) {
            log.error("Error eliminando menú", e);
        }
    }
    
    @Command
    @NotifyChange({"showDialog", "selectedMenu"})
    public void cancelEdit() {
        selectedMenu = null;
        showDialog = false;
    }
    
    @Command
    @NotifyChange("pageResult")
    public void refresh() {
        loadData();
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
