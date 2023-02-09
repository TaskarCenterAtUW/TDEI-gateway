package com.tdei.gateway.gtfsflex.service.contract;

import com.tdei.gateway.gtfsflex.model.GtfsFlexServiceModel;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IGtfsFlexService {

    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param body
     */
    String uploadFlexFile(Principal principal, GtfsFlexUpload body, MultipartFile file) throws FileUploadException;

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal    - Current user
     * @param tdeiRecordId - Record id of the file
     * @return
     */
    Tuple2<InputStream, HttpHeaders> getFlexFile(Principal principal, String tdeiRecordId) throws FileNotFoundException;

    /**
     * Gets the paginated list of the flex files with given parameters
     *
     * @param principal
     * @param confidenceLevel
     * @param flexSchemaVersion
     * @param tdeiOrgId
     * @param dateTime
     * @param tdeiRecordId
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<GtfsFlexDownload> listFlexFiles(Principal principal,
                                         String servletPath,
                                         Optional<String> tdeiServiceId,
                                         Optional<Double[]> bbox,
                                         Optional<Integer> confidenceLevel,
                                         Optional<String> flexSchemaVersion,
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
    List<VersionSpec> listFlexVersions(Principal principal);

    /**
     * List GTFS Flex Services
     *
     * @param tdeiOrgId
     * @return
     */
    List<GtfsFlexServiceModel> listFlexServices(Principal principal, String tdeiOrgId);
}
