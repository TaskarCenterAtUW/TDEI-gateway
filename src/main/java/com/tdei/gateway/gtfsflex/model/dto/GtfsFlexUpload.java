package com.tdei.gateway.gtfsflex.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.main.model.common.dto.GeoJsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * represents meta data needed to upload a gtfs_flex data file
 */
@Schema(description = "represents meta data needed to upload a gtfs_flex data file")
@Validated
@Data
public class GtfsFlexUpload {
    @Schema(required = true, example = "4e991e7a-5c16-4ebf-ad31-3a3625bcca10", description = "tdei-assigned organization id. Represented as UUID. Organization ids can be retrieved using the /api/v1.0/organizations path.")
    @NotNull
    @JsonProperty("tdei_org_id")
    private String tdeiOrgId = null;

    @Schema(required = true, example = "5e991e7a-5c16-4ebf-ad31-3a3625bcca10", description = "TDEI id of a GTFS Flex service")
    @NotNull
    @JsonProperty("tdei_service_id")
    private String tdeiServiceId = null;

    @Schema(required = true, example = "See best practices document", description = "Description of who data was collected by. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collected_by")
    private String collectedBy = null;

    @Schema(required = true, example = "2023-02-10T09:30Z", description = "date-time that data was collected")
    @NotNull
    @Valid
    @JsonProperty("collection_date")
    private String collectionDate = null;

    @Schema(required = true, allowableValues = {"manual", "transform", "generated", "other"}, description = "Method by which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collection_method")
    private String collectionMethod = null;

    @Schema(required = true, example = "2023-02-10T09:30Z", description = "date from which this file is valid")
    @NotNull
    @Valid
    @JsonProperty("valid_from")
    private String validFrom = null;

    @Schema(description = "date until which this data is valid", example = "2023-02-10T09:30Z")
    @Valid
    @JsonProperty("valid_to")
    private String validTo = null;

    @Schema(required = true, allowableValues = {"3rdParty", "TDEITools", "InHouse"}, description = "Description of data source or sources from which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("data_source")
    private String dataSource = null;

    @Schema(required = true, description = "Polygon that indicates the boundaries of data included in this file.")
    @NotNull
    @JsonProperty("polygon")
    private GeoJsonObject polygon = null;

    @Schema(required = true, example = "v2.0", description = "version of gtfs flex schema this file conforms to")
    @NotNull
    @JsonProperty("flex_schema_version")
    private String flexSchemaVersion = null;
}
