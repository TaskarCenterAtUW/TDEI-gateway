package com.tdei.gateway.model.dto.osw;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.model.dto.common.TdeiGeneratorMetadata;
import com.tdei.gateway.model.dto.common.TdeiSystemMetadata;
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
public class OswFile {
    @JsonProperty("tdei_generator_metadata")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private TdeiGeneratorMetadata tdeiGeneratorMetadata = null;

    @JsonProperty("tdei_system_metadata")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private TdeiSystemMetadata tdeiSystemMetadata = null;

    @JsonProperty("osw_metadata")
    @Schema(required = true, description = "")
    @NotNull
    @Valid
    private OswMetadata oswMetadata = null;
}
