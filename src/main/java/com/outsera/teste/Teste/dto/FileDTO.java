package com.outsera.teste.Teste.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    @NotBlank(message = "The file name cannot be empty.")
    private String fileName;

    @NotBlank(message = "The file content cannot be empty.")
    private String fileContent;
    private LocalDateTime uploadTime;
    private boolean successful;
    private String errorMessage;
}
