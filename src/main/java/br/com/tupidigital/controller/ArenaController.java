package br.com.tupidigital.controller;

import br.com.tupidigital.dto.ArenaDTOs.*;
import br.com.tupidigital.service.ArenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/arena")
public class ArenaController {

    @Autowired
    private ArenaService arenaService;

    @GetMapping("/modulos-disponiveis")
    public ResponseEntity<List<TrilhaArenaDTO>> listarModulosDisponiveis() {
        return ResponseEntity.ok(arenaService.listarModulosDisponiveis());
    }

    @PostMapping("/criar")
    public ResponseEntity<CriarArenaResponseDTO> criarSala(@RequestBody @Valid CriarArenaRequestDTO dto) {
        CriarArenaResponseDTO response = arenaService.criarSala(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/entrar")
    public ResponseEntity<EntrarArenaResponseDTO> entrarSala(@RequestBody @Valid EntrarArenaRequestDTO dto) {
        return ResponseEntity.ok(arenaService.entrarSala(dto));
    }

    @GetMapping("/{pin}/estado")
    public ResponseEntity<EstadoArenaResponseDTO> obterEstado(
            @PathVariable String pin,
            @RequestParam(required = false) UUID participanteId) {
        return ResponseEntity.ok(arenaService.obterEstado(pin, participanteId));
    }

    @PostMapping("/{pin}/avancar")
    public ResponseEntity<EstadoArenaResponseDTO> avancarEtapa(@PathVariable String pin) {
        return ResponseEntity.ok(arenaService.avancarEtapa(pin));
    }

    @PostMapping("/{pin}/responder")
    public ResponseEntity<ResponderArenaResponseDTO> responder(
            @PathVariable String pin,
            @RequestBody @Valid ResponderArenaRequestDTO dto) {
        return ResponseEntity.ok(arenaService.responder(pin, dto));
    }

    @PostMapping("/{pin}/encerrar")
    public ResponseEntity<EstadoArenaResponseDTO> encerrarSala(@PathVariable String pin) {
        return ResponseEntity.ok(arenaService.encerrarSala(pin));
    }
}

