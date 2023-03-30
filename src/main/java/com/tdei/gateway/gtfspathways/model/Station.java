package com.tdei.gateway.gtfspathways.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.main.model.common.dto.GeoJsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Station {

    /**
     * tdei assigned station id. Necessary to ensure that station ids are unique.
     *
     * @return tdeiStationId
     **/
    @Schema(required = true, example = "4e991e7a-5c16-4ebf-ad31-3a3625bcca10", description = "tdei assigned station id. Necessary to ensure that station ids are unique.")
    @NotNull
    @JsonProperty("tdei_station_id")
    private String tdeiStationId = null;

    /**
     * typically the station_name used in GTFS files
     *
     * @return stationName
     **/
    @Schema(required = true, example = "Northgate", description = "typically the station_name used in GTFS files")
    @NotNull
    @JsonProperty("station_name")
    private String stationName = null;

    @Schema(required = true, description = "geo-json polygon.")
    private GeoJsonObject polygon = null;
}
