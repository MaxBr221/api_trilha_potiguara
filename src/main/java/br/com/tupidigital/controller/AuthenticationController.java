package br.com.tupidigital.controller;

import br.com.tupidigital.dto.LoginRequestDTO;
import br.com.tupidigital.dto.RegisterRequestDTO;
import br.com.tupidigital.dto.TokenResponseDTO;
import br.com.tupidigital.entity.Perfil;
import br.com.tupidigital.entity.Usuario;
import br.com.tupidigital.repository.UsuarioRepository;
import br.com.tupidigital.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @org.springframework.web.bind.annotation.GetMapping("/hash")
    public String getHash(@org.springframework.web.bind.annotation.RequestParam String pwd) {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(pwd);
    }


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private br.com.tupidigital.repository.TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;

    @Autowired
    private br.com.tupidigital.service.EmailService emailService;

    @Autowired
    private br.com.tupidigital.repository.LicaoRepository licaoRepository;

    @Autowired
    private br.com.tupidigital.repository.ProgressoUsuarioLicaoRepository progressoUsuarioLicaoRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        Usuario authUser = (Usuario) auth.getPrincipal();

        java.time.LocalDate hoje = java.time.LocalDate.now(java.time.ZoneId.of("America/Sao_Paulo"));
        java.time.LocalDate ultimaAtividade = authUser.getUltimaAtividade();

        if (ultimaAtividade != null && ultimaAtividade.isBefore(hoje.minusDays(1))) {
            if (authUser.getSequenciaAtual() != 0) {
                authUser.setSequenciaAtual(0);
                repository.save(authUser);
            }
        }

        var token = tokenService.generateToken(authUser);

        TokenResponseDTO.UsuarioSessaoDTO sessaoDTO = new TokenResponseDTO.UsuarioSessaoDTO(
                authUser.getId(),
                authUser.getNome(),
                authUser.getEmail(),
                authUser.getXp(),
                authUser.getSequenciaAtual(),
                authUser.getPerfil().name(),
                authUser.getFotoPerfil()
        );

        return ResponseEntity.ok(new TokenResponseDTO(token, sessaoDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDTO data) {
        if (this.repository.existsByEmail(data.email())) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());
        Perfil perfil = data.perfil() != null ? data.perfil() : Perfil.USER;

        Usuario newUsuario = new Usuario();
        newUsuario.setNome(data.nome());
        newUsuario.setEmail(data.email());
        newUsuario.setSenha(encryptedPassword);
        newUsuario.setPerfil(perfil);
        newUsuario.setXp(data.xpInicial() != null ? data.xpInicial() : 0);
        newUsuario.setSequenciaAtual(data.licaoConcluidaId() != null ? 1 : 0);
        if (data.licaoConcluidaId() != null) {
            newUsuario.setUltimaAtividade(java.time.LocalDate.now(java.time.ZoneId.of("America/Sao_Paulo")));
        }

        this.repository.save(newUsuario);

        if (data.licaoConcluidaId() != null) {
            java.util.Optional<br.com.tupidigital.entity.Licao> licao = licaoRepository.findById(data.licaoConcluidaId());
            if (licao.isPresent()) {
                br.com.tupidigital.entity.ProgressoUsuarioLicao progresso = new br.com.tupidigital.entity.ProgressoUsuarioLicao();
                progresso.setUsuario(newUsuario);
                progresso.setLicao(licao.get());
                progressoUsuarioLicaoRepository.save(progresso);
            }
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> esqueciSenha(@RequestBody br.com.tupidigital.dto.EsqueciSenhaDTO data) {
        Usuario usuario = (Usuario) repository.findByEmail(data.email());
        if (usuario != null) {
            String token = java.util.UUID.randomUUID().toString();
            
            br.com.tupidigital.entity.TokenRecuperacaoSenha tokenEntity = new br.com.tupidigital.entity.TokenRecuperacaoSenha();
            tokenEntity.setToken(token);
            tokenEntity.setUsuario(usuario);
            tokenEntity.setDataExpiracao(java.time.LocalDateTime.now().plusHours(2));
            
            tokenRecuperacaoSenhaRepository.save(tokenEntity);
            
            emailService.enviarEmail(data.email(), "Recuperação de Senha - Tupi Digital", "Você solicitou a recuperação de senha.\n\nCopie o código a seguir e cole no aplicativo para redefinir sua senha:\n\n" + token);
        }
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody br.com.tupidigital.dto.RedefinirSenhaDTO data) {
        java.util.Optional<br.com.tupidigital.entity.TokenRecuperacaoSenha> optionalToken = tokenRecuperacaoSenhaRepository.findByToken(data.token());
        
        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        br.com.tupidigital.entity.TokenRecuperacaoSenha tokenEntity = optionalToken.get();
        
        if (tokenEntity.getDataExpiracao().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }
        
        Usuario usuario = tokenEntity.getUsuario();
        usuario.setSenha(passwordEncoder.encode(data.novaSenha()));
        repository.save(usuario);
        
        tokenRecuperacaoSenhaRepository.delete(tokenEntity);
        
        return ResponseEntity.ok().build();
    }
}
