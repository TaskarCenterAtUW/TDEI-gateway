package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Feature {
    @JsonProperty("type")
    @Schema(allowableValues = {"Feature"})
    private String type = "Feature";

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("properties")
    private Object properties = null;

    @JsonProperty("geometry")
    private Geometry geometry = null;
}
