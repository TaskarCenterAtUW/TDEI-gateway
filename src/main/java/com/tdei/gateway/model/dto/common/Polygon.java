package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class Polygon {
    @JsonProperty("type")
    @Valid
    private String type = null;

    @JsonProperty("features")
    @Valid
    private List<PolygonFeatures> features = null;
}
