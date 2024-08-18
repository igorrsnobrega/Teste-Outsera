package com.outsera.teste.Teste.resource;

import com.outsera.teste.Teste.dto.FileUploadHistoryDTO;
import com.outsera.teste.Teste.dto.ProducerIntervalDTO;
import com.outsera.teste.Teste.model.FileUploadHistory;
import com.outsera.teste.Teste.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Year;
import java.util.*;

@RestController
@RequestMapping("/api/movies")
public class MovieResource {

    final private MovieService movieService;

    public MovieResource(MovieService movieService){
        this.movieService = movieService;
    }

    @PostMapping("/upload")
    public @ResponseBody ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        movieService.processCSVFile(file);
        return new ResponseEntity<>("File has been processed successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/producers-interval")
    public ProducerIntervalDTO getProducersInterval(
            @RequestParam(value = "inicio") Integer inicio,
            @RequestParam(value = "fim", required = false) Integer fim
    ) {

        if (Objects.isNull(fim)) {
            fim = Year.now().getValue();
        }

        return movieService.getProducerWithInterval(inicio, fim);
    }
}
