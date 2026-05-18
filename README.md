# SoundRate API

Uma mini API REST inspirada no Rate Your Music para usuários avaliarem álbuns musicais.

## Objetivo

API REST completa para gerenciamento de usuários, álbuns musicais e avaliações (reviews), permitindo CRUD completo, relacionamentos entre entidades, paginação, cache, HATEOAS e monitoramento.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.1.5**
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória
- **Spring Validation** - Validação de dados
- **Lombok** - Redução de boilerplate
- **Spring HATEOAS** - Hypermedia-driven REST APIs
- **Spring Cache** - Sistema de cache
- **Spring Boot Actuator** - Monitoramento
- **SpringDoc OpenAPI (Swagger)** - Documentação automática
- **Spring Boot DevTools** - Desenvolvimento ágil

## Entidades

### User
- id (Long)
- name (String)
- email (String, único)
- reviews (List<Review>)

### Album
- id (Long)
- title (String)
- artist (String)
- genre (String)
- releaseYear (Integer)
- reviews (List<Review>)

### Review
- id (Long)
- score (Integer, 0-10)
- comment (String)
- createdAt (LocalDateTime)
- user (User)
- album (Album)

## Endpoints da API

### Users
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/users` | Lista todos usuários (paginado) |
| GET | `/users/{id}` | Busca usuário por ID (com HATEOAS) |
| POST | `/users` | Cria novo usuário |
| PUT | `/users/{id}` | Atualiza usuário |
| DELETE | `/users/{id}` | Remove usuário |

### Albums
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/albums` | Lista todos álbuns (paginado) |
| GET | `/albums/{id}` | Busca álbum por ID (com HATEOAS) |
| POST | `/albums` | Cria novo álbum |
| PUT | `/albums/{id}` | Atualiza álbum |
| DELETE | `/albums/{id}` | Remove álbum |
| GET | `/albums/genre/{genre}` | Filtra álbuns por gênero |
| GET | `/albums/projection/{genre}` | Projection de álbuns por gênero |

### Reviews
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/reviews` | Lista todas avaliações (paginado) |
| GET | `/reviews/{id}` | Busca avaliação por ID (com HATEOAS) |
| POST | `/reviews` | Cria nova avaliação |
| PUT | `/reviews/{id}` | Atualiza avaliação |
| DELETE | `/reviews/{id}` | Remove avaliação |
| GET | `/reviews/album/{albumId}` | Lista avaliações de um álbum |