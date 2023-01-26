FROM eclipse-temurin:17.0.5_8-jre-alpine

COPY build/libs/*.jar /opt/app/application.jar

# cria um grupo e usuario spring, para não ter acesso root ao container por questoes de seguranca
RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

CMD java -jar /opt/app/application.jar