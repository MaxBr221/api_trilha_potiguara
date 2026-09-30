package br.com.tupidigital.service;

import br.com.tupidigital.dto.DashboardResponseDTO;
import br.com.tupidigital.entity.Usuario;
import br.com.tupidigital.repository.ProgressoUsuarioLicaoRepository;
import br.com.tupidigital.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import br.com.tupidigital.repository.ConquistaRepository;
import br.com.tupidigital.repository.UsuarioConquistaRepository;
import br.com.tupidigital.repository.ProgressoUsuarioExercicioRepository;
import br.com.tupidigital.dto.DashboardConquistaDTO;
import br.com.tupidigital.entity.UsuarioConquista;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProgressoUsuarioLicaoRepository progressoUsuarioLicaoRepository;
    
    @Autowired
    private ProgressoUsuarioExercicioRepository progressoUsuarioExercicioRepository;

    @Autowired
    private ConquistaRepository conquistaRepository;

    @Autowired
    private UsuarioConquistaRepository usuarioConquistaRepository;

    @Autowired
    private br.com.tupidigital.repository.NotificacaoRepository notificacaoRepository;

    public DashboardResponseDTO obterDashboard() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = usuarioRepository.findByEmail(email);
        
        if (userDetails == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario usuario = (Usuario) userDetails;
        
        long licoesConcluidas = progressoUsuarioLicaoRepository.countByUsuarioId(usuario.getId());
        
        long totalExercicios = progressoUsuarioExercicioRepository.countByUsuarioId(usuario.getId());
        long totalAcertos = progressoUsuarioExercicioRepository.countByUsuarioIdAndAcertouTrue(usuario.getId());
        
        int taxaAcerto = 0;
        if (totalExercicios > 0) {
            taxaAcerto = (int) ((totalAcertos * 100) / totalExercicios);
        }
        
        // Fetch conquistas
        Map<java.util.UUID, UsuarioConquista> conquistasDesbloqueadas = usuarioConquistaRepository.findByUsuarioId(usuario.getId())
                .stream()
                .collect(Collectors.toMap(uc -> uc.getConquista().getId(), uc -> uc));
                
        List<DashboardConquistaDTO> conquistasDTO = conquistaRepository.findAll().stream()
                .map(c -> {
                    UsuarioConquista uc = conquistasDesbloqueadas.get(c.getId());
                    boolean desbloqueada = uc != null;
                    int progresso = desbloqueada ? 100 : 0;
                    
                    if (!desbloqueada) {
                        if (c.getMetaXp() != null && c.getMetaXp() > 0) {
                            progresso = Math.min(100, (usuario.getXp() * 100) / c.getMetaXp());
                        } else if (c.getMetaLicoes() != null && c.getMetaLicoes() > 0) {
                            progresso = Math.min(100, (int) ((licoesConcluidas * 100) / c.getMetaLicoes()));
                        } else if (c.getMetaOfensiva() != null && c.getMetaOfensiva() > 0) {
                            progresso = Math.min(100, (usuario.getSequenciaAtual() * 100) / c.getMetaOfensiva());
                        }
                    }
                    
                    return new DashboardConquistaDTO(
                        c.getId(),
                        c.getTitulo(),
                        c.getDescricao(),
                        c.getIcone(),
                        c.getCorBase(),
                        desbloqueada,
                        progresso,
                        desbloqueada ? uc.getDataObtencao() : null
                    );
                })
                .collect(Collectors.toList());
        
        int sequenciaReal = usuario.getSequenciaAtual();
        java.time.LocalDate hoje = java.time.LocalDate.now(java.time.ZoneId.of("America/Sao_Paulo"));
        java.time.LocalDate ultimaAtividade = usuario.getUltimaAtividade();

        // Se o usuário não acessou ontem nem hoje, a ofensiva já foi perdida
        if (ultimaAtividade != null && ultimaAtividade.isBefore(hoje.minusDays(1))) {
            sequenciaReal = 0;
            if (usuario.getSequenciaAtual() != 0) {
                usuario.setSequenciaAtual(0);
                usuarioRepository.save(usuario);
            }
        }
        
        return new DashboardResponseDTO(
                usuario.getXp(),
                sequenciaReal,
                (int) licoesConcluidas,
                taxaAcerto,
                conquistasDTO
        );
    }
    public void atualizarPerfil(br.com.tupidigital.dto.UsuarioUpdateDTO data) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        if (data.nome() != null && !data.nome().isBlank()) {
            usuario.setNome(data.nome());
        }
        
        if (data.fotoPerfil() != null) {
            if (data.fotoPerfil().trim().isEmpty()) {
                usuario.setFotoPerfil(null);
            } else {
                usuario.setFotoPerfil(data.fotoPerfil());
            }
        }
        
        usuarioRepository.save(usuario);
    }
    
    public br.com.tupidigital.dto.UsuarioPerfilDTO obterPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        return new br.com.tupidigital.dto.UsuarioPerfilDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getXp(),
                usuario.getSequenciaAtual(),
                usuario.getPerfil().name(),
                usuario.getFotoPerfil()
        );
    }

    public void adicionarAmigo(java.util.UUID amigoId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario amigo = usuarioRepository.findById(amigoId).orElseThrow(() -> new RuntimeException("Amigo não encontrado"));
        
        if (usuario.getId().equals(amigoId)) {
            throw new RuntimeException("Você não pode adicionar a si mesmo como amigo");
        }
        
        boolean jaEraAmigo = usuario.getAmigos().contains(amigo);
        
        if (!jaEraAmigo) {
            usuario.adicionarAmigo(amigo);
            usuarioRepository.save(usuario);
            
            br.com.tupidigital.entity.Notificacao notificacao = new br.com.tupidigital.entity.Notificacao();
            notificacao.setUsuario(amigo);
            notificacao.setRemetente(usuario);
            notificacao.setTipo(br.com.tupidigital.entity.TipoNotificacao.NOVO_SEGUIDOR);
            notificacao.setMensagem("começou a seguir você");
            notificacaoRepository.save(notificacao);
        }
    }

    public void removerAmigo(java.util.UUID amigoId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario amigo = usuarioRepository.findById(amigoId).orElseThrow(() -> new RuntimeException("Amigo não encontrado"));
        
        usuario.removerAmigo(amigo);
        usuarioRepository.save(usuario);
    }

    public java.util.List<br.com.tupidigital.dto.AmigoDTO> listarAmigos() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        return usuario.getAmigos().stream()
                .map(amigo -> new br.com.tupidigital.dto.AmigoDTO(
                        amigo.getId(),
                        amigo.getNome(),
                        amigo.getXp(),
                        amigo.getSequenciaAtual(),
                        amigo.getFotoPerfil()
                ))
                .sorted(java.util.Comparator.comparing(br.com.tupidigital.dto.AmigoDTO::xp).reversed())
                .collect(Collectors.toList());
    }

    public br.com.tupidigital.dto.UsuarioPerfilPublicoDTO obterPerfilPublico(java.util.UUID amigoId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioLogado = (Usuario) usuarioRepository.findByEmail(email);
        
        Usuario amigo = usuarioRepository.findById(amigoId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        boolean isAmigo = usuarioLogado != null && usuarioLogado.getAmigos().contains(amigo);
        int totalAmigos = amigo.getAmigos().size();
        
        return new br.com.tupidigital.dto.UsuarioPerfilPublicoDTO(
                amigo.getId(),
                amigo.getNome(),
                amigo.getXp(),
                amigo.getSequenciaAtual(),
                amigo.getFotoPerfil(),
                totalAmigos,
                isAmigo
        );
    }
    public java.util.List<br.com.tupidigital.dto.UsuarioBuscaDTO> buscarUsuarios(String nome) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioLogado = (Usuario) usuarioRepository.findByEmail(email);
        
        java.util.List<Usuario> resultados;
        
        if (nome == null || nome.trim().isEmpty()) {
            resultados = usuarioRepository.findTop20ByIdNotOrderByXpDesc(usuarioLogado.getId());
        } else {
            resultados = usuarioRepository.buscarPorNomeOuEmail(
                nome.trim(), 
                usuarioLogado.getId(), 
                org.springframework.data.domain.PageRequest.of(0, 20)
            );
        }
        
        return resultados.stream()
                .map(u -> new br.com.tupidigital.dto.UsuarioBuscaDTO(
                        u.getId(),
                        u.getNome(),
                        u.getXp(),
                        u.getFotoPerfil()
                ))
                .collect(Collectors.toList());
    }

    public java.util.List<br.com.tupidigital.dto.NotificacaoDTO> listarNotificacoes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        return notificacaoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .map(n -> new br.com.tupidigital.dto.NotificacaoDTO(
                        n.getId(),
                        new br.com.tupidigital.dto.RemetenteDTO(n.getRemetente().getNome(), n.getRemetente().getFotoPerfil()),
                        n.getTipo().name(),
                        n.getMensagem(),
                        n.isLida(),
                        n.getCriadoEm()
                ))
                .collect(Collectors.toList());
    }

    public void marcarNotificacaoLida(java.util.UUID notificacaoId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        br.com.tupidigital.entity.Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
                
        if (!notificacao.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }
}
