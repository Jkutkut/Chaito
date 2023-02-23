FROM amazoncorretto:18.0.2-alpine3.16

# Set working directory
WORKDIR /app

# Copy the jar files
COPY ./jars/ /jars

# Copy DB
COPY db.properties /app/

# Define the entrypoint
ENTRYPOINT ["java", "-cp", "/jars/chaito_server.jar:/jars/sqlite-jdbc.jar", "org.chaito.Server", "3232"]

