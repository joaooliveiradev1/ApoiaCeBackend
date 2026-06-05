package com.example.demo.service;

import com.example.demo.models.Dto.EnqueteResponseDTO;
import com.example.demo.models.Dto.OpcaoEnqueteResponseDTO;
import com.example.demo.models.Dto.VotoRequestDTO;
import com.example.demo.models.Entity.Enquete;
import com.example.demo.models.Entity.OpcaoEnquete;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.models.Entity.Voto;
import com.example.demo.repository.EnqueteRepository;
import com.example.demo.repository.OpcaoEnqueteRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final EnqueteRepository enqueteRepository;
    private final OpcaoEnqueteRepository opcaoEnqueteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public EnqueteResponseDTO votar(String enqueteId, VotoRequestDTO dto, String emailUsuario) {
        Enquete enquete = buscarEnqueteAtiva(enqueteId);
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        if (votoRepository.existsByEnqueteIdAndUsuarioId(enqueteId, usuario.getId())) {
            throw new IllegalStateException("Usuário já votou nesta enquete");
        }

        OpcaoEnquete opcao = opcaoEnqueteRepository
                .findByIdAndEnqueteId(dto.getOpcaoId(), enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Opção não encontrada para esta enquete"));

        Voto voto = new Voto();
        voto.setEnquete(enquete);
        voto.setOpcao(opcao);
        voto.setUsuario(usuario);

        opcao.incrementarVoto();

        votoRepository.save(voto);
        opcaoEnqueteRepository.save(opcao);

        return toResponseDTO(recarregarEnquete(enqueteId));
    }

    @Transactional
    public EnqueteResponseDTO trocarVoto(String enqueteId, VotoRequestDTO dto, String emailUsuario) {
        Enquete enquete = buscarEnqueteAtiva(enqueteId);
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        Voto votoExistente = votoRepository.findByEnqueteIdAndUsuarioId(enqueteId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Voto não encontrado para este usuário nesta enquete"));

        OpcaoEnquete opcaoNova = opcaoEnqueteRepository
                .findByIdAndEnqueteId(dto.getOpcaoId(), enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Opção não encontrada para esta enquete"));

        OpcaoEnquete opcaoAnterior = votoExistente.getOpcao();

        if (opcaoAnterior.getId().equals(opcaoNova.getId())) {
            throw new IllegalStateException("O usuário já votou nesta opção");
        }

        opcaoAnterior.decrementarVoto();
        opcaoNova.incrementarVoto();

        votoExistente.setOpcao(opcaoNova);

        opcaoEnqueteRepository.save(opcaoAnterior);
        opcaoEnqueteRepository.save(opcaoNova);
        votoRepository.save(votoExistente);

        return toResponseDTO(recarregarEnquete(enqueteId));
    }

    @Transactional
    public EnqueteResponseDTO removerVoto(String enqueteId, String emailUsuario) {
        Enquete enquete = buscarEnqueteAtiva(enqueteId);
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        Voto votoExistente = votoRepository.findByEnqueteIdAndUsuarioId(enqueteId, usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Voto não encontrado para este usuário nesta enquete"));

        OpcaoEnquete opcao = votoExistente.getOpcao();
        opcao.decrementarVoto();

        votoExistente.softDelete();

        opcaoEnqueteRepository.save(opcao);
        votoRepository.save(votoExistente);

        return toResponseDTO(recarregarEnquete(enqueteId));
    }

    private Enquete buscarEnqueteAtiva(String enqueteId) {
        Enquete enquete = enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete não encontrada"));

        if (enquete.isEncerrada()) {
            throw new IllegalStateException("A enquete está encerrada");
        }

        return enquete;
    }

    private Usuario buscarUsuarioPorEmail(String emailUsuario) {
        return usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private Enquete recarregarEnquete(String enqueteId) {
        return enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new EntityNotFoundException("Enquete não encontrada"));
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