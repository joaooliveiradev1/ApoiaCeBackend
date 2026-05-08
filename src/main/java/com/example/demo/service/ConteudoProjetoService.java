package com.example.demo.service;

import com.example.demo.models.Dto.ConteudoProjetoRequestDTO;
import com.example.demo.models.Dto.ConteudoProjetoResponseDTO;
import com.example.demo.models.Dto.ConteudoReorderRequestDTO;
import com.example.demo.models.Entity.ConteudoProjeto;
import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Enums.TipoConteudo;
import com.example.demo.repository.ConteudoProjetoRepository;
import com.example.demo.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class ConteudoProjetoService {

    private final ConteudoProjetoRepository conteudoRepository;
    private final ProjetoRepository projetoRepository;

    public ConteudoProjetoService(ConteudoProjetoRepository conteudoRepository,
                                  ProjetoRepository projetoRepository) {
        this.conteudoRepository = conteudoRepository;
        this.projetoRepository = projetoRepository;
    }

    public ConteudoProjetoResponseDTO criar(String projetoId, ConteudoProjetoRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

        int posicao;

        if (dto.getPosicao() == null) {
            // insere final
            posicao = conteudoRepository.findMaxPosicaoByProjetoId(projetoId) + 1;
        } else {
            // insere na posição específica, deslocando o resto
            posicao = dto.getPosicao();
            conteudoRepository.shiftPosicaoParaCima(projetoId, posicao);
        }

        ConteudoProjeto conteudo = new ConteudoProjeto();
        conteudo.setProjeto(projeto);
        conteudo.setTipo(dto.getTipo());
        conteudo.setConteudo(dto.getConteudo());
        conteudo.setPosicao(posicao);

        return ConteudoProjetoResponseDTO.from(conteudoRepository.save(conteudo));
    }

    @Transactional(readOnly = true)
    public List<ConteudoProjetoResponseDTO> listarPorProjeto(String projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Projeto não encontrado");
        }

        return conteudoRepository
                .findByProjetoIdOrderByPosicaoAsc(projetoId)
                .stream()
                .map(ConteudoProjetoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConteudoProjetoResponseDTO> listarPorTipo(String projetoId, TipoConteudo tipo) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Projeto não encontrado");
        }

        return conteudoRepository
                .findByProjetoIdAndTipoOrderByPosicaoAsc(projetoId, tipo)
                .stream()
                .map(ConteudoProjetoResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConteudoProjetoResponseDTO buscarPorId(String projetoId, String id) {
        return conteudoRepository.findByIdAndProjetoId(id, projetoId)
                .map(ConteudoProjetoResponseDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado"));
    }

    public ConteudoProjetoResponseDTO atualizar(String projetoId, String id,
                                                ConteudoProjetoRequestDTO dto) {
        ConteudoProjeto conteudo = conteudoRepository.findByIdAndProjetoId(id, projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado"));

        conteudo.setTipo(dto.getTipo());
        conteudo.setConteudo(dto.getConteudo());

        if (dto.getPosicao() != null) {
            conteudo.setPosicao(dto.getPosicao());
        }

        return ConteudoProjetoResponseDTO.from(conteudoRepository.save(conteudo));
    }

    public void reordenar(String projetoId, ConteudoReorderRequestDTO dto) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Projeto não encontrado");
        }

        List<ConteudoProjeto> conteudos = conteudoRepository
                .findByProjetoIdOrderByPosicaoAsc(projetoId);

        // valida que os IDs recebidos batem com os existentes

        List<String> idsExistentes = conteudos.stream().map(ConteudoProjeto::getId).toList();
        boolean valido = dto.getIds().size() == idsExistentes.size()
                && dto.getIds().containsAll(idsExistentes);

        if (!valido) {
            throw new IllegalArgumentException(
                    "A lista de IDs não corresponde aos conteúdos do projeto");
        }

        AtomicInteger posicao = new AtomicInteger(0);
        dto.getIds().forEach(itemId -> {
            conteudos.stream()
                    .filter(c -> c.getId().equals(itemId))
                    .findFirst()
                    .ifPresent(c -> c.setPosicao(posicao.getAndIncrement()));
        });

        conteudoRepository.saveAll(conteudos);
    }

    public void deletar(String projetoId, String id) {
        ConteudoProjeto conteudo = conteudoRepository.findByIdAndProjetoId(id, projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado"));

        conteudo.softDelete();
        conteudoRepository.save(conteudo);
    }
}