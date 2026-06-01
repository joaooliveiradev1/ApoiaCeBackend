package com.example.demo.controller;

import com.example.demo.models.Dto.EnqueteResponseDTO;
import com.example.demo.models.Dto.VotoRequestDTO;
import com.example.demo.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enquetes")
@RequiredArgsConstructor
@Validated
public class VotoController {

    private final VotoService votoService;

    @Operation(summary = "Votar numa enquete")
    @PostMapping("/{enqueteId}/voto")
    public ResponseEntity<EnqueteResponseDTO> votar(
            @PathVariable @NotBlank String enqueteId,
            @Valid @RequestBody VotoRequestDTO dto,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        EnqueteResponseDTO response = votoService.votar(enqueteId, dto, emailUsuario);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Trocar voto de uma enquete")
    @PatchMapping("/{enqueteId}/voto")
    public ResponseEntity<EnqueteResponseDTO> trocarVoto(
            @PathVariable @NotBlank String enqueteId,
            @Valid @RequestBody VotoRequestDTO dto,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        EnqueteResponseDTO response = votoService.trocarVoto(enqueteId, dto, emailUsuario);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar voto de enquete")
    @DeleteMapping("/{enqueteId}/voto")
    public ResponseEntity<EnqueteResponseDTO> removerVoto(
            @PathVariable @NotBlank String enqueteId,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        EnqueteResponseDTO response = votoService.removerVoto(enqueteId, emailUsuario);
        return ResponseEntity.ok(response);
    }
}