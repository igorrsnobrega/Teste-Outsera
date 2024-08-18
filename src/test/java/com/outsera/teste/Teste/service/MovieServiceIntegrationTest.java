package com.outsera.teste.Teste.service;

import com.outsera.teste.Teste.dto.FileDTO;
import com.outsera.teste.Teste.dto.IntervalDTO;
import com.outsera.teste.Teste.dto.ProducerIntervalDTO;
import com.outsera.teste.Teste.exception.InvalidFileFormatException;
import com.outsera.teste.Teste.model.File;
import com.outsera.teste.Teste.model.Movie;
import com.outsera.teste.Teste.repository.FileRepository;
import com.outsera.teste.Teste.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieServiceIntegrationTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    public void setup(){
        movieRepository.deleteAll();
        fileRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testGetProducerWithInterval() {
        Movie movie1 = new Movie(1980, "Can't Stop the Music", "Associated Film Distribution", "A. Kitman Ho", true);
        Movie movie2 = new Movie(1981, "Mommie Dearest", "Paramount Pictures", "A. Kitman Ho", true);
        Movie movie3 = new Movie(1985, "Movie 3", "Studio 3", "Julius R. Nasso", true);
        Movie movie4 = new Movie(1990, "Movie 4", "Studio 4", "Steven Seagal", true);
        Movie movie5 = new Movie(1995, "Movie 5", "Studio 5", "A. Kitman Ho", true);

        movieRepository.saveAll(List.of(movie1, movie2, movie3, movie4, movie5));

        ProducerIntervalDTO result = movieService.getProducerWithInterval(1970, 2000);

        assertNotNull(result);

        List<IntervalDTO> minIntervals = result.getMin();
        assertEquals(1, minIntervals.size());
        assertEquals("A. Kitman Ho", minIntervals.get(0).getProducer());
        assertEquals(1, minIntervals.get(0).getInterval());
        assertEquals(1980, minIntervals.get(0).getPreviousWin());
        assertEquals(1981, minIntervals.get(0).getFollowingWin());

        List<IntervalDTO> maxIntervals = result.getMax();
        assertEquals(1, maxIntervals.size());
        assertEquals("A. Kitman Ho", maxIntervals.get(0).getProducer());
        assertEquals(14, maxIntervals.get(0).getInterval());
        assertEquals(1981, maxIntervals.get(0).getPreviousWin());
        assertEquals(1995, maxIntervals.get(0).getFollowingWin());
    }

    @Test
    public void testProcessCSVFile_validFile_shouldSaveMovies() throws Exception {

        String content = "year;title;studios;producers;winner\n" +
                "1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes;\n" +
                "1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub;";

        String base64Content = Base64.getEncoder().encodeToString(content.getBytes());

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName("movies.csv");
        fileDTO.setFileContent(base64Content);

        movieService.processCSVFile(fileDTO);

        List<Movie> movies = movieRepository.findAll();
        assertEquals(2, movies.size());

        File history = fileRepository.findByFileName("movies.csv");
        assertNotNull(history);
        assertTrue(history.isSuccessful());
    }

    @Test
    public void testProcessCSVFile_invalidFile_shouldThrowException() {
        String content = "1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes;\n" +
                "Invalid Line";

        String base64Content = Base64.getEncoder().encodeToString(content.getBytes());

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName("movies.csv");
        fileDTO.setFileContent(base64Content);

        InvalidFileFormatException exception = assertThrows(InvalidFileFormatException.class, () -> {
            movieService.processCSVFile(fileDTO);
        });

        assertEquals("Invalid number of fields in line: Invalid Line", exception.getMessage());

        List<Movie> movies = movieRepository.findAll();
        assertEquals(0, movies.size());

        File history = fileRepository.findByFileName("movies.csv");
        assertNotNull(history);
        assertFalse(history.isSuccessful());
    }
}

