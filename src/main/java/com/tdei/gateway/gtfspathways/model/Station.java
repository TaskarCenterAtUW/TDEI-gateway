package com.tdei.gateway.gtfspathways.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class Station {

    /**
     * tdei assigned station id. Necessary to ensure that station ids are unique.
     *
     * @return tdeiStationId
     **/
    @Schema(required = true, description = "tdei assigned station id. Necessary to ensure that station ids are unique.")
    @NotNull
    @JsonProperty("tdei_station_id")
    private String tdeiStationId = null;

    /**
     * typically the station_name used in GTFS files
     *
     * @return stationName
     **/
    @Schema(required = true, description = "typically the station_name used in GTFS files")
    @NotNull
    @JsonProperty("station_name")
    private String stationName = null;

}
