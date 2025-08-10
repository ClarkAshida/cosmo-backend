package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.PagedResponseDTO;
import com.cosmo.cosmo.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<PagedResponseDTO<DepartamentoResponseDTO>> getAllDepartamentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<DepartamentoResponseDTO> departamentos = departamentoService.findAll(pageable);
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
