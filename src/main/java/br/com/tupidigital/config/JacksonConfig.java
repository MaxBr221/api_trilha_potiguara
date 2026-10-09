package br.com.tupidigital.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Set;

@Configuration
public class JacksonConfig {

    // A policy that strictly removes all HTML tags. Adjust if you need to allow some tags.
    private static final PolicyFactory SANITIZER_POLICY = new HtmlPolicyBuilder().toFactory();

    // Campos Base64/URL que NÃO devem passar pelo sanitizador HTML (ele converte + em &#43; e = em &#61;,
    // corrompendo o Base64). Esses campos devem ser validados por regex estrita nos DTOs.
    private static final Set<String> RAW_FIELDS = Set.of("fotoPerfil", "imageUrl");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.deserializerByType(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                if (value != null) {
                    String trimmed = value.trim();
                    if (RAW_FIELDS.contains(p.currentName())) {
                        return trimmed;
                    }
                    // Remove leading/trailing whitespaces and sanitize HTML to prevent XSS
                    return SANITIZER_POLICY.sanitize(trimmed);
                }
                return null;
            }
        });
    }
}
