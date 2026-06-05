package com.example.demo.controller;

import com.example.demo.models.Dto.ConteudoProjetoRequestDTO;
import com.example.demo.models.Dto.ConteudoProjetoResponseDTO;
import com.example.demo.models.Dto.ConteudoReorderRequestDTO;
import com.example.demo.models.Enums.TipoConteudo;
import com.example.demo.service.ConteudoProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos/{projetoId}/conteudos")
public class ConteudoProjetoController {

    private final ConteudoProjetoService conteudoService;

    public ConteudoProjetoController(ConteudoProjetoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @Operation(summary = "Criar conteudo do projeto")
    @PostMapping
    public ResponseEntity<ConteudoProjetoResponseDTO> criar(
            @PathVariable String projetoId,
            @Valid @RequestBody ConteudoProjetoRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conteudoService.criar(projetoId, dto));
    }

    @Operation(summary = "Listar conteudos")
    @GetMapping
    public ResponseEntity<List<ConteudoProjetoResponseDTO>> listar(
            @PathVariable String projetoId,
            @RequestParam(required = false) TipoConteudo tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(conteudoService.listarPorTipo(projetoId, tipo));
        }
        return ResponseEntity.ok(conteudoService.listarPorProjeto(projetoId));
    }

    @Operation(summary = "Buscar conteudo by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ConteudoProjetoResponseDTO> buscarPorId(
            @PathVariable String projetoId,
            @PathVariable String id) {
        return ResponseEntity.ok(conteudoService.buscarPorId(projetoId, id));
    }

    @Operation(summary = "Att conteudo do projeto")
    @PutMapping("/{id}")
    public ResponseEntity<ConteudoProjetoResponseDTO> atualizar(
            @PathVariable String projetoId,
            @PathVariable String id,
            @Valid @RequestBody ConteudoProjetoRequestDTO dto) {
        return ResponseEntity.ok(conteudoService.atualizar(projetoId, id, dto));
    }

    @Operation(summary = "Reordenar conteudos")
    @PutMapping("/reordenar")
    public ResponseEntity<Void> reordenar(
            @PathVariable String projetoId,
            @Valid @RequestBody ConteudoReorderRequestDTO dto) {
        conteudoService.reordenar(projetoId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletar um conteudo do projeto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String projetoId,
            @PathVariable String id) {
        conteudoService.deletar(projetoId, id);
        return ResponseEntity.noContent().build();
    }
}