FROM eclipse-temurin:21

WORKDIR /app

COPY build/libs/*.jar app.jar

ARG VERSION=0.0.1
ARG JAR_NAME=server-cache-${VERSION}.jar

COPY build/libs/${JAR_NAME} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]