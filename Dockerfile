FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} url-shortner.jar
ENTRYPOINT ["java", "-jar", "url-shortner.jar"]