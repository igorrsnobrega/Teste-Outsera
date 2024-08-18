package com.outsera.teste.Teste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntervalDTO {

    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;
}
