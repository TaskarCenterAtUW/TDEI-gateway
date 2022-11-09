package com.tdei.gateway.unit.gtfsflex;

import com.tdei.gateway.gtfsflex.controller.GtfsFlexController;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.gtfsflex.service.GtfsFlexService;
import com.tdei.gateway.main.model.common.dto.Pageable;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
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
import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GtfsFlexControllerTests {
    @Mock
    private GtfsFlexService gtfsFlexService;

    @InjectMocks
    private GtfsFlexController gtfsFlexController;

    @Test
    void getFlexFile() {

        Principal mockPrincipal = mock(Principal.class);

        when(gtfsFlexService.getFlexFile(any(Principal.class), anyString())).thenReturn("filepath");
        var result = gtfsFlexController.getFlexFile(mockPrincipal, "101");

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("filepath");
    }

    @Test
    void listFlexFiles() {
        Principal mockPrincipal = mock(Principal.class);

        PageableResponse response = new PageableResponse();
        GtfsFlexDownload file = new GtfsFlexDownload();
        file.setDownloadUrl("downloadUrl");
        response.setList(Arrays.asList(file));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(gtfsFlexService.listFlexFiles(any(Principal.class), anyString(), anyInt(), anyString(), anyString(), any(), anyString(), anyInt(), anyInt())).thenReturn(response);
        var result = gtfsFlexController.listFlexFiles(mockPrincipal, "test", 1, "test", "test", OffsetDateTime.now(), "test", 1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getDownloadUrl()).isEqualTo("downloadUrl");
    }

    @Test
    void listFlexVersions() {
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

        when(gtfsFlexService.listFlexVersions(mockPrincipal)).thenReturn(response);
        var result = gtfsFlexController.listFlexVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getVersion()).isEqualTo("v1");
    }

    @Test
    void uploadFlexFile() throws FileUploadException {
        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(gtfsFlexService.uploadFlexFile(any(Principal.class), anyString(), any(GtfsFlexUpload.class), file)).thenReturn("newRecordId");
        var result = gtfsFlexController.uploadGtfsFlexFile(mockPrincipal, new GtfsFlexUpload(), "101", file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("newRecordId");
    }
}
