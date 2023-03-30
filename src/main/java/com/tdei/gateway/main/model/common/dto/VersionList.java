package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * List of API Versions.
 */
@Schema(description = "List of versions.")
@Validated
@Data
public class VersionList {
    @JsonProperty("versions")
    @Schema(description = "A list of the versions of a data standard (GTFS-Flex, GTFS-Pathways, OSW) supported by TDEI.")
    private List<VersionSpec> versions = new ArrayList<>();
}
