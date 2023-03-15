package com.tdei.gateway.main.model.common.dto;

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
public class Organization {

    @JsonProperty("tdei_org_id")
    @Schema(required = false, description = "tdei-assigned organization id. Necessary to ensure that organization ids are unique. Represented as a UUID.")
    private String tdei_org_id = null;

    @JsonProperty("org_name")
    @Schema(required = true, description = "org_name name. For transit agencies, typically the agency name used in GTFS releases.")
    private String org_name = null;

    @Schema(required = true, description = "geo-json polygon.")
    private Object polygon = null;

}
