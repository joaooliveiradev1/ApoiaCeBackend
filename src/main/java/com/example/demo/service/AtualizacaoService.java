package com.example.demo.service;

import com.example.demo.models.Dto.AtualizacaoRequestDTO;
import com.example.demo.models.Dto.AtualizacaoResponseDTO;
import com.example.demo.models.Entity.Atualizacao;
import com.example.demo.models.Entity.Projeto;
import com.example.demo.repository.AtualizacaoRepository;
import com.example.demo.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AtualizacaoService {

    private final AtualizacaoRepository atualizacaoRepository;
    private final ProjetoRepository projetoRepository;

    public AtualizacaoService(AtualizacaoRepository atualizacaoRepository,
                              ProjetoRepository projetoRepository) {
        this.atualizacaoRepository = atualizacaoRepository;
        this.projetoRepository = projetoRepository;
    }

    public AtualizacaoResponseDTO criar(String projetoId, AtualizacaoRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

        if (projeto.isEncerrado()) {
            throw new IllegalStateException("Não é possível criar atualizações em um projeto encerrado");
        }

        Atualizacao atualizacao = new Atualizacao();
        atualizacao.setProjeto(projeto);
        atualizacao.setTitulo(dto.getTitulo());
        atualizacao.setConteudoPublico(dto.getConteudoPublico());
        atualizacao.setConteudoExclusivo(dto.getConteudoExclusivo());
        atualizacao.setExclusiva(dto.isExclusiva());
        atualizacao.setPublicadaEm(
                dto.getPublicadaEm() != null ? dto.getPublicadaEm() : LocalDateTime.now()
        );

        return AtualizacaoResponseDTO.from(atualizacaoRepository.save(atualizacao));
    }

    @Transactional(readOnly = true)
    public List<AtualizacaoResponseDTO> listarPorProjeto(String projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Projeto não encontrado");
        }

        return atualizacaoRepository
                .findByProjetoIdOrderByPublicadaEmDesc(projetoId)
                .stream()
                .map(AtualizacaoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AtualizacaoResponseDTO buscarPorId(String projetoId, String id) {
        return atualizacaoRepository.findByIdAndProjetoId(id, projetoId)
                .map(AtualizacaoResponseDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Atualização não encontrada"));
    }

    public AtualizacaoResponseDTO atualizar(String projetoId, String id, AtualizacaoRequestDTO dto) {
        Atualizacao atualizacao = atualizacaoRepository.findByIdAndProjetoId(id, projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Atualização não encontrada"));

        atualizacao.setTitulo(dto.getTitulo());
        atualizacao.setConteudoPublico(dto.getConteudoPublico());
        atualizacao.setConteudoExclusivo(dto.getConteudoExclusivo());
        atualizacao.setExclusiva(dto.isExclusiva());

        if (dto.getPublicadaEm() != null) {
            atualizacao.setPublicadaEm(dto.getPublicadaEm());
        }

        return AtualizacaoResponseDTO.from(atualizacaoRepository.save(atualizacao));
    }

    public void deletar(String projetoId, String id) {
        Atualizacao atualizacao = atualizacaoRepository.findByIdAndProjetoId(id, projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Atualização não encontrada"));

        atualizacao.softDelete();
        atualizacaoRepository.save(atualizacao);
    }
}