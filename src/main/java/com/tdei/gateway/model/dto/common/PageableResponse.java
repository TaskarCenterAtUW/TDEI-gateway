package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "information included when providing a list.")
@Validated
@Data
public class PageableResponse<T> {
    @JsonProperty("pageable")
    private Pageable pageable = null;

    @JsonProperty("list")
    private List<T> list = new ArrayList<>();
}
