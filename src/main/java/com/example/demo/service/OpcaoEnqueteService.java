package com.example.demo.service;

import com.example.demo.models.Dto.EnqueteResponseDTO;
import com.example.demo.models.Dto.OpcaoEnqueteRequestDTO;
import com.example.demo.models.Dto.OpcaoEnqueteResponseDTO;
import com.example.demo.models.Entity.Enquete;
import com.example.demo.models.Entity.OpcaoEnquete;
import com.example.demo.repository.EnqueteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpcaoEnqueteService {

    private final EnqueteRepository enqueteRepository;

    @Transactional
    public EnqueteResponseDTO adicionarOpcao(String enqueteId, OpcaoEnqueteRequestDTO dto) {
        Enquete enquete = enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete não encontrada"));

        if (enquete.isEncerrada()) {
            throw new IllegalStateException("Não é possível adicionar opções em uma enquete encerrada");
        }

        boolean possuiVotos = enquete.getOpcoes().stream()
                .anyMatch(opcao -> opcao.getQtdVotos() > 0);

        if (possuiVotos) {
            throw new IllegalStateException("Não é possível adicionar opções em uma enquete que já recebeu votos");
        }

        OpcaoEnquete novaOpcao = new OpcaoEnquete();
        novaOpcao.setTitulo(dto.getTexto());
        novaOpcao.setQtdVotos(0);
        novaOpcao.setEnquete(enquete);

        enquete.getOpcoes().add(novaOpcao);

        Enquete enqueteAtualizada = enqueteRepository.save(enquete);

        return toResponseDTO(enqueteAtualizada);
    }

    private EnqueteResponseDTO toResponseDTO(Enquete enquete) {
        EnqueteResponseDTO dto = new EnqueteResponseDTO();
        dto.setId(enquete.getId());
        dto.setTitulo(enquete.getTitulo());
        dto.setDescricao(enquete.getDescricao());
        dto.setProjetoId(enquete.getProjeto().getId());
        dto.setDataCriacao(enquete.getDataCriacao());

        int totalVotos = enquete.getOpcoes().stream()
                .mapToInt(OpcaoEnquete::getQtdVotos)
                .sum();

        List<OpcaoEnqueteResponseDTO> opcoesDto = enquete.getOpcoes().stream()
                .map(opcao -> {
                    OpcaoEnqueteResponseDTO opcaoDto = new OpcaoEnqueteResponseDTO();
                    opcaoDto.setId(opcao.getId());
                    opcaoDto.setTexto(opcao.getTitulo());
                    opcaoDto.setQtdVotos(opcao.getQtdVotos());

                    double percentual = totalVotos == 0
                            ? 0.0
                            : (opcao.getQtdVotos() * 100.0) / totalVotos;

                    opcaoDto.setPercentual(percentual);
                    return opcaoDto;
                })
                .toList();

        dto.setOpcoes(opcoesDto);
        return dto;
    }
}