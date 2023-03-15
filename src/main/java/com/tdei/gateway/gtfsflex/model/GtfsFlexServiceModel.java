package com.tdei.gateway.gtfsflex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GtfsFlexServiceModel {
    @Schema(required = true, description = "name of the gtfs-flex service.")
    @JsonProperty("service_name")
    private String serviceName = null;

    @Schema(required = true, description = "tdei assigned service id. Necessary to ensure that service ids are unique.")
    @JsonProperty("tdei_service_id")
    private String tdeiServiceId = null;

    @Schema(required = true, description = "geo-json polygon.")
    private Object polygon = null;
}
