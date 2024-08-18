package com.outsera.teste.Teste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private int movieYear;
    private String title;
    private String studios;
    private String producers;
    private boolean winner;
}
