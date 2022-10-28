package com.tdei.gateway.osw.service.contract;

import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;

import java.security.Principal;

public interface IOswService {
    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param agencyId
     * @param body
     */
    String uploadOswFile(Principal principal, String agencyId, OswUpload body);

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal    - Current user
     * @param tdeiRecordId - Record id of the file
     * @return
     */
    String getOswFile(Principal principal, String tdeiRecordId);

    /**
     * Gets the paginated list of the flex files with given parameters
     *
     * @param principal
     * @param bbox
     * @param confidenceLevel
     * @param flexSchemaVersion
     * @param tdeiAgencyId
     * @param dateTime
     * @param tdeiRecordId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageableResponse<OswDownload> listOswFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiRecordId, Integer pageNo, Integer pageSize);

    /**
     * Returns the list of flex versions
     *
     * @param principal
     * @return
     */
    PageableResponse<VersionSpec> listOswVersions(Principal principal);
}
