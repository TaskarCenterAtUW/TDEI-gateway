package com.tdei.gateway.unit.osw;

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
import java.util.*;

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
    void getOswFile() throws IOException {
        MockHttpServletResponse mockHttp = new MockHttpServletResponse();
        Principal mockPrincipal = mock(Principal.class);
        InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes());
        HttpHeaders hdr = new HttpHeaders();
        hdr.add("Content-type", "test");
        hdr.add("Content-disposition", "test");
        when(oswService.getOswFile(any(Principal.class), anyString())).thenReturn(Tuples.of(anyInputStream, hdr));
        var result = oswController.getOswFile(mockPrincipal, "101", mockHttp);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void listOswFiles() throws FileNotFoundException {
        Principal mockPrincipal = mock(Principal.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        List<OswDownload> response = new ArrayList<>();
        OswDownload file = new OswDownload();
        file.setDownloadUrl("downloadUrl");
        response.addAll(Arrays.asList(file));

        when(oswService.listOswFiles(any(Principal.class), any(),
                any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
        var result = oswController.listOswFiles(mockPrincipal,
                request,
                Optional.of("test"),
                Optional.of("test"),
                //Optional.of(1),
                Optional.of("test"),
                Optional.of("test"),
                Optional.of(new Date()),
                Optional.of("test"),
                1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getDownloadUrl()).isEqualTo("downloadUrl");
    }

    @Test
    void listOswVersions() {
        Principal mockPrincipal = mock(Principal.class);

        List<VersionSpec> response = new ArrayList<>();
        VersionSpec spec = new VersionSpec();
        spec.setVersion("v1");
        response.addAll(Arrays.asList(spec));

        when(oswService.listOswVersions(mockPrincipal)).thenReturn(response);
        var result = oswController.listOswVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getVersion()).isEqualTo("v1");
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

        when(oswService.uploadOswFile(any(Principal.class), any(OswUpload.class), any())).thenReturn("newRecordId");
        var result = oswController.uploadOswFile(mockPrincipal, new OswUpload(), file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.ACCEPTED.value());
        assertThat(result.getBody()).isEqualTo("newRecordId");
    }
}
