FROM maven:3.6.3-jdk-11 AS appbuild
COPY . .
RUN mvn clean install

FROM openjdk:11 AS appserver
COPY --from=appbuild target/votingapp.jar .
ENV PORT=4040
ENTRYPOINT ["java", "-jar", "votingapp.jar"]
