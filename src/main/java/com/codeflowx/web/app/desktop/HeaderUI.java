/**
 * 
 */
package com.codeflowx.web.app.desktop;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.BaseMasterHandler;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.zkmax.zul.Drawer;

/**
 * @author manuel
 *
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
@Slf4j
@Getter
@Setter
public class HeaderUI extends BaseMasterHandler{
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper();
	@WireVariable("ctxBean")
	protected Context ctxBean;
	@WireVariable("entityDao")
	IEntityLocal dao;
	@WireVariable("context")
	GenericApplicationContext contexto;
	@WireVariable
	private Environment environment;
	private String iniciales;
	

	public String getIniciales() {
		return iniciales;
	}
	public void setIniciales(String iniciales) {
		this.iniciales = iniciales;
	}
	/**
	 * 
	 */
	public HeaderUI() {
		// TODO Auto-generated constructor stub
	}
	
	@Wire
	Drawer drawer;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		Selectors.wireVariables(page, this, Selectors.newVariableResolvers(getClass(), null));
		iniciales = getUser().getFirstname().substring(0,1).toUpperCase()+getUser().getLastname().substring(0,1).toUpperCase();
		this.view = view;
		EventQueues.lookup("messages",EventQueues.SESSION,true).subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				showMessage((String) event.getData());
			}
		});
		
		Selectors.wireComponents(view, this, false);
	    this.drawer = (Drawer) view.getFellow("drawer");
	    
	}
	@Override
	protected void doAfterCompose() throws Exception {
		// TODO Auto-generated method stub
		
	}
	protected void showMessage(String message) {
		
	}
	
	
	@Command
	public void openMenuPage() {
	   drawer.open();
	}

}
