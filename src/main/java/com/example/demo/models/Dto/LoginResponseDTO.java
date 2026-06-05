package com.example.demo.models.Dto;

import com.example.demo.models.Enums.UsuarioRole;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponseDTO {

    private String token;
    private String usuarioId;
    private String nome;
    private String email;
    private UsuarioRole role;
}
