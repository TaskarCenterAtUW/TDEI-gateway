package com.tdei.gateway.controller.gtfs.flex.contract;

import com.tdei.gateway.model.dto.common.VersionList;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexDownload;
import com.tdei.gateway.model.dto.gtfs.flex.GtfsFlexFile;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface IGtfsFlex {

    @Operation(summary = "Get a flex file", description = "Gets the details of a single instance of a `gtfs_flex_file` file details. See endpoint description for detail. ", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response object for returning flex data. Includes metadata about flex file and uri to flex file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GtfsFlexDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A file with the specified id was not found. Use the /tdei_consumer/v1.0/gtfs_pathways or /tdei_consumer/v1.0/gtfs_flex endpoints to find valid file ids. "),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<GtfsFlexDownload> getFlexFile(@Parameter(in = ParameterIn.PATH, description = "A unique identifier for a `flexDataFile`.", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId);


    @Operation(summary = "Retrieve latest GTFS flex data for an agency.", description = "Returns the latest gtfs flex data file associated with an agency. See endpoint description for detail.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response object for returning flex data. Includes metadata about flex file and uri to flex file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GtfsFlexDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A GTFS flex file that matches the specified parameters (combination of agencyid, confidence level, and version) was not found."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "latest",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<GtfsFlexDownload> getLatestFlexFile(@NotNull @Parameter(in = ParameterIn.QUERY, description = "The tdei assigned agency id. tdei agency_ids can be obtained using the /agencies/ path. Required to use tdei agency id as gtfs agency ids are not guaranteed to be unique across agencies.", required = true, schema = @Schema()) @Valid @RequestParam(value = "tdei_agency_id", required = true) Integer tdeiAgencyId, @Parameter(in = ParameterIn.QUERY, description = "Minimum confidence level required. Data returned will be at this confidence level or higher.", schema = @Schema()) @Valid @RequestParam(value = "confidence", required = false) Integer confidence, @Parameter(in = ParameterIn.QUERY, description = "version name of the flex schema version that the application requests. list of versions can be found with /api/gtfs_flex path", schema = @Schema()) @Valid @RequestParam(value = "schema_version", required = false) String schemaVersion);


    @Operation(summary = "List flex files meeting criteria.", description = "Returns a list of all `gtfs_flex_file` file entities. See endpoint description for detail.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `gtfs_flex_file` entities.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GtfsFlexFile.class)))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<GtfsFlexFile>> listFlexFiles(@Parameter(in = ParameterIn.QUERY, description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.", schema = @Schema()) @Valid @RequestParam(value = "bbox", required = false) String bbox, @Parameter(in = ParameterIn.QUERY, description = "Minimum confidence level required by application. Data returned will be at this confidence level or higher.", schema = @Schema()) @Valid @RequestParam(value = "confidence-level", required = false) Integer confidenceLevel, @Parameter(in = ParameterIn.QUERY, description = "version name of the pathways schema version that the application requests. list of versions can be found with /api/gtfs_pathways path", schema = @Schema()) @Valid @RequestParam(value = "version", required = false) String version);


    @Operation(summary = "Update a gtfs flex file.", description = "Updates meta data for an an existing gtfs flex file. See endpoint description for detail. ", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful response."),

            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A GTFS flex file that matches the specified parameters (combination of agencyid, confidence level, and version) was not found."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "{tdei_record_id}",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<Void> updateFlexFile(@Parameter(in = ParameterIn.PATH, description = "A unique identifier for a `flexDataFile`.", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId, @Parameter(in = ParameterIn.DEFAULT, description = "Flex data file meta data and uri from which the updated file can be retrieved.", required = true, schema = @Schema()) @Valid @RequestBody GtfsFlexUpload body);


    @Operation(summary = "upload a new gtfs_flex file", description = "Allows application to upload / create a new gtfs_flex file. See endpoint description for detail.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful file creation. returns data_record_id of new file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "upload",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Integer> uploadFlexFile(@Parameter(in = ParameterIn.DEFAULT, description = "specify metadata and uri for new file", required = true, schema = @Schema()) @Valid @RequestBody GtfsFlexUpload body);


    @Operation(summary = "List available GTFS flex versions", description = "Lists the versions of GTFS flex data which are supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of flex versions supported by TDEI.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionList.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<VersionList> listFlexVersions();


}

