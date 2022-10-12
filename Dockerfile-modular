FROM openjdk:17-jdk-slim-buster as builder
WORKDIR application 
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-jre-slim
WORKDIR application
COPY --from=builder application/dependencies/ ./
RUN true
COPY --from=builder application/spring-boot-loader/ ./
RUN true
COPY --from=builder application/snapshot-dependencies/ ./
RUN true
COPY --from=builder application/application/ ./
RUN true
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]