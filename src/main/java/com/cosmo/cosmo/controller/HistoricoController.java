/*
 * Copyright 2025 Flávio Alexandre Orrico Severiano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.dto.historico.*;
import com.cosmo.cosmo.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/historicos")
@CrossOrigin(origins = "*")
@Tag(name = "Históricos", description = "Gerenciamento de históricos de empréstimo de equipamentos. Controla a entrega, devolução e rastreamento de equipamentos emprestados aos usuários, incluindo operações múltiplas e relatórios detalhados.")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    // ==================== MÉTODOS CRUD BÁSICOS COM PAGINAÇÃO ====================

    @GetMapping
    @Operation(
        summary = "Listar todos os históricos",
        description = "Retorna uma lista paginada de todos os históricos de empréstimo de equipamentos cadastrados no sistema. " +
                     "Suporta ordenação por qualquer campo e paginação configurável."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de históricos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class),
                examples = @ExampleObject(
                    name = "Lista paginada de históricos",
                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "dataEntrega": "2025-09-10",
                              "dataDevolucao": null,
                              "observacoesEntrega": "Equipamento entregue em perfeitas condições",
                              "observacoesDevolucao": null,
                              "urlTermoEntrega": "https://storage.cosmo.com/termo123.pdf",
                              "urlTermoDevolucao": null,
                              "statusRegistroHistorico": "ATIVO",
                              "usuario": {
                                "id": 1,
                                "nome": "João Silva"
                              },
                              "equipamento": {
                                "id": 1,
                                "serialNumber": "NB001ABC123",
                                "marca": "Dell",
                                "modelo": "Inspiron 15"
                              }
                            }
                          ],
                          "totalElements": 200,
                          "totalPages": 20,
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
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar históricos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<HistoricoResponseDTO>> getAllHistoricos(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "desc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<HistoricoResponseDTO> historicos = historicoService.findAll(pageable);
        return ResponseEntity.ok(historicos);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar histórico por ID",
        description = "Retorna os detalhes de um histórico específico baseado no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Histórico encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HistoricoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar históricos"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<HistoricoResponseDTO> getHistoricoById(
            @Parameter(description = "ID único do histórico", required = true, example = "1")
            @PathVariable Long id) {
        HistoricoResponseDTO historico = historicoService.findById(id);
        return ResponseEntity.ok(historico);
    }

    @PatchMapping("/{id}")
    @Operation(
        summary = "Editar histórico existente",
        description = "Edita apenas campos permitidos de um histórico (observações e URL de entrega). " +
                     "Não permite alterar equipamento, usuário ou dados de devolução."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Histórico editado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HistoricoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para editar históricos"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<HistoricoResponseDTO> editarHistorico(
            @Parameter(description = "ID único do histórico a ser editado", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados a serem atualizados no histórico",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = HistoricoUpdateDTO.class),
                    examples = @ExampleObject(
                        name = "Atualização de histórico",
                        value = """
                            {
                              "observacoesEntrega": "Observações atualizadas sobre a entrega",
                              "urlTermoEntrega": "https://storage.cosmo.com/termo-atualizado.pdf"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody HistoricoUpdateDTO updateDTO) {
        HistoricoResponseDTO historico = historicoService.updateHistorico(
                id,
                updateDTO.getObservacoesEntrega(),
                updateDTO.getUrlTermoEntrega()
        );
        return ResponseEntity.ok(historico);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(
        summary = "Cancelar histórico",
        description = "Cancela um histórico permanentemente. Só permite cancelar históricos sem devolução. " +
                     "Reverte o equipamento para status DISPONIVEL."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Histórico cancelado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HistoricoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Histórico não pode ser cancelado (já devolvido ou outros motivos)"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para cancelar históricos"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<HistoricoResponseDTO> cancelarHistorico(
            @Parameter(description = "ID único do histórico a ser cancelado", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Motivo do cancelamento",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CancelamentoHistoricoDTO.class),
                    examples = @ExampleObject(
                        name = "Cancelamento de histórico",
                        value = """
                            {
                              "motivoCancelamento": "Equipamento devolvido antecipadamente sem registro formal"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody CancelamentoHistoricoDTO cancelamentoDTO) {
        HistoricoResponseDTO historico = historicoService.cancelarHistorico(
                id,
                cancelamentoDTO.getMotivoCancelamento()
        );
        return ResponseEntity.ok(historico);
    }

    // ==================== MÉTODOS ESPECÍFICOS DE NEGÓCIO ====================

    @PostMapping("/entregar")
    @Operation(
        summary = "Entregar equipamento",
        description = "Realiza a entrega de um equipamento para um usuário. " +
                     "Automaticamente altera o status do equipamento para EM_USO e cria um novo registro de histórico."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Equipamento entregue com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HistoricoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou equipamento não disponível para entrega"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para entregar equipamentos"),
        @ApiResponse(responseCode = "404", description = "Equipamento ou usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<HistoricoResponseDTO> entregarEquipamento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados da entrega do equipamento",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EntregaEquipamentoDTO.class),
                    examples = @ExampleObject(
                        name = "Entrega de equipamento",
                        value = """
                            {
                              "equipamentoId": 1,
                              "usuarioId": 1,
                              "observacoesEntrega": "Equipamento entregue em perfeito estado. Orientado sobre cuidados básicos.",
                              "urlTermoEntrega": "https://storage.cosmo.com/termos/termo-entrega-123.pdf"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody EntregaEquipamentoDTO entregaDTO) {

        HistoricoResponseDTO historico = historicoService.entregarEquipamentoComDTO(entregaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(historico);
    }

    @PatchMapping("/{historicoId}/devolver")
    @Operation(
        summary = "Devolver equipamento",
        description = "Realiza a devolução de um equipamento. Atualiza o histórico existente e permite escolher o novo status do equipamento " +
                     "(DISPONIVEL, MANUTENCAO, etc.)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Equipamento devolvido com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HistoricoResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou equipamento já devolvido"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para processar devoluções"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<HistoricoResponseDTO> devolverEquipamento(
            @Parameter(description = "ID único do histórico de empréstimo", required = true, example = "1")
            @PathVariable Long historicoId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados da devolução do equipamento",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DevolucaoEquipamentoDTO.class),
                    examples = @ExampleObject(
                        name = "Devolução de equipamento",
                        value = """
                            {
                              "observacoesDevolucao": "Equipamento devolvido em bom estado, sem avarias visíveis.",
                              "urlTermoDevolucao": "https://storage.cosmo.com/termos/termo-devolucao-123.pdf",
                              "novoStatus": "DISPONIVEL"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody DevolucaoEquipamentoDTO devolucaoDTO) {

        HistoricoResponseDTO historico = historicoService.devolverEquipamento(
                historicoId,
                devolucaoDTO.getObservacoesDevolucao(),
                devolucaoDTO.getUrlTermoDevolucao(),
                devolucaoDTO.getNovoStatus()
        );
        return ResponseEntity.ok(historico);
    }

    // ==================== MÉTODOS DE CONSULTA COM PAGINAÇÃO ====================

    /**
     * Busca históricos por usuário (apenas ativos) com paginação
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PagedResponseDTO<HistoricoResponseDTO>> getHistoricosByUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataEntrega") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<HistoricoResponseDTO> historicos = historicoService.findByUsuarioId(usuarioId, pageable);
        return ResponseEntity.ok(historicos);
    }

    /**
     * Busca históricos por equipamento (apenas ativos) com paginação
     */
    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<PagedResponseDTO<HistoricoResponseDTO>> getHistoricosByEquipamento(
            @PathVariable Long equipamentoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataEntrega") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<HistoricoResponseDTO> historicos = historicoService.findByEquipamentoId(equipamentoId, pageable);
        return ResponseEntity.ok(historicos);
    }

    /**
     * Verifica se um equipamento está em uso
     */
    @GetMapping("/equipamento/{equipamentoId}/em-uso")
    public ResponseEntity<Boolean> isEquipamentoEmUso(@PathVariable Long equipamentoId) {
        boolean emUso = historicoService.isEquipamentoEmUso(equipamentoId);
        return ResponseEntity.ok(emUso);
    }

    /**
     * Busca o histórico ativo (em uso) de um equipamento
     */
    @GetMapping("/equipamento/{equipamentoId}/ativo")
    public ResponseEntity<HistoricoResponseDTO> getHistoricoAtivoByEquipamento(@PathVariable Long equipamentoId) {
        HistoricoResponseDTO historico = historicoService.findHistoricoAtivoByEquipamento(equipamentoId);
        return ResponseEntity.ok(historico);
    }

    // ==================== ENDPOINTS PARA OPERAÇÕES MÚLTIPLAS ====================

    /**
     * POST /api/historicos/entregar-multiplos
     * Realiza a entrega de múltiplos equipamentos para um usuário
     * Cada equipamento terá seu próprio registro de histórico individual
     */
    @PostMapping("/entregar-multiplos")
    public ResponseEntity<OperacaoMultiplaResponseDTO> entregarMultiplosEquipamentos(
            @Valid @RequestBody EntregaMultiplaDTO entregaMultiplaDTO) {

        OperacaoMultiplaResponseDTO resultado = historicoService.entregarMultiplosEquipamentos(entregaMultiplaDTO);

        // Se todos falharam, retorna BAD_REQUEST
        if (resultado.getItensSucesso() == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        // Se alguns falharam, retorna PARTIAL_CONTENT (207)
        if (resultado.getItensErro() > 0) {
            return ResponseEntity.status(207).body(resultado); // 207 Multi-Status
        }

        // Se todos tiveram sucesso, retorna CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    /**
     * PATCH /api/historicos/devolver-multiplos
     * Realiza a devolução de múltiplos equipamentos
     * Permite observações específicas para cada equipamento e URL única do termo
     */
    @PatchMapping("/devolver-multiplos")
    public ResponseEntity<OperacaoMultiplaResponseDTO> devolverMultiplosEquipamentos(
            @Valid @RequestBody DevolucaoMultiplaDTO devolucaoMultiplaDTO) {
        OperacaoMultiplaResponseDTO resultado = historicoService.devolverMultiplosEquipamentos(devolucaoMultiplaDTO);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<PagedResponseDTO<HistoricoResponseDTO>> filtrarHistoricos(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Long equipamentoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntregaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntregaFim,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDevolucaoInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDevolucaoFim,
            @RequestParam(required = false) String statusRegistroHistorico,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<HistoricoResponseDTO> historicos = historicoService.filtrarHistoricos(
                usuarioId, equipamentoId, dataEntregaInicio, dataEntregaFim,
                dataDevolucaoInicio, dataDevolucaoFim, statusRegistroHistorico, pageable
        );
        return ResponseEntity.ok(historicos);
    }
}
