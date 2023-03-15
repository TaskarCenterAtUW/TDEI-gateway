package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * The gtfs station information for an agency with data stored in tdei. Matches the gtfs spec for station.
 */
@Schema(description = "Includes gtfs station information and a tdei agency id. Necessary as gtfs station id may not be unique across agencies.")
@Validated
@Data
public class Station {
    @JsonProperty("tdei_station_id")
    @Schema(required = true, description = "tdei assigned station id. Necessary to ensure that station ids are unique.")
    @NotNull
    private String tdeiStationId = null;

    @JsonProperty("station_name")
    private String stationName = null;
}
