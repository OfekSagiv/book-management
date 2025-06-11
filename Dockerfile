FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy full source code
COPY . .

# Build the project without running tests
RUN mvn clean install -DskipTests

EXPOSE 8080

# Run the app in prod profile with dotenv support
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=prod"]
