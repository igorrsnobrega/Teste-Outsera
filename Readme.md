---

# Projeto de Gerenciamento de Filmes

Este projeto é uma aplicação Spring Boot para gerenciar informações sobre filmes, incluindo o upload de arquivos CSV, consulta de filmes e análise de intervalos de prêmios de produtores.

## Endpoints da API

### MovieResource

#### Upload de Arquivo CSV em Base64

- **Endpoint:** `/api/movies/upload`
- **Método:** `POST`
- **Descrição:** Faz o upload de um arquivo CSV codificado em Base64 contendo informações sobre filmes.
- **Parâmetros:**
  - `fileDTO` (FileDTO): Objeto contendo o nome do arquivo e o conteúdo codificado em Base64.
- **Exemplo de Requisição:**
  ```json
  {
      "fileName": "movies.csv",
      "fileContent": "base64_encoded_content_here"
  }
  ```
- **Resposta:**
  - `201 Created`: Arquivo processado com sucesso.
  - **Exemplo de Resposta:**
    ```json
    {
      "content": "File has been processed successfully."
    }
    ```

#### Obter Todos os Filmes

- **Endpoint:** `/api/movies`
- **Método:** `GET`
- **Descrição:** Retorna uma lista de todos os filmes.
- **Resposta:**
  - `200 OK`: Lista de filmes.
  - **Exemplo de Resposta:**
    ```json
    [
      {
        "year": 1980,
        "title": "Can't Stop the Music",
        "studios": "Associated Film Distribution",
        "producers": "Allan Carr",
        "winner": true
      },
      {
        "year": 1981,
        "title": "Mommie Dearest",
        "studios": "Paramount Pictures",
        "producers": "Frank Yablans",
        "winner": true
      }
    ]
    ```

#### Contar Todos os Filmes

- **Endpoint:** `/api/movies/count`
- **Método:** `GET`
- **Descrição:** Retorna a contagem de todos os filmes.
- **Resposta:**
  - `200 OK`: Número total de filmes.
  - **Exemplo de Resposta:**
    ```json
    {
      "content": "2"
    }
    ```

#### Obter Intervalo de Produtores

- **Endpoint:** `/api/movies/producers-interval`
- **Método:** `GET`
- **Descrição:** Retorna os produtores com o maior e menor intervalo entre prêmios consecutivos.
- **Parâmetros:**
  - `inicio` (Integer): Ano inicial do intervalo.
  - `fim` (Integer, opcional): Ano final do intervalo. Se não fornecido, será considerado o ano atual.
- **Resposta:**
  - `200 OK`: Intervalos de produtores.
  - **Exemplo de Resposta:**
    ```json
    {
      "min": [
        {
          "producer": "A. Kitman Ho",
          "interval": 1,
          "previousWin": 1980,
          "followingWin": 1981
        }
      ],
      "max": [
        {
          "producer": "A. Kitman Ho",
          "interval": 14,
          "previousWin": 1981,
          "followingWin": 1995
        }
      ]
    }
    ```

### FileResource

#### Obter Histórico de Uploads de Arquivos

- **Endpoint:** `/api/files`
- **Método:** `GET`
- **Descrição:** Retorna uma lista de todos os históricos de uploads de arquivos.
- **Resposta:**
  - `200 OK`: Lista de históricos de uploads.
  - **Exemplo de Resposta:**
    ```json
    [
      {
        "fileName": "movies.csv",
        "successful": true,
        "message": null
      }
    ]
    ```

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd seu-repositorio
   ```
3. Execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

## Testes

Para executar os testes, use o seguinte comando:
```bash
./mvnw test
```

## Docker

Para construir e executar a aplicação usando Docker:

1. Construa a imagem Docker:
   ```bash
   docker build -t nome-da-sua-imagem .
   ```
2. Execute o contêiner Docker:
   ```bash
   docker run -p 8080:8080 nome-da-sua-imagem
   ```

## Tecnologias Utilizadas

- Java
- Spring Boot
- Spring Data JPA
- H2 Database (para testes)
- JUnit 5
- Mockito
- Docker

---
