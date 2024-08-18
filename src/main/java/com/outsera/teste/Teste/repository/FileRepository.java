package com.outsera.teste.Teste.repository;

import com.outsera.teste.Teste.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    File findByFileName(String fileName);
}
