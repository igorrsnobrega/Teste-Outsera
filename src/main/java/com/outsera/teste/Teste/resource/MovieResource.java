package com.outsera.teste.Teste.resource;

import com.outsera.teste.Teste.dto.FileDTO;
import com.outsera.teste.Teste.dto.MovieDTO;
import com.outsera.teste.Teste.dto.ProducerIntervalDTO;
import com.outsera.teste.Teste.dto.ResponseDTO;
import com.outsera.teste.Teste.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/movies")
public class MovieResource {

    final private MovieService movieService;

    public MovieResource(MovieService movieService){
        this.movieService = movieService;
    }

    @PostMapping("/upload")
    public @ResponseBody ResponseEntity<ResponseDTO> uploadCSVBase64(@Valid @RequestBody FileDTO fileDTO) {
        movieService.processCSVFile(fileDTO);
        ResponseDTO response = new ResponseDTO("File has been processed successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping()
    public @ResponseBody ResponseEntity<List<MovieDTO>> getAll(){
        return new ResponseEntity<>(movieService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public @ResponseBody ResponseEntity<ResponseDTO> getCount(){
        ResponseDTO response = new ResponseDTO(String.valueOf(movieService.getAll().size()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/producers-interval")
    public @ResponseBody ProducerIntervalDTO getProducersInterval(
            @RequestParam(value = "inicio") Integer inicio,
            @RequestParam(value = "fim", required = false) Integer fim
    ) {

        if (Objects.isNull(fim)) {
            fim = Year.now().getValue();
        }

        return movieService.getProducerWithInterval(inicio, fim);
    }
}
