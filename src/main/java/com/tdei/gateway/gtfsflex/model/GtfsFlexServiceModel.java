package com.tdei.gateway.gtfsflex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GtfsFlexServiceModel {
    @JsonProperty("service_name")
    private String serviceName = null;

    @JsonProperty("tdei_service_id")
    private String tdeiServiceId = null;
}
