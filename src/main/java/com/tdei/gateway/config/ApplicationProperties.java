package com.tdei.gateway.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
@Data
public class ApplicationProperties {
    private SwaggerProperties swagger;
    private String appname;

    @Data
    @NoArgsConstructor
    public static class SwaggerProperties {
        private SwaggerContact contact;
        private String title;
        private String description;
        private String version;

        @Data
        @NoArgsConstructor
        public static class SwaggerContact {
            private String name = "";
            private String email = "";
            private String url = "";
        }
    }
}

