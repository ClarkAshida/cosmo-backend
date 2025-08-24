package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.AuthenticationResponseDto;
import com.cosmo.cosmo.dto.LoginRequestDto;
import com.cosmo.cosmo.dto.RefreshTokenRequestDto;
import com.cosmo.cosmo.entity.Permission;
import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.repository.UserRepository;
import com.cosmo.cosmo.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Value("${api.security.token.expiration}")
    private Long tokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            // Cria o token de autenticação com as credenciais
            var authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            );

            // Autentica as credenciais
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Obtém o usuário autenticado
            User user = (User) authentication.getPrincipal();

            // Gera os tokens
            String accessToken = tokenService.generateToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);

            // Cria o DTO com os dados do usuário
            AuthenticationResponseDto.UserDataDto userData = new AuthenticationResponseDto.UserDataDto(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.isEnabled(),
                    user.getPermissions().stream()
                            .map(Permission::getDescription)
                            .collect(Collectors.toList())
            );

            // Cria a resposta
            AuthenticationResponseDto response = new AuthenticationResponseDto(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    tokenExpiration / 1000, // converte para segundos
                    userData
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshRequest) {
        try {
            String refreshToken = refreshRequest.getRefreshToken();

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Busca o usuário pelo refresh token de forma otimizada
            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElse(null);

            if (user == null || !tokenService.validateRefreshToken(refreshToken, user)) {
                return ResponseEntity.badRequest().build();
            }

            // Gera novos tokens
            String newAccessToken = tokenService.generateToken(user);
            String newRefreshToken = tokenService.generateRefreshToken(user);

            // Cria o DTO com os dados do usuário
            AuthenticationResponseDto.UserDataDto userData = new AuthenticationResponseDto.UserDataDto(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.isEnabled(),
                    user.getPermissions().stream()
                            .map(Permission::getDescription)
                            .collect(Collectors.toList())
            );

            // Cria a resposta
            AuthenticationResponseDto response = new AuthenticationResponseDto(
                    newAccessToken,
                    newRefreshToken,
                    "Bearer",
                    tokenExpiration / 1000, // converte para segundos
                    userData
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
