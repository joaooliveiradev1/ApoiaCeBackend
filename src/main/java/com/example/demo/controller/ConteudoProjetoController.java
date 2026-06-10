package com.example.demo.controller;

import com.example.demo.models.Dto.ConteudoProjetoRequestDTO;
import com.example.demo.models.Dto.ConteudoProjetoResponseDTO;
import com.example.demo.models.Dto.ConteudoReorderRequestDTO;
import com.example.demo.models.Enums.TipoConteudo;
import com.example.demo.service.ConteudoProjetoService;

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

    @PostMapping
    public ResponseEntity<ConteudoProjetoResponseDTO> criar(
            @PathVariable String projetoId,
            @Valid @RequestBody ConteudoProjetoRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conteudoService.criar(projetoId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ConteudoProjetoResponseDTO>> listar(
            @PathVariable String projetoId,
            @RequestParam(required = false) TipoConteudo tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(conteudoService.listarPorTipo(projetoId, tipo));
        }
        return ResponseEntity.ok(conteudoService.listarPorProjeto(projetoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConteudoProjetoResponseDTO> buscarPorId(
            @PathVariable String projetoId,
            @PathVariable String id) {
        return ResponseEntity.ok(conteudoService.buscarPorId(projetoId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConteudoProjetoResponseDTO> atualizar(
            @PathVariable String projetoId,
            @PathVariable String id,
            @Valid @RequestBody ConteudoProjetoRequestDTO dto) {
        return ResponseEntity.ok(conteudoService.atualizar(projetoId, id, dto));
    }

    @PutMapping("/reordenar")
    public ResponseEntity<Void> reordenar(
            @PathVariable String projetoId,
            @Valid @RequestBody ConteudoReorderRequestDTO dto) {
        conteudoService.reordenar(projetoId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String projetoId,
            @PathVariable String id) {
        conteudoService.deletar(projetoId, id);
        return ResponseEntity.noContent().build();
    }
}