package com.suinsit.leka.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
 
@Configuration
public class WebFluxConfig {
 
    @Bean
    public WebFilter responseHeaderFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().getHeaders().add("X-Content-Type-Options", "nosniff");
            return chain.filter(exchange)
                .onErrorResume(DataBufferLimitException.class, ex -> 
                    Mono.error(new PayloadTooLargeException("Request payload too large")));
        };
    }
}
 
class PayloadTooLargeException extends RuntimeException {
    public PayloadTooLargeException(String message) {
        super(message);
    }
}
