package com.example.demo.models.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpcaoEnqueteResponseDTO {

    private String id;
    private String texto;
    private Integer qtdVotos;
    private Double percentual;
}