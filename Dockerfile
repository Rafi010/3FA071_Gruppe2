# Use an official OpenJDK 21 image as the base
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# Copy project files
COPY . /app

# Build the application using Maven
RUN mvn clean package -DskipTests

# Expose the application's port
EXPOSE 8080

# Define the command to run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar target/*.jar"]
