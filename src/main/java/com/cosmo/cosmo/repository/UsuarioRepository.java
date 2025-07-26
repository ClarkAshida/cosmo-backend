package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para encontrar um usuário pelo nome de usuário
    Usuario findByUsername(String username);
}
