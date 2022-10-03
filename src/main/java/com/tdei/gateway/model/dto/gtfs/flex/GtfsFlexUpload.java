package com.tdei.gateway.model.dto.gtfs.flex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.model.dto.common.Agency;
import com.tdei.gateway.model.dto.common.TdeiGeneratorMetadata;
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
    @JsonProperty("agency")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private Agency agency = null;

    @JsonProperty("uri")
    @Schema(required = true, description = "uri of the file to be uploaded")
    @NotNull
    private String uri = null;

    @JsonProperty("tdei_generator_metadata")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private TdeiGeneratorMetadata tdeiGeneratorMetadata = null;
}
