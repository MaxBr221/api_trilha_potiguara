# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Primeiro, copiamos o pom.xml e baixamos as dependências para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Depois, copiamos o código-fonte
COPY src ./src

# Compilamos a aplicação gerando o arquivo JAR
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos o JAR do build stage
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para iniciar a aplicação com limite de memória otimizado para o Render
ENTRYPOINT ["java", "-Xmx300m", "-Xms300m", "-XX:+UseG1GC", "-jar", "app.jar"]
