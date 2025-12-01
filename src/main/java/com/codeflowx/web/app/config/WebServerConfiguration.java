package com.codeflowx.web.app.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableWebSocket
@EnableWebSecurity
@Order(1)
public class WebServerConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ZUL_FILES = "/zkau/web/**/*.zul";
    private static final String[] ZK_RESOURCES = {
            "/zkau/web/**/js/**",
            "/zkau/web/**/zul/css/**",
            "/zkau/web/**/font/**",
            "/zkau/web/**/img/**",
            "/console/img/**",
            "/console/images/**",
            "/console/iconos/**",
            "/console/assets/**",
            "/console/css/**",
            "/console/global_assets/**"
    };
    private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

    @Value("${suinsit.security.whitelist:/,/healthz,/actuator/**,/login.zhtml}")
    private String whitelist;

    @Value("${suinsit.security.blacklist:/desktop/**,/portal/**,/console/**}")
    private String blacklist;
   
    @Autowired
    private Environment environment;


    // Configuración de Seguridad
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers( "/actuator/**").permitAll()
            .antMatchers(ZUL_FILES).denyAll()
            .antMatchers(HttpMethod.GET, "/zkau/web/**/js/**",
            		"/zkau/web/**/zul/css/**",
            		"/zkau/web/**/font/**",
            		"/zkau/web/**/img/**",
            		"/console/img/**",
            		"/console/images/**",
            		 "/console/iconos/**",
            		"/console/assets/**",
            		"/console/css/**",
            		"/console/global_assets/**").permitAll()
            .regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll()
            .requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll()
            .antMatchers(whitelist.split(",")).permitAll()
            .antMatchers(blacklist.split(",")).authenticated()
            .and()
            .formLogin()
            .loginPage("/login.zhtml")
            .defaultSuccessUrl(environment.getProperty("suinsit.desktop.ctx") + "/" + 
                             environment.getProperty("suinsit.desktop.page"))
            .and()
            .logout()
            .logoutUrl("/login.zhtml?logout")
            .logoutSuccessUrl("/login.zhtml");

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }
}