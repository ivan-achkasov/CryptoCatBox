FROM amazoncorretto:17.0.7
EXPOSE 8080
COPY build/libs/*.jar /app/main-api-service.jar
ENTRYPOINT ["sh", "-c", "java -jar \
 -Dspring.datasource.url=${DB_URL} \
 -Dspring.datasource.username=${DB_USER} \
 -Dspring.datasource.password=${DB_PASS} \
 /app/main-api-service.jar"]