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

import com.cosmo.cosmo.dto.empresa.EmpresaRequestDTO;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.service.EmpresaService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
@Tag(name = "Empresas", description = "Gerenciamento de empresas do sistema. Permite criar, listar, atualizar e excluir empresas, além de realizar buscas com filtros e paginação.")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    @Operation(
        summary = "Listar todas as empresas",
        description = "Retorna uma lista paginada de todas as empresas cadastradas no sistema. " +
                     "Suporta ordenação por qualquer campo e paginação configurável."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de empresas retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class),
                examples = @ExampleObject(
                    name = "Lista paginada de empresas",
                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "nome": "Empresa Principal Ltda",
                              "cnpj": "12.345.678/0001-90",
                              "endereco": "Rua das Flores, 123, Centro",
                              "telefone": "(11) 98765-4321",
                              "email": "contato@empresa.com.br"
                            },
                            {
                              "id": 2,
                              "nome": "Filial Norte",
                              "cnpj": "12.345.678/0002-71",
                              "endereco": "Av. Norte, 456, Zona Norte",
                              "telefone": "(11) 91234-5678",
                              "email": "norte@empresa.com.br"
                            }
                          ],
                          "totalElements": 15,
                          "totalPages": 2,
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
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar empresas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<EmpresaResponseDTO>> getAllEmpresas(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "nome")
            @RequestParam(defaultValue = "nome") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<EmpresaResponseDTO> empresas = empresaService.findAll(pageable);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar empresa por ID",
        description = "Retorna os detalhes de uma empresa específica baseada no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Empresa encontrada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmpresaResponseDTO.class),
                examples = @ExampleObject(
                    name = "Empresa encontrada",
                    value = """
                        {
                          "id": 1,
                          "nome": "Empresa Principal Ltda",
                          "cnpj": "12.345.678/0001-90",
                          "endereco": "Rua das Flores, 123, Centro",
                          "telefone": "(11) 98765-4321",
                          "email": "contato@empresa.com.br"
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar empresas"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EmpresaResponseDTO> getEmpresaById(
            @Parameter(description = "ID único da empresa", required = true, example = "1")
            @PathVariable Long id) {
        EmpresaResponseDTO empresa = empresaService.findById(id);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    @Operation(
        summary = "Criar nova empresa",
        description = "Cria uma nova empresa no sistema. O CNPJ deve ser único e válido."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Empresa criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmpresaResponseDTO.class),
                examples = @ExampleObject(
                    name = "Empresa criada",
                    value = """
                        {
                          "id": 3,
                          "nome": "Nova Filial Sul",
                          "cnpj": "98.765.432/0001-10",
                          "endereco": "Rua Sul, 789, Zona Sul",
                          "telefone": "(11) 95555-5555",
                          "email": "sul@empresa.com.br"
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou CNPJ já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar empresas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EmpresaResponseDTO> createEmpresa(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados da empresa a ser criada",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EmpresaRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Nova empresa",
                        value = """
                            {
                              "nome": "Nova Filial Sul",
                              "cnpj": "98.765.432/0001-10",
                              "endereco": "Rua Sul, 789, Zona Sul",
                              "telefone": "(11) 95555-5555",
                              "email": "sul@empresa.com.br"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO empresa = empresaService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar empresa existente",
        description = "Atualiza completamente os dados de uma empresa existente. Todos os campos obrigatórios devem ser fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Empresa atualizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmpresaResponseDTO.class),
                examples = @ExampleObject(
                    name = "Empresa atualizada",
                    value = """
                        {
                          "id": 1,
                          "nome": "Empresa Principal S.A.",
                          "cnpj": "12.345.678/0001-90",
                          "endereco": "Av. das Flores, 123, Centro - Atualizado",
                          "telefone": "(11) 98765-4321",
                          "email": "novo-contato@empresa.com.br"
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar empresas"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EmpresaResponseDTO> updateEmpresa(
            @Parameter(description = "ID único da empresa a ser atualizada", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados atualizados da empresa",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EmpresaRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Dados para atualização",
                        value = """
                            {
                              "nome": "Empresa Principal S.A.",
                              "cnpj": "12.345.678/0001-90",
                              "endereco": "Av. das Flores, 123, Centro - Atualizado",
                              "telefone": "(11) 98765-4321",
                              "email": "novo-contato@empresa.com.br"
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO empresa = empresaService.update(id, requestDTO);
        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir empresa",
        description = "Remove uma empresa do sistema. ATENÇÃO: Esta operação não pode ser desfeita. " +
                     "Verifique se não há departamentos ou usuários vinculados à empresa antes de excluir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empresa excluída com sucesso"),
        @ApiResponse(responseCode = "400", description = "Empresa possui dependências e não pode ser excluída"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir empresas"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteEmpresa(
            @Parameter(description = "ID único da empresa a ser excluída", required = true, example = "1")
            @PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
    @Operation(
        summary = "Filtrar empresas",
        description = "Busca empresas aplicando filtros opcionais. Todos os filtros são opcionais e podem ser combinados. " +
                     "A busca por nome é case-insensitive e busca por correspondência parcial."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Filtro aplicado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class),
                examples = @ExampleObject(
                    name = "Resultado filtrado",
                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "nome": "Empresa Principal Ltda",
                              "cnpj": "12.345.678/0001-90",
                              "endereco": "Rua das Flores, 123, Centro",
                              "telefone": "(11) 98765-4321",
                              "email": "contato@empresa.com.br"
                            }
                          ],
                          "totalElements": 1,
                          "totalPages": 1,
                          "size": 10,
                          "number": 0,
                          "first": true,
                          "last": true
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar empresas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<EmpresaResponseDTO>> filtrarEmpresas(
            @Parameter(description = "Filtro por nome da empresa (busca parcial, case-insensitive)", example = "principal")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "nome")
            @RequestParam(defaultValue = "nome") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponseDTO<EmpresaResponseDTO> empresas = empresaService.filtrarEmpresas(nome, pageable);
        return ResponseEntity.ok(empresas);
    }
}
