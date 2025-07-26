package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.DepartamentoResponseDTO;
import com.cosmo.cosmo.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@CrossOrigin(origins = "*")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<List<DepartamentoResponseDTO>> getAllDepartamentos() {
        List<DepartamentoResponseDTO> departamentos = departamentoService.findAll();
        return ResponseEntity.ok(departamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> getDepartamentoById(@PathVariable Long id) {
        DepartamentoResponseDTO departamento = departamentoService.findById(id);
        return ResponseEntity.ok(departamento);
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponseDTO> createDepartamento(@Valid @RequestBody DepartamentoRequestDTO requestDTO) {
        DepartamentoResponseDTO departamento = departamentoService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> updateDepartamento(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoRequestDTO requestDTO) {
        DepartamentoResponseDTO departamento = departamentoService.update(id, requestDTO);
        return ResponseEntity.ok(departamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        departamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
