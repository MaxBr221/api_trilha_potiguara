package br.com.tupidigital.service;

import br.com.tupidigital.dto.ExercicioResponseDTO;
import br.com.tupidigital.dto.ValidacaoRespostaRequestDTO;
import br.com.tupidigital.dto.ValidacaoRespostaResponseDTO;
import br.com.tupidigital.entity.Exercicio;
import br.com.tupidigital.entity.Usuario;
import br.com.tupidigital.repository.ExercicioRepository;
import br.com.tupidigital.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.tupidigital.repository.ProgressoUsuarioExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExercicioService {

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProgressoUsuarioExercicioRepository progressoUsuarioExercicioRepository;

    public List<ExercicioResponseDTO> listarExerciciosPorLicao(UUID licaoId) {
        return exercicioRepository.findByLicaoIdOrderByOrdemIndexAsc(licaoId).stream()
                .map(ExercicioResponseDTO::new)
                .toList();
    }

    public ValidacaoRespostaResponseDTO validarResposta(UUID exercicioId, ValidacaoRespostaRequestDTO request) {
        Exercicio exercicio = exercicioRepository.findById(exercicioId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        String respostaUsuario = request.respostaUsuario() != null ? request.respostaUsuario().trim() : "";
        String respostaCorreta = exercicio.getRespostaCorreta().trim();

        // Comparação simples (ignorando case)
        boolean correta = respostaUsuario.equalsIgnoreCase(respostaCorreta);

        Integer xpGanho = correta ? exercicio.getPontuacaoXp() : 0;

        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String email = auth.getName();
            org.springframework.security.core.userdetails.UserDetails userDetails = usuarioRepository.findByEmail(email);
            
            if (userDetails != null) {
                Usuario usuario = (Usuario) userDetails;

                if (correta) {
                    usuario.setXp(usuario.getXp() + xpGanho);
                    usuarioRepository.save(usuario);
                }

                // Salva o histórico de progresso do exercício
                br.com.tupidigital.entity.ProgressoUsuarioExercicio progressoExercicio = new br.com.tupidigital.entity.ProgressoUsuarioExercicio();
                progressoExercicio.setUsuario(usuario);
                progressoExercicio.setExercicio(exercicio);
                progressoExercicio.setAcertou(correta);
                progressoExercicio.setCriadoEm(java.time.LocalDateTime.now());
                
                progressoUsuarioExercicioRepository.save(progressoExercicio);
            }
        }
        
        return new ValidacaoRespostaResponseDTO(correta, xpGanho, exercicio.getRespostaCorreta());
    }
}
