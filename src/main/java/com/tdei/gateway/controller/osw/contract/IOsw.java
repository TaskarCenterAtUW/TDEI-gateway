package com.tdei.gateway.controller.osw.contract;

import com.tdei.gateway.model.dto.common.VersionList;
import com.tdei.gateway.model.dto.gtfs.pathways.GtfsPathwaysUpload;
import com.tdei.gateway.model.dto.osw.OswDownload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface IOsw {


    @Operation(summary = "Get an osw file", description = "Gets the details of a single instance of a `gtfs_flex_file` file details. See endpoint description for detail. ", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A url to a gzip'd geojson OSW file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OswDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "A file with the specified id was not found. Use the /tdei_consumer/v1.0/gtfs_pathways or /tdei_consumer/v1.0/gtfs_flex endpoints to find valid file ids. "),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<OswDownload> getOswFile(@Parameter(in = ParameterIn.PATH, description = "A unique identifier for a `flexDataFile`.", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId);


    @Operation(summary = "List osw files meeting criteria.", description = "Returns a list of all osw file entities. See endpoint description for detail.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A url to a gzip'd geojson OSW file.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OswDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "404", description = "OSW data meeting the specifications (version and bounding box) was not found.Data is expected to be available for King and Snohomish counties in Washington, Multnomah and Columbia counties in Oregon, and Baltimore and Harford counties in Maryland."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<OswDownload> listOswFiles(@NotNull @Parameter(in = ParameterIn.QUERY, description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.", required = true, schema = @Schema()) @Valid @RequestParam(value = "bounding_box", required = true) String boundingBox, @Parameter(in = ParameterIn.QUERY, description = "Minimum confidence level required. Data returned will be at this confidence level or higher.", schema = @Schema()) @Valid @RequestParam(value = "confidence_level", required = false) Integer confidenceLevel, @Parameter(in = ParameterIn.QUERY, description = "A string with the name of the schema version the application requests.", schema = @Schema()) @Valid @RequestParam(value = "schema_version", required = false) String schemaVersion);


    @Operation(summary = "List available OSW versions", description = "Lists the versions of OSW data which are supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey")}, tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of pathways versions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionList.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID)."),

            @ApiResponse(responseCode = "500", description = "An server error occurred.")})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<VersionList> listOswVersions();

    @Operation(summary = "Update an osw file.", description = "Updates meta data for an an existing osw file. See endpoint description for detail. ", security = {
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
    ResponseEntity<Void> updateOswFile(@Parameter(in = ParameterIn.PATH, description = "A unique identifier for a `gtfs_pathways`.", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId, @Parameter(in = ParameterIn.DEFAULT, description = "Updated `gtfs_pathways` information.", required = true, schema = @Schema()) @Valid @RequestBody GtfsPathwaysUpload body);

}
