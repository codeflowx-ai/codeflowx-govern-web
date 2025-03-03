/*
 * Copyright (c) 2020-2025 EnartSystems.com
 * @author Manuel González (enartsystems) 
 */
package com.suinsit.webapp.config;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.EntityDao;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.orm.jdbc.hikaricp.MultiDbConnectionPool;
import org.enartframework.suinsit.Context;
import org.enartframework.suinsit.applications.config.ConfigApplication;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
//import org.enartframework.suinsit.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import org.zkoss.spring.config.ZkScopesConfigurer;

import com.suinsit.framework.integration.mail.MailProvider;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan({ "com.suinsit.*", "org.enartframework.integration.aws.s3.*", "com.suinsit.framework.integration.*",
		"com.suinsit.applications.erp.*", "com.suinsit.applications.app.*", "org.enartframework.suinsit.*",
		"org.enartframework.orm.jdbc.hikaricp.*" })
@Import({ ZkScopesConfigurer.class })
@Slf4j
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class,RabbitAutoConfiguration.class})
public class Configure implements Serializable {

	
	private static final long serialVersionUID = 1L;
	public static final String QUEUE_K8S = "suinsit.k8s.amq.queue";
	public static final String DIRECT_DEPLOY_PROJECT = "suinsit.deploy.project.amq.queue";
	public final static String QUEUE_NAME = "suinsit.studio.k8s.amq.queue";
	public final static String FANOUT_QUEUE_2_NAME = "io.suinist.platform.amqp.fanout.queue2";
	public final static String FANOUT_EXCHANGE_NAME = "io.suinist.platform.amqp.fanout.exchange";
	public final static String TOPIC_QUEUE_1_NAME = "io.suinist.platform.amqp.topic.queue1";
	public final static String TOPIC_QUEUE_2_NAME = "io.suinist.platform.amqp.topic.queue2";
	public final static String TOPIC_EXCHANGE_NAME = "suinsit.studio.amqp.topic.exchange";
	public static final String BINDING_PATTERN_IMPORTANT = "*.important.*";
	public static final String BINDING_PATTERN_ERROR = "#.error";

	@Autowired
	org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory;
	@Autowired
	private Environment environment;
	@Autowired
	DataSource datasource;
	@Autowired
	MailProvider mail;
	@Autowired
	ConfigApplication configApp;
	@Autowired
	private SuinsitProperties suinsitProperties;
	
	
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(DIRECT_DEPLOY_PROJECT);
	}

	@Bean
	public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
		return new AsyncRabbitTemplate(rabbitTemplate);
	}

	@Bean(name = "rabbitMQ")
	public RabbitTemplate rabbitTemplate() throws Exception {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Queue queue() {
		return new Queue(QUEUE_NAME, true, false, false);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(TOPIC_EXCHANGE_NAME, true, false);
	}

	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange()).with(QUEUE_NAME);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory customRabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(jsonMessageConverter());
		return factory;
	}

	@Bean(name = "multiPool")
	public MultiDbConnectionPool getMultiDbConnectionPool(DataSource datasource) {
		MultiDbConnectionPool multiPool = new MultiDbConnectionPool();
		try {
			multiPool.setRootDataSource(datasource);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return multiPool;
	}

	@Bean(name = "entityDao")
	public IEntityLocal getIEntityLocal(DataSource datasource) {
		IEntityLocal dao = new EntityDao();
		dao.setDataSource(datasource);
		try {
			log.debug(datasource.getConnection().getSchema());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return dao;
	}
	@Bean
	public EntityDao getEntityDaol(DataSource datasource) {
		EntityDao dao = new EntityDao();
		dao.setDataSource(datasource);
		try {
			log.debug(datasource.getConnection().getSchema());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return dao;
	}
	@Bean(name = "ctxBean")
	public Context getContextBean() throws Exception {
		Context ctx = new Context();
		ctx.setConfig(configApp);
		 // Usar las propiedades tipadas
	    ctx.setMode(suinsitProperties.getApplication().getMode());
	    ctx.setCustomerName(suinsitProperties.getCustomer().getName());
	    ctx.setCustomerUID(suinsitProperties.getCustomer().getUid());
	    ctx.setCustomerLabel(suinsitProperties.getCustomer().getLabel());
	    ctx.setPathHome(suinsitProperties.getPaths().getHome());
	    ctx.setPathClasses(suinsitProperties.getPaths().getHome()+ "/data/classes");
	    ctx.setPathStudio(suinsitProperties.getPaths().getStudio());
		ctx.setPathDeploy(suinsitProperties.getPaths().getHome());
		ctx.setPathArchitect(suinsitProperties.getPaths().getHome());
	    ctx.setEnviroment("production");
		if (System.getenv(ENVIROMENTS.SUINSIT_EXECUTION.name()) != null) {
			ctx.setExecution(System.getenv(ENVIROMENTS.SUINSIT_EXECUTION.name()));
		}
		ctx.setProductName(suinsitProperties.getApplication().getProduct());
		ctx.setApplicationName(suinsitProperties.getApplication().getName());
		ctx.getSecurity().put("scope", environment.getRequiredProperty("suinsit.security.scope"));
		ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
		Map<String, Object> prop = new HashMap<>();
		prop.put("APPLICATION_DS", datasource);
		env.getPropertySources().addFirst(new MapPropertySource("SUINSIT_PROPS", prop));
		log.debug(ctx.toString());
		return ctx;
	}

	@Bean
    public WebClient webClient() {
        return WebClient.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024))
            .build();
    }
}