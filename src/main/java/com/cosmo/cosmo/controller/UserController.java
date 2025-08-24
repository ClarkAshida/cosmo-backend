package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.UserCreateDto;
import com.cosmo.cosmo.dto.UserResponseDto;
import com.cosmo.cosmo.dto.UserUpdateDto;
import com.cosmo.cosmo.entity.Permission;
import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.repository.PermissionRepository;
import com.cosmo.cosmo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Listar todos os usuários - Apenas ADMIN
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userRepository.findAll();

        List<UserResponseDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    /**
     * Buscar usuário por ID - Apenas ADMIN
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(convertToDto(user.get()));
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Criar novo usuário - Apenas ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto userDto) {
        try {
            // Verifica se o userName já existe
            if (userRepository.existsByUserName(userDto.getUserName())) {
                return ResponseEntity.badRequest().build();
            }

            User user = new User();
            user.setUserName(userDto.getUserName());
            user.setFullName(userDto.getFullName());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEnabled(userDto.getEnabled());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);

            // Busca e define as permissões
            if (userDto.getPermissionIds() != null && !userDto.getPermissionIds().isEmpty()) {
                List<Permission> permissions = permissionRepository.findAllById(userDto.getPermissionIds());
                user.setPermissions(permissions);
            } else {
                user.setPermissions(new ArrayList<>());
            }

            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Atualizar usuário - Apenas ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = optionalUser.get();

            // Atualiza os campos se fornecidos
            if (userDto.getFullName() != null) {
                user.setFullName(userDto.getFullName());
            }

            if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            if (userDto.getEnabled() != null) {
                user.setEnabled(userDto.getEnabled());
            }

            // Atualiza as permissões se fornecidas
            if (userDto.getPermissionIds() != null) {
                List<Permission> permissions = permissionRepository.findAllById(userDto.getPermissionIds());
                user.setPermissions(permissions);
            }

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(convertToDto(updatedUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletar usuário - Apenas ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ativar/Desativar usuário - Apenas ADMIN
     */
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> toggleStatus(@PathVariable Long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = optionalUser.get();
            user.setEnabled(!user.isEnabled());

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(convertToDto(updatedUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Converte User para UserResponseDto
     */
    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.isEnabled(),
                user.getPermissions().stream()
                        .map(Permission::getDescription)
                        .collect(Collectors.toList())
        );
    }
}
