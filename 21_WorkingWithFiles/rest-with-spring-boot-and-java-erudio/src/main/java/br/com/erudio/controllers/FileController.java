package br.com.erudio.controllers;

import br.com.erudio.controllers.docs.FileControllerDocs;
import br.com.erudio.data.dto.UploadFileResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileController implements FileControllerDocs {

    @Override
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @Override
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return List.of();
    }

    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        return null;
    }
}