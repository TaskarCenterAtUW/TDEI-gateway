package com.tdei.gateway.controller.osw.contract;

import com.tdei.gateway.model.dto.common.PageableResponse;
import com.tdei.gateway.model.dto.common.VersionSpec;
import com.tdei.gateway.model.dto.osw.OswDownload;
import com.tdei.gateway.model.dto.osw.OswUpload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

public interface IOsw {

    @Operation(summary = "returns a geojson osw file", description = "returns a specific osw file identified by the record_id", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the osw file - will be geojson. I wasn't sure what should be returned here - I set the type geojson, but was not sure how this is typically done. "),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "404", description = "OSW data meeting the specifications (version and bounding box) was not found.Data is expected to be available for King and Snohomish counties in Washington, Multnomah and Columbia counties in Oregon, and Baltimore and Harford counties in Maryland.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{tdei_record_id}/",
            //produces = {"geojson"},
            method = RequestMethod.GET)
    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<Void> getOswFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "tdei_record_id for a file, represented as a uuid", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId);


    @Operation(summary = "List osw files meeting criteria.", description = "This endpoint returns a list of url to gzip'd geojson files with osw data that meet the specified criteria. Criteria that can be specified include: bounding box, minimum confidence level and pathways version.  This endpoint can be used by an application developer to obtain a list of osw files in the TDEI system meeting the specified criteria.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `osw_download` entities.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OswDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "404", description = "OSW data meeting the specifications (version and bounding box) was not found.Data is expected to be available for King and Snohomish counties in Washington, Multnomah and Columbia counties in Oregon, and Baltimore and Harford counties in Maryland.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/json", "multipart/form-data"},
            method = RequestMethod.GET)
    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<PageableResponse<OswDownload>> listOswFiles(Principal principal, @NotNull @Parameter(in = ParameterIn.QUERY, description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.", required = true, schema = @Schema()) @Valid @RequestParam(value = "bounding_box", required = true) String boundingBox, @Parameter(in = ParameterIn.QUERY, description = "Minimum confidence level required. Data returned will be at this confidence level or higher. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).", schema = @Schema()) @Valid @RequestParam(value = "confidence_level", required = false) Integer confidenceLevel, @Parameter(in = ParameterIn.QUERY, description = "A string with the name of the schema version the application requests.", schema = @Schema()) @Valid @RequestParam(value = "osw_schema_version", required = false) String oswSchemaVersion, @Parameter(in = ParameterIn.QUERY, description = "if included, returns the metadata for the specified file, all other parameters will be ignored.", schema = @Schema()) @Valid @RequestParam(value = "tdei_record_id", required = false) String tdeiRecordId, @Parameter(in = ParameterIn.QUERY, description = "integer, defaults to 0", schema = @Schema()) @Valid @RequestParam(value = "page_no", required = false) Integer pageNo, @Parameter(in = ParameterIn.QUERY, description = "integer, between 20 and 100, defaults to 20", schema = @Schema()) @Valid @RequestParam(value = "page_size", required = false) Integer pageSize);

    @Operation(summary = "List available OSW versions", description = "Lists the versions of OSW data which are supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of OSW versions supported by TDEI.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionSpec.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<PageableResponse<VersionSpec>> listOswVersions(Principal principal);

    @Operation(summary = "Path used to upload/create a new osw data file.", description = "This path allows a user to upload or create a new osw file. The caller must provide metadata about the file - includes information about how and when the data was collected and valid dates of the file. Returns the tdei_record_id of the uploaded file.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful file creation. returns tdei_record_id of new file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed.", content = @Content),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{agencyId}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    @PreAuthorize("@authService.hasAgencyPermission(#principal,  #agencyId, 'tdei-user', 'osw-data_generator', 'osw-poc')")
    ResponseEntity<String> uploadOswFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "", schema = @Schema()) @RequestParam() String agencyId, @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody OswUpload body);

}
