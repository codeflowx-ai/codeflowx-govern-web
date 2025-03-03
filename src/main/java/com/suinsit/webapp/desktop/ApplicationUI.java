/*
 * Copyright (c) 2011-2020 EnartSystems.com
 *
 * This program and the accompanying materials are made available under the
 * terms of the EUROPEAN UNION PUBLIC LICENCE v. 1.2 which is available at
 * https://joinup.ec.europa.eu/collection/eupl/news/understanding-eupl-v12 Version 1.2
 * which is available at https://joinup.ec.europa.eu/sites/default/files/custom-page/attachment/eupl_v1.2_en.pdf
 * License-Identifier: EUPL-1.2
 * @author Manuel González (enartsystems) 
  */
package com.suinsit.webapp.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.enartframework.core.shared.commons.TextParser;
import org.enartframework.core.shared.logger.EnartLoggerFactory;
import org.enartframework.core.shared.logger.IEnartLogger;
import org.enartframework.orm.jdbc.hikaricp.MultiDbConnectionPool;
import org.enartframework.suinsit.Context;
import org.enartframework.suinsit.config.DesktopBean;
import org.enartframework.suinsit.factory.IFactoryArchitect;
import org.enartframework.suinsit.model.application.AppModule;
import org.enartframework.suinsit.model.application.AppProject;
import org.enartframework.suinsit.model.application.AppVersion;
import org.enartframework.zk.utils.Notificacion.TYPENOTI;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.enartsystems.webapp.beans.AplicationBean;
import com.enartsystems.webapp.front.zkoss.MasterHandler;
import com.enartsystems.webapp.mapping.DatasourceFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suinsit.webapp.wizzars.application.CreateApp;

import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
@Getter
@Setter
public class ApplicationUI extends MasterHandler {
	private static final long serialVersionUID = 1L;
	private static final IEnartLogger log = EnartLoggerFactory.getLogger(ApplicationUI.class);
	AppProject project;
	AppModule modulo;
//	String currentPage;
	boolean addVersion=true;
	boolean addModules=true;
	boolean addScreen=true;
	DesktopBean desktopBean;
	AplicationBean application;
	@WireVariable("ctxBean")
	protected Context ctxBean;
	@WireVariable
	private Environment environment;
	@WireVariable("context")
    private GenericApplicationContext contexto;
	
	ObjectMapper mapper = new ObjectMapper();
	

	List<AppVersion> versiones = new ArrayList<>();
	List<AppModule>   modulos= new ArrayList<>();
	
	
	@Init(superclass=true)
	public void iniciar() throws Exception{
		
	}
	
	/**
	 * nos permite controlar componentes del la vista
	 * @param view
	 * @throws Exception
	 */
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		Selectors.wireComponents(view, this, false);
		super.doAfterCompose(view,this);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		eventQ = EventQueues.lookup("application", EventQueues.DESKTOP, true);
		project = ctxBean.getPj();
		AppVersion ver = new AppVersion();
		ver.setIdproject(new AppProject());
		ver.getIdproject().setIdxproject(project.getIdxproject());
		versiones.addAll(ctxBean.getPj().getMddmversion());
		ctxBean.setPj(project);
		ctxBean.setWks(project.getIdworkspace());
		project.setMddmversion(versiones);
		String path =ctxBean.getPlatform().getPathStudio() +File.separator+ TextParser.getFormaterUnixPath(project.getTxpath()).toLowerCase().trim() ;
		///opt/suinsit/licences/jobynet/workspaces/users/jobynet/default/
		
