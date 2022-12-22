package com.tdei.gateway.osw.controller.contract;

import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

public interface IOsw {

    @Operation(summary = "returns a geojson osw file", description = "returns a specific osw file identified by the record_id", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the osw file - will be geojson. I wasn't sure what should be returned here - I set the type geojson, but was not sure how this is typically done. "),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "404", description = "OSW data meeting the specifications (version and bounding box) was not found.Data is expected to be available for King and Snohomish counties in Washington, Multnomah and Columbia counties in Oregon, and Baltimore and Harford counties in Maryland.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "{tdei_record_id}",
            produces = {"application/octet-stream"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<?> getOswFile(Principal principal, @Parameter(in = ParameterIn.PATH, description = "tdei_record_id for a file, represented as a uuid", required = true, schema = @Schema()) @PathVariable("tdei_record_id") String tdeiRecordId, HttpServletResponse response) throws IOException;


    @Operation(summary = "List osw files meeting criteria.", description = "This endpoint returns a list of url to gzip'd geojson files with osw data that meet the specified criteria. Criteria that can be specified include: bounding box, minimum confidence level and pathways version.  This endpoint can be used by an application developer to obtain a list of osw files in the TDEI system meeting the specified criteria.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `osw_download` entities.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OswDownload.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "404", description = "OSW data meeting the specifications (version and bounding box) was not found.Data is expected to be available for King and Snohomish counties in Washington, Multnomah and Columbia counties in Oregon, and Baltimore and Harford counties in Maryland.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/json", "multipart/form-data"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<List<OswDownload>> listOswFiles(Principal principal,
                                                   HttpServletRequest req,
                                                   @Parameter(in = ParameterIn.QUERY, description = "Id of a service in the tdei system. gtfs service ids may not be unique.",
                                                           schema = @Schema()) @Valid @RequestParam(value = "tdei_service_id", required = false) Optional<String> tdeiServiceId,
                                                   @Parameter(in = ParameterIn.QUERY,
                                                           description = "A bounding box which specifies the area to be searched. A bounding box is specified by a string providing the lat/lon coordinates of the corners of the bounding box. Coordinate should be specified as west, south, east, north.", schema = @Schema())
                                                   @Valid @RequestParam(value = "bbox", required = false) Optional<String> bbox,
//                                                                     @Parameter(in = ParameterIn.QUERY,
//                                                                             description = "Minimum confidence level required by application. Data returned will be at this confidence level or higher. Confidence level range is: 0 (very low confidence) to 100 (very high confidence).", schema = @Schema())
//                                                                     @Valid @RequestParam(value = "confidence_level", required = false) Integer confidenceLevel,
                                                   @Parameter(in = ParameterIn.QUERY,
                                                           description = "version name of the flex schema version that the application requests. list of versions can be found with /api/v1/osw/versions path", schema = @Schema())
                                                   @Valid @RequestParam(value = "flex_schema_version", required = false) Optional<String> flexSchemaVersion,
                                                   @Parameter(in = ParameterIn.QUERY,
                                                           description = "tdei-assigned agency id. Necessary to ensure that agency ids are unique. Represented as a UUID.", schema = @Schema())
                                                   @Valid @RequestParam(value = " tdei_org_id", required = false) Optional<String> tdeiOrgId,
                                                   @Parameter(in = ParameterIn.QUERY, description = "date-time (Format. YYYY-MM-DD) for which the caller is interested in obtaining files. all files that are valid at the specified date-time and meet the other criteria will be returned.",
                                                           schema = @Schema()) @Valid @RequestParam(value = "date_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> dateTime,
                                                   @Parameter(in = ParameterIn.QUERY, description = "if included, returns the metadata for the specified file, all other parameters will be ignored.", schema = @Schema())
                                                   @Valid @RequestParam(value = "tdei_record_id", required = false) Optional<String> tdeiRecordId,
                                                   @Parameter(in = ParameterIn.QUERY, description = "Integer, defaults to 1.", schema = @Schema()) @Valid @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                   @Parameter(in = ParameterIn.QUERY, description = "page size. integer, between 200 to 100, defaults to 20.",
                                                           schema = @Schema()) @Valid @RequestParam(value = "page_size", required = false, defaultValue = "20") Integer pageSize) throws FileNotFoundException;

    @Operation(summary = "List available OSW versions", description = "Lists the versions of OSW data which are supported by TDEI.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of OSW versions supported by TDEI.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionSpec.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "versions",
            produces = {"application/json"},
            method = RequestMethod.GET)
        //@PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<List<VersionSpec>> listOswVersions(Principal principal);

    @Operation(summary = "Path used to upload/create a new osw data file.", description = "This path allows a user to upload or create a new osw file. The caller must provide metadata about the file - includes information about how and when the data was collected and valid dates of the file. Returns the tdei_record_id of the uploaded file.", security = {
            @SecurityRequirement(name = "AuthorizationToken")}, tags = {"OSW"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "The request has been accepted for processing.", content = @Content(mediaType = "application/text", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "The request was invalid. For example, trying to do a meta-data update that is not allowed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "This request is unauthorized. appID is invalid. Please obtain a valid application ID (appID).", content = @Content),
            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "",
            produces = {"application/text"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            method = RequestMethod.POST)
    @PreAuthorize("@authService.hasOrgPermission(#principal, #meta.tdeiOrgId,  'osw_data_generator')")
    ResponseEntity<String> uploadOswFile(Principal principal, @RequestPart("meta") @Valid OswUpload meta,
                                         @RequestPart("file") @NotNull MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException;

}
