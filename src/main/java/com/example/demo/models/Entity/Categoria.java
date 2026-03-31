package com.example.demo.models.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Cor deve ser um hex válido ex: #3B82F6")
    @Column(name = "cor", length = 20)
    private String cor;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        var now = OffsetDateTime.now();
        if (criadoEm == null) criadoEm = now;
        if (atualizadoEm == null) atualizadoEm = now;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}