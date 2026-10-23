package br.com.tupidigital.controller;

import br.com.tupidigital.dto.DicaExercicioResponseDTO;
import br.com.tupidigital.dto.ExercicioResponseDTO;
import br.com.tupidigital.dto.ValidacaoAudioRequestDTO;
import br.com.tupidigital.dto.ValidacaoAudioResponseDTO;
import br.com.tupidigital.dto.ValidacaoRespostaRequestDTO;
import br.com.tupidigital.dto.ValidacaoRespostaResponseDTO;
import br.com.tupidigital.service.ExercicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
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

    @PostMapping(value = "/exercicios/{id}/validar-audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ValidacaoAudioResponseDTO> validarAudioMultipart(
            @PathVariable UUID id,
            @RequestParam("audio") MultipartFile audio) {
        try {
            byte[] bytes = audio.getBytes();
            String mimeType = audio.getContentType();
            return ResponseEntity.ok(exercicioService.validarAudio(id, bytes, mimeType));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar arquivo de áudio: " + e.getMessage());
        }
    }

    @PostMapping(value = "/exercicios/{id}/validar-audio", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidacaoAudioResponseDTO> validarAudioJson(
            @PathVariable UUID id,
            @RequestBody @Valid ValidacaoAudioRequestDTO request) {
        try {
            String base64Clean = request.audioBase64();
            String mimeType = request.mimeType();
            if (base64Clean != null && base64Clean.contains(",")) {
                String[] parts = base64Clean.split(",");
                if ((mimeType == null || mimeType.isBlank()) && parts[0].contains(":") && parts[0].contains(";")) {
                    mimeType = parts[0].substring(parts[0].indexOf(":") + 1, parts[0].indexOf(";"));
                }
                base64Clean = parts[1];
            }
            byte[] bytes = (base64Clean != null && !base64Clean.isBlank()) ? Base64.getDecoder().decode(base64Clean) : new byte[0];
            return ResponseEntity.ok(exercicioService.validarAudio(id, bytes, mimeType));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao decodificar áudio Base64: " + e.getMessage());
        }
    }

    @GetMapping("/exercicios/{id}/dica")
    public ResponseEntity<DicaExercicioResponseDTO> obterDica(@PathVariable UUID id) {
        return ResponseEntity.ok(exercicioService.obterDica(id));
    }
}

