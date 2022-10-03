package com.tdei.gateway.model.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * The gtfs agency information for an agency with data stored in tdei. Matches the gtfs spec for agency.
 */
@Schema(description = "The gtfs agency information for an agency with data stored in tdei. Matches the gtfs spec for agency. ")
@Validated
@Data
public class Agency {

    @JsonProperty("tdei_agency_id")
    @Schema(required = false, description = "TDEI-assigned agency id. Assigned by TDEI to avoid potential conflicts between GTFS agency ids.")
    private Integer tdeiAgencyId = null;

    @JsonProperty("agency_id")
    @Schema(required = true, description = "The agency id.")
    @NotNull
    private Integer agencyId = null;

    @JsonProperty("agency_name")
    @Schema(required = true, description = "Agency name.")
    private String agencyName = null;

    @JsonProperty("agency_url")
    @Schema(required = true, description = "URL of the transit agency.")
    @NotNull
    private String agencyUrl = null;

    @JsonProperty("agency_timezone")
    @Schema(required = true, description = "Timezone where transit agency is located.")
    @NotNull
    private String agencyTimezone = null;

    @JsonProperty("agency_lang")
    @Schema(description = "Primary language used by this transit agency. Should be provided to help GTFS consumers choose capitalization rules and other language-specific settings for the dataset.")
    private String agencyLang = null;

    @JsonProperty("agency_phone")
    @Schema(description = "A voice telephone number for the specified agency. This field is a string value that presents the telephone number as typical for the agency's service area. It may contain punctuation marks to group the digits of the number. Dialable text (for example, TriMet's \"503-238-RIDE\") is permitted, but the field must not contain any other descriptive text.")
    private String agencyPhone = null;

    @JsonProperty("agency_email")
    @Schema(description = "Email address actively monitored by the agency’s customer service department. This email address should be a direct contact point where transit riders can reach a customer service representative at the agency.")
    private String agencyEmail = null;

    @JsonProperty("agency_polygon")
    @Schema(required = true, description = "A polygon which represents the boundaries of the agency's service area. A polygon is specified by a string providing the lat/lon coordinates of the polygon. The last lat/lon in the string should be the same as the first lat/lon in the string.")
    @NotNull
    private String agencyPolygon = null;

    @JsonProperty("agency_fare_url")
    @Schema(description = "URL of fare information for the agency.")
    private String agencyFareUrl = null;
}
