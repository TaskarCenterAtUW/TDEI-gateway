package com.tdei.gateway.unit.osw;

import com.tdei.gateway.main.model.common.dto.Pageable;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import com.tdei.gateway.osw.controller.OswController;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import com.tdei.gateway.osw.service.OswService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OswControllerTests {
    @Mock
    private OswService oswService;

    @InjectMocks
    private OswController oswController;

    @Test
    void getOswFile() {

        Principal mockPrincipal = mock(Principal.class);

        when(oswService.getOswFile(any(Principal.class), anyString())).thenReturn("filepath");
        var result = oswController.getOswFile(mockPrincipal, "101");

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("filepath");
    }

    @Test
    void listOswFiles() {
        Principal mockPrincipal = mock(Principal.class);

        PageableResponse response = new PageableResponse();
        OswDownload file = new OswDownload();
        file.setDownloadUrl("downloadUrl");
        response.setList(Arrays.asList(file));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(oswService.listOswFiles(any(Principal.class), anyString(), anyInt(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(response);
        var result = oswController.listOswFiles(mockPrincipal, "test", 1, "test", "test", 1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getDownloadUrl()).isEqualTo("downloadUrl");
    }

    @Test
    void listOswVersions() {
        Principal mockPrincipal = mock(Principal.class);

        PageableResponse response = new PageableResponse();
        VersionSpec spec = new VersionSpec();
        spec.setVersion("v1");
        response.setList(Arrays.asList(spec));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(oswService.listOswVersions(mockPrincipal)).thenReturn(response);
        var result = oswController.listOswVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getVersion()).isEqualTo("v1");
    }

    @Test
    void uploadOswFile() throws FileUploadException {

        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(oswService.uploadOswFile(any(Principal.class), anyString(), any(OswUpload.class), any())).thenReturn("newRecordId");
        var result = oswController.uploadOswFile(mockPrincipal, new OswUpload(), "101", file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("newRecordId");
    }
}
