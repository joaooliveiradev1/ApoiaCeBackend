package com.example.demo.service;

import com.example.demo.models.Dto.EnqueteRequestDTO;
import com.example.demo.models.Dto.EnqueteResponseDTO;
import com.example.demo.models.Dto.EnqueteUpdateRequestDTO;
import com.example.demo.models.Dto.OpcaoEnqueteRequestDTO;
import com.example.demo.models.Dto.OpcaoEnqueteResponseDTO;
import com.example.demo.models.Entity.Enquete;
import com.example.demo.models.Entity.OpcaoEnquete;
import com.example.demo.models.Entity.Projeto;
import com.example.demo.repository.EnqueteRepository;
import com.example.demo.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnqueteService {

    private final EnqueteRepository enqueteRepository;
    private final ProjetoRepository projetoRepository;

    @Transactional
    public EnqueteResponseDTO criar(String projetoId, EnqueteRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

        Enquete enquete = new Enquete();
        enquete.setProjeto(projeto);
        enquete.setTitulo(dto.getTitulo());
        enquete.setDescricao(dto.getDescricao());
        enquete.setAtiva(true);

        List<OpcaoEnquete> opcoes = new ArrayList<>();

        for (OpcaoEnqueteRequestDTO opcaoDto : dto.getOpcoes()) {
            OpcaoEnquete opcao = new OpcaoEnquete();
            opcao.setTitulo(opcaoDto.getTexto());
            opcao.setQtdVotos(0);
            opcao.setEnquete(enquete);
            opcoes.add(opcao);
        }

        enquete.setOpcoes(opcoes);

        Enquete enqueteSalva = enqueteRepository.save(enquete);
        return toResponseDTO(enqueteSalva);
    }

    @Transactional
    public List<EnqueteResponseDTO> listarPorProjeto(String projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Projeto não encontrado");
        }

        return enqueteRepository.findByProjetoIdWithOpcoes(projetoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public EnqueteResponseDTO buscarPorId(String enqueteId) {
        Enquete enquete = enqueteRepository.findByIdWithOpcoes(enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete não encontrada"));

        return toResponseDTO(enquete);
    }

    @Transactional
    public EnqueteResponseDTO atualizar(String enqueteId, EnqueteUpdateRequestDTO dto) {
        Enquete enquete = enqueteRepository.findByIdWithOpcoes(enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete não encontrada"));

        if (dto.getTitulo() != null && !dto.getTitulo().isBlank()) {
            enquete.setTitulo(dto.getTitulo());
        }

        if (dto.getDescricao() != null) {
            enquete.setDescricao(dto.getDescricao());
        }

        Enquete enqueteAtualizada = enqueteRepository.save(enquete);
        return toResponseDTO(enqueteAtualizada);
    }

    @Transactional
    public void deletar(String enqueteId) {
        Enquete enquete = enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete não encontrada"));

        enquete.softDelete();
        enqueteRepository.save(enquete);
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