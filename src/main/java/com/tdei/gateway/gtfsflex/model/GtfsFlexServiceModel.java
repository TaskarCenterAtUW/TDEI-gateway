package com.tdei.gateway.gtfsflex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.main.model.common.dto.GeoJsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GtfsFlexServiceModel {
    @Schema(required = true, example = "Hyde Shuttle", description = "name of the gtfs-flex service.")
    @JsonProperty("service_name")
    private String serviceName = null;

    @Schema(required = true, example = "5e991e7a-5c16-4ebf-ad31-3a3625bcca10", description = "tdei assigned service id. Necessary to ensure that service ids are unique.")
    @JsonProperty("tdei_service_id")
    private String tdeiServiceId = null;

    @Schema(required = true, description = "geo-json polygon.")
    private GeoJsonObject polygon = null;
}
