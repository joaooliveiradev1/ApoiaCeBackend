package com.example.demo.controller;

import com.example.demo.models.Dto.ProjetoRequestDTO;
import com.example.demo.models.Dto.ProjetoResponseDTO;
import com.example.demo.models.Dto.ProjetoUpdateDTO;
import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.models.Enums.TipoAssinatura;
import com.example.demo.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjetoResponseDTO> criar(
            @Valid @RequestBody ProjetoRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String emailUser = userDetails.getUsername();
        ProjetoResponseDTO response = projetoService.criar(dto, emailUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<Page<ProjetoResponseDTO>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) StatusProjeto status,
            @RequestParam(required = false) String categoriaId,
            @RequestParam(required = false) TipoAssinatura tipoAssinatura,
            @PageableDefault(size = 12, sort = "criadoEm") Pageable pageable
    ) {
        return ResponseEntity.ok(
                projetoService.listar(status, categoriaId, tipoAssinatura, titulo, pageable)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(projetoService.buscarPorId(id));
    }


    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorSlug(@PathVariable String slug) {
        return ResponseEntity.ok(projetoService.buscarPorSlug(slug));
    }

    @GetMapping("/criador/{criadorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ProjetoResponseDTO>> listarPorCriador(
            @PathVariable String criadorId,
            @RequestParam(required = false) StatusProjeto status,
            @PageableDefault(size = 12, sort = "criadoEm") Pageable pageable
    ) {
        return ResponseEntity.ok(
                projetoService.listarPorCriador(criadorId, status, pageable)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjetoResponseDTO> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ProjetoUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(
                projetoService.atualizar(id, dto, userDetails.getUsername(), isAdmin)
        );
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjetoResponseDTO> atualizarStatus(
            @PathVariable String id,
            @RequestParam StatusProjeto status,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(
                projetoService.atualizarStatus(id, status, userDetails.getUsername(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletar(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        projetoService.deletar(id, userDetails.getUsername(), isAdmin);
        return ResponseEntity.noContent().build();
    }
}