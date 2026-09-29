package br.com.tupidigital.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {

    @Bean
    public FlywayMigrationStrategy repairAndMigrateStrategy() {
        return flyway -> {
            // O repair() remove as migrations que falharam da tabela flyway_schema_history
            // e realinha os checksums. Como a V51 falhou e foi a última, ele vai apagá-la do histórico.
            flyway.repair();
            // Em seguida, roda as migrations pendentes (a nossa V51 corrigida vai rodar perfeitamente agora)
            flyway.migrate();
        };
    }
}
