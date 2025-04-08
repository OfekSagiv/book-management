FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Copy pom.xml and download dependencies (uses cache!)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the full project
COPY . .

# Build the project
RUN mvn clean install

EXPOSE 8080

CMD mvn clean compile && \
    mvn exec:java -Dexec.mainClass="com.ofeksag.book_management.utils.EnvFileGenerator" && \
    mvn spring-boot:run