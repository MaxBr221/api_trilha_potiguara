package br.com.tupidigital.controller;

import br.com.tupidigital.dto.DashboardResponseDTO;
import br.com.tupidigital.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me/dashboard")
    public ResponseEntity<DashboardResponseDTO> obterDashboard() {
        return ResponseEntity.ok(usuarioService.obterDashboard());
    }

    @org.springframework.web.bind.annotation.GetMapping("/me")
    public ResponseEntity<br.com.tupidigital.dto.UsuarioPerfilDTO> obterPerfil() {
        return ResponseEntity.ok(usuarioService.obterPerfil());
    }

    @org.springframework.web.bind.annotation.PutMapping("/me")
    public ResponseEntity<Void> atualizarPerfil(@org.springframework.web.bind.annotation.RequestBody @Valid br.com.tupidigital.dto.UsuarioUpdateDTO data) {
        usuarioService.atualizarPerfil(data);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.PostMapping("/amigos/{id}")
    public ResponseEntity<Void> adicionarAmigo(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        usuarioService.adicionarAmigo(id);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/amigos/{id}")
    public ResponseEntity<Void> removerAmigo(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        usuarioService.removerAmigo(id);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.GetMapping("/amigos")
    public ResponseEntity<java.util.List<br.com.tupidigital.dto.AmigoDTO>> listarAmigos() {
        return ResponseEntity.ok(usuarioService.listarAmigos());
    }

    @org.springframework.web.bind.annotation.GetMapping("/perfil/{id}")
    public ResponseEntity<br.com.tupidigital.dto.UsuarioPerfilPublicoDTO> obterPerfilPublico(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        return ResponseEntity.ok(usuarioService.obterPerfilPublico(id));
    }

    @org.springframework.web.bind.annotation.GetMapping("/busca")
    public ResponseEntity<java.util.List<br.com.tupidigital.dto.UsuarioBuscaDTO>> buscarUsuarios(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String nome) {
        return ResponseEntity.ok(usuarioService.buscarUsuarios(nome));
    }

    @org.springframework.web.bind.annotation.GetMapping("/notificacoes")
    public ResponseEntity<java.util.List<br.com.tupidigital.dto.NotificacaoDTO>> listarNotificacoes() {
        return ResponseEntity.ok(usuarioService.listarNotificacoes());
    }

    @org.springframework.web.bind.annotation.PutMapping("/notificacoes/{id}/lida")
    public ResponseEntity<Void> marcarNotificacaoLida(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        usuarioService.marcarNotificacaoLida(id);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/notificacoes/{id}")
    public ResponseEntity<Void> removerNotificacao(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        usuarioService.removerNotificacao(id);
        return ResponseEntity.ok().build();
    }
}
