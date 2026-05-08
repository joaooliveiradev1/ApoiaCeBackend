package com.example.demo.models.Dto;

import com.example.demo.models.Entity.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class CategoriaResponseDTO {

    private String id;
    private String nome;
    private String cor;
    private OffsetDateTime criadoEm;

    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setCor(categoria.getCor());
        dto.setCriadoEm(categoria.getCriadoEm());
        return dto;
    }
}