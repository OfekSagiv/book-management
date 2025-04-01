package com.ofeksag.book_management.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

public class EnvFileGenerator {
    public static void main(String[] args) {
        String envFileName = ".env";
        Path envPath = Paths.get(envFileName);

        if (Files.exists(envPath)) {
            System.out.println(envFileName + " already exists.");
        } else {
            try {
                byte[] randomBytes = new byte[32];
                new SecureRandom().nextBytes(randomBytes);
                String secretKey = Base64.getEncoder().encodeToString(randomBytes);
                String content = "SECRET_KEY=" + secretKey + "\n";
                Files.write(envPath, content.getBytes());
                System.out.println(envFileName + " created with SECRET_KEY.");
            } catch (IOException e) {
                System.err.println("Failed to create " + envFileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}