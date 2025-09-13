/*
 * Copyright 2025 Flávio Alexandre Orrico Severiano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.AccountCredentialsDTO;
import com.cosmo.cosmo.dto.TokenDTO;
import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.security.JwtTokenProvider;
import com.cosmo.cosmo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários, incluindo login e renovação de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/signin")
    @Operation(
        summary = "Realizar login no sistema",
        description = "Autentica um usuário com email e senha, retornando tokens JWT de acesso e renovação. " +
                     "O token de acesso deve ser usado para acessar endpoints protegidos, enquanto o token de renovação " +
                     "pode ser usado para obter novos tokens de acesso sem precisar fazer login novamente.",
        security = {} // Remove autenticação para este endpoint
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenDTO.class),
                examples = @ExampleObject(
                    name = "Resposta de login bem-sucedido",
                    value = """
                        {
                          "username": "admin@cosmo.com",
                          "authenticated": true,
                          "created": "2025-09-12T22:00:00.000+00:00",
                          "expiration": "2025-09-12T23:00:00.000+00:00",
                          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Credenciais inválidas - email ou senha incorretos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenDTO.class),
                examples = @ExampleObject(
                    name = "Credenciais inválidas",
                    value = """
                        {
                          "username": "user@example.com",
                          "authenticated": false,
                          "created": "2025-09-12T22:00:00.000+00:00",
                          "expiration": null,
                          "accessToken": null,
                          "refreshToken": null
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos ou malformados"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor"
        )
    })
    public ResponseEntity<TokenDTO> signin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciais de acesso do usuário",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccountCredentialsDTO.class),
                examples = @ExampleObject(
                    name = "Credenciais de login",
                    value = """
                        {
                          "email": "admin@cosmo.com",
                          "password": "admin123"
                        }
                        """
                )
            )
        )
        @RequestBody AccountCredentialsDTO credentials) {
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
    @Operation(
        summary = "Renovar token de acesso",
        description = "Renova o token de acesso usando um token de renovação válido. " +
                     "O token de renovação deve ser enviado no header Authorization com o prefixo 'Bearer '. " +
                     "Este endpoint retorna novos tokens de acesso e renovação, invalidando os anteriores.",
        security = {} // Remove autenticação padrão pois usa refresh token
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token renovado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenDTO.class),
                examples = @ExampleObject(
                    name = "Token renovado com sucesso",
                    value = """
                        {
                          "username": "admin@cosmo.com",
                          "authenticated": true,
                          "created": "2025-09-12T22:30:00.000+00:00",
                          "expiration": "2025-09-12T23:30:00.000+00:00",
                          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Token de renovação inválido, expirado ou não corresponde ao usuário",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenDTO.class),
                examples = @ExampleObject(
                    name = "Token de renovação inválido",
                    value = """
                        {
                          "username": "admin@cosmo.com",
                          "authenticated": false,
                          "created": "2025-09-12T22:30:00.000+00:00",
                          "expiration": null,
                          "accessToken": null,
                          "refreshToken": null
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Header Authorization ausente ou formato inválido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor"
        )
    })
    public ResponseEntity<TokenDTO> refreshToken(
            @Parameter(
                description = "Email do usuário para renovação do token",
                required = true,
                example = "admin@cosmo.com"
            )
            @PathVariable String email,
            @Parameter(
                description = "Token de renovação no formato 'Bearer <refresh_token>'",
                required = true,
                example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            )
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
