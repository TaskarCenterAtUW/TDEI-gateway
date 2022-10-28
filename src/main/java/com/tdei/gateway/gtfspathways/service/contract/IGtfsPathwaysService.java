package com.tdei.gateway.gtfspathways.service.contract;

import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;

import java.security.Principal;
import java.time.OffsetDateTime;

public interface IGtfsPathwaysService {
    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param agencyId
     * @param body
     */
    String uploadPathwaysFile(Principal principal, String agencyId, GtfsPathwaysUpload body);

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal    - Current user
     * @param tdeiRecordId - Record id of the file
     * @return
     */
    String getPathwaysFile(Principal principal, String tdeiRecordId);

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
    PageableResponse<GtfsPathwaysDownload> listPathwaysFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiAgencyId, OffsetDateTime dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize);

    /**
     * Returns the list of flex versions
     *
     * @param principal
     * @return
     */
    PageableResponse<VersionSpec> listPathwaysVersions(Principal principal);
}
