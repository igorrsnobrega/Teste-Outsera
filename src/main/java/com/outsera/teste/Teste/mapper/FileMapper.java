package com.outsera.teste.Teste.mapper;

import com.outsera.teste.Teste.dto.FileDTO;
import com.outsera.teste.Teste.model.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileDTO toDTO(File file) {
        return new FileDTO(
                file.getFileName(),
                file.getFileContent(),
                file.getUploadTime(),
                file.isSuccessful(),
                file.getErrorMessage()
        );
    }
}
