package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * The gtfs agency information for an agency with data stored in tdei. Matches the gtfs spec for agency.
 */
@Schema(description = "Includes gtfs agency information and a tdei agency id. Necessary as gtfs agency id may not be unique across agencies.")
@Validated
@Data
public class Agency {

    @JsonProperty("tdei_agency_id")
    @Schema(required = false, description = "TDEI-assigned agency id. Assigned by TDEI to avoid potential conflicts between GTFS agency ids.")
    private Integer tdeiAgencyId = null;

    @JsonProperty("agency_name")
    @Schema(required = true, description = "Agency name.")
    private String agencyName = null;

}
