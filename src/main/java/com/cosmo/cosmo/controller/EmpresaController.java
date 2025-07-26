package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.EmpresaRequestDTO;
import com.cosmo.cosmo.dto.EmpresaResponseDTO;
import com.cosmo.cosmo.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> getAllEmpresas() {
        List<EmpresaResponseDTO> empresas = empresaService.findAll();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> getEmpresaById(@PathVariable Long id) {
        EmpresaResponseDTO empresa = empresaService.findById(id);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> createEmpresa(@Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO empresa = empresaService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> updateEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO empresa = empresaService.update(id, requestDTO);
        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
