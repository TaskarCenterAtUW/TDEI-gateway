package com.tdei.gateway.gtfsflex.service.contract;

import com.tdei.gateway.gtfsflex.model.GtfsFlexServiceModel;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.OffsetDateTime;

public interface IGtfsFlexService {

    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param tdeiOrgId
     * @param body
     */
    String uploadFlexFile(Principal principal, String tdeiOrgId, GtfsFlexUpload body, MultipartFile file) throws FileUploadException;

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal    - Current user
     * @param tdeiRecordId - Record id of the file
     * @return
     */
    String getFlexFile(Principal principal, String tdeiRecordId);

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
    PageableResponse<GtfsFlexDownload> listFlexFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiOrgId, OffsetDateTime dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize);

    /**
     * Returns the list of flex versions
     *
     * @param principal
     * @return
     */
    PageableResponse<VersionSpec> listFlexVersions(Principal principal);

    /**
     * List GTFS Flex Services
     *
     * @param tdeiOrgId
     * @return
     */
    PageableResponse<GtfsFlexServiceModel> listFlexServices(Principal principal, String tdeiOrgId);
}
