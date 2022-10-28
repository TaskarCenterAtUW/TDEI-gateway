package com.tdei.gateway.gtfsflex.service;

import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.gtfsflex.service.contract.IGtfsFlexService;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.OffsetDateTime;

@Service
public class GtfsFlexService implements IGtfsFlexService {
    @Override
    public String uploadFlexFile(Principal principal, String agencyId, GtfsFlexUpload body) {
        return null;
    }

    @Override
    public String getFlexFile(Principal principal, String tdeiRecordId) {
        return null;
    }

    @Override
    public PageableResponse<GtfsFlexDownload> listFlexFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiAgencyId, OffsetDateTime dateTime, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public PageableResponse<VersionSpec> listFlexVersions(Principal principal) {
        return null;
    }
}
