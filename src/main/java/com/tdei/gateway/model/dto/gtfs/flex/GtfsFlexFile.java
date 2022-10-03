package com.tdei.gateway.model.dto.gtfs.flex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.gateway.model.dto.common.Agency;
import com.tdei.gateway.model.dto.common.TdeiGeneratorMetadata;
import com.tdei.gateway.model.dto.common.TdeiSystemMetadata;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * represents a gtfs_flex data file. same as in consumer api,
 */
@Schema(description = "represents a gtfs_flex data file. same as in consumer api, ")
@Validated
@Data
public class GtfsFlexFile {
    @JsonProperty("agency")
    private Agency agency = null;

    @JsonProperty("tdei_generator_metadata")
    @Schema(required = true, description = "")
    @NotNull
    private TdeiGeneratorMetadata tdeiGeneratorMetadata = null;

    @JsonProperty("tdei_system_metadata")
    @Schema(required = true, description = "")
    @NotNull
    private TdeiSystemMetadata tdeiSystemMetadata = null;
}
