package br.com.erudio.controllers.docs;

import br.com.erudio.data.dto.UploadFileResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

    UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file);
    List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files);
    ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request);
}