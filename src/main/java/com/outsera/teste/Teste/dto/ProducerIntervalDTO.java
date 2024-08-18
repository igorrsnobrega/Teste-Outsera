package com.outsera.teste.Teste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProducerIntervalDTO {

    private List<IntervalDTO> min;
    private List<IntervalDTO> max;
}
