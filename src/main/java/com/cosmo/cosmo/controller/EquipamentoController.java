package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.equipamento.*;
import com.cosmo.cosmo.enums.TipoEquipamento;
import com.cosmo.cosmo.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@CrossOrigin(origins = "*")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    // ==================== ENDPOINTS DE LEITURA (GET) ====================

    /**
     * GET /api/equipamentos
     * Busca todos os equipamentos (mantido, mas agora retorna EquipamentoResponseDTO)
     */
    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> findAll() {
        List<EquipamentoResponseDTO> equipamentos = equipamentoService.findAll();
        return ResponseEntity.ok(equipamentos);
    }

    /**
     * GET /api/equipamentos/{id}
     * Busca equipamento por ID (mantido, mas agora retorna EquipamentoResponseDTO com details)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> findById(@PathVariable Long id) {
        EquipamentoResponseDTO equipamento = equipamentoService.findById(id);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * GET /api/equipamentos/tipo/{tipo}
     * Busca equipamentos por tipo específico
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<EquipamentoResponseDTO>> findByTipo(@PathVariable TipoEquipamento tipo) {
        List<EquipamentoResponseDTO> equipamentos = equipamentoService.findByTipo(tipo);
        return ResponseEntity.ok(equipamentos);
    }

    /**
     * GET /api/equipamentos/tipo/{tipo}/count
     * Conta equipamentos por tipo específico
     */
    @GetMapping("/tipo/{tipo}/count")
    public ResponseEntity<Long> countByTipo(@PathVariable TipoEquipamento tipo) {
        Long count = equipamentoService.countByTipo(tipo);
        return ResponseEntity.ok(count);
    }

    // ==================== ENDPOINTS DE CRIAÇÃO (POST) ====================

    /**
     * POST /api/equipamentos/notebook
     * Cria um novo notebook
     */
    @PostMapping("/notebook")
    public ResponseEntity<EquipamentoResponseDTO> createNotebook(@Valid @RequestBody NotebookCreateDTO createDTO) {
        EquipamentoResponseDTO response = equipamentoService.createNotebook(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/equipamentos/desktop
     * Cria um novo desktop
     */
    @PostMapping("/desktop")
    public ResponseEntity<EquipamentoResponseDTO> createDesktop(@Valid @RequestBody DesktopCreateDTO createDTO) {
        EquipamentoResponseDTO response = equipamentoService.createDesktop(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/equipamentos/celular
     * Cria um novo celular
     */
    @PostMapping("/celular")
    public ResponseEntity<EquipamentoResponseDTO> createCelular(@Valid @RequestBody CelularCreateDTO createDTO) {
        EquipamentoResponseDTO response = equipamentoService.createCelular(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/equipamentos/chip
     * Cria um novo chip
     */
    @PostMapping("/chip")
    public ResponseEntity<EquipamentoResponseDTO> createChip(@Valid @RequestBody ChipCreateDTO createDTO) {
        EquipamentoResponseDTO response = equipamentoService.createChip(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/equipamentos/impressora
     * Cria uma nova impressora
     */
    @PostMapping("/impressora")
    public ResponseEntity<EquipamentoResponseDTO> createImpressora(@Valid @RequestBody ImpressoraCreateDTO createDTO) {
        EquipamentoResponseDTO response = equipamentoService.createImpressora(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/equipamentos/monitor
     * Cria um novo monitor
     */
    @PostMapping("/monitor")
    public ResponseEntity<EquipamentoResponseDTO> createMonitor(@Valid @RequestBody MonitorCreateDTO createDTO) {
        EquipamentoResponseDTO response = equipamentoService.createMonitor(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== ENDPOINTS DE ATUALIZAÇÃO (PUT) ====================

    /**
     * PUT /api/equipamentos/notebook/{id}
     * Atualiza um notebook existente
     */
    @PutMapping("/notebook/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateNotebook(
            @PathVariable Long id,
            @Valid @RequestBody NotebookUpdateDTO updateDTO) {
        EquipamentoResponseDTO response = equipamentoService.updateNotebook(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/equipamentos/desktop/{id}
     * Atualiza um desktop existente
     */
    @PutMapping("/desktop/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateDesktop(
            @PathVariable Long id,
            @Valid @RequestBody DesktopUpdateDTO updateDTO) {
        EquipamentoResponseDTO response = equipamentoService.updateDesktop(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/equipamentos/celular/{id}
     * Atualiza um celular existente
     */
    @PutMapping("/celular/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateCelular(
            @PathVariable Long id,
            @Valid @RequestBody CelularUpdateDTO updateDTO) {
        EquipamentoResponseDTO response = equipamentoService.updateCelular(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/equipamentos/chip/{id}
     * Atualiza um chip existente
     */
    @PutMapping("/chip/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateChip(
            @PathVariable Long id,
            @Valid @RequestBody ChipUpdateDTO updateDTO) {
        EquipamentoResponseDTO response = equipamentoService.updateChip(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/equipamentos/impressora/{id}
     * Atualiza uma impressora existente
     */
    @PutMapping("/impressora/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateImpressora(
            @PathVariable Long id,
            @Valid @RequestBody ImpressoraUpdateDTO updateDTO) {
        EquipamentoResponseDTO response = equipamentoService.updateImpressora(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/equipamentos/monitor/{id}
     * Atualiza um monitor existente
     */
    @PutMapping("/monitor/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateMonitor(
            @PathVariable Long id,
            @Valid @RequestBody MonitorUpdateDTO updateDTO) {
        EquipamentoResponseDTO response = equipamentoService.updateMonitor(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    // ==================== ENDPOINT DE EXCLUSÃO (DELETE) ====================

    /**
     * DELETE /api/equipamentos/{id}
     * Remove um equipamento (mantido, funciona para qualquer tipo)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        equipamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== ENDPOINTS LEGADOS (DEPRECATED) ====================

    /**
     * @deprecated Use os endpoints específicos por tipo
     * POST /api/equipamentos - Método legado
     */
    @Deprecated
    @PostMapping
    public ResponseEntity<String> createEquipamento(@RequestBody Object requestDTO) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Endpoint descontinuado. Use os endpoints específicos: /notebook, /celular, /impressora, etc.");
    }

    /**
     * @deprecated Use os endpoints específicos por tipo
     * PUT /api/equipamentos/{id} - Método legado
     */
    @Deprecated
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEquipamento(@PathVariable Long id, @RequestBody Object requestDTO) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Endpoint descontinuado. Use os endpoints específicos: /notebook/{id}, /celular/{id}, /impressora/{id}, etc.");
    }

    // ==================== ENDPOINTS AUXILIARES ====================

    /**
     * GET /api/equipamentos/tipos
     * Lista todos os tipos de equipamento disponíveis
     */
    @GetMapping("/tipos")
    public ResponseEntity<TipoEquipamento[]> getTiposEquipamento() {
        return ResponseEntity.ok(TipoEquipamento.values());
    }
}
