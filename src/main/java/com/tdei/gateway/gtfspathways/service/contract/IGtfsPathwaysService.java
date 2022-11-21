package com.tdei.gateway.gtfspathways.service.contract;

import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.main.model.common.dto.Station;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IGtfsPathwaysService {
    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param tdeiOrgId
     * @param body
     */
    String uploadPathwaysFile(Principal principal, String tdeiOrgId, GtfsPathwaysUpload body, MultipartFile file) throws FileUploadException;

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal    - Current user
     * @param tdeiRecordId - Record id of the file
     * @return
     */
    ResponseEntity<Flux<DataBuffer>> getPathwaysFile(Principal principal, String tdeiRecordId) throws FileNotFoundException;

    /**
     * Gets the paginated list of the flex files with given parameters
     *
     * @param principal
     * @param bbox
     * @param confidenceLevel
     * @param pathwaysSchemaVersion
     * @param tdeiOrgId
     * @param dateTime
     * @param tdeiRecordId
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<GtfsPathwaysDownload> listPathwaysFiles(Principal principal,
                                                 String servletPath,
                                                 Optional<String> tdeiStationId,
                                                 Optional<Integer> confidenceLevel,
                                                 Optional<String> pathwaysSchemaVersion,
                                                 Optional<Date> dateTime,
                                                 Optional<String> tdeiOrgId,
                                                 Optional<String> tdeiRecordId,
                                                 Integer pageNo,
                                                 Integer pageSize) throws FileNotFoundException;

    /**
     * Returns the list of flex versions
     *
     * @param principal
     * @return
     */
    List<VersionSpec> listPathwaysVersions(Principal principal);


    /**
     * Returns the paginated list of stations
     *
     * @param principal - current user
     * @return Paginated list of stations
     */
    List<Station> listStations(Principal principal);
}
