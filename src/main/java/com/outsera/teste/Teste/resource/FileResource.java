package com.outsera.teste.Teste.resource;

import com.outsera.teste.Teste.dto.FileUploadHistoryDTO;
import com.outsera.teste.Teste.service.FileUploadHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileResource {

    final private FileUploadHistoryService fileUploadHistoryService;

    public FileResource(FileUploadHistoryService fileUploadHistoryService){
        this.fileUploadHistoryService = fileUploadHistoryService;
    }

    @GetMapping()
    public ResponseEntity<List<FileUploadHistoryDTO>> getAllFileUploads() {
        List<FileUploadHistoryDTO> uploads = fileUploadHistoryService.getAllFileUploads();
        return ResponseEntity.ok(uploads);
    }
}
