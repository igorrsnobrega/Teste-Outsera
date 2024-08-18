package com.outsera.teste.Teste.repository;

import com.outsera.teste.Teste.model.FileUploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadHistoryRepository extends JpaRepository<FileUploadHistory, Long> {

    FileUploadHistory findByFileName(String fileName);
}
