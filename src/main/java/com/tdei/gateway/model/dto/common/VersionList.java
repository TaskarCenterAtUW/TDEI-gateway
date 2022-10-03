package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * List of API Versions.
 */
@Schema(description = "List of API Versions.")
@Validated
@Data
public class VersionList {
    @JsonProperty("versions")
    @Schema(description = "versions")
    @Valid
    private List<String> versions = null;
}
