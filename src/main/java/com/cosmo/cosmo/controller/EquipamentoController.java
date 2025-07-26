package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.EquipamentoRequestDTO;
import com.cosmo.cosmo.dto.EquipamentoResponseDTO;
import com.cosmo.cosmo.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@CrossOrigin(origins = "*")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> getAllEquipamentos() {
        List<EquipamentoResponseDTO> equipamentos = equipamentoService.findAll();
        return ResponseEntity.ok(equipamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> getEquipamentoById(@PathVariable Long id) {
        EquipamentoResponseDTO equipamento = equipamentoService.findById(id);
        return ResponseEntity.ok(equipamento);
    }

    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> createEquipamento(@Valid @RequestBody EquipamentoRequestDTO requestDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateEquipamento(
            @PathVariable Long id,
            @Valid @RequestBody EquipamentoRequestDTO requestDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.update(id, requestDTO);
        return ResponseEntity.ok(equipamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipamento(@PathVariable Long id) {
        equipamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
