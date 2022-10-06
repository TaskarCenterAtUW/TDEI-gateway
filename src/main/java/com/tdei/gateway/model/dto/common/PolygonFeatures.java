package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolygonFeatures {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("properties")
    private Object properties = null;

    @JsonProperty("geometry")
    private PolygonGeometry geometry = null;
}
