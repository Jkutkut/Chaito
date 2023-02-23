FROM amazoncorretto:18.0.2-alpine3.16

# Set working directory
WORKDIR /app

# Copy the jar files
COPY ./chaito_server.jar /jars/

# Define the entrypoint
ENTRYPOINT ["java", "-cp", "/jars/chaito_server.jar", "org.chaito.Server"]

