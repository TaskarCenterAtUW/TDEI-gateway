package com.tdei.gateway.core.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
@Data
public class ApplicationProperties {
    private SwaggerProperties swagger;
    private AppProperties application;
    private GtfsFlexProperties gtfsFlex;
    private GtfsPathwaysProperties gtfsPathways;
    private OswProperties osw;
    private ManagementSvcProperties managementSvc;

    @Data
    @NoArgsConstructor
    public static class ManagementSvcProperties {
        private String stationUrl;
        private String serviceUrl;
        private String orgUrl;
    }

    @Data
    @NoArgsConstructor
    public static class GtfsFlexProperties {
        private String uploadUrl;
        private String dataUrl;
    }

    @Data
    @NoArgsConstructor
    public static class GtfsPathwaysProperties {
        private String uploadUrl;
        private String dataUrl;
    }

    @Data
    @NoArgsConstructor
    public static class OswProperties {
        private String uploadUrl;
        private String dataUrl;
    }

    @Data
    @NoArgsConstructor
    public static class AppProperties {
        private String appName;
        private String authServerUrl;
    }

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

