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

import com.cosmo.cosmo.dto.departamento.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.service.DepartamentoService;
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
@RequestMapping("/api/departamentos")
@CrossOrigin(origins = "*")
@Tag(name = "Departamentos", description = "Gerenciamento de departamentos da organização. Permite criar, listar, atualizar e excluir departamentos, além de realizar buscas com filtros e paginação.")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    @Operation(
        summary = "Listar todos os departamentos",
        description = "Retorna uma lista paginada de todos os departamentos cadastrados no sistema. " +
                     "Suporta ordenação por qualquer campo e paginação configurável."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de departamentos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class),
                examples = @ExampleObject(
                    name = "Lista paginada de departamentos",
                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "nome": "Tecnologia da Informação",
                              "descricao": "Departamento responsável pela infraestrutura de TI",
                              "empresa": {
                                "id": 1,
                                "nome": "Empresa Principal"
                              }
                            },
                            {
                              "id": 2,
                              "nome": "Recursos Humanos",
                              "descricao": "Departamento de gestão de pessoas",
                              "empresa": {
                                "id": 1,
                                "nome": "Empresa Principal"
                              }
                            }
                          ],
                          "totalElements": 25,
                          "totalPages": 3,
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
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar departamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<DepartamentoResponseDTO>> getAllDepartamentos(
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
        PagedResponseDTO<DepartamentoResponseDTO> departamentos = departamentoService.findAll(pageable);
        return ResponseEntity.ok(departamentos);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar departamento por ID",
        description = "Retorna os detalhes de um departamento específico baseado no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Departamento encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DepartamentoResponseDTO.class),
                examples = @ExampleObject(
                    name = "Departamento encontrado",
                    value = """
                        {
                          "id": 1,
                          "nome": "Tecnologia da Informação",
                          "descricao": "Departamento responsável pela infraestrutura de TI",
                          "empresa": {
                            "id": 1,
                            "nome": "Empresa Principal"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar departamentos"),
        @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<DepartamentoResponseDTO> getDepartamentoById(
            @Parameter(description = "ID único do departamento", required = true, example = "1")
            @PathVariable Long id) {
        DepartamentoResponseDTO departamento = departamentoService.findById(id);
        return ResponseEntity.ok(departamento);
    }

    @PostMapping
    @Operation(
        summary = "Criar novo departamento",
        description = "Cria um novo departamento no sistema. O nome do departamento deve ser único dentro da empresa."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Departamento criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DepartamentoResponseDTO.class),
                examples = @ExampleObject(
                    name = "Departamento criado",
                    value = """
                        {
                          "id": 3,
                          "nome": "Marketing",
                          "descricao": "Departamento de marketing e comunicação",
                          "empresa": {
                            "id": 1,
                            "nome": "Empresa Principal"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou nome do departamento já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar departamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<DepartamentoResponseDTO> createDepartamento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do departamento a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DepartamentoRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Novo departamento",
                        value = """
                            {
                              "nome": "Marketing",
                              "descricao": "Departamento de marketing e comunicação",
                              "empresaId": 1
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody DepartamentoRequestDTO requestDTO) {
        DepartamentoResponseDTO departamento = departamentoService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamento);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar departamento existente",
        description = "Atualiza completamente os dados de um departamento existente. Todos os campos obrigatórios devem ser fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Departamento atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DepartamentoResponseDTO.class),
                examples = @ExampleObject(
                    name = "Departamento atualizado",
                    value = """
                        {
                          "id": 1,
                          "nome": "Tecnologia da Informação e Inovação",
                          "descricao": "Departamento responsável pela infraestrutura de TI e projetos de inovação",
                          "empresa": {
                            "id": 1,
                            "nome": "Empresa Principal"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar departamentos"),
        @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<DepartamentoResponseDTO> updateDepartamento(
            @Parameter(description = "ID único do departamento a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados atualizados do departamento",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DepartamentoRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Dados para atualização",
                        value = """
                            {
                              "nome": "Tecnologia da Informação e Inovação",
                              "descricao": "Departamento responsável pela infraestrutura de TI e projetos de inovação",
                              "empresaId": 1
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody DepartamentoRequestDTO requestDTO) {
        DepartamentoResponseDTO departamento = departamentoService.update(id, requestDTO);
        return ResponseEntity.ok(departamento);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir departamento",
        description = "Remove um departamento do sistema. ATENÇÃO: Esta operação não pode ser desfeita. " +
                     "Verifique se não há usuários ou equipamentos vinculados ao departamento antes de excluir."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Departamento excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Departamento possui dependências e não pode ser excluído"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir departamentos"),
        @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteDepartamento(
            @Parameter(description = "ID único do departamento a ser excluído", required = true, example = "1")
            @PathVariable Long id) {
        departamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
    @Operation(
        summary = "Filtrar departamentos",
        description = "Busca departamentos aplicando filtros opcionais. Todos os filtros são opcionais e podem ser combinados. " +
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
                              "nome": "Tecnologia da Informação",
                              "descricao": "Departamento responsável pela infraestrutura de TI",
                              "empresa": {
                                "id": 1,
                                "nome": "Empresa Principal"
                              }
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
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar departamentos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<DepartamentoResponseDTO>> filtrarDepartamentos(
            @Parameter(description = "Filtro por nome do departamento (busca parcial, case-insensitive)", example = "tecnologia")
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
        PagedResponseDTO<DepartamentoResponseDTO> departamentos = departamentoService.filtrarDepartamentos(nome, pageable);
        return ResponseEntity.ok(departamentos);
    }
}
