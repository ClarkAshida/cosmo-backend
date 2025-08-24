package com.cosmo.cosmo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // tempo de expiração em segundos

    // Dados do usuário autenticado
    private UserDataDto user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDataDto {
        private Long id;
        private String userName;
        private String fullName;
        private Boolean enabled;
        private List<String> permissions;
    }
}
