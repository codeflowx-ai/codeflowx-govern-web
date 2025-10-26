package com.suinsit.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "suinsit")
@Getter
@Setter

public class SuinsitProperties {
    private Customer customer = new Customer();
    private Application application = new Application();
    private Paths paths = new Paths();
    
    @Getter
    @Setter
    public static class Customer {
        private String name;
        private String uid;
        private String label;
        private String namespace;
    }
    
    @Getter
    @Setter
    public static class Application {
        private String name;
        private String product;
        private String version;
        private String mode;
    }
    
    @Getter
    @Setter
    public static class Paths {
        private String home;
        private String logs;
        private String root;
        private String studio;
        private String architecture;
        private String projets;
    }
}
