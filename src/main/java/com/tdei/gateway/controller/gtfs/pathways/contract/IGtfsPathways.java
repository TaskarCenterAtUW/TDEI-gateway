package com.tdei.gateway.controller.gtfs.pathways.contract;

import com.tdei.gateway.model.dto.common.VersionList;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysDownload;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysFile;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysUpload;
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
public interface IGtfsPathways {


    @Operation(summary = "Retrieve most recent GTFS pathways data for a station.", description = "", security = {
            @SecurityRequirement(name = "ApiKey"),
            @SecurityRequirement(name = "AuthorizationToken")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of urls with gtfs pathways data. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GtfsPathwaysDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A GTFS pathways file that matches the specified parameters (combination of agencyid, stationid, confidence level, and version) was not found."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "latest",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<GtfsPathwaysDownload> getLatestPathwaysFile(@NotNull @Parameter(in = ParameterIn.QUERY, description = "The tdei assigned station id. tdei_station_ids can be obtained using the /stations/ path. Required to use tdei station id as gtfs station ids are not guaranteed to be unique across agencies.", required = true, schema = @Schema()) @Valid @RequestParam(value = "tdei_station_id", required = true) Integer tdeiStationId, @Parameter(in = ParameterIn.QUERY, description = "Minimum confidence level required. Data returned will be at this confidence level or higher.", schema = @Schema()) @Valid @RequestParam(value = "confidence", required = false) Integer confidence, @Parameter(in = ParameterIn.QUERY, description = "version name of the flex schema version that the application requests. list of versions can be found with /api/gtfs_flex path", schema = @Schema()) @Valid @RequestParam(value = "schema_version", required = false) String schemaVersion);


    @Operation(summary = "Get a gtfs pathways file", description = "Gets the details of a single instance of a `gtfs_pathways_file`.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of urls with gtfs pathways data. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GtfsPathwaysDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A file with the specified id was not found. Use the /tdei_consumer/v1.0/gtfs_pathways or /tdei_consumer/v1.0/gtfs_flex endpoints to find valid file ids. "),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<GtfsPathwaysDownload> getPathwaysFile(@Parameter(in = ParameterIn.PATH, description = "A unique identifier for a `gtfs_pathways`.", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId);


    @Operation(summary = "List pathways files meeting criteria.", description = "Returns a list of all `gtfs_pathways_file` file entities. See endpoint description for detail.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `gtfs_pathways_file` entities.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GtfsPathwaysFile.class)))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<GtfsPathwaysFile>> listPathwaysFiles(@Parameter(in = ParameterIn.QUERY, description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.", schema = @Schema()) @Valid @RequestParam(value = "bbox", required = false) String bbox, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "confidence_level", required = false) Integer confidenceLevel, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "version", required = false) String version);


    @Operation(summary = "List available GTFS Pathways versions", description = "Lists the versions of GTFS pathways data which are supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of pathways versions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionList.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<VersionList> listPathwaysVersions();


    @Operation(summary = "Update a gtfs pathways file.", description = "Updates meta data for an an existing gtfs pathways file. See endpoint description for detail. ", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful response."),

            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A GTFS pathways file that matches the specified parameters (combination of agencyid, stationid, confidence level, and version) was not found."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "{tdei_record_id}",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<Void> updatePathwaysFile(@Parameter(in = ParameterIn.PATH, description = "A unique identifier for a `gtfs_pathways`.", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId, @Parameter(in = ParameterIn.DEFAULT, description = "Updated `gtfs_pathways` information.", required = true, schema = @Schema()) @Valid @RequestBody GtfsPathwaysUpload body);


    @Operation(summary = "create pathways file.", description = "Allows application to upload / create a new gtfs pathways file. See endpoint description for detail.", security = {
            @SecurityRequirement(name = "AuthorizationToken")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "File creation accepted.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "upload",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Integer> uploadPathwaysFile(@Parameter(in = ParameterIn.DEFAULT, description = "meta-data and uri for pathways file", required = true, schema = @Schema()) @Valid @RequestBody GtfsPathwaysUpload body);

}
