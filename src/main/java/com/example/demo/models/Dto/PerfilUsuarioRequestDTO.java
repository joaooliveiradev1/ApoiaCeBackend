package com.example.demo.models.Dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PerfilUsuarioRequestDTO {

    @Size(max = 1000, message = "Bio deve ter no máximo 1000 caracteres")
    private String bio;

    @Size(max = 120, message = "Chave PIX deve ter no máximo 120 caracteres")
    private String pixChave;

    private String contaBancaria;

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPixChave() { return pixChave; }
    public void setPixChave(String pixChave) { this.pixChave = pixChave; }

    public String getContaBancaria() { return contaBancaria; }
    public void setContaBancaria(String contaBancaria) { this.contaBancaria = contaBancaria; }
}