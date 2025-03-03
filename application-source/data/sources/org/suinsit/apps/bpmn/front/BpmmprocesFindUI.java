package org.suinsit.apps.bpmn.front;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.enartframework.core.shared.commons.TextParser;
import org.enartframework.core.shared.commons.system.JavaMemManagement;
import org.enartframework.core.shared.logger.EnartLoggerFactory;
import org.enartframework.core.shared.logger.IEnartLogger;
import org.enartframework.jvm.burningwave.Finder;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.dao.EntityDao;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.nocode.datamodel.jdbc.Criteria;
import org.enartframework.nocode.datamodel.jdbc.Criterias;
import org.enartframework.nocode.datamodel.jdbc.Evaluation;
import org.enartframework.nocode.datamodel.jdbc.Operation;
import org.enartframework.nocode.datamodel.jdbc.Order;
import org.enartframework.nocode.datamodel.jdbc.Order.DIRECTION;
import org.enartframework.nocode.datamodel.model.Entity;
import org.enartframework.nocode.datamodel.model.Field;
import org.enartframework.nocode.datamodel.model.Foreing;
import org.enartframework.nocode.datamodel.model.Query;
import org.enartframework.nocode.datamodel.model.QueryBuilder;
import org.enartframework.nocode.datamodel.model.TYPEFIELD;
import org.enartframework.orm.DataScroll;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
import org.enartframework.suinsit.model.front.MappingField;
import org.enartframework.suinsit.model.front.Service;
import org.enartframework.web.annotation.View;
import org.enartframework.web.exception.UiException;
import org.enartframework.web.zk.page.SearchPageUI;
import org.enartframework.zk.utils.MessageWin;
import org.enartframework.zk.utils.Notificacion.TYPENOTI;
import org.jsoup.Jsoup;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.suinsit.nocode.report.Format;
import org.suinsit.nocode.web.*;
import org.suinsit.nocode.web.components.ReportBuilder;
import org.suinsit.nocode.web.components.SubTableHandler;
import org.suinsit.nocode.web.components.SubTableHandler.EditEvent;
import org.suinsit.nocode.web.converters.ItemsConverter;
import org.suinsit.nocode.web.model.EntityComparator;
import org.suinsit.nocode.web.model.ListModelEntityGroup;
import com.enartsystems.platform.nocode.builder.ScreenField;
import com.enartsystems.platform.nocode.builder.ScreenModel;
import com.enartsystems.webapp.beans.AplicationBean;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.ToServerCommand;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
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
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;
import com.enartsystems.mapping.BuilderDataBase;
import com.enartsystems.mapping.BuilderModel;
import com.enartsystems.mapping.reflection.BeanHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.suinsit.apps.bpmn.Bpmmproces;
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
@View()
@ToServerCommand({ "doLoad", "doLoadBean", "doEdit", "props", "doRemove" })
@Setter
@Slf4j
public class   BpmmprocesFindUI extends FindEntityUI {
	private static final long serialVersionUID = 1L;
	private Bpmmproces data = new Bpmmproces(); 
	public BpmmprocesFindUI() {
	 
		super();

	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("PATH_MODEL") String PATH_MODEL, @ExecutionArgParam("PATH_SCREEN") String PATH_SCREEN,
			@ExecutionArgParam("PATH_SCHEMA") String PATH_SCHEMA) throws Exception {
			
	}
	public void postInit() {
		URLClassLoader urlClassLoader;
		try {
			urlClassLoader = new URLClassLoader(new URL[] { new File(ctxBean.getPathClasses()).toURL() }, classLoader);
			Thread.currentThread().setContextClassLoader(urlClassLoader);
			pathDirModel = ctxBean.getPathHome() + File.separator + "data" + File.separator + "model";
			builderDB = new BuilderDataBase(pathDirModel);
			builder = new BuilderModel(pathDirModel);
			
			
			
			
		} catch (MalformedURLException e) {
			log.error("", e);
		}
	};


}