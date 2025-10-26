package com.suinsit.webapp.nocode;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.enartframework.core.shared.commons.SortCollections;
import org.enartframework.core.shared.commons.TextParser;
import org.enartframework.core.shared.commons.system.JavaMemManagement;
import org.enartframework.core.shared.logger.EnartLoggerFactory;
import org.enartframework.core.shared.logger.IEnartLogger;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.dao.EntityDao;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.nocode.datamodel.jdbc.Criteria;
import org.enartframework.nocode.datamodel.jdbc.Criterias;
import org.enartframework.nocode.datamodel.jdbc.Evaluation;
import org.enartframework.nocode.datamodel.jdbc.Operation;
import org.enartframework.nocode.datamodel.model.Entity;
import org.enartframework.nocode.datamodel.model.Field;
import org.enartframework.nocode.datamodel.model.Foreing;
import org.enartframework.nocode.datamodel.model.Query;
import org.enartframework.nocode.datamodel.model.QueryBuilder;
import org.enartframework.orm.DataScroll;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.orm.jdbc.hikaricp.MultiDbConnectionPool;
import org.enartframework.schema.database.IMetaModel;
import org.enartframework.security.Usuario;
import org.enartframework.suinsit.Context;
import org.enartframework.suinsit.config.DatabaseBean;
import org.enartframework.suinsit.config.SchemaBD;
import org.enartframework.suinsit.factory.IFactoryArchitect;
import org.enartframework.suinsit.model.application.AppArchitecture;
import org.enartframework.suinsit.model.application.AppProject;
import org.enartframework.suinsit.workspace.project.PlatformActive;
import org.enartframework.suinsit.workspace.project.Project;
import org.enartframework.web.exception.UiException;
import org.enartframework.zk.utils.MessageWin;
import org.enartframework.zk.utils.Notificacion.TYPENOTI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.suinsit.nocode.web.BuildEntityFromClass;
import org.suinsit.nocode.web.MenuBean;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Library;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Drawer;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window.Mode;
import org.zkoss.zul.Style;
import org.zkoss.zul.Window;
import org.zkoss.zul.theme.Themes;

import com.enartsystems.mapping.BuilderModel;
import com.enartsystems.platform.nocode.builder.ScreenModel;
import com.enartsystems.webapp.beans.AplicationBean;
import com.enartsystems.webapp.front.zkoss.MasterHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suinsit.discovery.domain.bpmn.ProcessRQ;
import com.suinsit.discovery.domain.bpmn.model.CustomerDto;
import com.suinsit.discovery.domain.bpmn.model.TaskDto;
import com.suinsit.webapp.wizzars.application.CreateApp;

