# Cosmo - Gerenciador de Inventário de TI

> 🚧 **Atenção:** Este projeto e sua documentação estão em desenvolvimento ativo. Funcionalidades e estruturas podem ser alteradas. 🚧

## Sobre o Projeto

**Cosmo** é um sistema web desenvolvido para simplificar o gerenciamento de inventários de ativos de TI. O objetivo do projeto é fornecer uma plataforma centralizada para cadastrar, rastrear e gerenciar o ciclo de vida de equipamentos como desktops, notebooks, celulares, impressoras, entre outros.

O sistema controla não apenas o inventário físico, mas também as movimentações desses equipamentos, associando cada ativo a um usuário e mantendo um histórico detalhado e seguro de todas as entregas, devoluções e cancelamentos.

## Tecnologias Utilizadas

O backend do Cosmo é construído com as seguintes tecnologias:

* **Java 23**
* **Spring Boot 3.x**
* **Spring Web:** Para a construção de APIs RESTful.
* **Spring Data JPA:** Para a persistência de dados e comunicação com o banco.
* **MySQL:** Banco de dados relacional para armazenamento dos dados.
* **Lombok:** Para reduzir código boilerplate em classes Java.

> A implementação de segurança com **Spring Security** está planejada para futuras atualizações.

## Estrutura do Banco de Dados

A base de dados é o coração do sistema, projetada para garantir a integridade e a rastreabilidade das informações. A estrutura atual é composta pelas seguintes tabelas principais:

* `equipamento`: Armazena todos os detalhes dos ativos de TI.
* `usuario`: Contém as informações dos colaboradores que utilizam os equipamentos.
* `departamento` e `empresa`: Tabelas de apoio para organização estrutural.
* `historico`: A tabela mais importante para a auditoria. Ela registra cada movimentação (entrega/devolução) como um evento imutável. Para corrigir erros, um registro não é apagado, mas sim **cancelado** através do campo `status_registro_historico`, preservando toda a trilha de auditoria.

Abaixo está o diagrama de relacionamento entre as entidades:

![Diagrama do Banco de Dados](docs/images/cosmo_db.png)

## Documentação da API

A API RESTful do Cosmo oferece endpoints para gerenciar todas as entidades do sistema. Todas as rotas utilizam JSON como formato de dados e estão disponíveis com CORS habilitado.

### 🏢 **Empresas** - `/api/empresas`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/empresas` | Lista todas as empresas cadastradas |
| `GET` | `/api/empresas/{id}` | Busca uma empresa específica por ID |
| `POST` | `/api/empresas` | Cadastra uma nova empresa |
| `PUT` | `/api/empresas/{id}` | Atualiza dados de uma empresa existente |
| `DELETE` | `/api/empresas/{id}` | Remove uma empresa do sistema |

### 🏛️ **Departamentos** - `/api/departamentos`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/departamentos` | Lista todos os departamentos |
| `GET` | `/api/departamentos/{id}` | Busca um departamento específico por ID |
| `POST` | `/api/departamentos` | Cadastra um novo departamento |
| `PUT` | `/api/departamentos/{id}` | Atualiza dados de um departamento |
| `DELETE` | `/api/departamentos/{id}` | Remove um departamento do sistema |

### 👤 **Usuários** - `/api/usuarios`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/usuarios` | Lista todos os usuários do sistema |
| `GET` | `/api/usuarios/{id}` | Busca um usuário específico por ID |
| `POST` | `/api/usuarios` | Cadastra um novo usuário |
| `PUT` | `/api/usuarios/{id}` | Atualiza dados de um usuário |
| `DELETE` | `/api/usuarios/{id}` | **Desativa** um usuário (soft delete) |
| `PATCH` | `/api/usuarios/{id}/reativar` | Reativa um usuário previamente desativado |

> **Nota:** O delete de usuários é um "soft delete" - apenas marca o usuário como inativo, preservando os dados no banco.

### 💻 **Equipamentos** - `/api/equipamentos`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/equipamentos` | Lista todos os equipamentos cadastrados |
| `GET` | `/api/equipamentos/{id}` | Busca um equipamento específico por ID |
| `POST` | `/api/equipamentos` | Cadastra um novo equipamento |
| `PUT` | `/api/equipamentos/{id}` | Atualiza dados de um equipamento |
| `DELETE` | `/api/equipamentos/{id}` | Remove um equipamento do sistema |

### 📝 **Histórico** - `/api/historicos`

#### **CRUD Básico**
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/historicos` | Lista todos os registros de histórico |
| `GET` | `/api/historicos/{id}` | Busca um registro específico por ID |
| `PATCH` | `/api/historicos/{id}` | Edita observações e URL de termo de entrega |
| `PATCH` | `/api/historicos/{id}/cancelar` | Cancela um histórico permanentemente |

#### **Operações de Negócio**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/historicos/entregar` | Realiza entrega de equipamento para usuário |
| `PATCH` | `/api/historicos/{id}/devolver` | Registra devolução de equipamento |

#### **Consultas Específicas**
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/historicos/usuario/{usuarioId}` | Lista históricos de um usuário específico |
| `GET` | `/api/historicos/equipamento/{equipamentoId}` | Lista históricos de um equipamento |
| `GET` | `/api/historicos/equipamento/{equipamentoId}/em-uso` | Verifica se equipamento está em uso |
| `GET` | `/api/historicos/equipamento/{equipamentoId}/ativo` | Busca histórico ativo de um equipamento |

## Recursos Especiais da API

### **Validações de Negócio**
- **Usuários:** Não permite cadastro de email ou CPF duplicados
- **Histórico:** Mantém auditoria completa com cancelamento ao invés de exclusão
- **Equipamentos:** Controle automático de status baseado no histórico de uso

### **Soft Delete**
- Usuários são desativados ao invés de deletados, preservando histórico
- Possibilidade de reativação através de endpoint específico

### **Auditoria Completa**
- Todo histórico de movimentação é preservado
- Cancelamentos são registrados sem perder dados originais
- Controle automático de status de equipamentos

## Próximos Passos

O projeto está em fase inicial. Os próximos passos incluem:
- [ ] Adição da camada de segurança com autenticação e autorização.
- [ ] Criação de testes unitários e de integração.
- [ ] Documentação detalhada da API (Swagger/OpenAPI).
- [ ] Implementação de filtros e paginação nas consultas.