FROM maven:3.9.4-eclipse-temurin-17-alpine as builder
WORKDIR .
COPY . .
RUN mvn -f /pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR .
COPY --from=builder /target/*.jar /*.jar
#VOLUME ${files_storage_directory}
ENTRYPOINT ["java", "-jar", "/*.jar"]
