package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * List of API Versions.
 */
@Schema(description = "Specification for a data standards (GTFS-Flex, GTFS-Pathways, OSW) versions or TDEI API.", example = "{\n" +
        "\"version\": \"v1.0\",\n" +
        "\"documentation\": \"link to v1.0 documentation\",\n" +
        "\"specification\": \"link to the v1.0 specification\"\n" +
        "}")
@Validated
@Data
public class VersionSpec {
    @JsonProperty("version")
    @Schema(description = "version string")
    private String version = null;

    @JsonProperty("documentation")
    @Schema(description = "link to the documentation")
    private String documentation = null;

    @JsonProperty("specification")
    @Schema(description = "link to the specification for the data standard")
    private String specification = null;
}
