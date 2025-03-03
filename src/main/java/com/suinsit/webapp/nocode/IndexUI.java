package com.suinsit.webapp.nocode;

import java.io.File;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.View;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.suinsit.framework.integration.mail.MailProvider;
import com.suinsit.webapp.login.LoginUI;

import lombok.extern.slf4j.Slf4j;
@View
//Anotacion utilizada para la navegacion
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
@org.springframework.stereotype.Component
@Slf4j
@Scope("prototype")
public class IndexUI extends MasterPage{
	@WireVariable
	protected Session session;
	@WireVariable DataSource datasource;
	@WireVariable
	MailProvider mail;
	@WireVariable("rabbitMQ")
    private RabbitTemplate rabbitTemplate;
	@WireVariable("ctxBean")
	protected Context ctxBean;
	@WireVariable("context")
	GenericApplicationContext context;
	@WireVariable
	private Environment environment;

	@AfterCompose()
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		
		appendPage(ctxBean.getPathHome()+"/apps/webapp/login.zhtml", view, params);
	    //super.getDesktop().getCurrentDirectory();
	    
	    //super.getDesktop().getResourceURI("index.zul");
	//  ctxBean.getPathHome()+ File.separator + "data"+ File.separator + "model";
   }

	@Override
	public void setBeans(Object bean) {
		// TODO Auto-generated method stub
		
	}
}