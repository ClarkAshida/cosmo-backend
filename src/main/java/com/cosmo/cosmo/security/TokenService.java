package com.cosmo.cosmo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cosmo.cosmo.entity.Permission;
import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Extrair as permissões como uma string separada por vírgulas
            String permissions = user.getPermissions().stream()
                    .map(Permission::getDescription)
                    .collect(Collectors.joining(","));

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getUsername())
                    .withClaim("permissions", permissions)
                    .withClaim("fullName", user.getFullName())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return ""; // Token inválido retorna string vazia
        }
    }

    public String getPermissionsFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getClaim("permissions")
                    .asString();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusSeconds(expiration / 1000)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateRefreshToken(User user) {
        try {
            // Gera um token único usando UUID
            String refreshToken = UUID.randomUUID().toString();

            // Define expiração do refresh token para 24 horas
            Instant expiryTime = LocalDateTime.now()
                    .plusHours(24)
                    .toInstant(ZoneOffset.of("-03:00"));

            // Atualiza o usuário com o novo refresh token
            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiryTime(expiryTime);

            // Salva no banco de dados
            userRepository.save(user);

            return refreshToken;

        } catch (Exception exception) {
            throw new RuntimeException("Erro ao gerar refresh token", exception);
        }
    }

    public boolean validateRefreshToken(String refreshToken, User user) {
        return refreshToken.equals(user.getRefreshToken()) &&
                user.getRefreshTokenExpiryTime().isAfter(Instant.now());
    }
}
