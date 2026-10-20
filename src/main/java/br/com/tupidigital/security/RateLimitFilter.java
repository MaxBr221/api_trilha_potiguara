package br.com.tupidigital.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Limite ampliado para 2500 requisições por minuto por IP (permite que 20+ usuários no mesmo Wi-Fi de apresentação usem simultaneamente sem bloqueio)
        Bandwidth limit = Bandwidth.builder()
                .capacity(2500)
                .refillGreedy(2500, Duration.ofMinutes(1))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket resolveBucket(HttpServletRequest request) {
        String ip = getClientIpAddress(request);
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0].trim();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Nunca limitar preflight CORS (OPTIONS) para evitar falhas silenciosas no navegador
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket = resolveBucket(request);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Too Many Requests\",\"message\":\"Muitas requisições simultâneas. Aguarde um instante.\"}");
        }
    }
}
