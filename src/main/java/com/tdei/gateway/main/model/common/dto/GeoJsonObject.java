package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;

@Schema(description = "geo-json polygon")
@Data
public class GeoJsonObject {
    @JsonProperty("type")
    @Schema(allowableValues = {"FeatureCollection"})
    private String type = "FeatureCollection";

    @JsonProperty("features")
    private ArrayList<Feature> features = null;
}
