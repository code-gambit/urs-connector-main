FROM openjdk:15

COPY build/libs/*.jar ./
CMD java -jar *.jar
