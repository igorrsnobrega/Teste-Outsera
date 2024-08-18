package com.outsera.teste.Teste.service;

import com.outsera.teste.Teste.dto.FileUploadHistoryDTO;
import com.outsera.teste.Teste.dto.IntervalDTO;
import com.outsera.teste.Teste.dto.ProducerIntervalDTO;
import com.outsera.teste.Teste.exception.FileProcessingException;
import com.outsera.teste.Teste.exception.InvalidFileFormatException;
import com.outsera.teste.Teste.mapper.FileUploadHistoryMapper;
import com.outsera.teste.Teste.model.FileUploadHistory;
import com.outsera.teste.Teste.model.Movie;
import com.outsera.teste.Teste.repository.FileUploadHistoryRepository;
import com.outsera.teste.Teste.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MovieService {

    final private MovieRepository movieRepository;
    final private FileUploadHistoryService fileUploadHistoryService;

    public MovieService(
            MovieRepository movieRepository,
            FileUploadHistoryService fileUploadHistoryService){
        this.movieRepository = movieRepository;
        this.fileUploadHistoryService = fileUploadHistoryService;
    }

    public void processCSVFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            fileUploadHistoryService.saveFileUploadHistory(fileName, false, "The file is not a CSV.");
            throw new InvalidFileFormatException("The file is not a CSV.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length < 4) {
                    fileUploadHistoryService.saveFileUploadHistory(fileName, false, "Invalid number of fields in line: " + line);
                    throw new InvalidFileFormatException("Invalid number of fields in line: " + line);
                }
                try {
                    Movie movie = new Movie();
                    movie.setMovieYear(Integer.parseInt(fields[0].trim()));
                    movie.setTitle(fields[1].trim());
                    movie.setStudios(fields[2].trim());
                    movie.setProducers(fields[3].trim());
                    movie.setWinner(fields.length == 5 && "yes".equalsIgnoreCase(fields[4].trim()));

                    movieRepository.save(movie);
                } catch (NumberFormatException e) {
                    fileUploadHistoryService.saveFileUploadHistory(fileName, false, "Invalid year format in line: " + line);
                    throw new InvalidFileFormatException("Invalid year format in line: " + line);
                } catch (Exception e) {
                    fileUploadHistoryService.saveFileUploadHistory(fileName, false, "Error processing line: " + line);
                    throw new FileProcessingException("Error processing line: " + line, e);
                }
            }
            fileUploadHistoryService.saveFileUploadHistory(fileName, true, null);
        } catch (IOException e) {
            fileUploadHistoryService.saveFileUploadHistory(fileName, false, "Error reading file");
            throw new FileProcessingException("Error reading file", e);
        }
    }

    public ProducerIntervalDTO getProducerWithInterval(int inicio, int fim) {

        List<String> producers = movieRepository.findDistinctProducers();
        List<IntervalDTO> minIntervals = new ArrayList<>();
        List<IntervalDTO> maxIntervals = new ArrayList<>();

        int smallestInterval = Integer.MAX_VALUE;
        int largestInterval = Integer.MIN_VALUE;

        for (String producer : producers) {
            List<Movie> movies = movieRepository.findByProducersAndWinnerTrueOrderByMovieYearAsc(producer);

            for (int i = 0; i < movies.size() - 1; i++) {
                int year = movies.get(i).getMovieYear();
                if (year >= inicio && year <= fim) {
                    int interval = movies.get(i + 1).getMovieYear() - year;

                    if (interval < smallestInterval) {
                        smallestInterval = interval;
                        minIntervals.clear();
                        minIntervals.add(new IntervalDTO(producer, interval, year, movies.get(i + 1).getMovieYear()));
                    } else if (interval == smallestInterval) {
                        minIntervals.add(new IntervalDTO(producer, interval, year, movies.get(i + 1).getMovieYear()));
                    }

                    if (interval > largestInterval) {
                        largestInterval = interval;
                        maxIntervals.clear();
                        maxIntervals.add(new IntervalDTO(producer, interval, year, movies.get(i + 1).getMovieYear()));
                    } else if (interval == largestInterval) {
                        maxIntervals.add(new IntervalDTO(producer, interval, year, movies.get(i + 1).getMovieYear()));
                    }
                }
            }
        }

        return new ProducerIntervalDTO(minIntervals, maxIntervals);
    }

}
