package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class Geometry {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("coordinates")
    @Valid
    @ArraySchema(minItems = 4)
    private List<List<List<Double>>> coordinates = null;
}
