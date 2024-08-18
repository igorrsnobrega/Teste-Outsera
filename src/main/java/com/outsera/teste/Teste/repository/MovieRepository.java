package com.outsera.teste.Teste.repository;

import com.outsera.teste.Teste.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByProducersAndWinnerTrueOrderByMovieYearAsc(String produtor);

    @Query("SELECT DISTINCT m.producers FROM Movie m")
    List<String> findDistinctProducers();

}
