package com.cosmo.cosmo.controller;

import com.cosmo.cosmo.dto.UserRequestDTO;
import com.cosmo.cosmo.dto.UserResponseDTO;
import com.cosmo.cosmo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import com.cosmo.cosmo.specification.UserSpecification;
import com.cosmo.cosmo.entity.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Administração de Usuários", description = "Gerenciamento de usuários administradores do sistema. Permite criar, listar, atualizar e excluir usuários com diferentes permissões e roles, além de realizar buscas com filtros avançados e paginação.")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(
        summary = "Criar novo usuário de sistema",
        description = "Cria um novo usuário no sistema de autenticação. Este usuário terá acesso ao sistema e pode receber diferentes permissões e roles."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EntityModel.class),
                examples = @ExampleObject(
                    name = "Usuário criado com sucesso",
                    value = """
                        {
                          "id": 1,
                          "firstName": "João",
                          "lastName": "Silva",
                          "email": "joao.silva@empresa.com",
                          "enabled": true,
                          "accountNonExpired": true,
                          "accountNonLocked": true,
                          "credentialsNonExpired": true,
                          "_links": {
                            "self": {
                              "href": "http://localhost:8080/api/users/1"
                            }
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar usuários"),
        @ApiResponse(responseCode = "409", description = "Email já está em uso por outro usuário"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EntityModel<UserResponseDTO>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do usuário a ser criado",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Novo usuário de sistema",
                        value = """
                            {
                              "firstName": "João",
                              "lastName": "Silva",
                              "email": "joao.silva@empresa.com",
                              "password": "MinhaSenh@123"
                            }
                            """
                    )
                )
            )
            @RequestBody UserRequestDTO userRequest) {
        log.info("Creating new user with email: {}", userRequest.email());
        try {
            UserResponseDTO createdUser = userService.create(userRequest);

            // Adicionar apenas o link self
            EntityModel<UserResponseDTO> userModel = EntityModel.of(createdUser)
                .add(linkTo(methodOn(UserController.class).getUserById(createdUser.id())).withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
        } catch (RuntimeException exception) {
            log.error("Error creating user: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os usuários de sistema",
        description = "Retorna uma lista paginada de todos os usuários do sistema de autenticação. " +
                     "Suporta ordenação por qualquer campo e paginação configurável com links HATEOAS."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedModel.class),
                examples = @ExampleObject(
                    name = "Lista paginada de usuários",
                    value = """
                        {
                          "_embedded": {
                            "userResponseDTOList": [
                              {
                                "id": 1,
                                "firstName": "Admin",
                                "lastName": "Sistema",
                                "email": "admin@cosmo.com",
                                "enabled": true,
                                "accountNonExpired": true,
                                "accountNonLocked": true,
                                "credentialsNonExpired": true,
                                "_links": {
                                  "self": {
                                    "href": "http://localhost:8080/api/users/1"
                                  }
                                }
                              }
                            ]
                          },
                          "_links": {
                            "self": {
                              "href": "http://localhost:8080/api/users?page=0&size=10"
                            },
                            "first": {
                              "href": "http://localhost:8080/api/users?page=0&size=10"
                            },
                            "last": {
                              "href": "http://localhost:8080/api/users?page=2&size=10"
                            },
                            "next": {
                              "href": "http://localhost:8080/api/users?page=1&size=10"
                            }
                          },
                          "page": {
                            "size": 10,
                            "totalElements": 25,
                            "totalPages": 3,
                            "number": 0
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para listar usuários"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedModel<EntityModel<UserResponseDTO>>> getAllUsers(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "firstName")
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        log.info("Retrieving all users with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort.Direction sortDirection = Sort.Direction.fromString(sortDir);
        Sort sortObj = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<UserResponseDTO> users = userService.findAll(pageable);

        // Converter para EntityModel com apenas link self para cada usuário
        Page<EntityModel<UserResponseDTO>> userModels = users.map(user ->
            EntityModel.of(user)
                .add(linkTo(methodOn(UserController.class).getUserById(user.id())).withSelfRel())
        );

        // Criar PagedModel com links de paginação
        PagedModel<EntityModel<UserResponseDTO>> pagedModel = PagedModel.of(
            userModels.getContent(),
            new PagedModel.PageMetadata(
                userModels.getSize(),
                userModels.getNumber(),
                userModels.getTotalElements(),
                userModels.getTotalPages()
            )
        );

        // Adicionar links de paginação
        pagedModel.add(linkTo(methodOn(UserController.class)
            .getAllUsers(page, size, sortBy, sortDir)).withSelfRel());

        pagedModel.add(linkTo(methodOn(UserController.class)
            .getAllUsers(0, size, sortBy, sortDir)).withRel("first"));

        pagedModel.add(linkTo(methodOn(UserController.class)
            .getAllUsers(userModels.getTotalPages() - 1, size, sortBy, sortDir)).withRel("last"));

        if (userModels.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                .getAllUsers(page - 1, size, sortBy, sortDir)).withRel("prev"));
        }

        if (userModels.hasNext()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                .getAllUsers(page + 1, size, sortBy, sortDir)).withRel("next"));
        }

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar usuário de sistema por ID",
        description = "Retorna os detalhes de um usuário específico do sistema de autenticação baseado no seu identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EntityModel.class),
                examples = @ExampleObject(
                    name = "Usuário encontrado",
                    value = """
                        {
                          "id": 1,
                          "firstName": "João",
                          "lastName": "Silva",
                          "email": "joao.silva@empresa.com",
                          "enabled": true,
                          "accountNonExpired": true,
                          "accountNonLocked": true,
                          "credentialsNonExpired": true,
                          "_links": {
                            "self": {
                              "href": "http://localhost:8080/api/users/1"
                            }
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
    public ResponseEntity<EntityModel<UserResponseDTO>> getUserById(
            @Parameter(description = "ID único do usuário", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Retrieving user with ID: {}", id);
        try {
            UserResponseDTO user = userService.findById(id);

            // Adicionar apenas o link self
            EntityModel<UserResponseDTO> userModel = EntityModel.of(user)
                .add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());

            return ResponseEntity.ok(userModel);
        } catch (RuntimeException exception) {
            log.error("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar usuário de sistema",
        description = "Atualiza completamente os dados de um usuário do sistema de autenticação. Todos os campos obrigatórios devem ser fornecidos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = @ExampleObject(
                    name = "Usuário atualizado",
                    value = """
                        {
                          "id": 1,
                          "firstName": "João Carlos",
                          "lastName": "Silva Santos",
                          "email": "joao.silva@empresa.com",
                          "enabled": true,
                          "accountNonExpired": true,
                          "accountNonLocked": true,
                          "credentialsNonExpired": true
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
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID único do usuário a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados atualizados do usuário",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRequestDTO.class),
                    examples = @ExampleObject(
                        name = "Dados para atualização",
                        value = """
                            {
                              "firstName": "João Carlos",
                              "lastName": "Silva Santos",
                              "email": "joao.silva@empresa.com",
                              "password": "NovaSenha@456"
                            }
                            """
                    )
                )
            )
            @RequestBody UserRequestDTO userRequest) {
        log.info("Updating user with ID: {}", id);
        try {
            UserResponseDTO updatedUser = userService.update(id, userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException exception) {
            log.error("Error updating user with ID {}: {}", id, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir usuário de sistema",
        description = "Remove permanentemente um usuário do sistema de autenticação. ATENÇÃO: Esta operação não pode ser desfeita."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir usuários"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID único do usuário a ser excluído", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException exception) {
            log.error("Error deleting user with ID {}: {}", id, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filtrar")
    @Operation(
        summary = "Filtrar usuários de sistema",
        description = "Busca usuários do sistema aplicando filtros opcionais. Todos os filtros são opcionais e podem ser combinados. " +
                     "As buscas por nome e email são case-insensitive e buscam por correspondência parcial. Retorna resultados com links HATEOAS."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Filtro aplicado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedModel.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token de autenticação inválido ou ausente"),
        @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para filtrar usuários"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PagedModel<EntityModel<UserResponseDTO>>> filterUsers(
            @Parameter(description = "Filtro por primeiro nome (busca parcial, case-insensitive)", example = "joão")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Filtro por último nome (busca parcial, case-insensitive)", example = "silva")
            @RequestParam(required = false) String lastName,
            @Parameter(description = "Filtro por email (busca parcial, case-insensitive)", example = "@empresa.com")
            @RequestParam(required = false) String email,
            @Parameter(description = "Filtro por status habilitado", example = "true")
            @RequestParam(required = false) Boolean enabled,
            @Parameter(description = "Filtro por conta não expirada", example = "true")
            @RequestParam(required = false) Boolean accountNonExpired,
            @Parameter(description = "Filtro por conta não bloqueada", example = "true")
            @RequestParam(required = false) Boolean accountNonLocked,
            @Parameter(description = "Filtro por credenciais não expiradas", example = "true")
            @RequestParam(required = false) Boolean credentialsNonExpired,
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "firstName")
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        log.info("Filtering users with criteria - firstName: {}, lastName: {}, email: {}, enabled: {}",
                firstName, lastName, email, enabled);

        Sort.Direction sortDirection = Sort.Direction.fromString(sortDir);
        Sort sortObj = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<User> spec = Specification.where(UserSpecification.hasFirstName(firstName))
                .and(UserSpecification.hasLastName(lastName))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasEnabled(enabled))
                .and(UserSpecification.hasAccountNonExpired(accountNonExpired))
                .and(UserSpecification.hasAccountNonLocked(accountNonLocked))
                .and(UserSpecification.hasCredentialsNonExpired(credentialsNonExpired));

        Page<UserResponseDTO> users = userService.findAll(spec, pageable);

        // Converter para EntityModel com apenas link self para cada usuário
        Page<EntityModel<UserResponseDTO>> userModels = users.map(user ->
            EntityModel.of(user)
                .add(linkTo(methodOn(UserController.class).getUserById(user.id())).withSelfRel())
        );

        // Criar PagedModel com links de paginação
        PagedModel<EntityModel<UserResponseDTO>> pagedModel = PagedModel.of(
            userModels.getContent(),
            new PagedModel.PageMetadata(
                userModels.getSize(),
                userModels.getNumber(),
                userModels.getTotalElements(),
                userModels.getTotalPages()
            )
        );

        // Adicionar links de paginação
        pagedModel.add(linkTo(methodOn(UserController.class)
            .filterUsers(firstName, lastName, email, enabled, accountNonExpired,
                        accountNonLocked, credentialsNonExpired, page, size, sortBy, sortDir)).withSelfRel());

        pagedModel.add(linkTo(methodOn(UserController.class)
            .filterUsers(firstName, lastName, email, enabled, accountNonExpired,
                        accountNonLocked, credentialsNonExpired, 0, size, sortBy, sortDir)).withRel("first"));

        pagedModel.add(linkTo(methodOn(UserController.class)
            .filterUsers(firstName, lastName, email, enabled, accountNonExpired,
                        accountNonLocked, credentialsNonExpired, userModels.getTotalPages() - 1, size, sortBy, sortDir)).withRel("last"));

        if (userModels.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                .filterUsers(firstName, lastName, email, enabled, accountNonExpired,
                            accountNonLocked, credentialsNonExpired, page - 1, size, sortBy, sortDir)).withRel("prev"));
        }

        if (userModels.hasNext()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                .filterUsers(firstName, lastName, email, enabled, accountNonExpired,
                            accountNonLocked, credentialsNonExpired, page + 1, size, sortBy, sortDir)).withRel("next"));
        }

        return ResponseEntity.ok(pagedModel);
    }
}
