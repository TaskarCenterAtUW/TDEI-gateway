package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Schema(description = "provides information about pageability.")
@Validated
@Data
public class Pageable {
    @Schema(required = true, description = "total number of pages in the response")
    @NotNull
    @JsonProperty("total_pages")
    private Integer totalPages = null;

    @Schema(required = true, description = "the start page of the current response")
    @NotNull
    @JsonProperty("current_page")
    private Integer currentPage = null;

    @Schema(required = true, description = "The total number of items in this response.")
    @NotNull
    @JsonProperty("total_items")
    private Integer totalItems = null;

    @Schema(required = true, description = "the number of pages provided in this response.")
    @NotNull
    @JsonProperty("num_pages")
    private Integer numPages = null;
}
