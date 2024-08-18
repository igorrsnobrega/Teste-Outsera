package com.outsera.teste.Teste.service;

import com.outsera.teste.Teste.dto.FileDTO;
import com.outsera.teste.Teste.mapper.FileMapper;
import com.outsera.teste.Teste.model.File;
import com.outsera.teste.Teste.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    final private FileRepository fileRepository;
    final private FileMapper fileMapper;

    public FileService(FileMapper fileMapper,
                       FileRepository fileRepository){
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    public void saveFileHistory(String fileName, String content, boolean successful, String errorMessage) {
        File history = new File();
        history.setFileName(fileName);
        history.setFileContent(content);
        history.setUploadTime(LocalDateTime.now());
        history.setSuccessful(successful);
        history.setErrorMessage(errorMessage);

        fileRepository.save(history);
    }

    public List<FileDTO> getAllFileUploads() {
        return fileRepository.findAll()
                .stream()
                .map(fileMapper::toDTO)
                .collect(Collectors.toList());
    }
}
