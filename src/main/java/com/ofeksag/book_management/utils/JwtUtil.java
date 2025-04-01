package com.ofeksag.book_management.utils;

import com.ofeksag.book_management.exception.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final Key secretKey;
    private final long jwtExpirationInMs;

    public JwtUtil(@Value("${jwt.expiration}") long jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
        String secret;
        try {
            secret = loadSecretFromFile(".env");
        } catch (EnvFileNotFoundException | SecretKeyNotFoundException
                 | EmptySecretKeyException | SecretKeyTooShortException e) {
            System.err.println("Error loading secret key: " + e.getMessage());
            throw e;
        }
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        this.secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private String loadSecretFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                byte[] randomBytes = new byte[32];
                new SecureRandom().nextBytes(randomBytes);
                String generatedSecret = Base64.getEncoder().encodeToString(randomBytes);
                String content = "SECRET_KEY=" + generatedSecret + "\n";
                Files.write(path, content.getBytes());

                return generatedSecret;
            }
            List<String> lines = Files.readAllLines(path);
            Optional<String> secretLine = lines.stream()
                    .filter(line -> line.startsWith("SECRET_KEY="))
                    .findFirst();
            if (secretLine.isEmpty()) {
                throw new SecretKeyNotFoundException("SECRET_KEY not found in " + filePath);
            }
            String secret = secretLine.get().split("=", 2)[1].trim();
            if (secret.isEmpty()) {
                throw new EmptySecretKeyException("SECRET_KEY in file '" + filePath + "' is empty.");
            }
            if (secret.getBytes().length < 32) {
                throw new SecretKeyTooShortException("SECRET_KEY is too short. It must be at least 32 bytes.");
            }
            return secret;
        } catch (IOException e) {
            throw new RuntimeException("Error reading secret key from file: " + filePath, e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtDeserializationException("Failed to deserialize JWT: " + e.getMessage(), e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}