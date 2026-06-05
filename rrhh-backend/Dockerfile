# Stage 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build
LABEL maintainer="Enterprise Architecture Team"
WORKDIR /app

RUN apk add --no-cache maven

# Copiar el pom y descargar dependencias (aprovechando el caché de capas de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y construir el artefacto
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Crear un usuario no privilegiado por seguridad (Estándar de Producción 2026)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar solo el JAR resultante de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Configuración de memoria para contenedores y puerto
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
