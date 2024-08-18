package com.outsera.teste.Teste.resource;

import com.outsera.teste.Teste.dto.FileDTO;
import com.outsera.teste.Teste.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileResource {

    final private FileService fileService;

    public FileResource(FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping()
    public ResponseEntity<List<FileDTO>> getAllFileUploads() {
        List<FileDTO> uploads = fileService.getAllFileUploads();
        return ResponseEntity.ok(uploads);
    }
}
