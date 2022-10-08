FROM openjdk:11 as builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootjar


FROM openjdk:11
COPY --from=builder build/libs/*.jar app.jar
HEALTHCHECK --interval=10s --timeout=3s CMD curl -L -s GET 'http://localhost:50004/ping' || PONG
EXPOSE 50004
ENTRYPOINT ["java","-Dspring.profiles.active=product","-jar","/app.jar"]