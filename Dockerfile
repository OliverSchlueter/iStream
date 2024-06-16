FROM openjdk:21
LABEL authors="oliver"
COPY build/libs/iStream*.jar /app/iStream.jar
EXPOSE 7457
CMD ["java", "-jar", "/app/iStream.jar"]