package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.UsuarioResponseDTO;
import com.cosmo.cosmo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO usuario = usuarioService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO usuario = usuarioService.update(id, requestDTO);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deactivateById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<UsuarioResponseDTO> reativarUsuario(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.reactivateById(id);
        return ResponseEntity.ok(usuario);
    }
}
