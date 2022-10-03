package com.tdei.gateway.model.dto.gtfs.pathways;

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
    @JsonProperty("url")
    @Schema(required = true, description = "url from which the file can be downloaded")
    @NotNull
    private String url = null;

    @JsonProperty("gtfs_pathways_file")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private GtfsPathwaysFile gtfsPathwaysFile = null;
}
