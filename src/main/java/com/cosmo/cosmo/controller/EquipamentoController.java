package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.PagedResponseDTO;
import com.cosmo.cosmo.dto.equipamento.*;
import com.cosmo.cosmo.enums.TipoEquipamento;
import com.cosmo.cosmo.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // ==================== ENDPOINTS DE LEITURA PAGINADOS (GET) ====================

    /**
     * GET /api/equipamentos
     * Busca todos os equipamentos com paginação e ordenação
     */
    @GetMapping
    public ResponseEntity<PagedResponseDTO<EquipamentoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() :
            Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<EquipamentoResponseDTO> equipamentos = equipamentoService.findAll(pageable);
        return ResponseEntity.ok(equipamentos);
    }

    /**
     * GET /api/equipamentos/{id}
     * Busca equipamento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> findById(@PathVariable Long id) {
        EquipamentoResponseDTO equipamento = equipamentoService.findById(id);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * GET /api/equipamentos/tipo/{tipo}
     * Busca equipamentos por tipo específico com paginação
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<PagedResponseDTO<EquipamentoResponseDTO>> findByTipo(
            @PathVariable TipoEquipamento tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "marca") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() :
            Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<EquipamentoResponseDTO> equipamentos = equipamentoService.findByTipo(tipo, pageable);
        return ResponseEntity.ok(equipamentos);
    }

    // ==================== ENDPOINTS DE CRIAÇÃO ESPECÍFICOS (POST) ====================

    /**
     * POST /api/equipamentos/notebook
     * Cria um novo notebook
     */
    @PostMapping("/notebook")
    public ResponseEntity<EquipamentoResponseDTO> createNotebook(@Valid @RequestBody NotebookCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createNotebook(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/desktop
     * Cria um novo desktop
     */
    @PostMapping("/desktop")
    public ResponseEntity<EquipamentoResponseDTO> createDesktop(@Valid @RequestBody DesktopCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createDesktop(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/celular
     * Cria um novo celular
     */
    @PostMapping("/celular")
    public ResponseEntity<EquipamentoResponseDTO> createCelular(@Valid @RequestBody CelularCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createCelular(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/chip
     * Cria um novo chip
     */
    @PostMapping("/chip")
    public ResponseEntity<EquipamentoResponseDTO> createChip(@Valid @RequestBody ChipCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createChip(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/impressora
     * Cria uma nova impressora
     */
    @PostMapping("/impressora")
    public ResponseEntity<EquipamentoResponseDTO> createImpressora(@Valid @RequestBody ImpressoraCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createImpressora(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/monitor
     * Cria um novo monitor
     */
    @PostMapping("/monitor")
    public ResponseEntity<EquipamentoResponseDTO> createMonitor(@Valid @RequestBody MonitorCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createMonitor(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    // ==================== ENDPOINTS DE ATUALIZAÇÃO ESPECÍFICOS (PUT) ====================

    /**
     * PUT /api/equipamentos/notebook/{id}
     * Atualiza um notebook existente
     */
    @PutMapping("/notebook/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateNotebook(@PathVariable Long id, @Valid @RequestBody NotebookUpdateDTO updateDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.updateNotebook(id, updateDTO);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * PUT /api/equipamentos/desktop/{id}
     * Atualiza um desktop existente
     */
    @PutMapping("/desktop/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateDesktop(@PathVariable Long id, @Valid @RequestBody DesktopUpdateDTO updateDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.updateDesktop(id, updateDTO);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * PUT /api/equipamentos/celular/{id}
     * Atualiza um celular existente
     */
    @PutMapping("/celular/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateCelular(@PathVariable Long id, @Valid @RequestBody CelularUpdateDTO updateDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.updateCelular(id, updateDTO);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * PUT /api/equipamentos/chip/{id}
     * Atualiza um chip existente
     */
    @PutMapping("/chip/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateChip(@PathVariable Long id, @Valid @RequestBody ChipUpdateDTO updateDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.updateChip(id, updateDTO);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * PUT /api/equipamentos/impressora/{id}
     * Atualiza uma impressora existente
     */
    @PutMapping("/impressora/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateImpressora(@PathVariable Long id, @Valid @RequestBody ImpressoraUpdateDTO updateDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.updateImpressora(id, updateDTO);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * PUT /api/equipamentos/monitor/{id}
     * Atualiza um monitor existente
     */
    @PutMapping("/monitor/{id}")
    public ResponseEntity<EquipamentoResponseDTO> updateMonitor(@PathVariable Long id, @Valid @RequestBody MonitorUpdateDTO updateDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.updateMonitor(id, updateDTO);
        return ResponseEntity.ok(equipamento);
    }

    // ==================== ENDPOINT DE EXCLUSÃO (DELETE) ====================

    /**
     * DELETE /api/equipamentos/{id}
     * Exclui um equipamento por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        equipamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== ENDPOINTS AUXILIARES ====================

    /**
     * GET /api/equipamentos/tipo/{tipo}/count
     * Conta equipamentos por tipo
     */
    @GetMapping("/tipo/{tipo}/count")
    public ResponseEntity<Long> countByTipo(@PathVariable TipoEquipamento tipo) {
        Long count = equipamentoService.countByTipo(tipo);
        return ResponseEntity.ok(count);
    }
}
