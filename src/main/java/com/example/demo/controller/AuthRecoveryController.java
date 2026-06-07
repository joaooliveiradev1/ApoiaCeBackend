package com.example.demo.controller;

import com.example.demo.models.Dto.ForgotPasswordRequestDTO;
import com.example.demo.models.Dto.ResetPasswordRequestDTO;
import com.example.demo.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class AuthRecoveryController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private static class TokenData {
        String token;
        LocalDateTime expiracao;
        TokenData(String token, LocalDateTime expiracao) {
            this.token = token;
            this.expiracao = expiracao;
        }
    }

    private final Map<String, TokenData> tokenStorage = new ConcurrentHashMap<>();

    public AuthRecoveryController(UsuarioRepository usuarioRepository,
                                   PasswordEncoder passwordEncoder,
                                   JavaMailSender mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();

        var usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "E-mail nao cadastrado em nossa base."));
        }

        String token = String.format("%06d", new Random().nextInt(1000000));
        tokenStorage.put(email, new TokenData(token, LocalDateTime.now().plusMinutes(15)));

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@apoiace.com");
            message.setTo(email);
            message.setSubject("ApoiAce - Codigo de Recuperacao de Senha");
            message.setText("Ola!\n\nVoce solicitou a recuperacao de senha no ApoiAce.\n"
                    + "Use o codigo abaixo para criar uma nova senha:\n\n"
                    + token + "\n\n"
                    + "Este codigo e valido por 15 minutos.\n"
                    + "Se nao foi voce quem solicitou, ignore este e-mail.");
            mailSender.send(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erro ao enviar o e-mail de recuperacao."));
        }

        return ResponseEntity.ok(Map.of("message", "Codigo de verificacao enviado com sucesso."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();
        TokenData cached = tokenStorage.get(email);

        if (cached == null || !cached.token.equals(request.getToken())) {
            return ResponseEntity.status(400).body(Map.of("message", "Codigo de verificacao invalido."));
        }

        if (LocalDateTime.now().isAfter(cached.expiracao)) {
            tokenStorage.remove(email);
            return ResponseEntity.status(400).body(Map.of("message", "Este codigo ja expirou. Gere um novo."));
        }

        var usuario = usuarioRepository.findByEmail(email).orElseThrow();
        usuario.setSenhaHash(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuario);

        tokenStorage.remove(email);

        return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso!"));
    }
}