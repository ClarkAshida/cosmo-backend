package com.cosmo.cosmo.service;

import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id: " + id));
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = findById(id);

        usuario.setNome(usuarioDetails.getNome());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setCargo(usuarioDetails.getCargo());
        usuario.setCpf(usuarioDetails.getCpf());
        usuario.setCidade(usuarioDetails.getCidade());
        usuario.setDepartamento(usuarioDetails.getDepartamento());
        usuario.setEmpresa(usuarioDetails.getEmpresa());
        usuario.setAtivo(usuarioDetails.getAtivo());

        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

}