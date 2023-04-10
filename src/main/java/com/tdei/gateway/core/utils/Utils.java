package com.tdei.gateway.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class Utils {

    /**
     * Converts the POJO to JSON String
     *
     * @param obj - POJO class object
     * @return Json String
     */
    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = JsonMapper.builder()
                    .findAndAddModules()
                    .build();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatDate(Date date, Optional<String> format) {
        if (format.isEmpty()) format = Optional.of("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.get());
        return simpleDateFormat.format(date);
    }

    public static String getDomainURL(HttpServletRequest req) throws MalformedURLException {
        URL url = new URL(req.getRequestURL().toString());
        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();
        String domainURL = "";
// if the port is not explicitly specified in the input, it will be -1.
        if (port == -1) {
            domainURL = String.format("%s://%s", protocol, host);
        } else {
            domainURL = String.format("%s://%s:%d", protocol, host, port);
        }
        return domainURL;
    }
}
