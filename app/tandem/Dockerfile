FROM openjdk:21-jdk-slim

LABEL founder="gnatykdm"

WORKDIR /app
COPY target/tandem-0.0.1-SNAPSHOT.jar /app/app.jar


COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh
EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app/app.jar"]
