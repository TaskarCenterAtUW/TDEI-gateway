package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * List of API Versions.
 */
@Schema(description = "Specification for a data standards (GTFS-Flex, GTFS-Pathways, OSW) versions.")
@Validated
@Data
public class VersionSpec {
    @JsonProperty("version")
    private String version = null;

    @JsonProperty("documentation")
    private String documentation = null;

    @JsonProperty("specification")
    private String specification = null;
}
