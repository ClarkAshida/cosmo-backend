package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.dto.usuario.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.usuario.UsuarioResponseDTO;
import com.cosmo.cosmo.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema. Permite criar, listar, atualizar e desativar usuários, além de realizar buscas com filtros e paginação.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista paginada de todos os usuários cadastrados no sistema. " +
                     "Suporta ordenação por qualquer campo e paginação configurável."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponseDTO.class),
                examples = @ExampleObject(
                    name = "Lista paginada de usuários",
                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "nome": "João Silva",
                              "email": "joao.silva@empresa.com",
                              "cpf": "123.456.789-00",
                              "telefone": "(11) 99999-1234",
                              "cargo": "Analista de TI",
                              "ativo": true,
                              "departamento": {
                                "id": 1,
                                "nome": "Tecnologia da Informação"
                              }
                            },
                            {
                              "id": 2,
                              "nome": "Maria Santos",
                              "email": "maria.santos@empresa.com",
                              "cpf": "987.654.321-00",
                              "telefone": "(11) 88888-5678",
                              "cargo": "Gerente de RH",
                              "ativo": true,
                              "departamento": {
                                "id": 2,
                                "nome": "Recursos Humanos"
                              }
                            }
                          ],
                          "totalElements": 50,
                          "totalPages": 5,
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
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar usuários"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<UsuarioResponseDTO>> getAllUsuarios(
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
        PagedResponseDTO<UsuarioResponseDTO> usuarios = usuarioService.findAll(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os detalhes de um usuário específico baseado no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDTO.class),
                examples = @ExampleObject(
                    name = "Usuário encontrado",
                    value = """
                        {
                          "id": 1,
                          "nome": "João Silva",
                          "email": "joao.silva@empresa.com",
                          "cpf": "123.456.789-00",
                          "telefone": "(11) 99999-1234",
                          "cargo": "Analista de TI",
                          "ativo": true,
                          "departamento": {
                            "id": 1,
                            "nome": "Tecnologia da Informação"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar usuários"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(
            @Parameter(description = "ID único do usuário", required = true, example = "1")
            @PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(
        summary = "Criar novo usuário",
        description = "Cria um novo usuário no sistema. O CPF e email devem ser únicos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDTO.class),
                examples = @ExampleObject(
                    name = "Usuário criado",
                    value = """
                        {
                          "id": 3,
                          "nome": "Carlos Oliveira",
                          "email": "carlos.oliveira@empresa.com",
                          "cpf": "111.222.333-44",
                          "telefone": "(11) 77777-9999",
                          "cargo": "Desenvolvedor",
                          "ativo": true,
                          "departamento": {
                            "id": 1,
                            "nome": "Tecnologia da Informação"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou CPF/email já existe"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar usuários"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UsuarioResponseDTO> createUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do usuário a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Novo usuário",
                        value = """
                            {
                              "nome": "Carlos Oliveira",
                              "email": "carlos.oliveira@empresa.com",
                              "cpf": "111.222.333-44",
                              "telefone": "(11) 77777-9999",
                              "cargo": "Desenvolvedor",
                              "departamentoId": 1
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO usuario = usuarioService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar usuário existente",
        description = "Atualiza completamente os dados de um usuário existente. Todos os campos obrigatórios devem ser fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDTO.class),
                examples = @ExampleObject(
                    name = "Usuário atualizado",
                    value = """
                        {
                          "id": 1,
                          "nome": "João Silva Santos",
                          "email": "joao.silva@empresa.com",
                          "cpf": "123.456.789-00",
                          "telefone": "(11) 99999-1234",
                          "cargo": "Analista Sênior de TI",
                          "ativo": true,
                          "departamento": {
                            "id": 1,
                            "nome": "Tecnologia da Informação"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar usuários"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @Parameter(description = "ID único do usuário a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados atualizados do usuário",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Dados para atualização",
                        value = """
                            {
                              "nome": "João Silva Santos",
                              "email": "joao.silva@empresa.com",
                              "cpf": "123.456.789-00",
                              "telefone": "(11) 99999-1234",
                              "cargo": "Analista Sênior de TI",
                              "departamentoId": 1
                            }
                            """
                    )
                )
            )
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO usuario = usuarioService.update(id, requestDTO);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Desativar usuário",
        description = "Desativa um usuário no sistema. O usuário não é excluído fisicamente, apenas marcado como inativo. " +
                     "Esta operação pode ser revertida usando o endpoint de reativação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para desativar usuários"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID único do usuário a ser desativado", required = true, example = "1")
            @PathVariable Long id) {
        usuarioService.deactivateById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reativar")
    @Operation(
        summary = "Reativar usuário",
        description = "Reativa um usuário que foi previamente desativado, tornando-o ativo novamente no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário reativado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDTO.class),
                examples = @ExampleObject(
                    name = "Usuário reativado",
                    value = """
                        {
                          "id": 1,
                          "nome": "João Silva",
                          "email": "joao.silva@empresa.com",
                          "cpf": "123.456.789-00",
                          "telefone": "(11) 99999-1234",
                          "cargo": "Analista de TI",
                          "ativo": true,
                          "departamento": {
                            "id": 1,
                            "nome": "Tecnologia da Informação"
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para reativar usuários"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UsuarioResponseDTO> reativarUsuario(
            @Parameter(description = "ID único do usuário a ser reativado", required = true, example = "1")
            @PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.reactivateById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/filtrar")
    @Operation(
        summary = "Filtrar usuários",
        description = "Busca usuários aplicando filtros opcionais. Todos os filtros são opcionais e podem ser combinados. " +
                     "As buscas por nome e email são case-insensitive e buscam por correspondência parcial."
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
                              "nome": "João Silva",
                              "email": "joao.silva@empresa.com",
                              "cpf": "123.456.789-00",
                              "telefone": "(11) 99999-1234",
                              "cargo": "Analista de TI",
                              "ativo": true,
                              "departamento": {
                                "id": 1,
                                "nome": "Tecnologia da Informação"
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
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar usuários"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedResponseDTO<UsuarioResponseDTO>> filtrarUsuarios(
            @Parameter(description = "Filtro por nome do usuário (busca parcial, case-insensitive)", example = "joão")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Filtro por email do usuário (busca parcial, case-insensitive)", example = "silva@")
            @RequestParam(required = false) String email,
            @Parameter(description = "Filtro por CPF do usuário (busca exata)", example = "123.456.789-00")
            @RequestParam(required = false) String cpf,
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
        PagedResponseDTO<UsuarioResponseDTO> usuarios = usuarioService.filtrarUsuarios(nome, email, cpf, pageable);
        return ResponseEntity.ok(usuarios);
    }
}
