package com.tdei.gateway.unit.gtfspathways;

import com.tdei.gateway.gtfspathways.controller.GtfsPathwaysController;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysDownload;
import com.tdei.gateway.gtfspathways.model.dto.GtfsPathwaysUpload;
import com.tdei.gateway.gtfspathways.service.GtfsPathwaysService;
import com.tdei.gateway.main.model.common.dto.Pageable;
import com.tdei.gateway.main.model.common.dto.PageableResponse;
import com.tdei.gateway.main.model.common.dto.Station;
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

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;

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
    void getPathwaysFile() throws FileNotFoundException {

//        Principal mockPrincipal = mock(Principal.class);
//
//        when(gtfsPathwaysService.getPathwaysFile(any(Principal.class), anyString())).thenReturn(new ResponseEntity<>());
//        var result = gtfsPathwaysController.getPathwaysFile(mockPrincipal, "101");
//
//        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
//        assertThat(result.getBody()).isEqualTo("filepath");
    }


    @Test
    void listStations() {
        Principal mockPrincipal = mock(Principal.class);

        PageableResponse response = new PageableResponse();
        Station station = new Station();
        station.setStationName("TACOMA");
        response.setList(Arrays.asList(station));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(gtfsPathwaysService.listStations(mockPrincipal)).thenReturn(response);
        var result = gtfsPathwaysController.listStations(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getStationName()).isEqualTo("TACOMA");
    }

    @Test
    void listPathwaysFiles() {
        Principal mockPrincipal = mock(Principal.class);

        PageableResponse response = new PageableResponse();
        GtfsPathwaysDownload file = new GtfsPathwaysDownload();
        file.setDownloadUrl("downloadUrl");
        response.setList(Arrays.asList(file));
        Pageable pg = new Pageable();
        pg.setCurrentPage(1);
        pg.setNumPages(1);
        pg.setTotalItems(1);
        pg.setTotalPages(1);
        response.setPageable(pg);

        when(gtfsPathwaysService.listPathwaysFiles(any(Principal.class), anyString(), anyInt(), anyString(), anyString(), any(), anyString(), anyInt(), anyInt())).thenReturn(response);
        var result = gtfsPathwaysController.listPathwaysFiles(mockPrincipal, "test", 1, "test", new Date(), "test", "test", 1, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getDownloadUrl()).isEqualTo("downloadUrl");
    }

    @Test
    void listPathwaysVersions() {
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

        when(gtfsPathwaysService.listPathwaysVersions(mockPrincipal)).thenReturn(response);
        var result = gtfsPathwaysController.listPathwaysVersions(mockPrincipal);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody().getList().stream().findFirst().get().getVersion()).isEqualTo("v1");
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

        when(gtfsPathwaysService.uploadPathwaysFile(any(Principal.class), anyString(), any(GtfsPathwaysUpload.class), file)).thenReturn("newRecordId");
        var result = gtfsPathwaysController.uploadPathwaysFile(mockPrincipal, new GtfsPathwaysUpload(), "101", file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("newRecordId");
    }
}
