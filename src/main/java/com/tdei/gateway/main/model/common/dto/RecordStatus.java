package com.tdei.gateway.main.model.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Response class for `status` API call
 */
@Data
@Schema(description = "Describes the status of an uploaded record")
public class RecordStatus {

    @JsonProperty("tdeiRecordId")
    @Schema(required = true, description = "Record ID of the file generated")
    private String tdeiRecordId = null;
    @JsonProperty("stage")
    @Schema(description = "Current stage of the file processing")
    private String stage = null;

    @JsonProperty("status")
    @Schema(description = "Current status of processing. (failed, in progress or complete). If failed, shows the failure reason")
    private String status = null;

    @JsonProperty("isComplete")
    @Schema(description = "Whether processing is complete. (will be true if any stage fails)")
    private boolean isComplete = false;
}
