# Cosmo - Gerenciador de Inventário de TI

> 🚧 **Atenção:** Este projeto e sua documentação estão em desenvolvimento ativo. Funcionalidades e estruturas podem ser alteradas. 🚧

## Sobre o Projeto

**Cosmo** é um sistema web desenvolvido para simplificar o gerenciamento de inventários de ativos de TI. O objetivo do projeto é fornecer uma plataforma centralizada para cadastrar, rastrear e gerenciar o ciclo de vida de equipamentos como desktops, notebooks, celulares, impressoras, entre outros.

O sistema controla não apenas o inventário físico, mas também as movimentações desses equipamentos, associando cada ativo a um usuário e mantendo um histórico detalhado e seguro de todas as entregas, devoluções e cancelamentos.

### ✨ Principais Funcionalidades

- **🔐 Autenticação JWT**: Sistema de login seguro com tokens de acesso e refresh
- **👥 Gerenciamento de Usuários**: Sistema completo de usuários com perfis e permissões
- **Gestão Completa de Ativos**: Cadastro e controle de diversos tipos de equipamentos
- **Sistema de Herança**: Arquitetura orientada a objetos com subclasses específicas para cada tipo de equipamento
- **Controle de Movimentação**: Histórico completo de entregas e devoluções
- **Auditoria Imutável**: Registros de histórico preservados através de cancelamento em vez de exclusão
- **Validações Inteligentes**: Controle automático de campos únicos e integridade de dados
- **🔗 HATEOAS Simplificado**: Links de navegação otimizados - apenas "self" para recursos individuais
- **📄 Paginação Completa**: Sistema robusto de paginação com metadados e links de navegação
- **🎯 Filtros Dinâmicos**: Sistema avançado de filtragem usando JPA Specifications
- **📊 Ordenação Flexível**: Ordenação por qualquer campo com direção ascendente/descendente

## Tecnologias Utilizadas

O backend do Cosmo é construído com as seguintes tecnologias:

* **Java 23**
* **Spring Boot 3.x**
* **Spring Web:** Para a construção de APIs RESTful.
* **Spring Data JPA:** Para a persistência de dados e comunicação com o banco.
* **Spring Security:** Sistema de segurança com autenticação JWT.
* **Spring HATEOAS:** Para implementação de links de navegação hipermídia.
* **JPA Specifications:** Para filtragem dinâmica e segura de dados.
* **JWT (JSON Web Tokens):** Para autenticação stateless e segura.
* **MySQL:** Banco de dados relacional para armazenamento dos dados.
* **Lombok:** Para reduzir código boilerplate em classes Java.
* **Flyway:** Para controle de versão do banco de dados.

![Diagrama do Banco de Dados](docs/images/cosmo_db.png)

## Documentação da API

A API RESTful do Cosmo oferece endpoints modernos e específicos por tipo de equipamento. Todas as rotas utilizam JSON como formato de dados, possuem CORS habilitado, suportam paginação e incluem links HATEOAS otimizados.

### 🔐 **Autenticação** - `/auth`

#### **Sistema de Login e Tokens JWT**
| Método | Rota | Descrição | Payload |
|--------|------|-----------|---------|
| `POST` | `/auth/signin` | Autentica usuário e gera tokens JWT | `{"email": "user@example.com", "password": "senha123"}` |
| `PUT` | `/auth/refresh/{email}` | Renova tokens usando refresh token | Header: `Authorization: Bearer {refreshToken}` |

**Fluxo de Autenticação:**
1. **Login**: Envie credenciais para `/auth/signin`
2. **Acesso**: Use o `accessToken` no header `Authorization: Bearer {token}`
3. **Renovação**: Quando o token expirar, use o `refreshToken` em `/auth/refresh/{email}`

### 👤 **Usuários do Sistema** - `/api/users`

#### **CRUD de Usuários da Aplicação (Com Paginação e Filtros)**
| Método | Rota | Descrição | Payload |
|--------|------|-----------|---------|
| `POST` | `/api/users` | Cadastra um novo usuário do sistema | `{"firstName": "João", "lastName": "Silva", "email": "joao@cosmo.com", "password": "senha123"}` |
| `GET` | `/api/users` | **📄 Lista usuários com paginação completa** | Parâmetros: `page`, `size`, `sortBy`, `sortDir` |
| `GET` | `/api/users/{id}` | Busca um usuário específico por ID | - |
| `PUT` | `/api/users/{id}` | Atualiza dados de um usuário | `{"firstName": "João", "lastName": "Santos", "email": "joao.santos@cosmo.com"}` |
| `DELETE` | `/api/users/{id}` | Remove um usuário do sistema | - |
| `GET` | `/api/users/filtrar` | **🔍 Filtra usuários com paginação** | Filtros + paginação |

