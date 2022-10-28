package com.tdei.gateway.gtfspathways.service;

import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.contract.IGtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.OffsetDateTime;

@Service
public class GtfsPathwaysService implements IGtfsPathwaysService {
    @Override
    public String uploadPathwaysFile(Principal principal, String agencyId, GtfsPathwaysUpload body) {
        return null;
    }

    @Override
    public String getPathwaysFile(Principal principal, String tdeiRecordId) {
        return null;
    }

    @Override
    public PageableResponse<GtfsPathwaysDownload> listPathwaysFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiAgencyId, OffsetDateTime dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public PageableResponse<VersionSpec> listPathwaysVersions(Principal principal) {
        return null;
    }
}
