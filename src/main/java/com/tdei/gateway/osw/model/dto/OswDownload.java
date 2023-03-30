package com.tdei.gateway.osw.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.main.model.common.dto.GeoJsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Describes a osw file meta data.
 */
@Schema(description = "represents a osw data file.")
@Validated
@Data
public class OswDownload {
    @Schema(required = true, example = "4e991e7a-5c16-4ebf-ad31-3a3625bcca10", description = "tdei-assigned organization id. Represented as UUID. Organization ids can be retrieved using the /api/v1/organizations path.")
    @NotNull
    @JsonProperty("tdei_org_id")
    private String tdeiOrgId = null;

    @Schema(required = true, description = "Description of who data was collected by. See Best Practices document for information on how to format this string.", example = "See best practices document")
    @NotNull
    @JsonProperty("collected_by")
    private String collectedBy = null;

    @Schema(required = true, description = "date-time that data was collected", example = "2018-02-10T09:30Z")
    @NotNull
    @Valid
    @JsonProperty("collection_date")
    private String collectionDate = null;

    @Schema(required = true, allowableValues = {"manual", "transform", "generated", "other"}, description = "Method by which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collection_method")
    private String collectionMethod = null;

    @Schema(required = true, example = "2018-02-10T09:30Z", description = "date from which this file is valid")
    @NotNull
    @Valid
    @JsonProperty("valid_from")
    private String validFrom = null;

    @Schema(example = "2018-02-10T09:30Z", description = "date until which this data is valid")
    @Valid
    @JsonProperty("valid_to")
    private String validTo = null;

    @Schema(required = true, example = "41", description = "tdei-generated confidence level. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).")
    @NotNull
    @JsonProperty("confidence_level")
    private Integer confidenceLevel = null;

    @Schema(required = true, allowableValues = {"3rdParty", "TDEITools", "InHouse"}, description = "Description of data source or sources from which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("data_source")
    private String dataSource = null;

    @Schema(required = true, description = "Polygon that indicates the boundaries of data included in this file.")
    @NotNull
    @JsonProperty("polygon")
    private GeoJsonObject polygon = null;

    @Schema(required = true, example = "4e991e7a-5c16-4ebf-ad31-3a3625bcca10", description = "unique id identifying the file in the tdei system, can be used to retrieve the file itself.")
    @NotNull
    @JsonProperty("tdei_record_id")
    private String tdeiRecordId = null;

    @Schema(required = true, example = "v1.1", description = "version of osw schema this file conforms to")
    @NotNull
    @JsonProperty("osw_schema_version")
    private String oswSchemaVersion = null;

    @Schema(required = true, example = "/api/v1/osw/{tdei_record_id}", description = "The url from which this file can be downloaded.")
    @NotNull
    @JsonProperty("download_url")
    private String downloadUrl = null;
}