**Exemplo de Response com Paginação:**
```json
{
  "_embedded": {
    "userResponseDTOList": [
      {
        "id": 1,
        "firstName": "João",
        "lastName": "Silva",
        "email": "joao@cosmo.com",
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
      "href": "http://localhost:8080/api/users?page=0&size=10&sortBy=firstName&sortDir=asc"
    },
    "first": {
      "href": "http://localhost:8080/api/users?page=0&size=10&sortBy=firstName&sortDir=asc"
    },
    "last": {
      "href": "http://localhost:8080/api/users?page=2&size=10&sortBy=firstName&sortDir=asc"
    },
    "next": {
      "href": "http://localhost:8080/api/users?page=1&size=10&sortBy=firstName&sortDir=asc"
    }
  },
  "page": {
    "size": 10,
    "totalElements": 25,
    "totalPages": 3,
    "number": 0
  }
}
```

> **Nota:** Estes são os usuários da aplicação Cosmo (administradores, operadores), diferentes dos usuários finais que recebem equipamentos (entidade `Usuario`).

### 🏢 **Empresas** - `/api/empresas`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/empresas` | **📄 Lista empresas com paginação** |
| `GET` | `/api/empresas/{id}` | Busca empresa por ID (apenas link "self") |
| `POST` | `/api/empresas` | Cadastra uma nova empresa |
| `PUT` | `/api/empresas/{id}` | Atualiza dados de uma empresa existente |
| `DELETE` | `/api/empresas/{id}` | Remove uma empresa do sistema |
| `GET` | `/api/empresas/filtrar` | **🔍 Filtra empresas com paginação** |

### 🏛️ **Departamentos** - `/api/departamentos`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/departamentos` | **📄 Lista departamentos com paginação** |
| `GET` | `/api/departamentos/{id}` | Busca departamento por ID (apenas link "self") |
| `POST` | `/api/departamentos` | Cadastra um novo departamento |
| `PUT` | `/api/departamentos/{id}` | Atualiza dados de um departamento |
| `DELETE` | `/api/departamentos/{id}` | Remove um departamento do sistema |
| `GET` | `/api/departamentos/filtrar` | **🔍 Filtra departamentos com paginação** |

### 👤 **Usuários Finais** - `/api/usuarios`
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/usuarios` | **📄 Lista usuários com paginação** |
| `GET` | `/api/usuarios/{id}` | Busca usuário por ID (apenas link "self") |
| `POST` | `/api/usuarios` | Cadastra um novo usuário |
| `PUT` | `/api/usuarios/{id}` | Atualiza dados de um usuário |
| `DELETE` | `/api/usuarios/{id}` | **Desativa** um usuário (soft delete) |
| `PATCH` | `/api/usuarios/{id}/reativar` | Reativa um usuário previamente desativado |
| `GET` | `/api/usuarios/filtrar` | **🔍 Filtra usuários com paginação** |

> **Nota:** O delete de usuários é um "soft delete" - apenas marca o usuário como inativo, preservando os dados no banco.

### 💻 **Equipamentos** - `/api/equipamentos`

#### **📋 Consultas Gerais**
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/equipamentos` | **📄 Lista equipamentos com paginação** |
| `GET` | `/api/equipamentos/{id}` | Busca equipamento por ID (apenas link "self") |
| `GET` | `/api/equipamentos/tipo/{tipo}` | **📄 Lista por tipo com paginação** |
| `GET` | `/api/equipamentos/tipo/{tipo}/count` | Conta equipamentos por tipo |
| `DELETE` | `/api/equipamentos/{id}` | Remove um equipamento do sistema |
| `GET` | `/api/equipamentos/filtrar` | **🔍 Filtra equipamentos com paginação** |

