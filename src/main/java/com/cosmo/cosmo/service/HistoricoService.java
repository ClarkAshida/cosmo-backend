package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.HistoricoRequestDTO;
import com.cosmo.cosmo.dto.HistoricoResponseDTO;
import com.cosmo.cosmo.dto.EntregaEquipamentoDTO;
import com.cosmo.cosmo.entity.Historico;
import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.mapper.HistoricoMapper;
import com.cosmo.cosmo.repository.HistoricoRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private HistoricoMapper historicoMapper;

    @Autowired
    private EquipamentoService equipamentoService;

    @Autowired
    private UsuarioService usuarioService;

    public List<HistoricoResponseDTO> findAll() {
        return historicoRepository.findAll()
                .stream()
                .map(historicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public HistoricoResponseDTO findById(Long id) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));
        return historicoMapper.toResponseDTO(historico);
    }

    /**
     * Registra a entrega de um equipamento usando EntregaEquipamentoDTO
     * Abstrai a conversão do DTO e chama o método principal de entrega
     */
    @Transactional
    public HistoricoResponseDTO entregarEquipamentoComDTO(EntregaEquipamentoDTO entregaDTO) {
        // Converter EntregaEquipamentoDTO para HistoricoRequestDTO (regra de negócio no service)
        HistoricoRequestDTO requestDTO = new HistoricoRequestDTO();
        requestDTO.setEquipamentoId(entregaDTO.getEquipamentoId());
        requestDTO.setUsuarioId(entregaDTO.getUsuarioId());
        requestDTO.setObservacoesEntrega(entregaDTO.getObservacoesEntrega());
        requestDTO.setUrlTermoEntrega(entregaDTO.getUrlTermoEntrega());

        return entregarEquipamento(requestDTO);
    }

    /**
     * Registra a entrega de um equipamento para um usuário
     * Automaticamente altera o status do equipamento para EM_USO
     */
    @Transactional
    public HistoricoResponseDTO entregarEquipamento(HistoricoRequestDTO requestDTO) {
        Equipamento equipamento = equipamentoService.findEntityById(requestDTO.getEquipamentoId());
        Usuario usuario = usuarioService.findEntityById(requestDTO.getUsuarioId());

        // Validar se o equipamento está disponível para entrega
        if (equipamento.getStatus() == StatusEquipamento.EM_USO) {
            throw new ValidationException("Equipamento já está em uso e não pode ser entregue");
        }

        if (equipamento.getStatus() == StatusEquipamento.DANIFICADO) {
            throw new ValidationException("Equipamento danificado não pode ser entregue");
        }

        // Criar o histórico de entrega
        Historico historico = historicoMapper.toEntity(requestDTO, equipamento, usuario);
        historico.setDataEntrega(LocalDateTime.now());

        // Alterar status do equipamento para EM_USO
        equipamento.setStatus(StatusEquipamento.EM_USO);

        // Salvar histórico e atualizar equipamento através do repository
        historico = historicoRepository.save(historico);
        equipamentoService.updateEntityStatus(equipamento.getId(), StatusEquipamento.EM_USO);

        return historicoMapper.toResponseDTO(historico);
    }

    /**
     * Registra a devolução de um equipamento
     * Permite definir o novo status do equipamento (por padrão DISPONIVEL)
     */
    @Transactional
    public HistoricoResponseDTO devolverEquipamento(Long historicoId,
                                                   String observacoesDevolucao,
                                                   String urlTermoDevolucao,
                                                   StatusEquipamento novoStatus) {

        Historico historico = historicoRepository.findById(historicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + historicoId));

        // Validar se o equipamento realmente está em uso
        if (historico.getDataDevolucao() != null) {
            throw new ValidationException("Equipamento já foi devolvido anteriormente");
        }

        // Validar se o novo status é válido para devolução
        if (novoStatus == StatusEquipamento.EM_USO) {
            throw new ValidationException("Não é possível devolver um equipamento mantendo status EM_USO");
        }

        // Atualizar dados da devolução
        historico.setDataDevolucao(LocalDateTime.now());
        historico.setObservacoesDevolucao(observacoesDevolucao);
        historico.setUrlTermoDevolucao(urlTermoDevolucao);

        // Definir status final (padrão: DISPONIVEL)
        StatusEquipamento statusFinal = novoStatus != null ? novoStatus : StatusEquipamento.DISPONIVEL;

        // Salvar histórico e atualizar status do equipamento
        historico = historicoRepository.save(historico);
        equipamentoService.updateEntityStatus(historico.getEquipamento().getId(), statusFinal);

        return historicoMapper.toResponseDTO(historico);
    }

    /**
     * Método simplificado para devolução (status padrão: DISPONIVEL)
     */
    @Transactional
    public HistoricoResponseDTO devolverEquipamento(Long historicoId,
                                                   String observacoesDevolucao,
                                                   String urlTermoDevolucao) {
        return devolverEquipamento(historicoId, observacoesDevolucao, urlTermoDevolucao, StatusEquipamento.DISPONIVEL);
    }

    /**
     * Cancela um histórico permanentemente
     * Só permite cancelar históricos que ainda não foram devolvidos
     * Reverte o equipamento para status DISPONIVEL
     */
    @Transactional
    public HistoricoResponseDTO cancelarHistorico(Long id, String motivoCancelamento) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));

        // Validar se o histórico já foi cancelado
        if (!historico.getStatusRegistroHistorico()) {
            throw new ValidationException("Este histórico já foi cancelado anteriormente");
        }

        // Validar se o histórico já teve devolução
        if (historico.getDataDevolucao() != null) {
            throw new ValidationException("Não é possível cancelar um histórico que já teve devolução");
        }

        // Cancelar o histórico
        historico.setStatusRegistroHistorico(false);
        historico.setMotivoCancelamento(motivoCancelamento);
        historico.setDataCancelamento(LocalDateTime.now());

        // Reverter status do equipamento para DISPONIVEL
        equipamentoService.updateEntityStatus(historico.getEquipamento().getId(), StatusEquipamento.DISPONIVEL);

        historico = historicoRepository.save(historico);
        return historicoMapper.toResponseDTO(historico);
    }

    /**
     * Atualiza apenas informações limitadas do histórico (observações e URL de entrega)
     * Não permite alterar equipamento, usuário ou dados de devolução
     */
    public HistoricoResponseDTO updateHistorico(Long id, String observacoesEntrega, String urlTermoEntrega) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));

        // Validar se o histórico está ativo
        if (!historico.getStatusRegistroHistorico()) {
            throw new ValidationException("Não é possível editar um histórico cancelado");
        }

        // Atualizar apenas campos permitidos
        historico.setObservacoesEntrega(observacoesEntrega);
        historico.setUrlTermoEntrega(urlTermoEntrega);

        historico = historicoRepository.save(historico);
        return historicoMapper.toResponseDTO(historico);
    }

    /**
     * Método legado - mantido para compatibilidade, mas agora usa entregarEquipamento
     */
    @Deprecated
    public HistoricoResponseDTO save(HistoricoRequestDTO requestDTO) {
        return entregarEquipamento(requestDTO);
    }

    /**
     * Método update legado - removido para usar novo método updateHistorico
     */
    @Deprecated
    public HistoricoResponseDTO update(Long id, HistoricoRequestDTO requestDTO) {
        throw new ValidationException("Método de atualização descontinuado. Use as rotas específicas de edição.");
    }

    /**
     * Método delete legado - removido para usar cancelamento
     */
    @Deprecated
    public void deleteById(Long id) {
        throw new ValidationException("Não é possível deletar históricos. Use o cancelamento para registros incorretos.");
    }

    // Métodos de consulta específicos - atualizados para considerar status ativo
    public List<HistoricoResponseDTO> findByUsuarioId(Long usuarioId) {
        return historicoRepository.findByUsuarioId(usuarioId)
                .stream()
                .filter(h -> h.getStatusRegistroHistorico()) // Só históricos ativos
                .map(historicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> findByEquipamentoId(Long equipamentoId) {
        return historicoRepository.findByEquipamentoId(equipamentoId)
                .stream()
                .filter(h -> h.getStatusRegistroHistorico()) // Só históricos ativos
                .map(historicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifica se um equipamento está atualmente em uso (considera apenas históricos ativos)
     */
    public boolean isEquipamentoEmUso(Long equipamentoId) {
        List<Historico> historicos = historicoRepository.findByEquipamentoId(equipamentoId);
        return historicos.stream()
                .anyMatch(h -> h.getStatusRegistroHistorico() && h.getDataDevolucao() == null);
    }

    /**
     * Busca histórico ativo (sem devolução) de um equipamento
     */
    public HistoricoResponseDTO findHistoricoAtivoByEquipamento(Long equipamentoId) {
        List<Historico> historicos = historicoRepository.findByEquipamentoId(equipamentoId);
        Historico historicoAtivo = historicos.stream()
                .filter(h -> h.getStatusRegistroHistorico() && h.getDataDevolucao() == null)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum histórico ativo encontrado para o equipamento: " + equipamentoId));

        return historicoMapper.toResponseDTO(historicoAtivo);
    }
}
