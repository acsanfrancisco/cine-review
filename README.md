[README-cine-review.md](https://github.com/user-attachments/files/33520200/README-cine-review.md)
# 🎬 Cine Review API

<p align="center">
  Uma API REST para pesquisar filmes, cadastrar usuários e publicar avaliações, integrando informações de filmes com a API do <a href="https://www.themoviedb.org/">The Movie Database (TMDB)</a>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white" alt="Java 25">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.1.1">
  <img src="https://img.shields.io/badge/API-REST-005571" alt="REST API">
  <img src="https://img.shields.io/badge/Docs-OpenAPI%20%2F%20Swagger-85EA2D?logo=swagger&logoColor=black" alt="OpenAPI / Swagger">
</p>

## 📌 Sobre o projeto

O **Cine Review** é um projeto backend desenvolvido com Java e Spring Boot para praticar a construção de APIs REST, organização em camadas, persistência com JPA, validação de dados, tratamento centralizado de exceções, testes automatizados e integração com serviços externos.

A aplicação permite pesquisar filmes pelo título usando o TMDB, salvar filmes no banco de dados local e relacioná-los a avaliações feitas por usuários cadastrados. As avaliações podem ser consultadas por usuário ou por filme.

> O projeto utiliza a API do TMDB, mas não é autenticado nem certificado pelo TMDB.

## ✨ Funcionalidades

### 🎞️ Filmes
- Pesquisar filmes por título na API do TMDB.
- Consultar os detalhes de um filme no TMDB antes de salvá-lo.
- Salvar filmes no banco de dados local usando o identificador externo (`externalId`).
- Impedir o cadastro duplicado de um filme com o mesmo identificador externo.
- Consultar filmes salvos pelo ID local ou pelo ID externo.

### 👤 Usuários
- Cadastrar usuários com nome completo e e-mail.
- Validar os dados recebidos na criação do usuário.
- Impedir o cadastro de e-mails duplicados.
- Consultar usuários por ID ou e-mail.
- Excluir usuários que não possuam avaliações associadas.
- Impedir a exclusão de usuários que já tenham avaliações.

### ⭐ Avaliações
- Criar uma avaliação associada a um usuário existente e a um filme salvo.
- Validar a nota, que deve estar entre `0` e `10`.
- Consultar avaliações por ID do usuário, e-mail do usuário ou identificador externo do filme.
- Retornar uma lista vazia quando não houver avaliações para a consulta, desde que o usuário ou filme consultado exista.

### 🧰 API e tratamento de erros
- Endpoints REST versionados com o prefixo `/api/v1`.
- Documentação interativa com OpenAPI/Swagger UI.
- Validação de entrada com Jakarta Bean Validation.
- Tratamento global de exceções com respostas de erro padronizadas.
- Separação entre entidades, DTOs, mapeadores, serviços, repositórios e integração externa.

## 🛠️ Tecnologias utilizadas

| Tecnologia | Uso |
|---|---|
| Java 25 | Linguagem da aplicação |
| Spring Boot 4.1.1 | Configuração e inicialização da aplicação |
| Spring Web MVC | Criação dos endpoints REST |
| Spring `RestClient` | Comunicação HTTP com a API do TMDB |
| Spring Data JPA | Persistência e acesso aos dados |
| Hibernate | ORM |
| H2 Database | Banco de dados em memória para desenvolvimento e testes |
| Jakarta Bean Validation | Validação dos dados de entrada |
| Springdoc OpenAPI / Swagger UI | Documentação e exploração da API |
| Lombok | Redução de código repetitivo |
| JUnit 5 | Testes automatizados |
| Mockito | Isolamento de dependências nos testes unitários |
| Maven Wrapper | Execução do Maven sem instalação global obrigatória |

## 🧱 Organização do projeto

O código segue uma arquitetura em camadas para separar responsabilidades.

```text
src/
├── main/
│   ├── java/acsanfrancisco/cine_review/
│   │   ├── config/              # Configuração da documentação OpenAPI
│   │   ├── controller/          # Endpoints REST
│   │   ├── doc/                 # Contratos e anotações de documentação
│   │   ├── dto/
│   │   │   ├── request/         # Dados recebidos pela API
│   │   │   └── response/        # Dados devolvidos pela API
│   │   ├── entity/              # Entidades JPA
│   │   ├── exception/           # Exceções da aplicação
│   │   │   └── handler/         # Tratamento global de erros
│   │   ├── integration/
│   │   │   └── tmdb/            # Cliente e DTOs da integração com o TMDB
│   │   ├── mapper/              # Conversão entre entidades e DTOs
│   │   ├── repository/          # Acesso aos dados
│   │   └── service/             # Regras de negócio
│   └── resources/
│       ├── application.properties
│       └── application-test.properties
└── test/
    └── java/                    # Testes automatizados
```

### Modelo de domínio

- **User**: representa uma pessoa cadastrada, com nome e e-mail.
- **Movie**: representa um filme salvo localmente e mantém o `externalId` fornecido pelo TMDB.
- **Review**: representa uma avaliação, relacionando um usuário a um filme e armazenando nota, texto e data de criação.

Um filme precisa ser salvo na aplicação antes de receber avaliações. O identificador externo permite relacionar o registro local aos dados retornados pelo TMDB sem depender do ID gerado pelo banco local.

## 🧩 Padrões de projeto GoF

Além das abstrações criadas no próprio código, o projeto utiliza recursos do Spring Framework que aplicam padrões de projeto internamente. Os exemplos abaixo distinguem o que aparece na implementação da aplicação do que é fornecido pelo framework.

### Proxy (GoF) — gerenciamento transacional pelo Spring

**Onde:** métodos anotados com `@Transactional` em `MovieService`, `UserService` e `ReviewService`.

**Como aparece no projeto:** o Spring aplica o gerenciamento declarativo de transações por meio de proxies AOP. Quando uma chamada passa pelo proxy, o Spring inicia, confirma ou reverte a transação conforme o resultado da execução e a configuração transacional.

### Strategy (GoF) — seleção de comportamento por contrato

**Onde no código da aplicação:** interface `MovieSearch<S, M>`, implementação `TmdbService` e dependência de `MovieSearch` em `TmdbMovieService`.

**Como aparece no projeto:** `TmdbMovieService` depende da abstração `MovieSearch`, em vez de depender diretamente da classe concreta `TmdbService`. Isso permite introduzir outra implementação do contrato e escolher qual implementação será fornecida pelo Spring.

### Adapter (GoF) — integração com o TMDB e Spring MVC

**Onde no código da aplicação:** `TmdbService`, que implementa `MovieSearch<TmdbSearchResponse, TmdbMovieResponse>` e utiliza `RestClient` para chamar a API do TMDB.

**Como aparece no projeto:** `TmdbService` expõe as operações de pesquisa esperadas pela aplicação por meio do contrato `MovieSearch`, enquanto encapsula os caminhos, parâmetros HTTP e tratamento de erros específicos do TMDB. Isso funciona como uma camada adaptadora entre o contrato consumido por `TmdbMovieService` e a API externa.

### Factory Method (GoF) — criação do `RestClient` pelo Spring

**Onde:** método `restClient()` anotado com `@Bean` na classe `TmdbRestClient`.

**Como aparece no projeto:** o método constrói e configura o `RestClient` com a URL base, o token de autorização e o cabeçalho `Accept`. O Spring chama esse método de fábrica para registrar o objeto configurado no contêiner e disponibilizá-lo para injeção em `TmdbService`.

### Singleton (GoF) — escopo padrão dos beans Spring

**Onde:** componentes como `TmdbService`, `TmdbMovieService`, `MovieService`, `ReviewService` e `UserService`, registrados com `@Service`.

**Como aparece no projeto:** por padrão, o Spring registra esses beans no escopo `singleton`, mantendo uma instância por definição de bean dentro do contêiner e reutilizando-a nas injeções. Isso se relaciona ao objetivo do padrão **Singleton**, pois evita criar uma nova instância do serviço a cada injeção.

## 🚀 Como executar localmente

### Pré-requisitos

- JDK 25.
- Git (para clonar o repositório).
- Uma chave de acesso à API do TMDB.

O projeto inclui o Maven Wrapper (`mvnw` e `mvnw.cmd`), então não é necessário instalar o Maven globalmente.

### 1. Clone o repositório

```bash
git clone https://github.com/acsanfrancisco/cine_review.git
cd cine_review
```

Se o nome ou a URL do repositório for diferente, utilize a URL configurada no seu GitHub.

### 2. Configure as credenciais do TMDB

Na raiz do projeto, crie um arquivo chamado `.env` usando `.env.example` como referência:

```dotenv
TMDB_API_URL=https://api.themoviedb.org/3
TMDB_API_TOKEN=seu_token_de_acesso_tmdb
```

O token deve ser um **API Read Access Token** válido do TMDB. Você pode consultar as opções de acesso na [documentação oficial do TMDB](https://developer.themoviedb.org/docs/getting-started).

Não envie o arquivo `.env` para o GitHub nem coloque seu token diretamente no código-fonte.

### 3. Execute a aplicação

**Windows (PowerShell ou CMD):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS / Git Bash:**

```bash
./mvnw spring-boot:run
```

Por padrão, a aplicação inicia em:

```text
http://localhost:8080
```

### 4. Acesse a documentação

Com a aplicação em execução, abra:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Console H2: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Configuração padrão do H2 Console:
- **JDBC URL:** `jdbc:h2:mem:cine-review`
- **User Name:** `sa`
- **Password:** deixe em branco

O banco padrão é em memória; os dados não são persistidos depois que a aplicação é encerrada.

## 📚 Endpoints da API

Todos os endpoints usam o prefixo `/api/v1`.

### Filmes — `/api/v1/movies`

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| `GET` | `/api/v1/movies?title=The%20Matrix` | Pesquisa filmes por título no TMDB | `200 OK` |
| `POST` | `/api/v1/movies?externalId=603` | Busca um filme no TMDB e salva no banco local | `201 Created` |
| `GET` | `/api/v1/movies/1` | Consulta um filme pelo ID local | `200 OK` |
| `GET` | `/api/v1/movies/external/603` | Consulta um filme pelo ID externo | `200 OK` |

A pesquisa por título consulta o TMDB; ela não representa uma busca por título nos filmes já salvos localmente. O valor `603` é um exemplo de identificador externo, não um ID local.

### Usuários — `/api/v1/users`

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| `POST` | `/api/v1/users` | Cadastra um usuário | `201 Created` |
| `GET` | `/api/v1/users/1` | Consulta usuário pelo ID | `200 OK` |
| `GET` | `/api/v1/users?email=usuario@example.com` | Consulta usuário pelo e-mail | `200 OK` |
| `DELETE` | `/api/v1/users/1` | Exclui usuário sem avaliações associadas | `204 No Content` |

### Avaliações — `/api/v1/reviews`

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| `POST` | `/api/v1/reviews` | Cria uma avaliação | `201 Created` |
| `GET` | `/api/v1/reviews/users/1` | Lista avaliações de um usuário pelo ID | `200 OK` |
| `GET` | `/api/v1/reviews/users?email=usuario@example.com` | Lista avaliações de um usuário pelo e-mail | `200 OK` |
| `GET` | `/api/v1/reviews/movies/603` | Lista avaliações de um filme pelo ID externo | `200 OK` |

## 🧪 Exemplos de requisições

### Cadastrar um usuário

`POST /api/v1/users`

```json
{
  "fullName": "Little Zé da Silva",
  "email": "zezinho@gmail.com"
}
```

Regras de validação:
- `fullName` é obrigatório e deve ter entre 5 e 255 caracteres.
- `email` é obrigatório e deve ter formato válido.
- O e-mail não pode estar cadastrado anteriormente.

### Pesquisar filmes

`GET /api/v1/movies?title=The%20Matrix`

Exemplo de chamada com cURL:

```bash
curl --get "http://localhost:8080/api/v1/movies" \
  --data-urlencode "title=The Matrix"
```

A resposta contém uma lista de filmes encontrados no TMDB, com informações como ID externo, título, data de lançamento, idioma original e título original. Caso não haja resultados, a pesquisa pode retornar uma lista vazia.

### Salvar um filme encontrado no TMDB

`POST /api/v1/movies?externalId=603`

```bash
curl -X POST "http://localhost:8080/api/v1/movies?externalId=603"
```

A aplicação consulta o filme no TMDB e salva os dados localmente. Se o filme não for encontrado ou se o `externalId` já estiver cadastrado, a API retorna um erro apropriado.

### Criar uma avaliação

`POST /api/v1/reviews`

```json
{
  "userId": 1,
  "movieExternalId": 603,
  "rating": 9.5,
  "overview": "Um clássico de ficção científica, com uma história marcante."
}
```

Regras de validação:
- `userId` e `movieExternalId` são obrigatórios.
- `rating` é obrigatório e deve estar entre `0` e `10`.
- `overview` é obrigatório e não pode estar em branco.
- O usuário precisa existir e o filme precisa estar salvo localmente.

## ⚠️ Tratamento de erros

A API possui um `GlobalExceptionHandler` para padronizar respostas de erro. Entre os principais casos tratados estão:

| Status HTTP | Situação |
|---|---|
| `400 Bad Request` | Operação inválida, como tentativa de excluir usuário com avaliações |
| `404 Not Found` | Usuário ou filme não encontrado |
| `409 Conflict` | E-mail ou filme com identificador externo já cadastrado |
| `422 Unprocessable Content` | Falha de validação ou corpo da requisição inválido |
| `502 Bad Gateway` | Falha na comunicação com a API externa do TMDB |
| `500 Internal Server Error` | Erro inesperado não tratado por uma regra específica |

As respostas de erro usam o formato definido pela classe `ErrorMessage`.

## 🧪 Testes automatizados

Os testes utilizam JUnit 5 e Mockito para validar regras de negócio e isolar dependências externas. Também há testes de integração com o contexto da aplicação e testes relacionados à persistência e aos controllers.

Os testes usam configuração própria em `application-test.properties`, com um banco H2 separado e schema recriado para os testes.

## 🔒 Observações e escopo atual

- A aplicação utiliza H2 em memória por padrão, sem configuração de banco de produção.
- A integração com o TMDB exige um token válido no arquivo `.env`.
- O projeto não implementa autenticação ou autorização.
- O conteúdo e a disponibilidade dos dados de filmes dependem do serviço externo TMDB.

## 🗺️ Possíveis evoluções

- Adicionar autenticação e autorização.
- Adicionar paginação às consultas que retornam listas.
- Implementar migrações de banco de dados.
- Ampliar a cobertura de testes.
- Configurar banco de dados persistente.
- Criar uma interface frontend para pesquisa e avaliação de filmes.

## 👨‍💻 Autor

**João Francisco Silva Mendes Santiago**

- GitHub: [@acsanfrancisco](https://github.com/acsanfrancisco)

---

Dados de filmes fornecidos pela [API do TMDB](https://www.themoviedb.org/). Este produto utiliza a API do TMDB, mas não é endossado nem certificado pelo TMDB.
