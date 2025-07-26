package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.HistoricoRequestDTO;
import com.cosmo.cosmo.dto.HistoricoResponseDTO;
import com.cosmo.cosmo.service.HistoricoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historicos")
@CrossOrigin(origins = "*")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<List<HistoricoResponseDTO>> getAllHistoricos() {
        List<HistoricoResponseDTO> historicos = historicoService.findAll();
        return ResponseEntity.ok(historicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoResponseDTO> getHistoricoById(@PathVariable Long id) {
        HistoricoResponseDTO historico = historicoService.findById(id);
        return ResponseEntity.ok(historico);
    }

    @PostMapping
    public ResponseEntity<HistoricoResponseDTO> createHistorico(@Valid @RequestBody HistoricoRequestDTO requestDTO) {
        HistoricoResponseDTO historico = historicoService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(historico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoResponseDTO> updateHistorico(
            @PathVariable Long id,
            @Valid @RequestBody HistoricoRequestDTO requestDTO) {
        HistoricoResponseDTO historico = historicoService.update(id, requestDTO);
        return ResponseEntity.ok(historico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorico(@PathVariable Long id) {
        historicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
