package com.suinsit.leka.config;
 
import org.springframework.web.server.WebFilter;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpHeaders;
 
@Configuration
public class ContentValidationConfig {
 
    @Bean
    public WebFilter contentValidationFilter() {
        return (exchange, chain) -> {
            String contentType = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.CONTENT_TYPE);
 
            if (contentType != null && contentType.contains("multipart/form-data")) {
                return chain.filter(exchange)
                    .onErrorResume(DataBufferLimitException.class, 
                        ex -> Mono.error(new PayloadTooLargeException("File too large")));
            }
            return chain.filter(exchange);
        };
    }
}
