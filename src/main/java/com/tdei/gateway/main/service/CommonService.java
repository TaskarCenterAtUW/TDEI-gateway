package com.tdei.gateway.main.service;

import com.tdei.gateway.main.model.common.dto.Agency;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.Station;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.main.service.contract.ICommonService;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommonService implements ICommonService {
    public PageableResponse<Agency> listAgencies(Principal principal) {
        return new PageableResponse<>();
    }

    @Override
    public PageableResponse<VersionSpec> listApiVersions(Principal principal) {
        return new PageableResponse<>();
    }

    @Override
    public PageableResponse<Station> listStations(Principal principal) {
        return new PageableResponse<>();
    }
}
