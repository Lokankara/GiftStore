FROM maven:3.8.4-openjdk-17-slim

COPY zero/Module#04 /usr/src/app

WORKDIR /usr/src/app

RUN mvn clean install -DskipTests

EXPOSE 8080 80

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "./web-app/target/web-app-1.0.0.jar", "--thin.dryrun"]

HEALTHCHECK --interval=30s --timeout=10s CMD curl --fail http://localhost:8080/api/health || exit 1
