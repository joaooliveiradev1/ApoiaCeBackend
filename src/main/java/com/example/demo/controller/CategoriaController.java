package com.example.demo.controller;

import com.example.demo.models.Dto.CategoriaRequestDTO;
import com.example.demo.models.Dto.CategoriaResponseDTO;
import com.example.demo.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Listar categorias")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar(
            @RequestParam(required = false) String nome) {

        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(categoriaService.buscarPorNome(nome));
        }
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @Operation(summary = "Buscar categoria by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable String id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @Operation(summary = "Criar categoria")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(
            @Valid @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.criar(request));
    }

    @Operation(summary = "Att categoria")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable String id,
            @Valid @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.ok(categoriaService.atualizar(id, request));
    }

    @Operation(summary = "Deletar categoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}