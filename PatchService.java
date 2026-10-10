import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PatchService {
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/java/br/com/tupidigital/service/ExercicioService.java");
        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        
        // Add HtmlUtils import if not present
        if (!content.contains("import org.springframework.web.util.HtmlUtils;")) {
            content = content.replace("import org.springframework.stereotype.Service;", "import org.springframework.stereotype.Service;\nimport org.springframework.web.util.HtmlUtils;");
        }
        
        // Replace the validation logic
        String target = "String respostaUsuario = request.respostaUsuario() != null ? request.respostaUsuario().replaceAll(\"<[^>]*>\", \"\").trim() : \"\";\n" +
                        "        String respostaCorreta = exercicio.getRespostaCorreta().trim();\n" +
                        "        boolean correta = respostaUsuario.equalsIgnoreCase(respostaCorreta);";
                        
        String replacement = "String respostaUsuario = request.respostaUsuario() != null ? request.respostaUsuario().replaceAll(\"<[^>]*>\", \"\").trim() : \"\";\n" +
                             "        respostaUsuario = HtmlUtils.htmlUnescape(respostaUsuario);\n" +
                             "        String respostaCorreta = exercicio.getRespostaCorreta().trim();\n" +
                             "        \n" +
                             "        // Normalizar strings para evitar falhas por aspas simples HTML (&#39;) ou aspas tipográficas\n" +
                             "        String rUserNorm = respostaUsuario.replace(\"'\", \"\").replace(\"’\", \"\").replace(\"&#39;\", \"\").replace(\"&apos;\", \"\");\n" +
                             "        String rCorrNorm = respostaCorreta.replace(\"'\", \"\").replace(\"’\", \"\").replace(\"&#39;\", \"\").replace(\"&apos;\", \"\");\n" +
                             "        \n" +
                             "        boolean correta = rUserNorm.equalsIgnoreCase(rCorrNorm) || respostaUsuario.equalsIgnoreCase(respostaCorreta);";
                             
        content = content.replace(target, replacement);
        Files.writeString(file.toPath(), content, StandardCharsets.UTF_8);
        System.out.println("Patched ExercicioService.java");
    }
}
