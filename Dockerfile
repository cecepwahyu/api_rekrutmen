# Use harbor image as a build environment
FROM harbor.local/jdk/openjdk:21-slim

# Set the working directory in the container
WORKDIR /APIREKRUTMEN

# Copy the project files into the container
COPY api-rekrutmen.jar .

# Expose port
EXPOSE 8080

# Specify the command to run SPA-UAD application
CMD ["java", "-jar", "api-rekrutmen.jar"]