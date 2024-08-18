package com.outsera.teste.Teste.mapper;

import com.outsera.teste.Teste.dto.MovieDTO;
import com.outsera.teste.Teste.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDTO toDTO(Movie movie){
        return new MovieDTO(
          movie.getMovieYear(),
          movie.getTitle(),
          movie.getStudios(),
          movie.getProducers(),
          movie.isWinner()
        );
    }
}
