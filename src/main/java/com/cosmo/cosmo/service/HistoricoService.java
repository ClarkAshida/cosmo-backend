package com.cosmo.cosmo.service;

import com.cosmo.cosmo.controller.HistoricoController;
import com.cosmo.cosmo.dto.*;
import com.cosmo.cosmo.entity.Historico;
import com.cosmo.cosmo.entity.equipamento.Equipamento;
import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.mapper.HistoricoMapper;
import com.cosmo.cosmo.repository.HistoricoRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    public PagedResponseDTO<HistoricoResponseDTO> findAll(Pageable pageable) {
        Page<Historico> page = historicoRepository.findAll(pageable);

        List<HistoricoResponseDTO> historicos = page.getContent()
                .stream()
                .map(historico -> {
                    HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
                    return addHateoasLinksWithNestedEntities(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<HistoricoResponseDTO>> embedded = new HashMap<>();
        embedded.put("historicos", historicos);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<HistoricoResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL
        addPaginationLinks(response, pageable, page);

        return response;
    }

    public PagedResponseDTO<HistoricoResponseDTO> findByUsuarioId(Long usuarioId, Pageable pageable) {
        Page<Historico> page = historicoRepository.findByUsuarioId(usuarioId, pageable);

        List<HistoricoResponseDTO> historicos = page.getContent()
                .stream()
                .filter(h -> h.getStatusRegistroHistorico()) // Só históricos ativos
                .map(historico -> {
                    HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
                    return addHateoasLinksWithNestedEntities(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<HistoricoResponseDTO>> embedded = new HashMap<>();
        embedded.put("historicos", historicos);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<HistoricoResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL específicos para usuário
        addPaginationLinksByUsuario(response, pageable, page, usuarioId);

        return response;
    }

    public PagedResponseDTO<HistoricoResponseDTO> findByEquipamentoId(Long equipamentoId, Pageable pageable) {
        Page<Historico> page = historicoRepository.findByEquipamentoId(equipamentoId, pageable);

        List<HistoricoResponseDTO> historicos = page.getContent()
                .stream()
                .filter(h -> h.getStatusRegistroHistorico()) // Só históricos ativos
                .map(historico -> {
                    HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
                    return addHateoasLinksWithNestedEntities(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<HistoricoResponseDTO>> embedded = new HashMap<>();
        embedded.put("historicos", historicos);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<HistoricoResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL específicos para equipamento
        addPaginationLinksByEquipamento(response, pageable, page, equipamentoId);

        return response;
    }

    public List<HistoricoResponseDTO> findAll() {
        return historicoRepository.findAll()
                .stream()
                .map(historico -> {
                    HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
                    return addHateoasLinks(dto);
                })
                .collect(Collectors.toList());
    }

    public HistoricoResponseDTO findById(Long id) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));
        HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
        return addHateoasLinks(dto);
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

        HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
        return addHateoasLinks(dto);
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

        HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
        return addHateoasLinks(dto);
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
        HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
        return addHateoasLinks(dto);
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
        HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
        return addHateoasLinks(dto);
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
                .map(historico -> {
                    HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
                    return addHateoasLinks(dto);
                })
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> findByEquipamentoId(Long equipamentoId) {
        return historicoRepository.findByEquipamentoId(equipamentoId)
                .stream()
                .filter(h -> h.getStatusRegistroHistorico()) // Só históricos ativos
                .map(historico -> {
                    HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historico);
                    return addHateoasLinks(dto);
                })
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

        HistoricoResponseDTO dto = historicoMapper.toResponseDTO(historicoAtivo);
        return addHateoasLinks(dto);
    }

    // ==================== MÉTODOS PARA OPERAÇÕES MÚLTIPLAS ====================

    /**
     * Realiza a entrega de múltiplos equipamentos para um usuário
     * Todos os equipamentos devem estar disponíveis para que a operação seja realizada
     * Se algum equipamento não estiver disponível, toda a transação é cancelada (rollback)
     */
    @Transactional
    public OperacaoMultiplaResponseDTO entregarMultiplosEquipamentos(EntregaMultiplaDTO entregaMultiplaDTO) {
        List<HistoricoResponseDTO> historicosProcessados = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        // Validar se o usuário existe
        Usuario usuario = usuarioService.findEntityById(entregaMultiplaDTO.getUsuarioId());

        // Pré-validação: verificar se todos os equipamentos estão disponíveis
        List<Equipamento> equipamentos = new ArrayList<>();
        for (Long equipamentoId : entregaMultiplaDTO.getEquipamentoIds()) {
            try {
                Equipamento equipamento = equipamentoService.findEntityById(equipamentoId);

                // Validar status do equipamento
                if (equipamento.getStatus() == StatusEquipamento.EM_USO) {
                    throw new ValidationException("Equipamento ID " + equipamentoId + " já está em uso");
                }

                if (equipamento.getStatus() == StatusEquipamento.DANIFICADO) {
                    throw new ValidationException("Equipamento ID " + equipamentoId + " está danificado e não pode ser entregue");
                }

                equipamentos.add(equipamento);

            } catch (Exception e) {
                erros.add("Equipamento ID " + equipamentoId + ": " + e.getMessage());
            }
        }

        // Se houver erros na validação, não processa nenhum item
        if (!erros.isEmpty()) {
            OperacaoMultiplaResponseDTO resultado = new OperacaoMultiplaResponseDTO(
                entregaMultiplaDTO.getEquipamentoIds().size(),
                0,
                erros.size(),
                historicosProcessados,
                erros,
                "Operação cancelada devido a erros de validação. Nenhum equipamento foi entregue."
            );
            return addHateoasLinksToOperacaoMultipla(resultado, "entrega");
        }

        // Processar cada equipamento individualmente
        for (Equipamento equipamento : equipamentos) {
            try {
                // Criar HistoricoRequestDTO para cada equipamento
                HistoricoRequestDTO requestDTO = new HistoricoRequestDTO();
                requestDTO.setEquipamentoId(equipamento.getId());
                requestDTO.setUsuarioId(entregaMultiplaDTO.getUsuarioId());
                requestDTO.setObservacoesEntrega(entregaMultiplaDTO.getObservacoesEntrega());
                requestDTO.setUrlTermoEntrega(entregaMultiplaDTO.getUrlTermoEntrega());

                // Realizar a entrega
                HistoricoResponseDTO historico = entregarEquipamento(requestDTO);
                historicosProcessados.add(historico);

            } catch (Exception e) {
                erros.add("Erro ao entregar equipamento ID " + equipamento.getId() + ": " + e.getMessage());
            }
        }

        String observacaoGeral = erros.isEmpty()
            ? "Todos os equipamentos foram entregues com sucesso"
            : "Operação concluída com alguns erros";

        OperacaoMultiplaResponseDTO resultado = new OperacaoMultiplaResponseDTO(
            entregaMultiplaDTO.getEquipamentoIds().size(),
            historicosProcessados.size(),
            erros.size(),
            historicosProcessados,
            erros,
            observacaoGeral
        );

        return addHateoasLinksToOperacaoMultipla(resultado, "entrega");
    }

    /**
     * Realiza a devolução de múltiplos equipamentos
     * Permite observações específicas para cada equipamento e URL única do termo de devolução
     */
    @Transactional
    public OperacaoMultiplaResponseDTO devolverMultiplosEquipamentos(DevolucaoMultiplaDTO devolucaoMultiplaDTO) {
        List<HistoricoResponseDTO> historicosProcessados = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        // Pré-validação: verificar se todos os históricos existem e estão válidos para devolução
        List<Historico> historicos = new ArrayList<>();
        for (ItemDevolucaoDTO item : devolucaoMultiplaDTO.getItensDevolvidos()) {
            try {
                Historico historico = historicoRepository.findById(item.getHistoricoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com ID: " + item.getHistoricoId()));

                // Validar se já foi devolvido
                if (historico.getDataDevolucao() != null) {
                    throw new ValidationException("Histórico ID " + item.getHistoricoId() + " já foi devolvido anteriormente");
                }

                // Validar se está ativo
                if (!historico.getStatusRegistroHistorico()) {
                    throw new ValidationException("Histórico ID " + item.getHistoricoId() + " está cancelado");
                }

                // Validar se o novo status é válido
                if (item.getNovoStatus() == StatusEquipamento.EM_USO) {
                    throw new ValidationException("Não é possível devolver equipamento mantendo status EM_USO");
                }

                historicos.add(historico);

            } catch (Exception e) {
                erros.add("Histórico ID " + item.getHistoricoId() + ": " + e.getMessage());
            }
        }

        // Se houver erros na validação, não processa nenhum item
        if (!erros.isEmpty()) {
            OperacaoMultiplaResponseDTO resultado = new OperacaoMultiplaResponseDTO(
                devolucaoMultiplaDTO.getItensDevolvidos().size(),
                0,
                erros.size(),
                historicosProcessados,
                erros,
                "Operação cancelada devido a erros de validação. Nenhum equipamento foi devolvido."
            );
            return addHateoasLinksToOperacaoMultipla(resultado, "devolucao");
        }

        // Processar cada devolução individualmente
        for (int i = 0; i < historicos.size(); i++) {
            Historico historico = historicos.get(i);
            ItemDevolucaoDTO item = devolucaoMultiplaDTO.getItensDevolvidos().get(i);

            try {
                // Atualizar dados da devolução
                historico.setDataDevolucao(LocalDateTime.now());
                historico.setObservacoesDevolucao(item.getObservacoesDevolucao());
                historico.setUrlTermoDevolucao(devolucaoMultiplaDTO.getUrlTermoDevolucao()); // URL única para todos

                // Definir status final (padrão: DISPONIVEL)
                StatusEquipamento statusFinal = item.getNovoStatus() != null ? item.getNovoStatus() : StatusEquipamento.DISPONIVEL;

                // Salvar histórico e atualizar status do equipamento
                historico = historicoRepository.save(historico);
                equipamentoService.updateEntityStatus(historico.getEquipamento().getId(), statusFinal);

                HistoricoResponseDTO historicoResponse = historicoMapper.toResponseDTO(historico);
                historicoResponse = addHateoasLinks(historicoResponse);
                historicosProcessados.add(historicoResponse);

            } catch (Exception e) {
                erros.add("Erro ao devolver equipamento (Histórico ID " + item.getHistoricoId() + "): " + e.getMessage());
            }
        }

        String observacaoGeral = erros.isEmpty()
            ? "Todos os equipamentos foram devolvidos com sucesso"
            : "Operação concluída com alguns erros";

        OperacaoMultiplaResponseDTO resultado = new OperacaoMultiplaResponseDTO(
            devolucaoMultiplaDTO.getItensDevolvidos().size(),
            historicosProcessados.size(),
            erros.size(),
            historicosProcessados,
            erros,
            observacaoGeral
        );

        return addHateoasLinksToOperacaoMultipla(resultado, "devolucao");
    }

    // ==================== MÉTODOS AUXILIARES PARA HATEOAS ====================

    private HistoricoResponseDTO addHateoasLinks(HistoricoResponseDTO dto) {
        Long id = dto.getId();

        // Link para si mesmo
        dto.add(linkTo(methodOn(HistoricoController.class).getHistoricoById(id)).withSelfRel());

        // Link para listar todos os históricos
        dto.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(0, 10, "id", "desc")).withRel("historicos"));

        // Links baseados no estado do histórico
        if (dto.getStatusRegistroHistorico()) {
            // Histórico ativo - permite edição
            dto.add(linkTo(methodOn(HistoricoController.class).editarHistorico(id, null)).withRel("editar"));

            if (dto.getDataDevolucao() == null) {
                // Não foi devolvido ainda - permite devolução e cancelamento
                dto.add(linkTo(methodOn(HistoricoController.class).devolverEquipamento(id, null)).withRel("devolver"));
                dto.add(linkTo(methodOn(HistoricoController.class).cancelarHistorico(id, null)).withRel("cancelar"));
            }
        }

        // Links para operações múltiplas
        dto.add(linkTo(methodOn(HistoricoController.class).entregarMultiplosEquipamentos(null)).withRel("entrega-multipla"));
        dto.add(linkTo(methodOn(HistoricoController.class).devolverMultiplosEquipamentos(null)).withRel("devolucao-multipla"));

        return dto;
    }

    private OperacaoMultiplaResponseDTO addHateoasLinksToOperacaoMultipla(OperacaoMultiplaResponseDTO dto, String tipoOperacao) {
        // Link para listar todos os históricos
        dto.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(0, 10, "id", "desc")).withRel("historicos"));

        // Links para operações múltiplas
        dto.add(linkTo(methodOn(HistoricoController.class).entregarMultiplosEquipamentos(null)).withRel("entrega-multipla"));
        dto.add(linkTo(methodOn(HistoricoController.class).devolverMultiplosEquipamentos(null)).withRel("devolucao-multipla"));

        // Link para nova operação do mesmo tipo
        if ("entrega".equals(tipoOperacao)) {
            dto.add(linkTo(methodOn(HistoricoController.class).entregarEquipamento(null)).withRel("nova-entrega"));
        } else if ("devolucao".equals(tipoOperacao)) {
            dto.add(linkTo(methodOn(HistoricoController.class).devolverMultiplosEquipamentos(null)).withRel("nova-devolucao"));
        }

        return dto;
    }

    private HistoricoResponseDTO addHateoasLinksWithNestedEntities(HistoricoResponseDTO dto) {
        // Adicionar links HATEOAS para o histórico principal
        addHateoasLinks(dto);

        // O DTO já vem com os objetos completos do mapper, não precisamos recriar
        // Apenas garantir que tenham os links HATEOAS se necessário

        return dto;
    }

    private void addPaginationLinks(PagedResponseDTO<HistoricoResponseDTO> response, Pageable pageable, Page<?> page) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ? 
            pageable.getSort().iterator().next().getProperty() : "id";
        String sortDir = pageable.getSort().iterator().hasNext() ? 
            (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "desc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(HistoricoController.class)
                    .getAllHistoricos(currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(HistoricoController.class)
                    .getAllHistoricos(currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }
    }

    private void addPaginationLinksByUsuario(PagedResponseDTO<HistoricoResponseDTO> response, Pageable pageable, Page<?> page, Long usuarioId) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ? 
            pageable.getSort().iterator().next().getProperty() : "dataEntrega";
        String sortDir = pageable.getSort().iterator().hasNext() ? 
            (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "desc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(HistoricoController.class)
                .getHistoricosByUsuario(usuarioId, currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(HistoricoController.class)
                .getHistoricosByUsuario(usuarioId, 0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(HistoricoController.class)
                .getHistoricosByUsuario(usuarioId, page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(HistoricoController.class)
                    .getHistoricosByUsuario(usuarioId, currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(HistoricoController.class)
                    .getHistoricosByUsuario(usuarioId, currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }

        // Link para todos os históricos
        response.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(0, 10, "id", "desc")).withRel("todos-historicos"));
    }

    private void addPaginationLinksByEquipamento(PagedResponseDTO<HistoricoResponseDTO> response, Pageable pageable, Page<?> page, Long equipamentoId) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ?
            pageable.getSort().iterator().next().getProperty() : "dataEntrega";
        String sortDir = pageable.getSort().iterator().hasNext() ?
            (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "desc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(HistoricoController.class)
                .getHistoricosByEquipamento(equipamentoId, currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(HistoricoController.class)
                .getHistoricosByEquipamento(equipamentoId, 0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(HistoricoController.class)
                .getHistoricosByEquipamento(equipamentoId, page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(HistoricoController.class)
                    .getHistoricosByEquipamento(equipamentoId, currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(HistoricoController.class)
                    .getHistoricosByEquipamento(equipamentoId, currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }

        // Link para todos os históricos
        response.add(linkTo(methodOn(HistoricoController.class)
                .getAllHistoricos(0, 10, "id", "desc")).withRel("todos-historicos"));
    }
}
