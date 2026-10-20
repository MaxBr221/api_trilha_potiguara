package br.com.tupidigital.security;

import br.com.tupidigital.entity.Perfil;
import br.com.tupidigital.entity.Usuario;
import br.com.tupidigital.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oAuth2User.getAttribute("email");
        if (email != null) {
            email = email.trim().toLowerCase();
        }

        String name = oAuth2User.getAttribute("name");
        if (name == null || name.isBlank()) {
            name = oAuth2User.getAttribute("given_name");
        }
        if (name == null || name.isBlank()) {
            name = (email != null && email.contains("@")) ? email.split("@")[0] : "Usuário Google";
        }
        if (name.length() > 250) {
            name = name.substring(0, 250);
        }

        String picture = oAuth2User.getAttribute("picture");

        try {
            // Verifica se o email já existe
            Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
            
            if (usuario == null) {
                // Cria um novo usuário
                usuario = new Usuario();
                usuario.setNome(name);
                usuario.setEmail(email);
                usuario.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
                usuario.setPerfil(Perfil.USER);
                usuario.setXp(0);
                usuario.setSequenciaAtual(0);
                if (picture != null && !picture.isBlank()) {
                    usuario.setFotoPerfil(picture);
                }
                usuario = usuarioRepository.save(usuario);
                log.info("Novo usuário criado com sucesso via Google OAuth2: {}", email);
            } else if (usuario.getFotoPerfil() == null && picture != null && !picture.isBlank()) {
                usuario.setFotoPerfil(picture);
                usuarioRepository.save(usuario);
            }
            
            // Gera o JWT
            String token = tokenService.generateToken(usuario);
            
            // Redireciona para o frontend com o token
            String targetUrl = frontendUrl + "/oauth2-redirect?token=" + token;
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            log.error("Erro ao autenticar/salvar usuário via Google OAuth2: ", e);
            String targetUrl = frontendUrl + "/oauth2-redirect?error=auth_failed";
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
