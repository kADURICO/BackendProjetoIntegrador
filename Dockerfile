FROM maven:3.9.9-amazoncorretto-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine
COPY --from=build target/*.jar conectIdade.jar
EXPOSE 8080

CMD ["java", "-jar", "/conectIdade.jar"]