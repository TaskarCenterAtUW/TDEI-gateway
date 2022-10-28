package com.tdei.gateway.osw.service;

import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import com.tdei.gateway.osw.service.contract.IOswService;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class OswService implements IOswService {
    @Override
    public String uploadOswFile(Principal principal, String agencyId, OswUpload body) {
        return null;
    }

    @Override
    public String getOswFile(Principal principal, String tdeiRecordId) {
        return null;
    }

    @Override
    public PageableResponse<OswDownload> listOswFiles(Principal principal, String bbox, Integer confidenceLevel, String flexSchemaVersion, String tdeiRecordId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public PageableResponse<VersionSpec> listOswVersions(Principal principal) {
        return null;
    }
}
