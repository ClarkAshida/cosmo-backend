package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.dto.equipamento.*;
import com.cosmo.cosmo.enums.TipoEquipamento;
import com.cosmo.cosmo.service.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Equipamentos", description = "Gerenciamento de equipamentos de TI. Permite criar, listar, atualizar e excluir equipamentos de diferentes tipos (notebooks, desktops, celulares, chips, impressoras e monitores), além de realizar buscas com filtros avançados e paginação.")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    // ==================== ENDPOINTS DE LEITURA PAGINADOS (GET) ====================

    /**
     * GET /api/equipamentos
     * Busca todos os equipamentos com paginação e ordenação
     */
    @GetMapping
    @Operation(
        summary = "Listar todos os equipamentos",
        description = "Retorna uma lista paginada de todos os equipamentos cadastrados no sistema, independente do tipo. " +
                     "Suporta ordenação por qualquer campo e paginação configurável."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de equipamentos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class),
                examples = @ExampleObject(
                    name = "Lista paginada de equipamentos",
                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "serialNumber": "NB001ABC123",
                              "numeroPatrimonio": "PAT001",
                              "marca": "Dell",
                              "modelo": "Inspiron 15 3000",
                              "estadoConservacao": "BOM",
                              "status": "DISPONIVEL",
                              "termoResponsabilidade": false,
                              "notaFiscal": "NF123456",
                              "statusPropriedade": "PROPRIO",
                              "tipo": "NOTEBOOK"
                            }
                          ],
                          "totalElements": 150,
                          "totalPages": 15,
                          "size": 10,
                          "number": 0,
                          "first": true,
                          "last": false
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<EquipamentoResponseDTO>> findAll(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
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
    @Operation(
        summary = "Buscar equipamento por ID",
        description = "Retorna os detalhes de um equipamento específico baseado no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Equipamento encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class),
                examples = @ExampleObject(
                    name = "Equipamento encontrado",
                    value = """
                        {
                          "id": 1,
                          "serialNumber": "NB001ABC123",
                          "numeroPatrimonio": "PAT001",
                          "marca": "Dell",
                          "modelo": "Inspiron 15 3000",
                          "estadoConservacao": "BOM",
                          "status": "DISPONIVEL",
                          "termoResponsabilidade": false,
                          "notaFiscal": "NF123456",
                          "statusPropriedade": "PROPRIO",
                          "tipo": "NOTEBOOK"
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar equipamentos"),
        @ApiResponse(responseCode = "404", description = "Equipamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> findById(
            @Parameter(description = "ID único do equipamento", required = true, example = "1")
            @PathVariable Long id) {
        EquipamentoResponseDTO equipamento = equipamentoService.findById(id);
        return ResponseEntity.ok(equipamento);
    }

    /**
     * GET /api/equipamentos/tipo/{tipo}
     * Busca equipamentos por tipo específico com paginação
     */
    @GetMapping("/tipo/{tipo}")
    @Operation(
        summary = "Buscar equipamentos por tipo",
        description = "Retorna uma lista paginada de equipamentos filtrados por tipo específico."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de equipamentos por tipo retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<EquipamentoResponseDTO>> findByTipo(
            @Parameter(description = "Tipo do equipamento", required = true, example = "NOTEBOOK", schema = @Schema(allowableValues = {"NOTEBOOK", "DESKTOP", "CELULAR", "CHIP", "IMPRESSORA", "MONITOR"}))
            @PathVariable TipoEquipamento tipo,
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "marca")
            @RequestParam(defaultValue = "marca") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
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
    @Operation(
        summary = "Criar novo notebook",
        description = "Cria um novo notebook no sistema. O número de série deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Notebook criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou número de série já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> createNotebook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do notebook a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotebookCreateDTO.class),
                    examples = @ExampleObject(
                        name = "Novo notebook",
                        value = """
                            {
                              "serialNumber": "NB002DEF456",
                              "numeroPatrimonio": "PAT002",
                              "marca": "Lenovo",
                              "modelo": "ThinkPad E14",
                              "estadoConservacao": "NOVO",
                              "status": "DISPONIVEL",
                              "termoResponsabilidade": false,
                              "notaFiscal": "NF789012",
                              "statusPropriedade": "PROPRIO",
                              "processador": "Intel Core i5-10210U",
                              "memoriaRam": "8GB DDR4",
                              "armazenamento": "256GB SSD",
                              "sistemaOperacional": "Windows 11 Pro"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody NotebookCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createNotebook(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/desktop
     * Cria um novo desktop
     */
    @PostMapping("/desktop")
    @Operation(
        summary = "Criar novo desktop",
        description = "Cria um novo desktop no sistema. O número de série deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Desktop criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou número de série já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> createDesktop(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do desktop a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DesktopCreateDTO.class)
                )
            )
            @Valid @RequestBody DesktopCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createDesktop(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/celular
     * Cria um novo celular
     */
    @PostMapping("/celular")
    @Operation(
        summary = "Criar novo celular",
        description = "Cria um novo celular no sistema. O número de série deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Celular criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou número de série já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> createCelular(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do celular a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CelularCreateDTO.class)
                )
            )
            @Valid @RequestBody CelularCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createCelular(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/chip
     * Cria um novo chip
     */
    @PostMapping("/chip")
    @Operation(
        summary = "Criar novo chip",
        description = "Cria um novo chip no sistema. O número de linha deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Chip criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou número de linha já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> createChip(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do chip a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChipCreateDTO.class)
                )
            )
            @Valid @RequestBody ChipCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createChip(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/impressora
     * Cria uma nova impressora
     */
    @PostMapping("/impressora")
    @Operation(
        summary = "Criar nova impressora",
        description = "Cria uma nova impressora no sistema. O número de série deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Impressora criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou número de série já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> createImpressora(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados da impressora a ser criada",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ImpressoraCreateDTO.class)
                )
            )
            @Valid @RequestBody ImpressoraCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createImpressora(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    /**
     * POST /api/equipamentos/monitor
     * Cria um novo monitor
     */
    @PostMapping("/monitor")
    @Operation(
        summary = "Criar novo monitor",
        description = "Cria um novo monitor no sistema. O número de série deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Monitor criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou número de série já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar equipamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> createMonitor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do monitor a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MonitorCreateDTO.class)
                )
            )
            @Valid @RequestBody MonitorCreateDTO createDTO) {
        EquipamentoResponseDTO equipamento = equipamentoService.createMonitor(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamento);
    }

    // ==================== ENDPOINTS DE ATUALIZAÇÃO ESPECÍFICOS (PUT) ====================

    /**
     * PUT /api/equipamentos/notebook/{id}
     * Atualiza um notebook existente
     */
    @PutMapping("/notebook/{id}")
    @Operation(
        summary = "Atualizar notebook existente",
        description = "Atualiza completamente os dados de um notebook existente. Todos os campos obrigatórios devem ser fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Notebook atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EquipamentoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar equipamentos"),
        @ApiResponse(responseCode = "404", description = "Notebook não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EquipamentoResponseDTO> updateNotebook(
            @Parameter(description = "ID único do notebook a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody NotebookUpdateDTO updateDTO) {
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
    @Operation(
        summary = "Excluir equipamento",
        description = "Remove um equipamento do sistema. ATENÇÃO: Esta operação não pode ser desfeita. " +
                     "Verifique se o equipamento não está em uso antes de excluir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Equipamento excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Equipamento está em uso e não pode ser excluído"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir equipamentos"),
        @ApiResponse(responseCode = "404", description = "Equipamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único do equipamento a ser excluído", required = true, example = "1")
            @PathVariable Long id) {
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

    @GetMapping("/filtrar")
    public ResponseEntity<PagedResponseDTO<EquipamentoResponseDTO>> filtrarEquipamentos(
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) String numeroPatrimonio,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String estadoConservacao,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean termoResponsabilidade,
            @RequestParam(required = false) String notaFiscal,
            @RequestParam(required = false) String statusPropriedade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<EquipamentoResponseDTO> equipamentos = equipamentoService.filtrarEquipamentos(
            serialNumber, numeroPatrimonio, marca, modelo, estadoConservacao,
            status, termoResponsabilidade, notaFiscal, statusPropriedade, pageable
        );
        return ResponseEntity.ok(equipamentos);
    }
}
