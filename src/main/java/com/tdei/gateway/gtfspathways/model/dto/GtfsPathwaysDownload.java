package com.tdei.gateway.gtfspathways.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Describes a gtfs pathways file meta data.
 */
@Schema(description = "Describes a gtfs pathways file meta data.")
@Validated
@Data
public class GtfsPathwaysDownload {
    @Schema(required = true, description = "tdei-assigned organization id. Represented as UUID. Organization ids can be retrieved using the /api/v1.0/organizations path.")
    @NotNull
    @JsonProperty("tdei_org_id")
    private String tdeiOrgId = null;

    @Schema(required = true, description = "Description of who data was collected by. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collected_by")
    private String collectedBy = null;

    @Schema(required = true, description = "date-time that data was collected")
    @NotNull
    @Valid
    @JsonProperty("collection_date")
    private String collectionDate = null;

    @Schema(required = true, description = "Method by which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collection_method")
    private String collectionMethod = null;

    @Schema(required = true, description = "date from which this file is valid")
    @NotNull
    @Valid
    @JsonProperty("valid_from")
    private String validFrom = null;

    @Schema(description = "date until which this data is valid")
    @Valid
    @JsonProperty("valid_to")
    private String validTo = null;

    @Schema(required = true, description = "tdei-generated confidence level. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).")
    @NotNull
    @JsonProperty("confidence_level")
    private Integer confidenceLevel = null;

    @Schema(required = true, description = "Description of data source or sources from which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("data_source")
    private String dataSource = null;

//    @Schema(required = true, description = "")
//    @NotNull
//    @JsonProperty("polygon")
//    private Polygon polygon = null;

    @Schema(required = true, description = "unique id identifying the file in the tdei system, can be used to retrieve the file itself.")
    @NotNull
    @JsonProperty("tdei_record_id")
    private String tdeiRecordId = null;

    @Schema(required = true, description = "version of gtfs pathways schema this file conforms to")
    @NotNull
    @JsonProperty("pathways_schema_version")
    private String pathwaysSchemaVersion = null;

    @Schema(required = true, description = "The url from which this file can be downloaded.")
    @NotNull
    @JsonProperty("download_url")
    private String downloadUrl = null;
}
