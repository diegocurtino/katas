package com.quoter.onlineloanquotes.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    private static Info getApiInfo() {
        return new Info()
                .title("OnlineLoanQuotes")
                .description("This is a simple Spring Boot app used to figures about loans between £100 and £15000 to be repaid in 36 months")
                .version("1.1")
                .contact(getContactInfo());
    }

    private static Contact getContactInfo() {
        return new Contact().name("DMC");
    }

    @Bean
    public OpenAPI apiDocumentation() {
        return new OpenAPI()
                .components(new Components())
                .info(getApiInfo());
    }
}
