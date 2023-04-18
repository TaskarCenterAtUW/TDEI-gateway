/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.35).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.tdei.gateway.main.controller.contract;

import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.RecordStatus;
import com.tdei.gateway.main.model.common.dto.VersionList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@Validated
public interface ICommon {
    @Operation(summary = "Authenticates the user to the TDEI system.", description = "Authenticates the user to the TDEI system. Returns access token.",
            tags = {"Authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - Returns the access token for the validated user.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "authenticate",
            produces = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<TokenResponse> authenticate(@RequestBody LoginModel loginModel);

    @Operation(summary = "List organizations", description = "Path used to retrieve the list of organizations with data in the TDEI system. Allows callers to get the tdei_org_id id for an organization.\n" +
            "\n" +
            "Returns the tdei_org_id and organization information for all organizations with data in the TDEI system.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"General"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - returns an array of `Agency` entities.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Organization.class)))),

            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "organizations",
            produces = {"application/json"},
            method = RequestMethod.GET)
//    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<List<Organization>> listOrganizations(Principal principal, HttpServletRequest httpServletRequest, @Parameter(in = ParameterIn.QUERY, description = "Integer, defaults to 1.", schema = @Schema()) @Valid @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                         @Parameter(in = ParameterIn.QUERY, description = "page size. integer, between 1 to 50, defaults to 10.",
                                                                 schema = @Schema()) @Valid @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize);

    @Operation(summary = "List available API versions", description = "Returns a json list of the versions of the TDEI API which are available.", security = {
            @SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "AuthorizationToken")}, tags = {"General"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of versions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersionList.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "api",
            produces = {"application/json"},
            method = RequestMethod.GET)
//    @PreAuthorize("@authService.hasPermission(#principal, 'tdei-user')")
    ResponseEntity<VersionList> listApiVersions(Principal principal, HttpServletRequest req) throws MalformedURLException;

    @Operation(summary = "Get status", description = "Fetches the status of an uploaded record", tags = {"General"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns status of record", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecordStatus.class))),
            @ApiResponse(responseCode = "400", description = "Record not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),
            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "status",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<RecordStatus> getStatus(@Parameter(in = ParameterIn.QUERY, description = "tdeiRecordId received during upload") String tdeiRecordId);

    @Operation(summary = "Re-issue access token", description = "Re-issues access token provided the valid refresh token",
            tags = {"Authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful validation of refresh token - Returns the refreshed access token.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),

            @ApiResponse(responseCode = "401", description = "This request is unauthenticated.", content = @Content),

            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)})
    @RequestMapping(value = "refresh-token",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<TokenResponse> refreshToken(@RequestBody String refreshToken);
}

