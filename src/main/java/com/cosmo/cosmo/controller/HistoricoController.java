package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.*;
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

    // ==================== MÉTODOS CRUD BÁSICOS ====================

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

    /**
     * Edita apenas campos permitidos de um histórico (observações e URL de entrega)
     * Não permite alterar equipamento, usuário ou dados de devolução
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HistoricoResponseDTO> editarHistorico(
            @PathVariable Long id,
            @Valid @RequestBody HistoricoUpdateDTO updateDTO) {
        HistoricoResponseDTO historico = historicoService.updateHistorico(
                id,
                updateDTO.getObservacoesEntrega(),
                updateDTO.getUrlTermoEntrega()
        );
        return ResponseEntity.ok(historico);
    }

    /**
     * Cancela um histórico permanentemente (substitui o DELETE)
     * Só permite cancelar históricos sem devolução
     * Reverte equipamento para DISPONIVEL
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<HistoricoResponseDTO> cancelarHistorico(
            @PathVariable Long id,
            @Valid @RequestBody CancelamentoHistoricoDTO cancelamentoDTO) {
        HistoricoResponseDTO historico = historicoService.cancelarHistorico(
                id,
                cancelamentoDTO.getMotivoCancelamento()
        );
        return ResponseEntity.ok(historico);
    }

    // ==================== MÉTODOS ESPECÍFICOS DE NEGÓCIO ====================

    /**
     * Realiza a entrega de um equipamento para um usuário
     * Automaticamente altera o status do equipamento para EM_USO
     */
    @PostMapping("/entregar")
    public ResponseEntity<HistoricoResponseDTO> entregarEquipamento(
            @Valid @RequestBody EntregaEquipamentoDTO entregaDTO) {

        // Lógica de conversão movida para o service
        HistoricoResponseDTO historico = historicoService.entregarEquipamentoComDTO(entregaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(historico);
    }

    /**
     * Realiza a devolução de um equipamento
     * Atualiza o histórico existente e permite escolher o novo status do equipamento
     */
    @PatchMapping("/{historicoId}/devolver")
    public ResponseEntity<HistoricoResponseDTO> devolverEquipamento(
            @PathVariable Long historicoId,
            @Valid @RequestBody DevolucaoEquipamentoDTO devolucaoDTO) {

        HistoricoResponseDTO historico = historicoService.devolverEquipamento(
                historicoId,
                devolucaoDTO.getObservacoesDevolucao(),
                devolucaoDTO.getUrlTermoDevolucao(),
                devolucaoDTO.getNovoStatus()
        );
        return ResponseEntity.ok(historico);
    }

    // ==================== MÉTODOS DE CONSULTA ====================

    /**
     * Busca históricos por usuário (apenas ativos)
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistoricoResponseDTO>> getHistoricosByUsuario(@PathVariable Long usuarioId) {
        List<HistoricoResponseDTO> historicos = historicoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(historicos);
    }

    /**
     * Busca históricos por equipamento (apenas ativos)
     */
    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<List<HistoricoResponseDTO>> getHistoricosByEquipamento(@PathVariable Long equipamentoId) {
        List<HistoricoResponseDTO> historicos = historicoService.findByEquipamentoId(equipamentoId);
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

        // Se todos falharam, retorna BAD_REQUEST
        if (resultado.getItensSucesso() == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        // Se alguns falharam, retorna PARTIAL_CONTENT (207)
        if (resultado.getItensErro() > 0) {
            return ResponseEntity.status(207).body(resultado); // 207 Multi-Status
        }

        // Se todos tiveram sucesso, retorna OK
        return ResponseEntity.ok(resultado);
    }
}
