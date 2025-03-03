package org.suinsit.apps.portalemp.front;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.enartframework.core.shared.commons.TextParser;
import org.enartframework.core.shared.logger.EnartLoggerFactory;
import org.enartframework.core.shared.logger.IEnartLogger;
import org.enartframework.integration.aws.s3.IS3FileHandler;
import org.enartframework.integration.aws.s3.S3Client;
import org.enartframework.integration.aws.s3.S3FileHandler;
import org.enartframework.mathematic.semantic.CalculateMathExpression;
import org.enartframework.mathematic.semantic.MathematicException;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.dao.EntityDao;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.nocode.datamodel.model.Descends;
import org.enartframework.nocode.datamodel.model.Entity;
import org.enartframework.nocode.datamodel.model.Field;
import org.enartframework.nocode.datamodel.model.Foreing;
import org.enartframework.nocode.datamodel.model.Query;
import org.enartframework.nocode.datamodel.model.TYPEFIELD;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
import org.enartframework.suinsit.model.EnumValues;
import org.enartframework.suinsit.model.front.MappingField;
import org.enartframework.suinsit.model.front.Service;
import org.enartframework.web.annotation.View;
import org.enartframework.web.exception.UiException;
import org.enartframework.web.zk.page.MasterPage;
import org.enartframework.zk.utils.MessageWin;
import org.enartframework.zk.utils.Notificacion.TYPENOTI;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.suinsit.nocode.web.components.ForeingsHandlers;
import org.suinsit.nocode.web.components.HideShowAreas;
import org.suinsit.nocode.web.components.MultipleHandler;
import org.suinsit.nocode.web.components.SubTableHandler;
import org.suinsit.nocode.web.converters.ItemsConverter;
import org.suinsit.nocode.web.events.ListenerEmailOperation;
import org.suinsit.nocode.web.events.ListenerMasterOperations;
import org.suinsit.nocode.web.validators.UniqueValidator;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.ToServerCommand;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.video.AVideo;
import org.zkoss.video.Video;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.enartsystems.mapping.BuilderModel;
import com.enartsystems.mapping.reflection.BeanHandler;
import com.enartsystems.platform.nocode.builder.EvaluateFields;
import com.enartsystems.platform.nocode.builder.RandomUUID;
import com.enartsystems.platform.nocode.builder.ScreenDescend;
import com.enartsystems.platform.nocode.builder.ScreenField;
import com.enartsystems.platform.nocode.builder.ScreenModel;
import com.enartsystems.webapp.mapping.BuilderDataBase;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.suinsit.nocode.web.ContextWeb;
import org.suinsit.nocode.web.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.suinsit.apps.portalemp.Rrhhcalendario;
/**
 * @author manuel
 *
 */
@VariableResolver(DelegatingVariableResolver.class)
@View()
@Init(superclass = true)
@Setter
@Getter
//@ToClientCommand("{doLoad}")
//@AutoNotifyChange
@ToServerCommand({ "doLoad", "doLoadBean", "doEdit", "props", "doRemove" })
public class RrhhcalendarioMasterUI extends MasterBeanUI  {
	private static final long serialVersionUID = 1L;
    private Rrhhcalendario data = new Rrhhcalendario(); 
/**
	 * 
	 * @param view
	 * @throws Exception
	 */
	public void preAfterCompose(Component view, String idpagina) throws Exception {
		loadClassLoader();
		if (view.getDesktop().getExecution().getArg().containsKey("PATH_SCREEN")) {
			params1.put("PATH_SCREEN", view.getDesktop().getExecution().getArg().get("PATH_SCREEN"));
			params1.put("PATH_SCHEMA", view.getDesktop().getExecution().getArg().get("PATH_SCHEMA"));
			params1.put("PATH_MODEL", view.getDesktop().getExecution().getArg().get("PATH_MODEL"));
		}
		HttpServletRequest request =(HttpServletRequest) getDesktop().getExecution().getNativeRequest();
		if(request.getParameter("env")!=null) {
			if(request.getParameter("env").equalsIgnoreCase("preview")) {
				//pagina invocada desde el diseñador de pantallas como vista previa
				preview = true;
				params1.remove("PATH_SCHEMA");
				((String)view.getDesktop().getExecution().getArg().get("PATH_SCREEN")).substring(0, ((String)view.getDesktop().getExecution().getArg().get("PATH_SCREEN")).indexOf("preview"));
				params1.put("PATH_SCHEMA", ((String)view.getDesktop().getExecution().getArg().get("PATH_SCREEN")).substring(0, ((String)view.getDesktop().getExecution().getArg().get("PATH_SCREEN")).indexOf("preview")));
			}
		}
		if (view.getDesktop().getExecution().getArg().containsKey("model")) {
			this.moduleCall = (String) view.getDesktop().getExecution().getArg().get("model");
		}
		initDao();
		pathDirModel = params1.containsKey("PATH_MODEL") ? params1.get("PATH_MODEL").toString()
				: environment.getProperty("APPLICATION_DIRMODEL");
		pathDirScreen = params1.containsKey("PATH_SCREEN") ? params1.get("PATH_SCREEN").toString()
				: environment.getProperty("APPLICATION_SCREENS");
		builder = new BuilderModel(pathDirModel);
		builderDatabase = new BuilderDataBase(pathDirModel);
		appName = environment.getProperty("APPLICATION_NAME");
		this.idpagina = idpagina;
		preAfterCompose();

	}
		@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		doAfterCompose(view);
		if (view.getAttribute("type") != null) {
			formType = (String) view.getAttribute("type");
		}
		if (view.getAttribute("msgform") != null) {
			msgform = (String) view.getAttribute("msgform");
		}
		hideShowAreas = new HideShowAreas(view, screenModel);
		pool.execute(new Runnable() {
			@Override
			public void run() {
				hideShowAreas.loadBean(form.copy(), beanData);
			}
		});
		
//		EventQueues.lookup("locale", EventQueues.SESSION, true).subscribe(new EventListener<Event>() {
//			@Override
//			public void onEvent(Event event) throws Exception {
//				updateLocate();
//			}
//		});
		// subtableHandler.setView(view);
		if (bus != null && popup) {
			EventQueues.lookup(bus + "SEND", EventQueues.GROUP, true).subscribe(new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("blank")) {
						clear();

					} else if (event.getName().equals("load")) {
						// Entity e = (Entity)event.getData();
						for (String key : screenModel.getForeingsToForeing().keySet()) {
							screenModel.getForeingsToForeing().get(key).forEach(fk -> {
								try {
									tmp.getForeingByAlias(key).getFkentity().getForeingByAlias(fk)
											.setFkentity(builder.loadEntity(tmp.getForeingByAlias(key).getFkentity()
													.getForeingByAlias(fk).getFkentitynamedb()));
								} catch (Exception e1) {
									log.error(e1);
								}
							});
						}
						// Entity e = builder.
						beanData = dao.loadById(clsBean, form.copy(), (Long) PropertyUtils
								.getNestedProperty(event.getData(), template.getPrimaryKey().toLowerCase()));
						evalDefaultValues(form = tmp.copy(),true);
						beanDataCpy = clsBean.getDeclaredConstructor().newInstance();
						beanHandler.copyValueFields(beanDataCpy, beanData, form.copy());
						appendSubtable = true;
						vlistenerOperations.forEach(lst -> {
							lst.onLoad(beanData, form.copy());
						});
						logActivity("edit",beanData,null,null);
					}
					Clients.evalJavaScript("load" + view.getId() + "();");
				}
			});
		}

		vlistenerOperations.forEach(lst -> {
			lst.onLoad(beanData, form.copy());
		});
	}
}