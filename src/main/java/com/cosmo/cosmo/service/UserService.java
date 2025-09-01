package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.UserRequestDTO;
import com.cosmo.cosmo.dto.UserResponseDTO;
import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Finding one user by email: {}", username);
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
        return user;
    }

    public UserResponseDTO findById(Long id) {
        log.info("Finding user by ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return mapToResponseDTO(user);
    }

    public List<UserResponseDTO> findAll() {
        log.info("Finding all users");
        return userRepository.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .toList();
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        log.info("Finding all users with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable)
            .map(this::mapToResponseDTO);
    }

    public Page<UserResponseDTO> findAll(Specification<User> spec, Pageable pageable) {
        log.info("Finding users with specification and pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(spec, pageable)
            .map(this::mapToResponseDTO);
    }

    public UserResponseDTO create(UserRequestDTO userRequest) {
        log.info("Creating new user with email: {}", userRequest.email());

        // Check if user already exists
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new RuntimeException("User with email " + userRequest.email() + " already exists");
        }

        User user = new User();
        user.setEmail(userRequest.email());
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    public UserResponseDTO update(Long id, UserRequestDTO userRequest) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Update fields
        user.setEmail(userRequest.email());
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());

        // Only update password if provided
        if (userRequest.password() != null && !userRequest.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
    }
}
