package com.outsera.teste.Teste.service;

import com.outsera.teste.Teste.dto.IntervalDTO;
import com.outsera.teste.Teste.dto.MovieDTO;
import com.outsera.teste.Teste.dto.ProducerIntervalDTO;
import com.outsera.teste.Teste.exception.FileProcessingException;
import com.outsera.teste.Teste.exception.InvalidFileFormatException;
import com.outsera.teste.Teste.mapper.MovieMapper;
import com.outsera.teste.Teste.model.Movie;
import com.outsera.teste.Teste.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    final private MovieRepository movieRepository;
    final private FileUploadHistoryService fileUploadHistoryService;
    final private MovieMapper movieMapper;

    public MovieService(
            MovieRepository movieRepository,
            FileUploadHistoryService fileUploadHistoryService,
            MovieMapper movieMapper){
        this.movieRepository = movieRepository;
        this.fileUploadHistoryService = fileUploadHistoryService;
        this.movieMapper = movieMapper;
    }

    public void processCSVFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            saveAndThrow(fileName, "The file is not a CSV.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); // escapa a primeira linha

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length < 4) {
                    saveAndThrow(fileName, "Invalid number of fields in line: " + line);
                }

                try {
                    Movie movie = parseMovie(fields);
                    movieRepository.save(movie);
                } catch (NumberFormatException e) {
                    saveAndThrow(fileName, "Invalid year format in line: " + line);
                } catch (Exception e) {
                    saveAndThrow(fileName, "Error processing line: " + line, e);
                }
            }
            fileUploadHistoryService.saveFileUploadHistory(fileName, true, null);
        } catch (IOException e) {
            saveAndThrow(fileName, "Error reading file", e);
        }
    }

    private Movie parseMovie(String[] fields) {
        Movie movie = new Movie();
        movie.setMovieYear(Integer.parseInt(fields[0].trim()));
        movie.setTitle(fields[1].trim());
        movie.setStudios(fields[2].trim());
        movie.setProducers(fields[3].trim());
        movie.setWinner(fields.length == 5 && "yes".equalsIgnoreCase(fields[4].trim()));
        return movie;
    }

    private void saveAndThrow(String fileName, String message) {
        fileUploadHistoryService.saveFileUploadHistory(fileName, false, message);
        throw new InvalidFileFormatException(message);
    }

    private void saveAndThrow(String fileName, String message, Exception e) {
        fileUploadHistoryService.saveFileUploadHistory(fileName, false, message);
        throw new FileProcessingException(message, e);
    }

    public ProducerIntervalDTO getProducerWithInterval(int inicio, int fim) {
        List<String> producers = movieRepository.findDistinctProducers();
        Map<String, List<Movie>> producerMoviesMap = new HashMap<>();

        for (String producer : producers) {
            String[] individualProducers = producer.split(",| and ");
            for (String individualProducer : individualProducers) {
                individualProducer = individualProducer.trim();
                if (!producerMoviesMap.containsKey(individualProducer)) {
                    producerMoviesMap.put(individualProducer, new ArrayList<>());
                }
                producerMoviesMap.get(individualProducer).addAll(movieRepository.findByProducersAndWinnerTrueOrderByMovieYearAsc(producer));
            }
        }

        List<IntervalDTO> minIntervals = new ArrayList<>();
        List<IntervalDTO> maxIntervals = new ArrayList<>();

        int smallestInterval = Integer.MAX_VALUE;
        int largestInterval = Integer.MIN_VALUE;

        for (Map.Entry<String, List<Movie>> entry : producerMoviesMap.entrySet()) {
            String producer = entry.getKey();
            List<Movie> movies = entry.getValue();
            movies.sort(Comparator.comparingInt(Movie::getMovieYear));

            for (int i = 0; i < movies.size() - 1; i++) {
                int year1 = movies.get(i).getMovieYear();
                int year2 = movies.get(i + 1).getMovieYear();

                if (year1 >= inicio && year1 <= fim && year2 >= inicio && year2 <= fim) {
                    int interval = year2 - year1;

                    if (interval < smallestInterval) {
                        smallestInterval = interval;
                        minIntervals.clear();
                        minIntervals.add(new IntervalDTO(producer, interval, year1, year2));
                    } else if (interval == smallestInterval) {
                        minIntervals.add(new IntervalDTO(producer, interval, year1, year2));
                    }

                    if (interval > largestInterval) {
                        largestInterval = interval;
                        maxIntervals.clear();
                        maxIntervals.add(new IntervalDTO(producer, interval, year1, year2));
                    } else if (interval == largestInterval) {
                        maxIntervals.add(new IntervalDTO(producer, interval, year1, year2));
                    }
                }
            }
        }

        return new ProducerIntervalDTO(minIntervals, maxIntervals);
    }

    public List<MovieDTO> getAll(){
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDTO)
                .collect(Collectors.toList());
    }
}
