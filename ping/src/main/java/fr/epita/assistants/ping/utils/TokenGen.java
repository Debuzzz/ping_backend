package fr.epita.assistants.ping.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import fr.epita.assistants.ping.data.model.UserModel;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

public class TokenGen {
    public static Duration TOKEN_VALIDITY = Duration.ofHours(1);

    public static String generateToken(UserModel user) {
        try {
            Logger.info("Generating token for user: " + user.getLogin());
            
            // On crée un builder de JWT claims
            JwtClaimsBuilder builder = Jwt.claims();

            // On définit les claims du token
            builder.subject(user.getId().toString()); 
            builder.groups(user.getIsAdmin() ? "admin" : "user");
            builder.issuedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            builder.expiresAt(LocalDateTime.now().plus(TOKEN_VALIDITY).toEpochSecond(ZoneOffset.UTC));
            builder.issuer("https://localhost:8080");

            // On signe et retourne le token
            String token = builder.sign();
            Logger.success("Token generated successfully");
            return token;
            
        } catch (Exception e) {
            Logger.error("Error generating token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