#### **📱 Criação por Tipo Específico (POST)**
| Método | Rota | Descrição | Campos Específicos |
|--------|------|-----------|-------------------|
| `POST` | `/api/equipamentos/notebook` | Cadastra um novo notebook | Sistema, processador, RAM, hostname, etc. |
| `POST` | `/api/equipamentos/desktop` | Cadastra um novo desktop | Sistema, processador, RAM, hostname, etc. |
| `POST` | `/api/equipamentos/celular` | Cadastra um novo celular | IMEI, IMEI2, EID, MDM |
| `POST` | `/api/equipamentos/chip` | Cadastra um novo chip | ICCID, número, operadora, plano |
| `POST` | `/api/equipamentos/impressora` | Cadastra uma nova impressora | Tipo, IP, colorida, multifuncional |
| `POST` | `/api/equipamentos/monitor` | Cadastra um novo monitor | Tamanho, resolução |

#### **✏️ Atualização por Tipo Específico (PUT)**
| Método | Rota | Descrição |
|--------|------|-----------|
| `PUT` | `/api/equipamentos/notebook/{id}` | Atualiza dados de um notebook |
| `PUT` | `/api/equipamentos/desktop/{id}` | Atualiza dados de um desktop |
| `PUT` | `/api/equipamentos/celular/{id}` | Atualiza dados de um celular |
| `PUT` | `/api/equipamentos/chip/{id}` | Atualiza dados de um chip |
| `PUT` | `/api/equipamentos/impressora/{id}` | Atualiza dados de uma impressora |
| `PUT` | `/api/equipamentos/monitor/{id}` | Atualiza dados de um monitor |

### 📝 **Histórico** - `/api/historicos`

#### **CRUD Básico**
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/historicos` | **📄 Lista históricos com paginação** |
| `GET` | `/api/historicos/{id}` | Busca histórico por ID (apenas link "self") |
| `PATCH` | `/api/historicos/{id}` | Edita observações e URL de termo de entrega |
| `PATCH` | `/api/historicos/{id}/cancelar` | Cancela um histórico permanentemente |

#### **Operações de Negócio**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/historicos/entregar` | Realiza entrega de equipamento para usuário |
| `PATCH` | `/api/historicos/{id}/devolver` | Registra devolução de equipamento |
| `POST` | `/api/historicos/entregar-multiplos` | Entrega múltiplos equipamentos para um usuário |
| `PATCH` | `/api/historicos/devolver-multiplos` | Devolução em lote de múltiplos equipamentos |

