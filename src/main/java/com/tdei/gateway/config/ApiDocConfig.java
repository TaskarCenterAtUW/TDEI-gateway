package com.tdei.gateway.config;

import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(applicationProperties.getSwagger().getTitle())
                        .description(applicationProperties.getSwagger().getDescription())
                        .version(applicationProperties.getSwagger().getVersion())
                        .contact(new Contact().name(applicationProperties.getSwagger().getContact().getName())
                                .email(applicationProperties.getSwagger().getContact().getEmail())
                                .url(applicationProperties.getSwagger().getContact().getUrl())))
                ;
    }
}
