package com.codeflowx.web.app.config;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import lombok.extern.slf4j.Slf4j;

@Configuration

@Slf4j
public class LocaleConfiguration {

    @Value("${TZ:Europe/Madrid}")
    private String timeZone;

    @Value("${LANG:es_ES.UTF-8}")
    private String lang;

    @PostConstruct
    public void init() {
        try {
            // Establecer zona horaria por defecto
            TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
            log.info("Timezone configurado: {}", timeZone);
            
            // Parsear y establecer locale de forma segura
            Locale locale = parseLocale(lang);
            Locale.setDefault(locale);
            log.info("Locale configurado: {}", locale);
            
        } catch (Exception e) {
            log.warn("Error configurando locale/timezone. Usando valores por defecto", e);
            // Usar valores por defecto si hay error
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));
            Locale.setDefault(new Locale("es", "ES"));
        }
    }

    private Locale parseLocale(String localeString) {
        try {
            if (localeString == null || localeString.trim().isEmpty()) {
                return new Locale("es", "ES");
            }

            // Separar primero por punto para manejar el .UTF-8
            String[] parts = localeString.split("\\.");
            String localePart = parts[0]; // es_ES

            // Separar el locale en language y country
            String[] localeParts = localePart.split("_");
            String language = localeParts[0].toLowerCase();
            String country = localeParts.length > 1 ? localeParts[1].toUpperCase() : "";

            return new Locale(language, country);
        } catch (Exception e) {
            log.warn("Error parseando locale '{}', usando es_ES", localeString, e);
            return new Locale("es", "ES");
        }
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.getDefault());
        resolver.setDefaultTimeZone(TimeZone.getDefault());
        return resolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}