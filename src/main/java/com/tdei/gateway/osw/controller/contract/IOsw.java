package com.tdei.gateway.osw.controller.contract;

import com.tdei.gateway.main.model.common.dto.VersionList;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IOsw {

    @Operation(summary = "returns a geojson osw file", description = "returns a specific osw file identified by the tdei_record_id", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the osw file - will be geojson.", content = @Content(mediaType = "application/octet-stream")),

            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),

            @ApiResponse(responseCode = "404", description = "A file with the specified tdei_record_id was not found. Use the /api/v1/osw endpoints to find valid file ids.", content = @Content(mediaType = "application/json")),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/octet-stream", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<?> getOswFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "tdei_record_id for a file, represented as a uuid", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId, HttpServletResponse response) throws IOException;


    @Operation(summary = "List osw files meeting criteria.", description = "This endpoint returns a list of url to gzip'd geojson files with osw data that meet the specified criteria. Criteria that can be specified include: a polygon (bounding box), minimum confidence level and osw version. This endpoint can be used by an application developer to obtain a list of osw files in the TDEI system meeting the specified criteria.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `OswDownload` entities.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OswDownload.class)))),
            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/json", "multipart/form-data"},
            method = RequestMethod.GET)
    ResponseEntity<List<OswDownload>> listOswFiles(Principal principal,
                                                   HttpServletRequest req,
                                                   @Parameter(in = ParameterIn.QUERY,
                                                           example = "[23.5, 34.65, 89.50, 65.98]",
                                                           description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.",
                                                           array = @ArraySchema(minItems = 4, maxItems = 4, schema = @Schema(implementation = Double.class))
                                                   )
                                                   @RequestParam(value = "bbox", required = false) Optional<Double[]> bbox,
//                                                                     @Parameter(in = ParameterIn.QUERY,
//                                                                             description = "Minimum confidence level required by application. Data returned will be at this confidence level or higher. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).", schema = @Schema())
//                                                                     @Valid @RequestParam(value = "confidence_level", required = false) Integer confidenceLevel,
                                                   @Parameter(in = ParameterIn.QUERY,
                                                           description = "version name of the osw schema version that the application requests. list of versions can be found with /api/v1/osw/versions.", schema = @Schema())
                                                   @Valid @RequestParam(value = "osw_schema_version", required = false) Optional<String> oswSchemaVersion,
                                                   @Parameter(in = ParameterIn.QUERY,
                                                           description = "tdei-assigned organization id. Represented as a UUID.", schema = @Schema())
                                                   @Valid @RequestParam(value = " tdei_org_id", required = false) Optional<String> tdeiOrgId,
                                                   @Parameter(in = ParameterIn.QUERY, description = "date-time for which the caller is interested in obtaining files. all files that are valid at the specified date-time and meet the other criteria will be returned.",
                                                           schema = @Schema()) @Valid @RequestParam(value = "date_time", required = false) Optional<String> dateTime,
                                                   @Parameter(in = ParameterIn.QUERY, description = "tdei_record_id, unique id represents file.", schema = @Schema())
                                                   @Valid @RequestParam(value = "tdei_record_id", required = false) Optional<String> tdeiRecordId,
                                                   @Parameter(in = ParameterIn.QUERY, description = "Integer, defaults to 1.", schema = @Schema()) @Valid @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                   @Parameter(in = ParameterIn.QUERY, description = "page size. integer, between 1 to 50, defaults to 10.",
                                                           schema = @Schema()) @Valid @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize) throws FileNotFoundException;

    @Operation(summary = "List available OSW versions", description = "Lists the versions of OSW data which are supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of OSW versions supported by TDEI.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionList.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<VersionList> listOswVersions(Principal principal, HttpServletRequest req) throws MalformedURLException;

    @Operation(summary = "Path used to upload/create a new osw data file.", description = "This path allows a user to upload or create a new osw file. The caller must provide metadata about the file - includes information about how and when the data was collected and valid dates of the file. Returns the tdei_record_id of the uploaded file.", security = {
            @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "The request has been accepted for processing. returns the tdei_record_id, unique identifier for uploaded file.", content = @Content(mediaType = "application/text", schema = @Schema(implementation = String.class, example = "c53cc40147c845a1a3a72867e10a7d88"))),
            @ApiResponse(responseCode = "400", description = "The request was invalid.", content = @Content),
            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),
            @ApiResponse(responseCode = "403", description = "This request is unauthorized.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/text"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            method = RequestMethod.POST)
    @PreAuthorize("@authService.hasOrgPermission(#principal, #meta.tdeiOrgId,  'osw_data_generator', 'poc', 'tdei_admin')")
    ResponseEntity<String> uploadOswFile(Principal principal, @RequestPart("meta") @Valid OswUpload meta,
                                         @RequestPart("file") @NotNull MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException;

}

