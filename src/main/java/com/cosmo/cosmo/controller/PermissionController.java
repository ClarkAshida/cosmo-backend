package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.entity.Permission;
import com.cosmo.cosmo.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * Listar todas as permissões - ADMIN e OPERADOR podem visualizar
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OPERADOR')")
    public ResponseEntity<List<Permission>> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return ResponseEntity.ok(permissions);
    }

    /**
     * Buscar permissão por ID - ADMIN e OPERADOR podem visualizar
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OPERADOR')")
    public ResponseEntity<Permission> findById(@PathVariable Long id) {
        return permissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
