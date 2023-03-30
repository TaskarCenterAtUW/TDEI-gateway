package com.tdei.gateway.main.service;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.config.exception.handler.exceptions.ApplicationException;
import com.tdei.gateway.core.config.exception.handler.exceptions.ResourceNotFoundException;
import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.RecordStatus;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.main.service.contract.ICommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonService implements ICommonService {

    private final ApplicationProperties applicationProperties;

    public List<Organization> listOrganizations(Principal principal, HttpServletRequest httpServletRequest, Integer pageNo, Integer pageSize) {
        try {
            //Get auth info
            String apiKey = httpServletRequest.getHeader("x-api-key");
            String authToken = httpServletRequest.getHeader("Authorization");

            WebClient webClient = WebClient.builder()
                    .build();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getManagementSvc().getOrgUrl());
            uri.queryParam("page_no", pageNo);
            uri.queryParam("page_size", pageSize);

            Mono<ResponseEntity<List<Organization>>> entity = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
                    .headers(httpHeaders -> {
                        if (apiKey != null) httpHeaders.set("x-api-key", apiKey);
                        if (authToken != null) httpHeaders.set("Authorization", authToken);
                    })
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(Organization.class);

            var response = entity.single().block().getBody();

            return response;
        } catch (Exception ex) {
            log.error("Error while listing organization ", ex);
            throw new ApplicationException("Error while listing organization");
        }
    }

    @Override
    public PageableResponse<VersionSpec> listApiVersions(Principal principal) {
        return new PageableResponse<>();
    }

    // Get status API inclusion

    @Override
    public RecordStatus getStatus(String tdeiRecordId) {

        try {
            WebClient webClient = WebClient.builder()
                    .build();
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(applicationProperties.getManagementSvc().getLoggerUrl());
            uri.queryParam("tdeiRecordId", tdeiRecordId);
            Mono<ResponseEntity<RecordStatus>> status = webClient.get()
                    .uri(uriBuilder -> uri
                            .build().toUri())
//                .headers(httpHeaders -> {
//                    if (apiKey != null) httpHeaders.set("x-api-key", apiKey);
//                    if (authToken != null) httpHeaders.set("Authorization", authToken);
//                })
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntity(RecordStatus.class);

            var response = status.single().block().getBody();
            return response;
        } catch (WebClientResponseException exception){
            log.error("Webclient exception", exception);
             if(exception.getStatusCode() == HttpStatus.NOT_FOUND){
                 throw new ResourceNotFoundException("Record Not found for "+tdeiRecordId);
             }
            throw exception;
        }

        catch (Exception e){
            log.error("Error while listing organization ", e);

            throw new ApplicationException("Error while fetching status");
        }

    }
}
