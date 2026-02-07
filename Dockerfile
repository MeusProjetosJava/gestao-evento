# 1️⃣ Imagem base com Java 21 (LTS)
FROM eclipse-temurin:21-jdk-alpine

# 2️⃣ Diretório de trabalho dentro do container
WORKDIR /app

# 3️⃣ Copia o JAR gerado pelo Maven
COPY target/gestao-evento-0.0.1-SNAPSHOT.jar app.jar

# 4️⃣ Porta usada pela aplicação
EXPOSE 8080

# 5️⃣ Comando para iniciar o Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
