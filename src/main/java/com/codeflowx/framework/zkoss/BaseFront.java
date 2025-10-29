package com.codeflowx.framework.zkoss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.enartframework.core.shared.commons.system.JavaMemManagement;
import org.enartframework.core.shared.context.EnartContext;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.nocode.datamodel.jdbc.Criterias;
import org.enartframework.orm.DataScroll;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.security.Usuario;
import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.annotation.View;
import org.enartframework.web.exception.UiException;
import org.enartframework.web.zk.page.BaseMasterHandler;
import org.enartframework.zk.utils.MessageWin;
import org.enartframework.zk.utils.Notificacion;
import org.enartframework.zk.utils.Notificacion.TYPENOTI;
import org.enartframework.zk.utils.bind.BeanValidator;
import org.enartframework.zk.utils.bind.LabelFieldConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public abstract class BaseFront<T> extends SelectorComposer implements Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String KEY_ORDER_VIEW_CTRL = "KEY_ORDER_VIEW_CTRL";
	public static final String IDDESKTOP = "iddesktop";
	protected Map<String, Object> params1 = new HashMap<>(0);
	protected DataScroll scroll;
	protected Criterias criterias;
	protected Object beanFind;
	protected Object dataParam;
	protected Integer pagActive;
	protected String search;
	protected View aview;
	protected Map<String, Object> params = new HashMap<String, Object>(0);
	protected Map<String, Object> paramsReload = new HashMap<String, Object>(0);
	protected Action action;
	String breadcrumb;
	protected String bus;
	protected int activePage = 0;
	protected boolean popup = false;
	protected String moduleCall;
	protected String catalogModule;
	protected Object vm;
	protected Object screenModeTmpl;;
	protected Component container;
	protected final static Map<String, DataSource> DB_POOLMAP = new ConcurrentHashMap<>(10);
	@WireVariable
	protected Page page;
	@WireVariable
	protected Desktop desktop;
	@WireVariable
	protected Session session;
	@WireVariable
	protected WebApp webapp;
	// @WireVariable("desktopScope")
	// protected Map<String, Object> desktopScope;
	@WireVariable("fileSystem")
	protected Properties filesystem;
	protected Component view;
	protected EventQueue eventQ;
	protected BindContext bindContext;
	protected Binder binder;
	private Usuario user;
	private BeanValidator validator;
	protected LabelFieldConverter fieldConverter;
	protected Component subpage;
	public void setUser(Usuario user) {
		this.user = user;
		Executions.getCurrent().getDesktop().getSession().setAttribute("USER", user);
	}

	public Usuario getUser() throws UiException {
		if (user == null) {
			if (Executions.getCurrent().getDesktop().getSession().getAttribute("USER") != null) {
				user = (Usuario) Executions.getCurrent().getDesktop().getSession().getAttribute("USER");
			}
		}

		return user;
	}
	public LabelFieldConverter getFieldConverter() {
		if(fieldConverter==null)fieldConverter = new LabelFieldConverter();
		return fieldConverter;
	}
	public Validator getValidator() {
		if(validator==null)validator = new BeanValidator();
		return validator;
	}
	// ========== Servicios y contexto Spring ==========
    @WireVariable
    protected BusinessService businessService;
    
    @Autowired
    protected IEntityLocal dao;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    @WireVariable("APPLICATION_DS")
    protected DataSource ds;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    /**
     * Registra la actividad del usuario en el sistema de auditoría
     */
    protected void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
	@Init
	public void init(@ExecutionArgParam("SCREEN_MODEL") Object screenModel, @BindingParam("vm") Object vm,
			@ExecutionArgParam("paginaIndex") String pagina, @BindingParam("pagina") String idpagina,
			@ContextParam(ContextType.VIEW) Component view,
			@ContextParam(ContextType.BIND_CONTEXT) BindContext bindContext,
			@ContextParam(ContextType.BINDER) Binder binder, @ExecutionArgParam("labelPage") String label,
			@ExecutionArgParam("beanFind") Object beanF, @ExecutionArgParam("scroll") DataScroll scroll,
			@ExecutionArgParam("criterias") Criterias criterias, @ExecutionArgParam("searchField") String search,
			@ExecutionArgParam("activePage") Integer page, @ExecutionArgParam("bean") Object bean,
			@ExecutionArgParam("action") Action action, @ExecutionArgParam("dataParam") Object dataParam,
			@ExecutionArgParam("event") String bus, @ExecutionArgParam("popup") Boolean popup,
			@ExecutionArgParam("module") String module, @ExecutionArgParam("CATALOGO") String catalogo,
			@ExecutionArgParam("CONTAINER") Component container) throws Exception {
		this.container = container;
		if (container != null) {
			paramsReload.put("CONTAINER", container);
		}
		this.bindContext = bindContext;
		this.binder = binder;
		if (label != null) {
			this.breadcrumb = label;
			paramsReload.put("labelPage", label);
		}
		screenModeTmpl = screenModel;
		paramsReload.put("SCREEN_MODEL", screenModeTmpl);

		if (catalogo != null) {
			catalogModule = catalogo;
		}
		if (pagina != null) {
			params.put("page", pagina);
			paramsReload.put("paginaIndex", pagina);
		}
		if (vm != null) {
			this.vm = vm;
		}
		if (beanF != null) {
			beanFind = beanF;
			paramsReload.put("beanFind", beanFind);
		}
		if (module != null) {
			this.moduleCall = module;
			paramsReload.put("module", module);
		}
		if (bus != null)
			this.bus = bus;
		if (popup != null)
			this.popup = popup;
		if (page != null) {
			pagActive = page;
			paramsReload.put("activePage", page);
		}
		if (scroll != null) {
			this.scroll = scroll;
			paramsReload.put("scroll", scroll);
		}
		if (criterias != null) {
			this.criterias = criterias;
			paramsReload.put("criterias", criterias);
		}
		if (action != null) {
			this.action = action;
			paramsReload.put("action", action);
		}
		if (dataParam != null) {
			this.dataParam = dataParam;
			paramsReload.put("dataParam", dataParam);
		}
		if (search != null) {
			this.search = search;
			paramsReload.put("searchField", search);
		}
		if (getClass().isAnnotationPresent(View.class)) {
			this.aview = getClass().getAnnotation(View.class);
		}
		if (bean != null && aview != null) {
			// try {
			if (bean != null && bean instanceof Map) {
				this.params = (Map<String, Object>) bean;
			} else {
				// PropertyUtils.setProperty(this, aview.bean(), bean);
				setBeans(bean);
			}

//			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//				log.error(e);
//			}
		} else {
			if (bean != null && bean instanceof Map) {
				this.params = (Map<String, Object>) bean;
			}
		}
		if (view.getDesktop().getExecution().getArg().containsKey("PATH_SCREEN")) {
			paramsReload.put("PATH_SCREEN", view.getDesktop().getExecution().getArg().get("PATH_SCREEN"));
			paramsReload.put("PATH_SCHEMA", view.getDesktop().getExecution().getArg().get("PATH_SCHEMA"));
			paramsReload.put("PATH_MODEL", view.getDesktop().getExecution().getArg().get("PATH_MODEL"));
		}
		preAfterCompose(view, idpagina);
		log.debug("init super");

	}

	public abstract void setBeans(Object bean);

	public void preAfterCompose(Component view, String idpagina) throws Exception {

	}
	Object child;
	public void doAfterCompose(Component view) throws Exception {
		super.doAfterCompose(view);
		this.view = view;
		this.child = child;
		desktop.setAttribute(KEY_ORDER_VIEW_CTRL, this);
	}

	@Command("toSearch")
	public void volver() {

		params1.put("beanFind", beanFind);
		params1.put("scroll", scroll);
		params1.put("activePage", pagActive);
		params1.put("criterias", criterias);
		params1.put("searchField", search);
		if (container != null) {
			params1.put("CONTAINER", container);
		}
		if (this.params.containsKey("page")) {
			if (container != null) {
				appendPage(params.get("page").toString(), container, params1);
			} else {
				if (page.getFellowIfAny(IDDESKTOP) != null) {
					appendPage(params.get("page").toString(), page.getFellow(IDDESKTOP), params1);
				} else {
					appendPage(params.get("page").toString(), view.getParent().getFellow(IDDESKTOP), params1);
				}
			}
		} else {
			if (page.getFellowIfAny(aview.idpageEmbed()) != null) {
				appendPage(params.get("page").toString(), page.getFellow(aview.idpageEmbed()), params1);
			} else {
				appendPage(params.get("page").toString(), view.getParent().getFellow(aview.idpageEmbed()), params1);
			}
		}
	}

	/**
	 * reload la pagina master
	 * 
	 * @param databean
	 * @param message
	 * @param url
	 */
	public void reloadFromStore(Object databean, String message, String url) {

		paramsReload.put("action", Action.LOADFOREDIT);
		if (message != null) {
			paramsReload.put("notification", message);
		}
		paramsReload.put("bean", databean);
		if (this.paramsReload.containsKey("page")) {
			if (container != null) {
				appendPage(url, container, paramsReload);
			} else {
				if (page.getFellowIfAny(IDDESKTOP) != null) {
					appendPage(url, page.getFellow(IDDESKTOP), paramsReload);
				} else {
					appendPage(url, view.getParent().getFellow(IDDESKTOP), paramsReload);
				}
			}
		} else {
			if (container != null) {
				appendPage(url, container, paramsReload);
			} else {
				if (page.getFellowIfAny(aview.idpageEmbed()) != null) {
					appendPage(url.toString(), page.getFellow(aview.idpageEmbed()), paramsReload);
				} else {
					appendPage(url, view.getParent().getFellow(aview.idpageEmbed()), paramsReload);
				}
			}

		}
	}

	public void returnFromStore(String message) {

		params1.put("beanFind", beanFind);
		params1.put("scroll", scroll);
		params1.put("activePage", pagActive);
		params1.put("criterias", criterias);
		params1.put("searchField", search);
		params1.put("activePage", activePage);
		if (container != null) {
			params1.put("CONTAINER", container);
		}
		if (message != null) {
			params1.put("notification", message);
		}
		if (this.params.containsKey("page")) {
			if (container != null) {
				appendPage(params.get("page").toString(), container, params1);
			} else {
				if (page.getFellowIfAny(IDDESKTOP) != null) {
					appendPage(params.get("page").toString(), page.getFellow(IDDESKTOP), params1);
				} else {
					appendPage(params.get("page").toString(), view.getParent().getFellow(IDDESKTOP), params1);
				}
			}
		} else {
			if (page.getFellowIfAny(aview.idpageEmbed()) != null) {
				appendPage(params.get("page").toString(), page.getFellow(aview.idpageEmbed()), params1);
			} else {
				appendPage(params.get("page").toString(), view.getParent().getFellow(aview.idpageEmbed()), params1);
			}
		}
	}

	public void navigateTo(Map<String, Object> params, String url) {
		try {
			Object beanFind = PropertyUtils.getSimpleProperty(this, aview.bean());
			params.put("beanFind", beanFind);
			scroll.getDataList().clear();
			params.put("scroll", scroll);
			params.put("criterias", criterias);
			params.put("searchField", search);
			params.put("activePage", activePage);
			if (container != null) {
				params.put("CONTAINER", container);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.error(e.getLocalizedMessage());
		}

		params.put("action", Action.LOADFOREDIT);
		if (container != null) {
			appendPage(url, container, params);
		} else {
			if (page.getFellowIfAny(IDDESKTOP) != null) {
				appendPage(url, page.getFellow(IDDESKTOP), params);
			} else {
				appendPage(url, view.getFellow(IDDESKTOP), params);
			}
		}

	}

	

	public void edit(@BindingParam("item") Object data) {
		Map<String, Object> params = new HashMap<>(0);
		try {
			Object beanFind = PropertyUtils.getSimpleProperty(this, aview.bean());
			params.put("beanFind", beanFind);
			scroll.getDataList().clear();
			params.put("scroll", scroll);
			params.put("criterias", criterias);
			params.put("searchField", search);
			params.put("activePage", activePage);
			if (container != null) {
				params.put("CONTAINER", container);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.error(e.getLocalizedMessage());
		}
		if (data != null) {
			params.put("bean", data);
			params.put("action", Action.LOADFOREDIT);
		} else {
			params.put("action", Action.NEW);
		}
		if (container != null) {
			appendPage(aview.pageMaster(), container, params);
		} else {
			appendPage(aview.pageMaster(), page.getFellow(IDDESKTOP), params);
		}

	}

	private void showMessagebox(String message, String method, String messageConfirm) {
		MessageWin.getMessageBox().show(message, Labels.getLabel("confirm.title", "Confirmar"),
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION,
				new EventListener<ClickEvent>() {
					@Override
					public void onEvent(ClickEvent event) throws Exception {
						if (Messagebox.Button.YES.equals(event.getButton())) {
							try {
								MethodUtils.invokeMethod(child, method, null);
								if (!messageConfirm.equals("") || messageConfirm.length() > 3) {
									MessageWin.getMessageBox().show(messageConfirm);
								}
							} catch (InvocationTargetException e) {
								log.error(e.getMessage());
								if (e.getCause() instanceof org.enartframework.web.exception.UiException) {
									String code = ((org.enartframework.web.exception.UiException) e.getCause())
											.getCode();
									int icode = ((org.enartframework.web.exception.UiException) e.getCause())
											.getMessageCode();
									String msg = ((org.enartframework.web.exception.UiException) e.getCause())
											.getMessage();
									MessageWin.getMessageBox().show(
											Labels.getLabel(code, "Error Code:" + icode + " msg:" + msg), "Error",
											Messagebox.OK, Messagebox.ERROR);
								} else {
									MessageWin.getMessageBox()
											.show(Labels.getLabel("system.error.nocontroled",
													"Error no controlado, por favor contacte con el administrador"),
													"Error", Messagebox.OK, Messagebox.ERROR);
								}

							}

						}
					}
				});
	}

	/**
	 * incluye una pagina elimando los hijos del componente recibido
	 * 
	 * @param page
	 * @param parent
	 * @param params
	 */
	public void appendPage(String page, Component parent, Map params) {
		try {
//		if(subpage!=null) {
//			subpage.detach();
//		}
			parent.getChildren().clear();
			JavaMemManagement.cleanMemory();
			InputStream zulInput = this.getClass().getClassLoader().getResourceAsStream(page);

			if (zulInput == null) {
				// verificamos file
				if (new File(page).exists()) {
					zulInput = new FileInputStream(new File(page));
				}
			}
			Reader zulReader = null;

			if (zulInput != null) {
				zulReader = new InputStreamReader(zulInput);
				subpage = Executions.createComponentsDirectly(zulReader, "zul", parent != null ? parent : view, params);
			} else {
				subpage = Executions.createComponents(page, parent, params);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			MessageWin.getMessageBox().show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * muestra una notificcion en pantalla
	 * 
	 * @param type
	 * @param message
	 */
	public void showNotification(TYPENOTI type, String message) {
		if (view.getFellowIfAny("notificacion") == null) {
			if (type.equals(TYPENOTI.ALERT) || type.equals(TYPENOTI.WARNING)) {
				MessageWin.getMessageBox().show(message, "Error", Messagebox.OK, Messagebox.ERROR);
			} else if (type.equals(TYPENOTI.INFORMATION)) {
				MessageWin.getMessageBox().show(message, "Información", Messagebox.OK, Messagebox.INFORMATION);
			} else if (type.equals(TYPENOTI.SUCCESS)) {
				MessageWin.getMessageBox().show(message, "Información", Messagebox.OK, Messagebox.INFORMATION);
			}
		} else {
			view.getFellowIfAny("notificacion").getChildren().clear();
			view.getFellowIfAny("notificacion").appendChild(new Notificacion(type, message));
			String timer = "5000";
			if (type.equals(TYPENOTI.ALERT) || type.equals(TYPENOTI.WARNING)) {
				timer = "35000";
			} else if (type.equals(TYPENOTI.INFORMATION)) {
				timer = "15000";
			}
			Clients.evalJavaScript("jq('$notificacion').show().slideUp(" + timer + ")");
		}
	}

	/**
	 * muestra una notificcion en pantalla
	 * 
	 * @param type
	 * @param message
	 */
	public void showNotification(TYPENOTI type, String message, String timer) {
		if (view.getFellowIfAny("notificacion") != null) {
			view.getFellowIfAny("notificacion").getChildren().clear();
			view.getFellowIfAny("notificacion").appendChild(new Notificacion(type, message));
			Clients.evalJavaScript("jq('$notificacion').show().slideUp(" + timer + ")");
		}
	}

	/**
	 * trata y muestra el error
	 * 
	 * @param e
	 */
	public void showError(Exception e, Object data) {
		if (e.getCause() instanceof org.enartframework.web.exception.UiException) {
			String code = ((org.enartframework.web.exception.UiException) e.getCause()).getCode();
			int icode = ((org.enartframework.web.exception.UiException) e.getCause()).getMessageCode();
			String msg = ((org.enartframework.web.exception.UiException) e.getCause()).getMessage();
			MessageWin.getMessageBox().show(Labels.getLabel(code, "Error Code:" + icode + " msg:" + msg), "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else if (e instanceof DaoException) {
			DaoException de = (DaoException) e;
			if (data != null)
				log.error("error .." + data.toString());
			log.error(de.getMessage());
			MessageWin.getMessageBox().show(de.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			MessageWin.getMessageBox()
					.show(Labels.getLabel("system.error.nocontroled",
							"Error no controlado, por favor contacte con el administrador"), "Error", Messagebox.OK,
							Messagebox.ERROR);
			log.error(e.getMessage());
		}
	}

	public Messagebox getMessageboxBy() {
		return MessageWin.getMessageBox();
	}

	/**
	 * trata y muestra el error
	 * 
	 * @param e
	 */
	public void showError(int icode, String code, String msg) {
		MessageWin.getMessageBox().show(Labels.getLabel(code, "Error Code:" + icode + " " + msg), "Error",
				Messagebox.OK, Messagebox.ERROR);
	}

	public void showError(String msg) {
		MessageWin.getMessageBox().show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
	}

	/**
	 * incluye un componente
	 * 
	 * @param page
	 * @param parent
	 * @param params
	 */
	public Component addComponent(String page, Component parent, Map params) {
		try {
			if (parent != null) {
				parent.getChildren().forEach(c -> {
					c.detach();
				});
			}
			JavaMemManagement.cleanMemory();
			InputStream zulInput = this.getClass().getClassLoader().getResourceAsStream(page);
			Reader zulReader = null;
			if (zulInput != null) {
				zulReader = new InputStreamReader(zulInput);

				subpage = Executions.createComponentsDirectly(zulReader, "zul", parent != null ? parent : view, params);

			} else {
				subpage = Executions.createComponents(page, parent, params);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			MessageWin.getMessageBox().show(
					Labels.getLabel("error.system.pagenofound", "No existe el componente/pagina"), "Error",
					Messagebox.OK, Messagebox.ERROR);
		}
		return subpage;
	}

}
