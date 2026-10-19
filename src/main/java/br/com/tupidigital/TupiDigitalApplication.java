package br.com.tupidigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class TupiDigitalApplication {

    public static void main(String[] args) {
        carregarEnv();
        SpringApplication.run(TupiDigitalApplication.class, args);
    }

    private static void carregarEnv() {
        java.util.List<java.io.File> possiveisArquivos = java.util.List.of(
            new java.io.File(".env"),
            new java.io.File("trilha-potiguara/.env")
        );

        for (java.io.File envFile : possiveisArquivos) {
            if (envFile.exists() && envFile.isFile()) {
                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(envFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                            int idx = line.indexOf('=');
                            String key = line.substring(0, idx).trim();
                            String value = line.substring(idx + 1).trim();
                            if (System.getProperty(key) == null && System.getenv(key) == null) {
                                System.setProperty(key, value);
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
                break;
            }
        }
    }

}
