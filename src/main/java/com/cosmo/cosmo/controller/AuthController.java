package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.AccountCredentialsDTO;
import com.cosmo.cosmo.dto.TokenDTO;
import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.security.JwtTokenProvider;
import com.cosmo.cosmo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signin(@RequestBody AccountCredentialsDTO credentials) {
        try {
            log.info("Authentication attempt for user: {}", credentials.email());

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password())
            );

            User user = (User) authentication.getPrincipal();

            // Extract roles for the token
            List<String> roles = user.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();

            // Generate tokens
            String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), roles);
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

            Date now = new Date();
            Date expiration = jwtTokenProvider.getExpirationDate(accessToken);

            TokenDTO tokenResponse = new TokenDTO(
                user.getEmail(),
                true,
                now,
                expiration,
                accessToken,
                refreshToken
            );

            log.info("Authentication successful for user: {}", credentials.email());
            return ResponseEntity.ok(tokenResponse);

        } catch (BadCredentialsException | UsernameNotFoundException exception) {
            log.warn("Authentication failed for user: {} - {}", credentials.email(), exception.getMessage());

            TokenDTO errorResponse = new TokenDTO(
                credentials.email(),
                false,
                new Date(),
                null,
                null,
                null
            );

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }

    @PutMapping("/refresh/{email}")
    public ResponseEntity<TokenDTO> refreshToken(
            @PathVariable String email,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            log.info("Token refresh attempt for user: {}", email);

            // Extract refresh token from Authorization header
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new BadCredentialsException("Invalid refresh token format");
            }

            String refreshToken = authorizationHeader.substring(7);

            // Validate refresh token
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new BadCredentialsException("Invalid or expired refresh token");
            }

            // Verify token subject matches the requested email
            String tokenSubject = jwtTokenProvider.getSubject(refreshToken);
            if (!email.equals(tokenSubject)) {
                throw new BadCredentialsException("Token subject does not match requested user");
            }

            // Load user details
            User user = (User) userService.loadUserByUsername(email);

            // Extract roles for the new token
            List<String> roles = user.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();

            // Generate new tokens
            String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), roles);
            String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

            Date now = new Date();
            Date expiration = jwtTokenProvider.getExpirationDate(newAccessToken);

            TokenDTO tokenResponse = new TokenDTO(
                user.getEmail(),
                true,
                now,
                expiration,
                newAccessToken,
                newRefreshToken
            );

            log.info("Token refresh successful for user: {}", email);
            return ResponseEntity.ok(tokenResponse);

        } catch (Exception exception) {
            log.warn("Token refresh failed for user: {} - {}", email, exception.getMessage());

            TokenDTO errorResponse = new TokenDTO(
                email,
                false,
                new Date(),
                null,
                null,
                null
            );

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }
}
