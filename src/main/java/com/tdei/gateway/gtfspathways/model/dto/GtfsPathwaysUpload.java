package com.tdei.gateway.gtfspathways.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.main.model.common.dto.GeoJsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Describes a gtfs pathways file meta data. Same as gtfs_pathways, but adds uri.
 */
@Schema(description = "Describes a gtfs pathways file meta data. Same as gtfs_pathways, but adds uri.")
@Validated
@Data
public class GtfsPathwaysUpload {
    @Schema(required = true, description = "tdei-assigned organization id. Represented as UUID. Organization ids can be retrieved using the /api/v1.0/organizations path.")
    @NotNull
    @JsonProperty("tdei_org_id")
    private String tdeiOrgId = null;

    @Schema(required = true, description = "tdei-assigned station id. Represented as UUID. Station ids can be retrieved using the /api/v1.0/stations path.")
    @JsonProperty("tdei_station_id")
    private String tdeiStationId = null;

    @Schema(required = true, description = "Description of who data was collected by.")
    @NotNull
    @JsonProperty("collected_by")
    private String collectedBy = null;

    @Schema(required = true, description = "date-time that data was collected")
    @NotNull
    @Valid
    @JsonProperty("collection_date")
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime collectionDate = null;

    @Schema(required = true, description = "Method by which the data was collected.")
    @NotNull
    @JsonProperty("collection_method")
    private String collectionMethod = null;

    @Schema(required = true, description = "date from which this file is valid")
    @NotNull
    @Valid
    @JsonProperty("valid_from")
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime validFrom = null;

    @Schema(description = "date until which this data is valid")
    @Valid
    @JsonProperty("valid_to")
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime validTo = null;

    @Schema(required = true, description = "Description of data source or sources from which the data was collected.")
    @NotNull
    @JsonProperty("data_source")
    private String dataSource = null;

    @Schema(required = true, description = "")
    @NotNull
    @JsonProperty("polygon")
    private GeoJsonObject polygon = null;

    @Schema(required = true, description = "version of gtfs pathways schema this file conforms to")
    @NotNull
    @JsonProperty("pathways_schema_version")
    private String pathwaysSchemaVersion = null;
}
