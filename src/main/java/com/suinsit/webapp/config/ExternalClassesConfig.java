package com.suinsit.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.ServletContextResource;
import javax.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

@Configuration
public class ExternalClassesConfig implements ServletContextInitializer {

    @Value("${suinsit.app.external-classes:/opt7suinsit/data/classes}")
    private String externalClassesPath;

    @Override
    public void onStartup(ServletContext servletContext) {
        try {
            File classesDir = new File(externalClassesPath);
            if (classesDir.exists()) {
                URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{classesDir.toURI().toURL()},
                    Thread.currentThread().getContextClassLoader()
                );
                Thread.currentThread().setContextClassLoader(classLoader);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading external classes", e);
        }
    }
}