		desktopBean = getArcHandler().getDesktop();
		try {
			File fil = new File(path+"/application.json");
			application = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(fil, AplicationBean.class);
		} catch (IOException e) {
			log.error(e);
		}
		EventQueues.lookup("desktopui", EventQueues.SESSION, true).publish(new Event("menu", view, "apphome"));;
		this.currentPage=desktopBean.getPage();
		//configureEnviroment();
	}
	@WireVariable("multiPool")
    MultiDbConnectionPool multiPool;
	/**
	 * configura las variables para poder ejecutar la aplicacion 
	 */
	private void configureEnviroment() {
		try {
			loadSchema();
		} catch (Exception e) {
			log.error(e);
		}
		log.info("configure-ds");
//		if(environment instanceof ConfigurableEnvironment) {
//	        ConfigurableEnvironment env = (ConfigurableEnvironment)environment;
//	        Map<String,Object> prop = new HashMap<>();
//	        prop.put("APPLICATION_DIRMODEL", ctxBean.getPlatform().getPathData() + File.separator + "model");
//	        prop.put("APPLICATION_SCREENS", ctxBean.getPlatform() +File.separator + project.getNamespace()+File.separator +"designer"+File.separator +  "screens");
//	        prop.put("APPLICATION_NAME", project.getTxname());
//	        prop.put("APPLICATION_SCHEMA", ctxBean.getPlatform().getSchema());
//	        env.getPropertySources().addFirst(new MapPropertySource("SUINSIT_PROPS", prop));
//	    }
		//TODO hay que revisar este problema de registro dinamico de beans no funciona
//		BeanFactory factory = SpringUtil.getApplicationContext();
//		SingletonBeanRegistry beanRegistry = context.getBeanFactory();
//		if(!beanRegistry.containsSingleton("SUINSIT_APP_DS")) {
//		//	context.registerBean("SUINSIT_APP_DS",DataSource.class, () -> getDataSource());
//			beanRegistry.registerSingleton("SUINSIT_APP_DS", DatasourceFactory.getDataSource(schema));
//		}else {
//			context.getBeanFactory().destroyBean("SUINSIT_APP_DS", context.getBean("SUINSIT_APP_DS"));
//		//	context.registerBean("SUINSIT_APP_DS",DataSource.class, () -> getDataSource());
//			beanRegistry.registerSingleton("SUINSIT_APP_DS", DatasourceFactory.getDataSource(schema));
//		}
		
	}
	@NotifyChange("*")
	@Command("storeLang")
	public void storeLang() {
		String path =ctxBean.getPathHome() + ctxBean.FOLDER_WORKSPACE + ctxBean.FOLDER_USER
				+ File.separator + ctxBean.getUsuario().getUsername().toLowerCase().trim()
				+ File.separator + ctxBean.getWks().getTxname().toLowerCase() + File.separator
				+ project.getTxname().toLowerCase().trim() + File.separator;
		try {
			File fil = new File(path+"/application.json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(fil, application);
			showNotification(TYPENOTI.SUCCESS, Labels.getLabel("store.lang","Configuración de lenguaje actualizado correctamente"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@NotifyChange("*")
	@Command("selectPage")
    public void selectPage(@BindingParam("action") String accion) {
		this.currentPage=accion;
		if(accion.equalsIgnoreCase("modulos")) {
			//listModules();
		}
	}
//	private void listModules() {
//		DataScroll scroll = new DataScroll();
//		scroll.setMaxRows(999);
//		AppModule bean = new AppModule();
//		bean.setIdversionprj(version);
//		try {
//			modulos = mddmoduleBO.executeQuery(scroll, bean).getDataList();
//		} catch (DaoException e) {
//			log.error(e);
//		}
//	}
	Window popupCreate;
private String currentPage;
	
	@NotifyChange("*")
	@Command("removeVersion")
    public void removeVersion(@BindingParam("version") AppVersion ver) {
		
	}
	@NotifyChange("*")
	@Command("createVersion")
    public void createVersion() {
		popupCreate= (Window) Executions.createComponents("wizzarVersion.zul", null, null);
		popupCreate.doModal();
		EventQueues.lookup("wizzarVersion", super.session, true).subscribe(new EventListener<Event>() {
		    @Override
			public void onEvent(Event event) throws Exception {
		  		popupCreate.detach();
				((CreateApp)event).getVersion();
			}
		});
	}
	@NotifyChange("*")
	@Command("changeModule")
    public void changeModule(@BindingParam("modulo") AppModule modulo) {
		this.modulo=modulo;
		ctxBean.setModule(modulo);
	}
	@NotifyChange("*")
	@Command("removeModule")
    public void removeModule(@BindingParam("modulo") AppModule modulo) {
		
	}
	@NotifyChange("*")
	@Command("createModule")
    public void createModule() {
		popupCreate= (Window) Executions.createComponents("wizzarModule.zul", null, null);
		popupCreate.doModal();
		EventQueues.lookup("wizzarModule", super.session, true).subscribe(new EventListener<Event>() {
		    @Override
			public void onEvent(Event event) throws Exception {
		  		popupCreate.detach();
				if(((CreateApp)event).getModule()!=null) {
					modulos.add(((CreateApp)event).getModule());
					changeModule(((CreateApp)event).getModule());
				}
			}
		});
	}
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doAfterCompose() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public IFactoryArchitect getFactoryByProject() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
