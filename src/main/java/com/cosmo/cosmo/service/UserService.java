package com.cosmo.cosmo.service;

import com.cosmo.cosmo.entity.User;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.InvalidCredentialsException;
import com.cosmo.cosmo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Tentando carregar usuário: {}", username);

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", username);
                    return new UsernameNotFoundException("Usuário '" + username + "' não encontrado");
                });

        if (!user.isEnabled()) {
            log.warn("Tentativa de login com usuário desabilitado: {}", username);
            throw new InvalidCredentialsException("Usuário '" + username + "' está desabilitado");
        }

        if (!user.isAccountNonExpired()) {
            log.warn("Tentativa de login com conta expirada: {}", username);
            throw new InvalidCredentialsException("Conta do usuário '" + username + "' está expirada");
        }

        if (!user.isAccountNonLocked()) {
            log.warn("Tentativa de login com conta bloqueada: {}", username);
            throw new InvalidCredentialsException("Conta do usuário '" + username + "' está bloqueada");
        }

        if (!user.isCredentialsNonExpired()) {
            log.warn("Tentativa de login com credenciais expiradas: {}", username);
            throw new InvalidCredentialsException("Credenciais do usuário '" + username + "' estão expiradas");
        }

        log.debug("Usuário carregado com sucesso: {} com {} permissões", username, user.getPermissions().size());
        return user;
    }

    public User findByUserName(String userName) {
        log.debug("Buscando usuário por userName: {}", userName);
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado: {}", userName);
                    return new ResourceNotFoundException("Usuário '" + userName + "' não encontrado");
                });
    }

    public User findById(Long id) {
        log.debug("Buscando usuário por ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
                });
    }

    public List<User> findAll() {
        log.debug("Buscando todos os usuários");
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        try {
            log.debug("Salvando usuário: {}", user.getUserName());

            if (user.getId() == null && userRepository.existsByUserName(user.getUserName())) {
                log.error("Tentativa de criar usuário com userName já existente: {}", user.getUserName());
                throw new InvalidCredentialsException("Nome de usuário '" + user.getUserName() + "' já existe");
            }

            // Se a senha foi alterada e não está criptografada, criptografar
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                log.debug("Criptografando senha para o usuário: {}", user.getUserName());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Definir valores padrão se não foram informados
            if (user.getAccountNonExpired() == null) {
                user.setAccountNonExpired(true);
            }
            if (user.getAccountNonLocked() == null) {
                user.setAccountNonLocked(true);
            }
            if (user.getCredentialsNonExpired() == null) {
                user.setCredentialsNonExpired(true);
            }
            if (user.getEnabled() == null) {
                user.setEnabled(true);
            }

            User savedUser = userRepository.save(user);
            log.info("Usuário salvo com sucesso: {}", savedUser.getUserName());
            return savedUser;

        } catch (InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao salvar usuário: {}", user.getUserName(), e);
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public User update(Long id, User user) {
        try {
            log.debug("Atualizando usuário com ID: {}", id);

            User existingUser = findById(id);

            // Verificar se o userName não está sendo usado por outro usuário
            if (!existingUser.getUserName().equals(user.getUserName()) &&
                userRepository.existsByUserName(user.getUserName())) {
                log.error("Tentativa de alterar userName para um já existente: {}", user.getUserName());
                throw new InvalidCredentialsException("Nome de usuário '" + user.getUserName() + "' já existe");
            }

            // Atualizar os campos
            existingUser.setUserName(user.getUserName());
            existingUser.setFullName(user.getFullName());

            // Se a senha foi alterada, criptografar
            if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                !user.getPassword().startsWith("$2a$")) {
                log.debug("Atualizando senha para o usuário: {}", user.getUserName());
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Atualizar flags de conta
            if (user.getAccountNonExpired() != null) {
                existingUser.setAccountNonExpired(user.getAccountNonExpired());
            }
            if (user.getAccountNonLocked() != null) {
                existingUser.setAccountNonLocked(user.getAccountNonLocked());
            }
            if (user.getCredentialsNonExpired() != null) {
                existingUser.setCredentialsNonExpired(user.getCredentialsNonExpired());
            }
            if (user.getEnabled() != null) {
                existingUser.setEnabled(user.getEnabled());
            }

            // Atualizar permissões se fornecidas
            if (user.getPermissions() != null) {
                existingUser.setPermissions(user.getPermissions());
            }

            User updatedUser = userRepository.save(existingUser);
            log.info("Usuário atualizado com sucesso: {}", updatedUser.getUserName());
            return updatedUser;

        } catch (ResourceNotFoundException | InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário com ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            log.debug("Deletando usuário com ID: {}", id);

            User user = findById(id);

            userRepository.deleteById(id);
            log.info("Usuário deletado com sucesso. ID: {}, userName: {}", id, user.getUserName());

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao deletar usuário com ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }

    public boolean existsByUserName(String userName) {
        log.debug("Verificando se userName existe: {}", userName);
        return userRepository.existsByUserName(userName);
    }

    @Transactional
    public User enableUser(Long id) {
        log.debug("Habilitando usuário com ID: {}", id);
        User user = findById(id);
        user.setEnabled(true);
        User enabledUser = userRepository.save(user);
        log.info("Usuário habilitado com sucesso: {}", enabledUser.getUserName());
        return enabledUser;
    }

    @Transactional
    public User disableUser(Long id) {
        log.debug("Desabilitando usuário com ID: {}", id);
        User user = findById(id);
        user.setEnabled(false);
        User disabledUser = userRepository.save(user);
        log.info("Usuário desabilitado com sucesso: {}", disabledUser.getUserName());
        return disabledUser;
    }

    @Transactional
    public User lockUser(Long id) {
        log.debug("Bloqueando usuário com ID: {}", id);
        User user = findById(id);
        user.setAccountNonLocked(false);
        User lockedUser = userRepository.save(user);
        log.info("Usuário bloqueado com sucesso: {}", lockedUser.getUserName());
        return lockedUser;
    }

    @Transactional
    public User unlockUser(Long id) {
        log.debug("Desbloqueando usuário com ID: {}", id);
        User user = findById(id);
        user.setAccountNonLocked(true);
        User unlockedUser = userRepository.save(user);
        log.info("Usuário desbloqueado com sucesso: {}", unlockedUser.getUserName());
        return unlockedUser;
    }
}
