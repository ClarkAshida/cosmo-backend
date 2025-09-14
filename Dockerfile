# Estágio 1: Build da Aplicação com Maven
# Usamos uma imagem que já vem com Maven e JDK 21, conforme seu pom.xml
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache de camadas do Docker.
# As dependências só serão baixadas novamente se o pom.xml mudar.
COPY pom.xml .

# Baixa todas as dependências do projeto
RUN mvn dependency:go-offline

# Copia todo o resto do código-fonte da sua aplicação
COPY src ./src

# Executa o build do projeto, gerando o arquivo .jar. Pulamos os testes para acelerar o build.
RUN mvn clean package -DskipTests

# Estágio 2: Criação da Imagem Final
# Usamos uma imagem JRE (Java Runtime Environment) que é muito menor que a JDK,
# pois só precisamos executar a aplicação, não compilá-la.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build para a imagem final
COPY --from=build /app/target/cosmo-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080, que é a porta que sua aplicação usa (definido no application.yml)
EXPOSE 8080

# Comando para executar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]