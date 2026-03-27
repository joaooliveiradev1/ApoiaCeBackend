package com.example.demo.models.Dto;

import com.example.demo.models.Enums.UsuarioRole;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private UsuarioRole role;
    private OffsetDateTime criadoEm;
}
