package com.quoter.onlineloanquotes.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class SecurityConfiguration implements WebFluxConfigurer {

    // TODO: Convert it to an application property so it's not hardcoded in the code.
    private static final String DEFAULT_FRONT_END_ORIGIN = "http://localhost:4200";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(DEFAULT_FRONT_END_ORIGIN)
                .allowedMethods("GET");
    }
}