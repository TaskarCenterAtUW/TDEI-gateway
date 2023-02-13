package com.tdei.gateway.unit.gtfspathways;

import com.tdei.gateway.gtfspathways.controller.GtfsPathwaysController;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.GtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.Station;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GtfsPathwaysControllerTests {
    @Mock
    private GtfsPathwaysService gtfsPathwaysService;

    @InjectMocks
    private GtfsPathwaysController gtfsPathwaysController;

    @Test
    void getPathwaysFile() throws IOException {
        MockHttpServletResponse mockHttp = new MockHttpServletResponse();
        Principal mockPrincipal = mock(Principal.class);
        InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes());
        HttpHeaders hdr = new HttpHeaders();
        hdr.add("Content-type", "test");
        hdr.add("Content-disposition", "test");
        when(gtfsPathwaysService.getPathwaysFile(any(Principal.class), anyString())).thenReturn(Tuples.of(anyInputStream, hdr));
        var result = gtfsPathwaysController.getPathwaysFile(mockPrincipal, "101", mockHttp);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    void listStations() {
        Principal mockPrincipal = mock(Principal.class);

        List<Station> response;

        Station station = new Station();
        station.setStationName("TACOMA");
        response = Arrays.asList(station);

        when(gtfsPathwaysService.listStations(mockPrincipal)).thenReturn(response);
        var result = gtfsPathwaysController.listStations(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getStationName()).isEqualTo("TACOMA");
    }

    @Test
    void listPathwaysFiles() throws FileNotFoundException {
        Principal mockPrincipal = mock(Principal.class);
        MockHttpServletRequest request = new MockHttpServletRequest();


        List<GtfsPathwaysDownload> response;
        GtfsPathwaysDownload file = new GtfsPathwaysDownload();
        file.setDownloadUrl("downloadUrl");
        response = Arrays.asList(file);

        when(gtfsPathwaysService.listPathwaysFiles(any(Principal.class), anyString(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
        var result = gtfsPathwaysController.listPathwaysFiles(mockPrincipal,
                request,
                Optional.of(new Double[]{10.0, 20.0, 30.0, 40.0}),
                //Optional.of(1),
                Optional.of("test"),
                Optional.of("test"),
                Optional.of(new Date()),
                Optional.of("test"),
                Optional.of("test"),
                1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getDownloadUrl()).isEqualTo("downloadUrl");
    }

    @Test
    void listPathwaysVersions() {
        Principal mockPrincipal = mock(Principal.class);

        List<VersionSpec> response;
        VersionSpec spec = new VersionSpec();
        spec.setVersion("v1");
        response = Arrays.asList(spec);


        when(gtfsPathwaysService.listPathwaysVersions(mockPrincipal)).thenReturn(response);
        var result = gtfsPathwaysController.listPathwaysVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().stream().findFirst().get().getVersion()).isEqualTo("v1");
    }

    @Test
    void uploadPathwaysFile() throws FileUploadException {

        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockHttpServletRequest request = new MockHttpServletRequest();

        String orgId = "101";
        when(gtfsPathwaysService.uploadPathwaysFile(any(Principal.class), any(GtfsPathwaysUpload.class), any(MockMultipartFile.class))).thenReturn("newRecordId");
        var result = gtfsPathwaysController.uploadPathwaysFile(mockPrincipal, new GtfsPathwaysUpload(), file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.ACCEPTED.value());
        assertThat(result.getBody()).isEqualTo("newRecordId");
    }
}
