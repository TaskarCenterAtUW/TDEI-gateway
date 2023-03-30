package com.tdei.gateway.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ApiDocConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public OpenAPI springOpenAPI() {
        final String securitySchemeName = "Access Token";
        return new OpenAPI()
                .info(new Info().title(applicationProperties.getSwagger().getTitle())
                        .description(applicationProperties.getSwagger().getDescription())
                        .version(applicationProperties.getSwagger().getVersion())
                        .contact(new Contact().name(applicationProperties.getSwagger().getContact().getName())
                                .email(applicationProperties.getSwagger().getContact().getEmail())
                                .url(applicationProperties.getSwagger().getContact().getUrl())))
                .addSecurityItem(new SecurityRequirement().addList("ApiKey"))
                .addSecurityItem(new SecurityRequirement().addList("AuthorizationToken"))
                .components(new Components()
                        .addSecuritySchemes("AuthorizationToken", oauthSecurityRequirement())
                        .addSecuritySchemes("ApiKey", apikeySecurityRequirement()));
    }

    private SecurityScheme oauthSecurityRequirement() {
        return new SecurityScheme()
                .scheme("bearer")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .description("Authorization Token required by all applications that use the TDEI Consumer API to perform authorized actions.")
                .name(HttpHeaders.AUTHORIZATION);
    }

    private SecurityScheme apikeySecurityRequirement() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .description("Api Key required by all applications that use the TDEI Consumer API.")
                .name("x-api-key");
    }
}
