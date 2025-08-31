package com.cosmo.cosmo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cosmo.cosmo.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private Long tokenExpireLength;

    @Value("${security.jwt.token.refresh-token.expire-length}")
    private Long refreshTokenExpireLength;

    private final UserService userService;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(secretKey);
        log.info("JWT Token Provider initialized successfully");
    }

    public String createAccessToken(String email, List<String> roles) {
        log.info("Creating access token for user: {}", email);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenExpireLength);

        return JWT.create()
                .withIssuer("cosmo-api")
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public String createRefreshToken(String email) {
        log.info("Creating refresh token for user: {}", email);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpireLength);

        return JWT.create()
                .withIssuer("cosmo-api")
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("cosmo-api")
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.error("Invalid JWT token: {}", exception.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("cosmo-api")
                    .build()
                    .verify(token);

            String email = decodedJWT.getSubject();
            UserDetails userDetails = userService.loadUserByUsername(email);

            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
        } catch (Exception exception) {
            log.error("Error getting authentication from token: {}", exception.getMessage());
            return null;
        }
    }

    public String getSubject(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("cosmo-api")
                    .build()
                    .verify(token);

            return decodedJWT.getSubject();
        } catch (Exception exception) {
            log.error("Error getting subject from token: {}", exception.getMessage());
            return null;
        }
    }

    public Date getExpirationDate(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("cosmo-api")
                    .build()
                    .verify(token);

            return decodedJWT.getExpiresAt();
        } catch (Exception exception) {
            log.error("Error getting expiration date from token: {}", exception.getMessage());
            return null;
        }
    }
}
