package br.com.tupidigital.controller;

import br.com.tupidigital.dto.ExercicioResponseDTO;
import br.com.tupidigital.dto.ValidacaoRespostaRequestDTO;
import br.com.tupidigital.dto.ValidacaoRespostaResponseDTO;
import br.com.tupidigital.service.ExercicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ExercicioController {

    @Autowired
    private ExercicioService exercicioService;

    @GetMapping("/licoes/{id}/exercicios")
    public ResponseEntity<List<ExercicioResponseDTO>> listarExerciciosPorLicao(@PathVariable UUID id) {
        return ResponseEntity.ok(exercicioService.listarExerciciosPorLicao(id));
    }

    @PostMapping("/exercicios/{id}/validar")
    public ResponseEntity<ValidacaoRespostaResponseDTO> validarResposta(
            @PathVariable UUID id,
            @RequestBody @Valid ValidacaoRespostaRequestDTO request) {
        return ResponseEntity.ok(exercicioService.validarResposta(id, request));
    }
}
