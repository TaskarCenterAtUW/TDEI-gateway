package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class PolygonGeometry {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("coordinates")
    @Valid
    private List<List<List<Double>>> coordinates = null;
}
