package com.tdei.gateway.gtfspathways.controller.contract;

import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.main.model.common.dto.Station;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Validated
public interface IGtfsPathways {


    @Operation(summary = "returns a gtfs_pathways file", description = "returns a specific gtfs_pathways file identified by the record_id", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"GTFS-Pathways"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Returns the file as application/octet-stream."),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized.", content = @Content),

            @ApiResponse(responseCode = "404", description = "A GTFS pathways file that matches the specified parameters (combination of agencyid, stationid, confidence level, and version) was not found.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/octet-stream"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<?> getPathwaysFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "tdei_record_id for a file, represented as a uuid", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId, HttpServletResponse response) throws IOException;

    @Operation(summary = "List pathways files meeting criteria.", description = "This endpoint returns a json list of all gtfs pathways files stored in the TDEI system that meet the specified criteria. Criteria that can be specified include: bounding box, minimum confidence level, pathways version, date time and agency id.  This endpoint can be used by an application developer to obtain a list of gtfs pathways files in the TDEI system meeting the specified criteria. This endpoint returns a list of file-metadata including the uris of the file, which can be used to fetch the files themselves.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"GTFS-Pathways"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `gtfs_pathways_download` entities.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GtfsPathwaysDownload.class)))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/json"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<List<GtfsPathwaysDownload>> listPathwaysFiles(
            Principal principal,
            HttpServletRequest req,
//            @Parameter(in = ParameterIn.QUERY,
//                    description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.",
//                    schema = @Schema()) @Valid @RequestParam(value = "bbox", required = false) String bbox,
            @Parameter(in = ParameterIn.QUERY, description = "Id of a station in the tdei system. gtfs station ids may not be unique.",
                    schema = @Schema()) @Valid @RequestParam(value = "tdei_station_id", required = false) Optional<String> tdeiStationId,
//            @Parameter(in = ParameterIn.QUERY,
//                    description = "Minimum confidence level required by application. Data returned will be at this confidence level or higher. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).",
//                    schema = @Schema()) @Valid @RequestParam(value = "confidence_level", required = false) Optional<Integer> confidenceLevel,
            @Parameter(in = ParameterIn.QUERY, description = "version name of the pathways schema version that the application requests. list of versions can be found with /api/v1.0/gtfs-pathways/versions path",
                    schema = @Schema()) @Valid @RequestParam(value = "pathways_schema_version", required = false) Optional<String> pathwaysSchemaVersion,
            @Parameter(in = ParameterIn.QUERY, description = "date-time (Format. YYYY-MM-DD) for which the caller is interested in obtaining files. all files that are valid at the specified date-time and meet the other criteria will be returned.",
                    schema = @Schema()) @Valid @RequestParam(value = "date_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> dateTime,
            @Parameter(in = ParameterIn.QUERY, description = "tdei-assigned agency id. Necessary to ensure that agency ids are unique. Represented as a UUID.",
                    schema = @Schema()) @Valid @RequestParam(value = "tdei_org_id", required = false) Optional<String> tdeiOrgId,
            @Parameter(in = ParameterIn.QUERY, description = "if included, returns the metadata for the specified file, all other parameters will be ignored.",
                    schema = @Schema()) @Valid @RequestParam(value = "tdei_record_id", required = false) Optional<String> tdeiRecordId,
            @Parameter(in = ParameterIn.QUERY, description = "Integer, defaults to 1.", schema = @Schema()) @Valid @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
            @Parameter(in = ParameterIn.QUERY, description = "page size. integer, between 1 to 50, defaults to 10.",
                    schema = @Schema()) @Valid @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize) throws FileNotFoundException;

    @Operation(summary = "List available GTFS Pathways versions", description = "Lists the versions of GTFS pathways data which are supported by TDEI. Returns a json list of the GTFS pathways versions supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"GTFS-Pathways"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of GTFS Pathways versions suppored by TDEI.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionSpec.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<List<VersionSpec>> listPathwaysVersions(Principal principal);

    @Operation(summary = "create pathways file", description = "This call allows a user to upload or create a new gtfs pathways file. The caller must provide metadata about the file. Required metadata includes information about how and when the data was collected and valid dates of the file. Returns the tdei_record_id of the uploaded file.", security = {
            @SecurityRequirement(name = "AuthorizationToken")}, tags = {"GTFS-Pathways"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "The request has been accepted for processing. Returns the tdei_record_id.", content = @Content(mediaType = "application/text", schema = @Schema(implementation = String.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid. The file may have failed a validation check or the metadata may have been invalid.", content = @Content),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/text"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            method = RequestMethod.POST)
    @PreAuthorize("@authService.hasOrgPermission(#principal, #meta.tdeiOrgId,  'pathways_data_generator', 'poc', 'tdei_admin')")
    ResponseEntity<String> uploadPathwaysFile(Principal principal, @RequestPart("meta") @Valid GtfsPathwaysUpload meta,
                                              @RequestPart("file") @NotNull MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException;


    @Operation(summary = "List Stations", description = "Path used to retrieve the list of stations with data in the TDEI system. Allows callers to get the tdei_station_id id for a station.  Returns the tdei_station_id and station information for all stations with data in the TDEI system. ", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"General"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns an array of `Station` entities. ", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Station.class)))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "stations",
            produces = {"application/json"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<List<Station>> listStations(Principal principal);

}
