package com.tdei.gateway.gtfspathways.service.contract;

import com.tdei.gateway.gtfspathways.model.Station;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.main.model.common.dto.VersionList;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IGtfsPathwaysService {
    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param body
     */
    String uploadPathwaysFile(Principal principal, GtfsPathwaysUpload body, MultipartFile file) throws Exception;

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal    - Current user
     * @param tdeiRecordId - Record id of the file
     * @return
     */
    Tuple2<InputStream, HttpHeaders> getPathwaysFile(Principal principal, String tdeiRecordId) throws IOException;

    /**
     * Gets the paginated list of the flex files with given parameters
     *
     * @param principal
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
                                                 Optional<Double[]> bbox,
                                                 Optional<String> tdeiStationId,
                                                 Optional<Integer> confidenceLevel,
                                                 Optional<String> pathwaysSchemaVersion,
                                                 Optional<String> dateTime,
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
    VersionList listPathwaysVersions(Principal principal);


    /**
     * Returns the paginated list of stations
     *
     * @param principal - current user
     * @return Paginated list of stations
     */
    List<Station> listStations(Principal principal, HttpServletRequest httpServletRequest, Optional<String> ownerOrg, Integer pageNo, Integer pageSize);
}
