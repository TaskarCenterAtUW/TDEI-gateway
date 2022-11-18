package com.tdei.gateway.main.service.contract;

import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;

import java.security.Principal;

public interface ICommonService {

    /**
     * Returns the paginated list of agencies
     *
     * @param principal - current user
     * @return Paginated list of agencies
     */
    PageableResponse<Organization> listOrganizations(Principal principal);

    /**
     * Returns the paginated list of api versions
     *
     * @param principal - current user
     * @return Paginated list of api versions
     */
    PageableResponse<VersionSpec> listApiVersions(Principal principal);

}
