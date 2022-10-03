package com.tdei.gateway.model.dto.gtfs.flex;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * represents a gtfs_flex data file. same as in consumer api, same as gtfs_flex, adds uri
 */
@Schema(description = "represents a gtfs_flex data file. same as in consumer api, same as gtfs_flex, adds uri")
@Validated
@Data
public class GtfsFlexDownload {
    @JsonProperty("url")
    @Schema(required = true, description = "url with link to file to be downloaded")
    @NotNull
    private String url = null;

    @JsonProperty("gtfs_flex_file")
    private GtfsFlexFile gtfsFlexFile = null;
}
