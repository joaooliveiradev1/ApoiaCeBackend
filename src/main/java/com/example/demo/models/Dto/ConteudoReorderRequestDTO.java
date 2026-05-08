package com.example.demo.models.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ConteudoReorderRequestDTO {


    // caso seja necessario reordenas os conteudos no front

    @NotEmpty(message = "Lista de IDs é obrigatória")
    private List<String> ids; // IDs na nova ordem desejada
}