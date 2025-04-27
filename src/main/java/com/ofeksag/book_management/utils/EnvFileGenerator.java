package com.ofeksag.book_management.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class EnvFileGenerator {

    public static void main(String[] args) {
        generateEnvFileIfNeeded();
        loadEnvVariables();
    }

    private static void generateEnvFileIfNeeded() {
        String envFileName = ".env";
        Path envPath = Paths.get(envFileName);

        if (Files.exists(envPath)) {
            System.out.println(envFileName + " already exists.");
        } else {
            try {
                byte[] secretKeyBytes = new byte[32];
                new SecureRandom().nextBytes(secretKeyBytes);
                String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);

                String dbPassword = generateRandomPassword(12);

                String content = ""
                        + "SECRET_KEY=" + secretKey + "\n"
                        + "DB_USERNAME=postgres\n"
                        + "DB_PASSWORD=" + dbPassword + "\n"
                        + "DB_NAME=bookdb\n";

                Files.write(envPath, content.getBytes());
                System.out.println(envFileName + " created with database and secret configurations.");
            } catch (IOException e) {
                System.err.println("Failed to create " + envFileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void loadEnvVariables() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(".env"));
            props.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
            System.out.println(".env variables loaded successfully.");
        } catch (IOException e) {
            System.out.println(".env file not found, skipping...");
        }
    }

    private static String generateRandomPassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
