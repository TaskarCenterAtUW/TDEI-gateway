package com.tdei.gateway.main.service.contract;

import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

public interface ICommonService {

    /**
     * Returns the paginated list of agencies
     *
     * @param principal - current user
     * @return Paginated list of agencies
     */
    List<Organization> listOrganizations(Principal principal, HttpServletRequest httpServletRequest, Integer pageNo, Integer pageSize);

    /**
     * Returns the paginated list of api versions
     *
     * @param principal - current user
     * @return Paginated list of api versions
     */
    PageableResponse<VersionSpec> listApiVersions(Principal principal);

}
