package com.outsera.teste.Teste.mapper;

import com.outsera.teste.Teste.dto.FileUploadHistoryDTO;
import com.outsera.teste.Teste.model.FileUploadHistory;
import org.springframework.stereotype.Component;

@Component
public class FileUploadHistoryMapper {

    public FileUploadHistoryDTO toDTO(FileUploadHistory fileUploadHistory) {
        return new FileUploadHistoryDTO(
                fileUploadHistory.getFileName(),
                fileUploadHistory.getUploadTime(),
                fileUploadHistory.isSuccessful(),
                fileUploadHistory.getErrorMessage()
        );
    }
}
