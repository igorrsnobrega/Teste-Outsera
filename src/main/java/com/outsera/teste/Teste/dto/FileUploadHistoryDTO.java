package com.outsera.teste.Teste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadHistoryDTO {

    private String fileName;
    private LocalDateTime uploadTime;
    private boolean successful;
    private String errorMessage;

}