import edu.emory.mathcs.backport.java.util.Collections;
import suinsit.framework.webclient.WebConfig;
import suinsit.framework.webclient.WebConnect;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DashboardUI extends MasterHandler {
	private static final long serialVersionUID = 1L;
	private List<WeakReference> references = new ArrayList<WeakReference>(0);
	private static final IEnartLogger log = EnartLoggerFactory.getLogger(DashboardUI.class);
	ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);;
	@WireVariable("ctxBean")
	protected Context ctxBean;
	@WireVariable("entityDao")
	IEntityLocal dao;
	@WireVariable("context")
	GenericApplicationContext context;
	@WireVariable
	private Environment environment;
	@WireVariable("multiPool")
	MultiDbConnectionPool multiPool;
	@WireVariable
	@Autowired
	DataSource datasource;
	DataSource ds;
	String pathDirScreen;
	String pathDirModel;
	String pathDirSchema;
	String pathMenu;
	String appName;
	String pageName;
	AplicationBean proyect;
	@Wire
	Style theme;
	Entity Ssoruserapp;
	Class<?> clsSsoruserapp;
	Entity eUsuario;
	Class<?> clsUsuario;
	BuilderModel builder;
	Query query;
	ScreenModel screenModel;
	List<Entity> beans = new ArrayList<>();
	Criterias criterias = new Criterias();
	Properties lang;
	String memory;
	EventQueue eventLocale;
	Component view;
	List<MenuBean> menus = new ArrayList<>();
	List<MenuBean> items = new ArrayList<>();
	Entity permisos;
	String menu;
	MenuBean item;
	Class<?> clsPermisos;
	Entity Bpmnvprocessemails;
	Class<?> clsBpmnvprocessemails;
	Entity Ssomenuitem;
	Class<?> clsSsomenuitem;
	AppProject projectBase;
	@Wire
	Div compactScreenIcon;
	@Wire
	Drawer drawer;
	protected List<String> namespaces = new ArrayList<String>(0);
	String namespace;
	String appVersion = "pro";

	public List<MenuBean> getItems() {
		return items;
	}

	public void setItems(List<MenuBean> items) {
		this.items = items;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List<String> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(List<String> namespaces) {
		this.namespaces = namespaces;
	}

	public AppProject getProjectBase() {
		return projectBase;
	}

	public void setProjectBase(AppProject projectBase) {
		this.projectBase = projectBase;
	}

	public Context getCtxBean() {
		return ctxBean;
	}

	List<AplicationBean> applications = new ArrayList<AplicationBean>();
	List<AplicationBean> applicationsStudio = new ArrayList<AplicationBean>();

	String search;
	private Class<?> ClsSsomenu;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<AplicationBean> getApplicationsStudio() {
		return applicationsStudio;
	}

	public void setApplicationsStudio(List<AplicationBean> applicationsStudio) {
		this.applicationsStudio = applicationsStudio;
	}

	public List<AplicationBean> getApplications() {
		return applications;
	}

	public void setApplications(List<AplicationBean> applications) {
		this.applications = applications;
	}

	public String getAppName() {
		return appName;
	}

	public MenuBean getItem() {
		return item;
	}

	public void setItem(MenuBean item) {
		this.item = item;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public List<MenuBean> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuBean> menus) {
		this.menus = menus;
	}

	public AplicationBean getProyect() {
		return proyect;
	}

	public void setProyect(AplicationBean proyect) {
		this.proyect = proyect;
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		Selectors.wireVariables(page, this, Selectors.newVariableResolvers(getClass(), null));
		this.view = view;
		super.view = view;
		log.info("view" + view);
		pathDirModel = ctxBean.getPathHome() + File.separator + "data" + File.separator + "model";
		URLClassLoader urlClassLoader;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			urlClassLoader = new URLClassLoader(new URL[] { new File(ctxBean.getPathClasses()).toURL() }, classLoader);
			Thread.currentThread().setContextClassLoader(urlClassLoader);
		} catch (MalformedURLException e) {
			log.error(e);
		}
		builder = new BuilderModel(ctxBean.getPathHome() + File.separator + "data" + File.separator + "model");
		Ssoruserapp = builder.loadFromDataGrid("SSORUSERAPP");
		clsSsoruserapp = Thread.currentThread().getContextClassLoader().loadClass("org.suinsit.apps."
				+ Ssoruserapp.getNamespace() + "." + TextParser.getClassNameFormater(Ssoruserapp.getCoentity()));
		eUsuario = builder.loadFromDataGrid("SSOUSUARIO");
		clsUsuario = Thread.currentThread().getContextClassLoader().loadClass("org.suinsit.apps."
				+ eUsuario.getNamespace() + "." + TextParser.getClassNameFormater(eUsuario.getCoentity()));
		permisos = builder.loadFromDataGrid("SSOVPERMROL");
		clsPermisos = Thread.currentThread().getContextClassLoader().loadClass("org.suinsit.apps."
				+ permisos.getNamespace() + "." + TextParser.getClassNameFormater(permisos.getCoentity()));
		Bpmnvprocessemails = builder.loadFromDataGrid("BPMNVPROCESSEMAILS");
		clsBpmnvprocessemails = Thread.currentThread().getContextClassLoader()
				.loadClass("org.suinsit.apps." + Bpmnvprocessemails.getNamespace() + "."
						+ TextParser.getClassNameFormater(Bpmnvprocessemails.getCoentity()));
		ClsSsomenu = Thread.currentThread().getContextClassLoader().loadClass("org.suinsit.apps.admin.Ssomenu");
		Ssomenuitem = builder.loadFromDataGrid("SSOMENUITEM");
		clsSsomenuitem = Thread.currentThread().getContextClassLoader().loadClass("org.suinsit.apps."
				+ Ssomenuitem.getNamespace() + "." + TextParser.getClassNameFormater(Ssomenuitem.getCoentity()));
		ds = environment.getProperty("APPLICATION_DS", DataSource.class);
		initDao();
		eventLocale = EventQueues.lookup("locale", EventQueues.SESSION, true);
		loadApps(getUser());
		drawer = (Drawer) view.getFellowIfAny("menuLeft", true).getFellow("drawer");
		if (view.getAttribute("dashboard") != null) {
			appendDashboard(view.getAttribute("namespace") != null ? view.getAttribute("namespace").toString() : null,
					view.getAttribute("dashboard").toString(), null);
		}
		references.add(new WeakReference<Object>(builder));
		references.add(new WeakReference<Object>(Ssoruserapp));
		references.add(new WeakReference<Object>(eUsuario));
		references.add(new WeakReference<Object>(permisos));
		references.add(new WeakReference<Object>(Ssomenuitem));
		references.add(new WeakReference<Object>(menus));
		references.add(new WeakReference<Object>(applicationsStudio));
		references.add(new WeakReference<Object>(items));
		references.add(new WeakReference<Object>(sapps));
		references.add(new WeakReference<Object>(apps));
	}

	@Command
	public void openMenuPage() {
		drawer.open();
	}

	@Command
	public void viewPage(@BindingParam("page") String page) {
		appendPage(page, iddesktop, Collections.emptyMap());
	}

	@NotifyChange("*")
	@GlobalCommand
	public void onOpenMenu(@BindingParam("item") MenuBean item, @BindingParam("contenedor") Div contenedor)
			throws UiException {
		if (!item.isLoaded()) {
			try {
				Object ssoMenu = dao.findById(ClsSsomenu, Long.valueOf(item.getId()));
				item.setDescripcion((String) getNested(ssoMenu, "descripcion"));
				item.setPageUrl((String) getNested(ssoMenu, "dashboardpage"));
				item.setNamespace((String) getNested(ssoMenu, "namespace"));
				item.setLoaded(true);
				if (item.getPageUrl() != null && item.getNamespace() != null) {
					item.setDesktop(true);
				}
			} catch (NumberFormatException | DaoException e) {
				log.warn(e.getMessage());
			}
		}
		if (item.isDesktop()) {
			Map<String, Object> args = new HashMap();
			args.put("PATH_MODEL", pathDirModel);
			String pathDirScreen = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(item.getNamespace()).toLowerCase() + File.separator + "webapp"
					+ File.separator + "pages";
			String pathDirSchema = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(item.getNamespace()).toLowerCase() + File.separator + "webapp"
					+ File.separator + "screens";
			args.put("PATH_SCREEN", pathDirScreen);
			args.put("PATH_SCHEMA", pathDirSchema);
			args.put("CONTAINER", contenedor);
			applications.forEach(ap -> {
				if (ap.getProjectBase().getNamespace().equals(item.getNamespace())) {
					setApplication(ap);
				}
			});
			if (item.getPageUrl() != null) {
				appendPage(pathDirScreen + File.separator + item.getPageUrl(), contenedor, args);
			} else {
				String prefix = item.getPrefix() != null ? item.getPrefix() : "index";
				appendPage(pathDirScreen + File.separator + prefix + item.getNameScreen() + ".zul", contenedor, args);
			}

			if (item.getPageUrl() != null) {
				appendPage(pathDirScreen + File.separator + item.getPageUrl(), contenedor, args);
			} else {
				String prefix = item.getPrefix() != null ? item.getPrefix() : "index";
				appendPage(pathDirScreen + File.separator + prefix + item.getNameScreen() + ".zul", contenedor, args);
			}
		}

	}

	private void appendDashboard(String namespace, String page, Component container) {
		Map<String, Object> args = new HashMap();
		if (namespace != null) {
			args.put("PATH_MODEL", pathDirModel);
			args.put("CONTAINER", container != null ? container : iddesktop);
			String pathDirScreen = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(namespace).toLowerCase() + File.separator + "webapp"
					+ File.separator + "pages";
			String pathDirSchema = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(namespace).toLowerCase() + File.separator + "webapp"
					+ File.separator + "screens";
			args.put("PATH_SCREEN", pathDirScreen);
			args.put("PATH_SCHEMA", pathDirSchema);
			appendPage(pathDirScreen + File.separator + page, container != null ? container : iddesktop, args);
		} else {
			appendPage(page, container != null ? container : iddesktop, args);
		}
	}

	private List<String> sapps = List.of();
	private List<Object> apps = List.of();

	private void loadPortalAPP() {
		ctxBean.getApplicationName();
		List<String> lfields = new ArrayList<>();
		try {
			Class portalapp = Thread.currentThread().getContextClassLoader()
					.loadClass("org.suinsit.apps.admin.Ssorportalapp");
			Entity e = BuildEntityFromClass.build(portalapp);
			e.getFields().forEach(sfield -> {
				lfields.add(sfield.getAtribute());
			});
			List<Foreing> lforeins = new ArrayList<>();
			e.getForeings().forEach(fg -> {
				lforeins.add(fg);
				fg.getFkentity().getFields().forEach(f -> {
					lfields.add(f.getAtribute());
				});
			});
			query = QueryBuilder.buildQueryWithLeftJoin(e, lforeins, lfields.toArray(new String[lfields.size()]));
			sapps = new ArrayList();
			apps = new ArrayList();
			List<?> tmp = dao.query(portalapp, new DataScroll(), query, criterias).getDataList();
			tmp.forEach(ob -> {
		//		if (!sapps.contains(getNested(ob, "idssoaplicacion.namespace").toString())) {
					String portal = (String) getNested(ob, "idssoportal.aplicacion");
					if (ctxBean.getApplicationName().equalsIgnoreCase(portal)) {
						sapps.add(getNested(ob, "idssoaplicacion.namespace").toString());
						apps.add(getNested(ob, "idssoaplicacion"));
					}
		//		}
			});
		} catch (Exception e) {
			log.debug("NO IMPLEMENTADA LA SEGURIDAD DE PORTALES");
		}

	}

	private void loadApps(Usuario usuario) {
		loadPortalAPP();
		List<String> lfields = new ArrayList<>();
		Ssoruserapp.getFields().forEach(sfield -> {
			lfields.add(sfield.getAtribute());
		});
		query = QueryBuilder.buildQueryWithLeftJoin(Ssoruserapp,
				List.of(Ssoruserapp.getForeingByAlias("idssoaplicacion")), lfields.toArray(new String[lfields.size()]));
		criterias = new Criterias();
		Field iduser = eUsuario.copy().getFieldByAtribute("IDXSSOUSUARIO").copy();
		iduser.setField("IDSSOUSUARIO0");
		iduser.setAtribute("IDSSOUSUARIO0");
		iduser.setValue(usuario.getId());
		criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, iduser));
		try {
			listProjects(dao.query(clsSsoruserapp, new DataScroll(), query, criterias).getDataList());
		} catch (Exception e) {
			log.error(e);
		}
	}

	public Object getNested(Object data, String prop) {
		try {
			return PropertyUtils.getNestedProperty(data, prop);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	@GlobalCommand("findApp")
	public void find() {
		try {
			applicationsStudio.clear();
			String path = ctxBean.getPlatform().getPathStudio();
			File fil = new File(path);
			fil.mkdirs();
			for (File file : fil.listFiles()) {
				if (file.isDirectory()) {
					File[] fils = file.listFiles(new FileFilter() {
						@Override
						public boolean accept(File f) {
							if (f.isFile()) {
								if (f.getName().endsWith("json") && f.getName().startsWith("applica")) {
									return true;
								}
							}
							return false;
						}
					});
					for (File file1 : fils) {
						AplicationBean ab = mapper.readValue(file1, AplicationBean.class);
						if (search != null) {
							if (ab.getProjectBase().getTxname().toLowerCase().contains(search)
									|| ab.getProjectBase().getNamespace().toLowerCase().contains(search)) {
								applicationsStudio.add(ab);
							}
						} else {
							applicationsStudio.add(ab);
						}
					}
				}
			}
			new SortCollections().sort(applicationsStudio, "projectBase.txname", true);
			if (ctxBean.getPlatform() != null) {
				this.projectBase = applicationsStudio.get(0).getProjectBase();
				ctxBean.setPj(applicationsStudio.get(0).getProjectBase());
			}
			BindUtils.postNotifyChange(null, null, this, "applicationsStudio");
		} catch (Exception e) {
			log.error(e);
			showNotification(TYPENOTI.ALERT, "please send error to support with CODE findEntitys-001");
		}
	}

	private void listProjects(List<Object> appss) throws Exception, JsonMappingException, IOException {
		applications.clear();
		for (Object ap : appss) {
			apps.forEach(app -> {
				if ((Long) getNested(app, "idxaplicacion") == (Long) getNested(ap, "idssoaplicacion.idxaplicacion")) {
					AplicationBean ab = new AplicationBean();
					ab.setData(ap);
					ab.setProjectBase(new AppProject());
					ab.getProjectBase().setTxname((String) getNested(ap, "idssoaplicacion.aplicacion"));
					ab.getProjectBase().setNamespace((String) getNested(ap, "idssoaplicacion.namespace"));
					ab.getProjectBase().setIdxproject((Long) getNested(ap, "idssoaplicacion.idxaplicacion"));
					ab.getProjectBase().setIcono((String) getNested(ap, "idssoaplicacion.iconclass"));
					ab.getProjectBase().setTitle((String) getNested(ap, "idssoaplicacion.titulo"));
					applications.add(ab);
				}
			});
		}
		new SortCollections().sort(applications, "projectBase.txname", true);
		loadMenusPerms();
	}

	private void deleteApp(AplicationBean app) throws Exception, IllegalAccessException, ClassNotFoundException {
		SchemaBD schema;
		IMetaModel metamodel;
		DatabaseBean defaultDatbaseBean = null;
		String path = ctxBean.getPlatform().getPathHome();
		try {
			FileUtils.forceDelete(
					new File(path + File.separator + "apps" + File.separator + app.getProjectBase().getNamespace()));
		} catch (Exception e) {
			log.error(e);
		}
		try {
			FileUtils.forceDelete(
					new File(path + File.separator + "studio" + File.separator + app.getProjectBase().getNamespace()));
		} catch (Exception e) {
			log.error(e);
		}
		applications.remove(app);
		BindUtils.postNotifyChange(null, null, this, "applications");
	}

	private void addApplication(AppProject project) {
		ctxBean.setPj(project);
		appendDashboard(null, "application.zul", null);
		// appendPage("application.zul", view, null);

	}

	String modulo;

	public String getModulo() {
		return modulo;
	}

	private void loadMenusPerms() throws Exception, JsonMappingException, IOException {
		Field r = permisos.getFieldByAtribute("ROL").copy();
		try {
			r.setValue((List<String>) getUser().getRoles());
		} catch (UiException e1) {
			log.error(e1);
		}
		List<String> lfields = new ArrayList<>();
		permisos.getFields().forEach(f -> {
			lfields.add(f.getAtribute());
		});
		QueryBuilder.buildQueryWithoutJoin(permisos, lfields.toArray(new String[lfields.size()]));
		applications.forEach(ab -> {
			Field a = permisos.getFieldByAtribute("IDXAPLICACION").copy();
			a.setValue(ab.getProjectBase().getIdxproject());
			criterias = new Criterias();
			criterias.addCriteria(new Criteria(Operation.AND, Evaluation.IN, r));
			criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, a));
			DataScroll ds = new DataScroll();
			ds.setMaxRows(5000);
			try {
				addMenus(ab, dao.query(clsPermisos, ds,
						QueryBuilder.buildQueryWithoutJoin(permisos, lfields.toArray(new String[lfields.size()])),
						criterias).getDataList());
				Executions.getCurrent().getSession().getAttributes().put("APP", ab);
			} catch (Exception e1) {
				log.error(e1);
			}
		});
	}

	private void addMenus(AplicationBean ab, List<Object> perms) throws Exception {
		if (perms.size() == 0)
			return;
		new SortCollections().sort(perms, "menu", true);
		String smenu = "";
		MenuBean appMenu = new MenuBean();
		appMenu.setName(ab.getProjectBase().getTitle());
		appMenu.setIconClass(ab.getProjectBase().getIcono());
		appMenu.setItems(new ArrayList<>());
		MenuBean menuBean = null;
		List<String> items = new ArrayList<>();
		for (Object perm : perms) {
			String menu = (String) getNested(perm, "menu");
			if (!menu.equals(smenu)) {
				smenu = menu;
				if (menuBean == null) {
					menuBean = new MenuBean();
					Optional<Object> app = apps.stream()
							.filter(app1 -> getNested(app1, "idxaplicacion").equals(getNested(perm, "idxaplicacion")))
							.findFirst();
					if (app.isPresent()) {
						if (getNested(app.get(), "dashboard") != null) {
							appMenu.setDesktop(true);
							appMenu.setNamespace(getNested(app.get(), "namespace").toString());
							appMenu.setPageUrl(getNested(app.get(), "dashboard").toString());
							appMenu.setId(String.valueOf(getNested(perm, "idxaplicacion")));
						}
					}
					menuBean.setItems(new ArrayList<>());
				} else {
					appMenu.getItems().add(menuBean);
					menuBean = new MenuBean();
					menuBean.setItems(new ArrayList<>());
					menuBean.setId(String.valueOf(getNested(perm, "idxssomenu")));
					Object ssoMenu = dao.findById(ClsSsomenu, (Long) getNested(perm, "idxssomenu"));
					menuBean.setDescripcion((String) getNested(ssoMenu, "descripcion"));
					menuBean.setPageUrl((String) getNested(ssoMenu, "dashboardpage"));
					menuBean.setNamespace((String) getNested(ssoMenu, "namespace"));
					menuBean.setIconClass((String) getNested(ssoMenu, "icono"));
					menuBean.setLoaded(true);
					if (menuBean.getPageUrl() != null && menuBean.getNamespace() != null) {
						menuBean.setDesktop(true);
					}
				}
				menuBean.setIconClass((String) getNested(perm, "icono"));
				menuBean.setName(smenu);
			}
			if (!items.contains((String) getNested(perm, "item"))) {
				items.add((String) getNested(perm, "item"));
				MenuBean item = new MenuBean();
				item.setId(String.valueOf(getNested(perm, "idxssomenuitem")));
				item.setName((String) getNested(perm, "item"));
				item.setNamespace((String) getNested(perm, "namespace"));
				item.setPageUrl((String) getNested(perm, "page"));

				menuBean.getItems().add(item);
			}
		}
		appMenu.getItems().add(menuBean);
		menus.add(appMenu);
	};

	private void initDao() {
		if (dao == null) {
			dao = new EntityDao();
			((EntityDao) dao).setDataSource(ds);
		}
	}

	@Wire
	Navbar navbar2;
	@Wire
	Div iddesktop;

	@NotifyChange("*")
	@GlobalCommand("selectNamespace")
	public void onSelectNamespace(@BindingParam("namespace") String namespace) throws UiException {
		ctxBean.setPj(applications.stream().filter(ab -> ab.getProjectBase().getNamespace().equals(namespace))
				.findFirst().get().getProjectBase());
		this.projectBase = ctxBean.getPj();
		this.namespace = namespace;
		// onSelectItem(item, menu,null);
	}

	String arquitectura;
	String project;
	String pageArq;
	String pageTitle;
	String menuArquitectura = "include/menuArquitecture.zul";

	public String getArquitectura() {
		return arquitectura;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getMenuArquitectura() {
		return menuArquitectura;
	}

	Div contenedor;
	Div blocked;

	@GlobalCommand("selectDataPage")
	public void onSelectPageArchitecture(@BindingParam("page") String pageArq, @BindingParam("title") String title,
			@BindingParam("menu") String menu) {
		if (contenedor == null) {
			contenedor = (Div) iddesktop.getFellow("contenedor", true);
		}
		this.menu = menu;
		this.pageTitle = title;
		if (pageArq.indexOf("${ARQ}") != -1) {
			Map<String, Object> args = new HashMap();
			args.put("PATH_MODEL", pathDirModel);
			String pathDirScreen = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(item.getNamespace()).toLowerCase() + File.separator + "webapp"
					+ File.separator + "pages";
			String pathDirSchema = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(item.getNamespace()).toLowerCase() + File.separator + "webapp"
					+ File.separator + "screens";
			args.put("PATH_SCREEN", pathDirScreen);
			args.put("PATH_SCHEMA", pathDirSchema);
			appendPage(pathDirScreen + File.separator + pageArq, contenedor, args);
		} else {
			appendPage(pageArq, contenedor, null);
		}
	}

	@NotifyChange("*")
	@GlobalCommand
	public void onCreateDashboard(@BindingParam("container") Div container, @BindingParam("sidebar") Div sidebar)
			throws UiException {
		container.getChildren();
		if (item.isDesktop()) {
			item.getPage();
			item.getPageUrl();
			if (item.getNamespace() != null) {
				appendDashboard(item.getNamespace(), item.getPageUrl(), container);
			} else if (item.getPageUrl() != null) {
				appendDashboard(null, item.getPageUrl(), container);
			}

		} else {

			item.getItems().forEach(menu -> {
				if (menu.isDesktop()) {
					item.getPage();
				}
			});
		}

	}

	@NotifyChange("*")
	@GlobalCommand
	public void onSelectMenu(@BindingParam("item") MenuBean item, @BindingParam("menu") Drawer drawer)
			throws UiException {
		items.clear();
		items.addAll(item.getItems());
		this.item = item;
		appendPage("dashboardapp.zul", iddesktop, Collections.emptyMap());
		drawer.close();
	}

	@GlobalCommand("selectItem")
	@NotifyChange({ "bloqueo", "contenedor" })
	public void onSelectItem(@BindingParam("item") MenuBean item, @BindingParam("main") String menu,
			@BindingParam("contenedor") Div contenedor) throws UiException {
		this.menu = menu;
		this.item = item;
		boolean bloqueo = true;
		if (blocked == null) {
			blocked = (Div) iddesktop.getFellow("blocked", true);
		}

		bloqueo = false;

		contenedor.getChildren().clear();
		if (item.getNamespace() == null && item.getPage() == null && item.getPageUrl() == null) {
			executeProcess(item);
		} else if (item.getNamespace() == null && item.getPageUrl() != null) {
			appendPage(item.getPageUrl(), contenedor, null);
		} else {
			Map<String, Object> args = new HashMap();
			args.put("PATH_MODEL", pathDirModel);
			String pathDirScreen = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(item.getNamespace()).toLowerCase() + File.separator + "webapp"
					+ File.separator + "pages";
			String pathDirSchema = ctxBean.getPathHome() + File.separator + "apps" + File.separator
					+ TextParser.getFormaterUnixPath(item.getNamespace()).toLowerCase() + File.separator + "webapp"
					+ File.separator + "screens";
			args.put("PATH_SCREEN", pathDirScreen);
			args.put("PATH_SCHEMA", pathDirSchema);
			args.put("CONTAINER", contenedor);
			args.put("BLOCKED", bloqueo);
			applications.forEach(ap -> {
				if (ap.getProjectBase().getNamespace().equals(item.getNamespace())) {
					setApplication(ap);
				}
			});
			if (item.getPageUrl() != null) {
				appendPage(pathDirScreen + File.separator + item.getPageUrl(), contenedor, args);
			} else {
				String prefix = item.getPrefix() != null ? item.getPrefix() : "index";
				appendPage(pathDirScreen + File.separator + prefix + item.getNameScreen() + ".zul", contenedor, args);
			}
		}
		if (bloqueo) {
			blocked.setVisible(true);
		} else {
			blocked.setVisible(false);
		}
	}

	private void configureWebClientPost(WebConnect webclient, String path) {
		List<Pair<HttpHeaders, String>> headers = new ArrayList<>();
		Pair p = Pair.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.add(p);
		WebConfig config = WebConfig.builder().method(HttpMethod.POST).headers(headers)
				.url(ctxBean.getConfig().getMicros().get("bpmn").getBaseUrl()
						+ ctxBean.getConfig().getMicros().get("bpmn").getPaths().get(path))
				.build();
		log.debug(ctxBean.getConfig().getMicros().get("bpmn").getBaseUrl()
				+ ctxBean.getConfig().getMicros().get("bpmn").getPaths().get(path));
		webclient.build(config);
	}

	private void executeProcess(MenuBean item) throws UiException {
		try {
			WebConnect<TaskDto, ProcessRQ> webclient = new WebConnect<TaskDto, ProcessRQ>();
			Object submenu = dao.loadById(clsSsomenuitem, Ssomenuitem, new Long(item.getId()));
			Object process = getNested(submenu, "idbpmmproces");
			String key = (String) getNested(process, "keyprocess");
			ProcessRQ pq = new ProcessRQ();
			pq.setProcessKey(key);
			pq.setVars(new HashMap());
			pq.setRoles(new ArrayList());

			List<String> lfields = new ArrayList<>();
			Bpmnvprocessemails.getFields().forEach(sfield -> {
				lfields.add(sfield.getAtribute());
			});
			// idssoaplicacion
			query = QueryBuilder.buildQueryWithInnerJoin(Bpmnvprocessemails,
					lfields.toArray(new String[lfields.size()]));
			criterias = new Criterias();
			Field iduser = Bpmnvprocessemails.copy().getFieldByAtribute("IDBPMMPROCES0").copy();
			iduser.setField("IDBPMMPROCES0");
			iduser.setAtribute("IDBPMMPROCES0");
			iduser.setValue(getNested(process, process.getClass().getAnnotation(Entidad.class).pk()));
			criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, iduser));
			dao.query(clsBpmnvprocessemails, new DataScroll(), query, criterias).getDataList().forEach(cls -> {
				pq.getVars().put(((String) getNested(cls, "grupo")).toUpperCase(), getNested(cls, "emails"));
				pq.getVars().put(((String) getNested(cls, "grupo")).toUpperCase() + "CC", getNested(cls, "emailscc"));
				pq.getVars().put(((String) getNested(cls, "grupo")).toUpperCase() + "BC", getNested(cls, "emailsbcc"));
			});
			pq.setCustomer(new CustomerDto());
			pq.getVars().put("APP_USER_USER", getUser().getUsername());
			pq.getVars().put("APP_USER_NAME", getUser().getFirstname());
			pq.getVars().put("APP_USER_LASTNAME", getUser().getLastname());
			pq.getVars().put("APP_USER_FULLNAME", getUser().getFullName());
			pq.getVars().put("APP_USER_EMAIL", getUser().getEmailuser());
			getUser().getRoles().forEach(rol -> {
				pq.getRoles().add(rol.toString().toUpperCase());
			});
			configureWebClientPost(webclient, "startAndGo");
			webclient.setBody(pq);
			webclient.setResponseData(TaskDto.class);
			TaskDto td = webclient.call();
			if (td.getFormKey() != null) {
				navigateTo(td);
			}
		} catch (DaoException e) {
			log.error(e.getMessage());
		}
	}

	public void navigateTo(TaskDto td) {
		String namespace = td.getFormKey().substring(0, td.getFormKey().indexOf("|"));
		Map<String, Object> params1 = new HashMap();
		params1.put("PATH_SCREEN", ctxBean.getPathHome() + "/apps/" + namespace + "/webapp/screens");
		params1.put("PATH_SCHEMA", ctxBean.getPathHome() + "/apps/" + namespace + "/webapp/screens");
		params1.put("PATH_MODEL", ctxBean.getPathHome() + "/data/model");
		params1.put("TASK", td);
		params1.put("page", ctxBean.getPathHome() + "/apps/bpmn/webapp/pages/bandeja.zul");
		String url = ctxBean.getPathHome() + "/apps/" + File.separator
				+ td.getFormKey().substring(0, td.getFormKey().indexOf("|")) + "/webapp/pages/"
				+ td.getFormKey().substring(td.getFormKey().indexOf("|") + 1, td.getFormKey().length());

		appendPage(url, iddesktop, params1);

	}

	@Command()
	/**
	 * utilizado para los asistentes de paginas iniciales o menu estaticos
	 * 
	 * @param page
	 */
	public void navigate(@BindingParam("page") String page) {
		Map<String, Object> args = new HashMap();
		appendPage(page, iddesktop, args);
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

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	// Crean una cookie con nombre y valor.
	public void setCookie(String name, String value) {
		HttpServletResponse resp = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
		resp.addCookie(new Cookie(name, value));
	}

	/**
	 * Cambia el tema de Zkoss entre compact y no, dependiendo del valor de la
	 * cookie, y después cambia el valor de la cookie.
	 *
	 * @author Ismael
	 *
	 */
	@NotifyChange("*")
	@Command("changeCompactScreen")
	public void compactScreenToggle() {
		String currentTheme = Themes.getCurrentTheme();
		if (currentTheme.endsWith("_c")) {
			// Si termina con "_c", lo quito
			currentTheme = currentTheme.substring(0, currentTheme.length() - 2);
			setCookie("compactScreen", "");
		} else {
			// Si no termina con "_c", lo agrego
			currentTheme = currentTheme + "_c";
			setCookie("compactScreen", "_c");
		}
		Themes.setTheme(Executions.getCurrent(), currentTheme);
		Library.setProperty("org.zkoss.theme.preferred", currentTheme);
		Executions.sendRedirect("");
	}

	@Destroy(superclass = true)
	public void destroy() {
		if (beans != null) {
			beans.clear();
			beans = null;
		}
		builder = null;
		Ssoruserapp = null;
		eUsuario = null;
		permisos = null;
		menu = null;
		Ssomenuitem = null;
		item = null;
		menu = null;
		if (menus != null) {
			menus.clear();
			menus = null;
		}
		if (applicationsStudio != null) {
			applicationsStudio.clear();
			applicationsStudio = null;
		}
		if (items != null) {
			items.clear();
			items = null;
		}
		if (sapps != null) {
			sapps.clear();
			sapps = null;
		}
		if (apps != null) {
			apps.clear();
			apps = null;
		}
		JavaMemManagement.cleanMemory();
	}
}
