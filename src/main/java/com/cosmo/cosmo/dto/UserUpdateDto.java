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
public class UserUpdateDto {

    private String fullName;
    private String password; // Opcional - se não informado, mantém a senha atual
    private Boolean enabled;
    private List<Long> permissionIds;
}
