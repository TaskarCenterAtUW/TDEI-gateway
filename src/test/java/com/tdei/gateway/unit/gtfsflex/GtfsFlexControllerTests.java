package com.tdei.gateway.unit.gtfsflex;

import com.tdei.gateway.gtfsflex.controller.GtfsFlexController;
import com.tdei.gateway.gtfsflex.model.GtfsFlexServiceModel;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexDownload;
import com.tdei.gateway.gtfsflex.model.dto.GtfsFlexUpload;
import com.tdei.gateway.gtfsflex.service.GtfsFlexService;
import com.tdei.gateway.main.model.common.dto.VersionSpec;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import reactor.util.function.Tuples;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void getFlexFile() throws IOException {

        MockHttpServletResponse mockHttp = new MockHttpServletResponse();
        Principal mockPrincipal = mock(Principal.class);
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        HttpHeaders hdr = new HttpHeaders();
        hdr.add("Content-type", "test");
        hdr.add("Content-disposition", "test");
        when(gtfsFlexService.getFlexFile(any(Principal.class), anyString())).thenReturn(Tuples.of(inputStream, hdr));
        var result = gtfsFlexController.getFlexFile(mockPrincipal, "101", mockHttp);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void listFlexFiles() throws FileNotFoundException {
        Principal mockPrincipal = mock(Principal.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        List<GtfsFlexDownload> response = new ArrayList<>();
        GtfsFlexDownload file = new GtfsFlexDownload();
        file.setDownloadUrl("downloadUrl");
        response.addAll(Arrays.asList(file));

        when(gtfsFlexService.listFlexFiles(any(Principal.class), anyString(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
        var result = gtfsFlexController.listFlexFiles(mockPrincipal,
                request,
                Optional.of("test"),
                //Optional.of(Arrays.asList(51.2867602, 51.6918741, -0.5103751, 0.3340155)),
                Optional.empty(),
                Optional.of("test"),
                Optional.of("test"),
                Optional.of("2023-03-03T03:04:00+05:30"),
                Optional.of("test"),
                1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getDownloadUrl()).isEqualTo("downloadUrl");
    }

    @Test
    void listFlexVersions() {
        Principal mockPrincipal = mock(Principal.class);

        List<VersionSpec> versions = new ArrayList<>();
        VersionSpec spec = new VersionSpec();
        spec.setVersion("v1");
        versions.addAll(Arrays.asList(spec));

        when(gtfsFlexService.listFlexVersions(mockPrincipal)).thenReturn(versions);
        var result = gtfsFlexController.listFlexVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getVersion()).isEqualTo("v1");
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

        when(gtfsFlexService.uploadFlexFile(any(Principal.class), any(GtfsFlexUpload.class), any())).thenReturn("newRecordId");
        var result = gtfsFlexController.uploadGtfsFlexFile(mockPrincipal, new GtfsFlexUpload(), file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.ACCEPTED.value());
        assertThat(result.getBody()).isEqualTo("newRecordId");
    }

    @Test
    void listServices() {
        Principal mockPrincipal = mock(Principal.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        List<GtfsFlexServiceModel> response = new ArrayList<>();
        GtfsFlexServiceModel service = new GtfsFlexServiceModel();
        service.setServiceName("Terminal");
        response.addAll(Arrays.asList(service));


        when(gtfsFlexService.listFlexServices(any(), any(), any(), any(), any())).thenReturn(response);
        var result = gtfsFlexController.listFlexServices(mockPrincipal, request, Optional.of("test"), 1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getServiceName()).isEqualTo("Terminal");
    }
}
