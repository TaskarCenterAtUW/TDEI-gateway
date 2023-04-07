package com.tdei.gateway.main.service.contract;

import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.RecordStatus;
import com.tdei.gateway.main.model.common.dto.VersionList;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
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
    VersionList listApiVersions(Principal principal, HttpServletRequest req) throws MalformedURLException;

    RecordStatus getStatus(String tdeiRecordId);

}
