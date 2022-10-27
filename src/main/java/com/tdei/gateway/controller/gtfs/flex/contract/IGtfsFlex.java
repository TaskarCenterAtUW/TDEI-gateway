package com.tdei.gateway.controller.gtfs.flex.contract;

import com.tdei.gateway.model.dto.common.PageableResponse;
import com.tdei.gateway.model.dto.common.VersionSpec;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexDownload;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexUpload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.OffsetDateTime;

@Validated
public interface IGtfsFlex {

    @Operation(summary = "returns a gtfs_pathways file", description = "returns a specific gtfs_pathways file identified by the record_id", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"GTFS-Flex"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Returns the file as an octet-stream."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "404", description = "A GTFS flex file that matches the specified parameters (combination of agencyid, confidence level, and version) was not found.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/octet-stream"},
            method = RequestMethod.GET)
    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<Void> getFlexFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "tdei_record_id for a file, represented as a uuid", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId);


    @Operation(summary = "List flex files meeting specified criteria.", description = "This endpoint returns a json list of all gtfs flex files stored in the TDEI system that meet the specified criteria. Criteria that can be specified include: bounding box, minimum confidence level, flex version, date time and agency id.  This endpoint can be used by an application developer to obtain a list of gtfs flex files in the TDEI system meeting the specified criteria. This endpoint returns a list of file-metadata including the uris of the file, which can be used to fetch the files themselves.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"GTFS-Flex"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `gtfs_flex_download` entities.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GtfsFlexDownload.class)))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<PageableResponse<GtfsFlexDownload>> listFlexFiles(Principal principal, @Parameter(in = ParameterIn.QUERY, description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.", schema = @Schema()) @Valid @RequestParam(value = "bbox", required = false) String bbox, @Parameter(in = ParameterIn.QUERY, description = "Minimum confidence level required by application. Data returned will be at this confidence level or higher. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).", schema = @Schema()) @Valid @RequestParam(value = "confidence_level", required = false) Integer confidenceLevel, @Parameter(in = ParameterIn.QUERY, description = "version name of the flex schema version that the application requests. list of versions can be found with /api/v1.0/gtfs_flex path", schema = @Schema()) @Valid @RequestParam(value = "flex_schema_version", required = false) String flexSchemaVersion, @Parameter(in = ParameterIn.QUERY, description = "tdei-assigned agency id. Necessary to ensure that agency ids are unique. Represented as a UUID.", schema = @Schema()) @Valid @RequestParam(value = "tdei_agency_id", required = false) String tdeiAgencyId, @Parameter(in = ParameterIn.QUERY, description = "date-time for which the caller is interested in obtaining files. all files that are valid at the specified date-time and meet the other criteria will be returned.", schema = @Schema()) @Valid @RequestParam(value = "date_time", required = false) OffsetDateTime dateTime, @Parameter(in = ParameterIn.QUERY, description = "if included, returns the metadata for the specified file, all other parameters will be ignored.", schema = @Schema()) @Valid @RequestParam(value = "tdei_record_id", required = false) String tdeiRecordId, @Parameter(in = ParameterIn.QUERY, description = "Page number, defaults to 0.", schema = @Schema()) @Valid @RequestParam(value = "page_no", required = false) Integer pageNo, @Parameter(in = ParameterIn.QUERY, description = "Integer, between 200 to 100, defaults to 20.", schema = @Schema()) @Valid @RequestParam(value = "page_size", required = false) Integer pageSize);

    @Operation(summary = "List available GTFS flex versions", description = "List GTFS flex versions supported by TDEI.  Returns a json list of the GTFS flex versions supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"GTFS-Flex"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of flex versions supported by TDEI.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionSpec.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<PageableResponse<VersionSpec>> listFlexVersions(Principal principal);

    @Operation(summary = "upload a new gtfs_flex file", description = "This call allows a user to upload or create a new gtfs pathways file. The caller must provide metadata about the file. Required metadata includes information about how and when the data was collected and valid dates of the file. Returns the tdei_record_id of the uploaded file.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {"GTFS-Flex"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful file creation. returns tdei_record_id of new file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{agencyId}",
            produces = {"application/json"},
            consumes = {"application/json", "multipart/form-data"},
            method = RequestMethod.POST)
    @PreAuthorize("@authService.hasAgencyPermission(#principal, #agencyId, 'tdei-user', 'gtfsflex-data_generator','gtfsflex-poc')")
    ResponseEntity<String> uploadFlexFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "", schema = @Schema()) @PathVariable() String agencyId, @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody GtfsFlexUpload body);

}

