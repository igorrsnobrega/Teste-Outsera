package com.outsera.teste.Teste.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FILE_UPLOAD_HISTORY")
public class FileUploadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private LocalDateTime uploadTime;
    private boolean successful;
    private String errorMessage;
}