#### **Consultas Específicas**
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/historicos/usuario/{usuarioId}` | **📄 Lista históricos de usuário com paginação** |
| `GET` | `/api/historicos/equipamento/{equipamentoId}` | **📄 Lista históricos de equipamento com paginação** |
| `GET` | `/api/historicos/equipamento/{equipamentoId}/em-uso` | Verifica se equipamento está em uso |
| `GET` | `/api/historicos/equipamento/{equipamentoId}/ativo` | Busca histórico ativo de um equipamento |
| `GET` | `/api/historicos/filtrar` | **🔍 Filtra históricos com paginação** |

## 🛡️ Recursos Especiais da API

### **🔗 HATEOAS Otimizado**
O sistema implementa HATEOAS de forma simplificada e eficiente:

**Para Recursos Individuais (GET /{id}):**
```json
{
  "id": 1,
  "nome": "Departamento TI",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/departamentos/1"
    }
  }
}
```

**Para Paginação (Mantém Links Completos):**
```json
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/departamentos?page=0&size=10&sortBy=nome&sortDir=asc"
    },
    "first": {
      "href": "http://localhost:8080/api/departamentos?page=0&size=10&sortBy=nome&sortDir=asc"
    },
    "last": {
      "href": "http://localhost:8080/api/departamentos?page=6&size=10&sortBy=nome&sortDir=asc"
    },
    "next": {
      "href": "http://localhost:8080/api/departamentos?page=1&size=10&sortBy=nome&sortDir=asc"
    }
  },
  "page": {
    "size": 10,
    "totalElements": 62,
    "totalPages": 7,
    "number": 0
  }
}
```

### **📄 Sistema de Paginação Robusto**
- **Paginação no Banco**: Consultas otimizadas sem carregar dados em memória
- **Metadados Completos**: Total de elementos, páginas e informações de navegação
- **Links de Navegação**: self, first, last, prev, next automaticamente gerados
- **Ordenação Configurável**: `sortBy` e `sortDir` para qualquer campo
- **Tamanho Flexível**: `size` configurável por requisição

**Parâmetros de Paginação:**
- `page`: Número da página (inicia em 0)
- `size`: Quantidade de itens por página (padrão: 10)
- `sortBy`: Campo para ordenação (ex: "nome", "id", "dataEntrega")
- `sortDir`: Direção da ordenação ("asc" ou "desc")

### **🎯 Filtros Avançados com JPA Specifications**
- **Busca Segura**: Prevenção automática contra SQL Injection
- **Filtros Combinados**: Múltiplos critérios em uma única consulta
- **Case-Insensitive**: Busca por texto ignorando maiúsculas/minúsculas
- **Filtros de Data**: Intervalos de data com formato `YYYY-MM-DD`
- **Filtros Relacionais**: Busca por IDs de entidades relacionadas
- **Paginação Integrada**: Todos os filtros suportam paginação

### **🔐 Sistema de Autenticação JWT**
- **Tokens Seguros**: Autenticação baseada em JWT com algoritmo HS256
- **Duplo Token**: Access token (8h) e refresh token (24h) para maior segurança
- **Stateless**: Não mantém sessões no servidor, escalável horizontalmente
- **Renovação Automática**: Renovação de tokens sem necessidade de novo login
- **Validação Rigorosa**: Verificação de assinatura, expiração e integridade dos tokens

### **✅ Validações Inteligentes de Negócio**
- **Campos Únicos**: Controle automático de duplicação (hostname, IMEI, ICCID, etc.)
- **ENUMs Validados**: Mensagens claras para valores inválidos
- **Integridade Referencial**: Validação de empresa e departamento
- **Histórico Consistente**: Controle automático de status baseado em movimentações

### **🛡️ Tratamento de Erros Avançado**
- **Mensagens Claras**: Erros específicos com dicas de correção
- **Códigos HTTP Apropriados**: 409 para conflitos, 400 para dados inválidos
- **Detalhes Estruturados**: Informações sobre campo, valor e possíveis soluções

## 📋 Status do Projeto

### ✅ **Funcionalidades Implementadas**
- [x] **🔐 Autenticação JWT**: Sistema completo de login e renovação de tokens
- [x] **👥 Gerenciamento de Usuários**: CRUD completo para usuários da aplicação
- [x] **📄 Paginação Completa**: Sistema de paginação em todos os endpoints de listagem
- [x] **🎯 Filtros Dinâmicos**: Sistema de filtros avançados com JPA Specifications
- [x] **🔗 HATEOAS Otimizado**: Links simplificados - apenas "self" para recursos individuais
- [x] **📊 Ordenação Flexível**: Por qualquer campo com direção configurável
- [x] **🛡️ Spring Security**: Configuração de segurança com JWT
- [x] **CRUD Completo**: Todas as entidades com operações básicas
- [x] **Validações de Negócio**: Campos únicos e integridade referencial
- [x] **Histórico de Movimentação**: Controle completo de entregas/devoluções
- [x] **Soft Delete**: Desativação de usuários preservando dados
- [x] **🛡️ Tratamento de Erros**: Mensagens estruturadas e códigos HTTP apropriados

### 🚧 **Próximos Passos**
- [ ] **🔐 Autorização Granular**: Implementação de roles e permissões nos endpoints
- [ ] **Importação/Exportação**: Funcionalidade para importar/exportar inventário em CSV/Excel
- [ ] **Testes**: Cobertura completa de testes unitários e de integração
- [ ] **Documentação**: Swagger/OpenAPI para documentação interativa
- [ ] **Dockerização**: Criação de imagens Docker para fácil deploy
- [ ] **Monitoramento**: Integração com ferramentas de monitoramento e logging
- [ ] **Deploy**: Configuração de pipeline CI/CD para deploy automatizado na Cloud
- [ ] **Frontend**: Desenvolvimento da interface web do Cosmo

### 🎯 **Melhorias Futuras**
- [ ] **Cache**: Implementação de cache para consultas frequentes
- [ ] **Auditoria Avançada**: Log detalhado de todas as operações
- [ ] **Backup Automático**: Sistema de backup e recuperação
- [ ] **API Rate Limiting**: Controle de taxa de requisições

## 📄 Licença

Este projeto está em desenvolvimento e a licença será definida em breve.

---

**Cosmo** - Simplificando a gestão de ativos de TI 🚀
