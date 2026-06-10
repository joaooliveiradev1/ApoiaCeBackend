package com.example.demo.controller;

import com.example.demo.models.Dto.EnqueteResponseDTO;
import com.example.demo.models.Dto.OpcaoEnqueteRequestDTO;
import com.example.demo.service.OpcaoEnqueteService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enquetes")
@RequiredArgsConstructor
@Validated
public class OpcaoEnqueteController {

    private final OpcaoEnqueteService opcaoEnqueteService;

    @PostMapping("/{enqueteId}/opcoes")
    public ResponseEntity<EnqueteResponseDTO> adicionarOpcao(
            @PathVariable @NotBlank String enqueteId,
            @Valid @RequestBody OpcaoEnqueteRequestDTO dto
    ) {
        EnqueteResponseDTO response = opcaoEnqueteService.adicionarOpcao(enqueteId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}