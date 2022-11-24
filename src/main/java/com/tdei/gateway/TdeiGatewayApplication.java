package com.tdei.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class TdeiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdeiGatewayApplication.class, args);
        log.info("TDEI Gateway API Started Successfully !");
        log.info("^^^");
    }

    //Diagnostic logs - info, trace, debug, error - Bug resolutions - Short persistance - App Insights - POC - Dev driven
    //Audit logs  - Event levels - Long persistance  - Internal system will make calls
    //Analytics logs - Ip tracking, user logged in, data usage, no. of data publish - Internal system will make calls

    // Audit/Analytics store
}
