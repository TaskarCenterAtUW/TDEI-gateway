package com.tdei.gateway.model.dto.gtfs.flex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.model.dto.common.Polygon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * represents meta data needed to upload a gtfs_flex data file
 */
@Schema(description = "represents meta data needed to upload a gtfs_flex data file")
@Validated
@Data
public class GtfsFlexUpload {
    @Schema(required = true, description = "tdei-assigned agency id. Represented as UUID. Agency ids can be retrieved using the /api/v1.0/agencies path.")
    @NotNull
    @JsonProperty("tdei_agency_id")
    private String tdeiAgencyId = null;

    @Schema(required = true, description = "Description of who data was collected by. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collected_by")
    private String collectedBy = null;

    @Schema(required = true, description = "date-time that data was collected")
    @NotNull
    @Valid
    @JsonProperty("collection_date")
    private OffsetDateTime collectionDate = null;

    @Schema(required = true, description = "Method by which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collection_method")
    private String collectionMethod = null;

    @Schema(required = true, description = "date from which this file is valid")
    @NotNull
    @Valid
    @JsonProperty("valid_from")
    private OffsetDateTime validFrom = null;

    @Schema(description = "date until which this data is valid")
    @Valid
    @JsonProperty("valid_to")
    private OffsetDateTime validTo = null;

    @Schema(required = true, description = "Description of data source or sources from which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("data_source")
    private String dataSource = null;

    @Schema(required = true, description = "")
    @NotNull
    @JsonProperty("polygon")
    private Polygon polygon = null;

    @Schema(required = true, description = "version of gtfs flex schema this file conforms to")
    @NotNull
    @JsonProperty("flex_schema_version")
    private String flexSchemaVersion = null;
}
