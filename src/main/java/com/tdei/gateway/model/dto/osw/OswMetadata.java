package com.tdei.gateway.model.dto.osw;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Placeholder for meta-data specific to OSW files
 */
@Schema(description = "Placeholder for meta-data specific to OSW files")
@Validated
@Data
public class OswMetadata {
    @JsonProperty("bbox")
    @Schema(required = true, description = "Bounding box for this file.")
    @NotNull
    private String bbox = null;
}
