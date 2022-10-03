package com.tdei.gateway.model.dto.gtfs.pathways;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.model.dto.common.Station;
import com.tdei.gateway.model.dto.common.TdeiGeneratorMetadata;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Describes a gtfs pathways file meta data. Same as gtfs_pathways, but adds uri.
 */
@Schema(description = "Describes a gtfs pathways file meta data. Same as gtfs_pathways, but adds uri.")
@Validated
@Data
public class GtfsPathwaysUpload {
    @JsonProperty("station")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private Station station = null;

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
