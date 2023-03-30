package com.tdei.gateway.main.controller;

import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.core.service.auth.AuthService;
import com.tdei.gateway.main.controller.contract.ICommon;
import com.tdei.gateway.main.model.common.dto.Organization;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.RecordStatus;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.main.service.CommonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "General")
public class CommonController implements ICommon {
    private final AuthService authService;
    private final CommonService commonService;

    @Override
    public ResponseEntity<TokenResponse> authenticate(LoginModel loginModel) {
        return ResponseEntity.ok(authService.authenticate(loginModel));
    }

    @Override
    public ResponseEntity<List<Organization>> listOrganizations(Principal principal, HttpServletRequest httpServletRequest, Integer pageNo, Integer pageSize) {

        var response = commonService.listOrganizations(principal, httpServletRequest, pageNo, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PageableResponse<VersionSpec>> listApiVersions(Principal principal) {

        PageableResponse response = commonService.listApiVersions(principal);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RecordStatus> getStatus(String tdeiRecordId) {
        return ResponseEntity.ok(commonService.getStatus(tdeiRecordId));
    }
}
