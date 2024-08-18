package com.outsera.teste.Teste.service;

import com.outsera.teste.Teste.dto.FileUploadHistoryDTO;
import com.outsera.teste.Teste.mapper.FileUploadHistoryMapper;
import com.outsera.teste.Teste.model.FileUploadHistory;
import com.outsera.teste.Teste.repository.FileUploadHistoryRepository;
import com.outsera.teste.Teste.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploadHistoryService {

    final private FileUploadHistoryRepository fileUploadHistoryRepository;
    final private FileUploadHistoryMapper fileUploadHistoryMapper;

    public FileUploadHistoryService(FileUploadHistoryMapper fileUploadHistoryMapper,
                                    FileUploadHistoryRepository fileUploadHistoryRepository){
        this.fileUploadHistoryRepository = fileUploadHistoryRepository;
        this.fileUploadHistoryMapper = fileUploadHistoryMapper;
    }

    public void saveFileUploadHistory(String fileName, boolean successful, String errorMessage) {
        FileUploadHistory history = new FileUploadHistory();
        history.setFileName(fileName);
        history.setUploadTime(LocalDateTime.now());
        history.setSuccessful(successful);
        history.setErrorMessage(errorMessage);

        fileUploadHistoryRepository.save(history);
    }

    public List<FileUploadHistoryDTO> getAllFileUploads() {
        return fileUploadHistoryRepository.findAll()
                .stream()
                .map(fileUploadHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
