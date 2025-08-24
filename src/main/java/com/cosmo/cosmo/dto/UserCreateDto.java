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
public class UserCreateDto {

    private String userName;
    private String fullName;
    private String password;
    private Boolean enabled = true;
    private List<Long> permissionIds;
}
