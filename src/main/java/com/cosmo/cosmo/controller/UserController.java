package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.UserRequestDTO;
import com.cosmo.cosmo.dto.UserResponseDTO;
import com.cosmo.cosmo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import com.cosmo.cosmo.specification.UserSpecification;
import com.cosmo.cosmo.entity.User;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PagedResourcesAssembler<UserResponseDTO> assembler;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequest) {
        log.info("Creating new user with email: {}", userRequest.email());
        try {
            UserResponseDTO createdUser = userService.create(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException exception) {
            log.error("Error creating user: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserResponseDTO>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "firstName") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        log.info("Retrieving all users with pagination - page: {}, size: {}, sort: {}, direction: {}",
                page, size, sort, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortObj = Sort.by(sortDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<UserResponseDTO> users = userService.findAll(pageable);
        PagedModel<EntityModel<UserResponseDTO>> pagedModel = assembler.toModel(users);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("Retrieving user with ID: {}", id);
        try {
            UserResponseDTO user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException exception) {
            log.error("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequest) {
        log.info("Updating user with ID: {}", id);
        try {
            UserResponseDTO updatedUser = userService.update(id, userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException exception) {
            log.error("Error updating user with ID {}: {}", id, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException exception) {
            log.error("Error deleting user with ID {}: {}", id, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<PagedModel<EntityModel<UserResponseDTO>>> filterUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Boolean accountNonExpired,
            @RequestParam(required = false) Boolean accountNonLocked,
            @RequestParam(required = false) Boolean credentialsNonExpired,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "firstName") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        log.info("Filtering users with criteria - firstName: {}, lastName: {}, email: {}, enabled: {}",
                firstName, lastName, email, enabled);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortObj = Sort.by(sortDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<User> spec = Specification.where(UserSpecification.hasFirstName(firstName))
                .and(UserSpecification.hasLastName(lastName))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasEnabled(enabled))
                .and(UserSpecification.hasAccountNonExpired(accountNonExpired))
                .and(UserSpecification.hasAccountNonLocked(accountNonLocked))
                .and(UserSpecification.hasCredentialsNonExpired(credentialsNonExpired));

        Page<UserResponseDTO> users = userService.findAll(spec, pageable);
        PagedModel<EntityModel<UserResponseDTO>> pagedModel = assembler.toModel(users);

        return ResponseEntity.ok(pagedModel);
    }
}
