package com.tdei.gateway.gtfspathways.service.contract;

import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
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
     * @param flexSchemaVersion
     * @param tdeiOrgId
     * @param dateTime
     * @param tdeiRecordId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageableResponse<GtfsPathwaysDownload> listPathwaysFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiOrgId, Date dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize);

    /**
     * Returns the list of flex versions
     *
     * @param principal
     * @return
     */
    PageableResponse<VersionSpec> listPathwaysVersions(Principal principal);


    /**
     * Returns the paginated list of stations
     *
     * @param principal - current user
     * @return Paginated list of stations
     */
    PageableResponse<Station> listStations(Principal principal);
}
