package com.outsera.teste.Teste.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "MOVIE")
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int movieYear;
    private String title;
    private String studios;
    private String producers;
    private boolean winner;

    public Movie(int movieYear, String title, String studios, String producers, boolean winner) {
        this.movieYear = movieYear;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
    }
}
