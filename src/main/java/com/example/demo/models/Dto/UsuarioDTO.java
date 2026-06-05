package com.example.demo.models.Dto;

import com.example.demo.models.Enums.UsuarioRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UsuarioDTO {

    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private LocalDate dataNascimento;
    private UsuarioRole role;
}
