# Estágio 1: Build (Altere a tag final de 17 para 21)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Copia apenas o arquivo de configuração de dependências primeiro (otimiza o cache do Docker)
COPY pom.xml .
# Baixa as dependências do projeto
RUN mvn dependency:go-offline

# Copia o código-fonte de fato
COPY src ./src

# Executa o build buildando o .jar limpo
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Altere de 17 para 21)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]