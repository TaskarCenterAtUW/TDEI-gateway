package com.tdei.gateway.osw.service.contract;

import com.tdei.gateway.main.model.common.dto.VersionList;
import com.tdei.gateway.osw.model.dto.OswDownload;
import com.tdei.gateway.osw.model.dto.OswUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IOswService {
    /**
     * Returns the uploaded file record id
     *
     * @param principal
     * @param body
     */
    String uploadOswFile(Principal principal, OswUpload body, MultipartFile file) throws FileUploadException;

    /**
     * Gets the requested gtfs flex file
     *
     * @param principal
     * @param tdeiRecordId
     * @return
     * @throws IOException
     */
    Tuple2<InputStream, HttpHeaders> getOswFile(Principal principal, String tdeiRecordId) throws FileNotFoundException;

    /**
     * Gets the paginated list of the flex files with given parameters
     *
     * @param principal
     * @param servletPath
     * @param confidenceLevel
     * @param oswSchemaVersion
     * @param dateTime
     * @param tdeiOrgId
     * @param tdeiRecordId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws FileNotFoundException
     */
    List<OswDownload> listOswFiles(Principal principal,
                                   String servletPath,
                                   Optional<Integer> confidenceLevel,
                                   Optional<Double[]> bbox,
                                   Optional<String> oswSchemaVersion,
                                   Optional<String> dateTime,
                                   Optional<String> tdeiOrgId,
                                   Optional<String> tdeiRecordId,
                                   Integer pageNo,
                                   Integer pageSize) throws FileNotFoundException;

    /**
     * Returns the list of flex versions
     *
     * @param principal
     * @return
     */
    VersionList listOswVersions(Principal principal, HttpServletRequest req) throws MalformedURLException;
}
