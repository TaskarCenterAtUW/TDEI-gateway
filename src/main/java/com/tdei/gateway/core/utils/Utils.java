package com.tdei.gateway.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Component;

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
}
