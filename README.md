# 👵 ConectIdade - Backend API

> API RESTful desenvolvida com Spring Boot para a plataforma **ConectIdade**, um projeto de inclusão digital e educação focado no público 60+.

O sistema gerencia usuários, trilhas de aprendizado (módulos e lições), gamificação (pontos e medalhas), acessibilidade e quizzes interativos.

---

## 🚀 Tecnologias Utilizadas

* **Java 21+**
* **Spring Boot 3** (Web, Data JPA, Validation)
* **Banco de Dados:** Relacional (compatível com MySQL/PostgreSQL/H2 via JPA)
* **Lombok:** Redução de código boilerplate.
* **ModelMapper:** Mapeamento entre Entidades e DTOs.
* **Maven:** Gerenciamento de dependências.

---

## 🛠️ Funcionalidades Principais

1.  **Gestão de Usuários & Acessibilidade**:
    * Login via Google ID.
    * Configurações personalizadas de acessibilidade: **Texto Grande** e **Alto Contraste**.
2.  **Trilhas de Aprendizagem**:
    * Estrutura hierárquica: **Módulos** > **Lições**.
    * Tipos de Lição suportados: `VÍDEO`, `SIMULADOR`, `QUIZ`.
3.  **Sistema de Progresso & Gamificação**:
    * Rastreamento de lições completas.
    * Sistema de Pontuação (XP).
    * Conquista de Medalhas.
4.  **Avaliação (Quizzes)**:
    * Perguntas e respostas dinâmicas.
    * Feedback imediato com explicação da resposta.

---

## 🗃️ Modelo de Dados (Entidades)

O banco de dados segue a seguinte estrutura relacional:

* **Usuario**: Armazena dados de perfil, configurações de acessibilidade e pontuação total.
* **Modulo**: Agrupa lições em uma ordem lógica.
* **Licao**: Conteúdo educacional. Possui um tipo (`LicaoTipo`) e recompensas em pontos.
* **ProgressoUsuario**: Registra se um usuário iniciou ou completou uma lição (`ProgressoStatus`).
* **Quiz / Pergunta / OpcaoResposta**: Estrutura para exercícios de fixação.
* **Medalha / UsuarioMedalha**: Sistema de conquistas desbloqueáveis.

---

## 🔌 Documentação da API

### 👤 Usuários (`/api/usuarios`)

#### 1. Login ou Cadastro
Verifica se o usuário existe pelo `googleId`. Se não, cria um novo usuário.
* **POST** `/api/usuarios/auth/login`
* **Body:**
    ```json
    {
      "googleId": "string",
      "email": "usuario@email.com",
      "nomeExibicao": "Nome Usuario"
    }
    ```

#### 2. Obter Perfil
* **GET** `/api/usuarios/{id}`

#### 3. Atualizar Configurações de Acessibilidade
* **PUT** `/api/usuarios/{id}/configuracoes`
* **Body:**
    ```json
    {
      "configTextoGrande": true,
      "configAltoContraste": false
    }
    ```

---

### 📚 Módulos e Lições (`/api/modulos`)

#### 1. Listar Módulos
Retorna todos os módulos ordenados.
* **GET** `/api/modulos`

#### 2. Listar Lições de um Módulo
* **GET** `/api/modulos/{id}/licoes`

---

### 📈 Progresso (`/api/progresso`)

#### 1. Obter Progresso Completo
Retorna pontuação total, IDs das lições completas e medalhas conquistadas.
* **GET** `/api/progresso/usuario/{usuarioId}`

#### 2. Completar Lição
Marca uma lição como `COMPLETO` e atribui os pontos ao usuário.
* **POST** `/api/progresso/usuario/{usuarioId}/completar-licao/{licaoId}`

#### 3. Conceder Medalha (Manual/Sistema)
* **POST** `/api/progresso/usuario/{usuarioId}/conceder-medalha`
* **Body:**
    ```json
    {
      "nomeMedalha": "Iniciante",
      "pontosExtras": 50
    }
    ```

#### 4. Resetar Progresso
Apaga todo o histórico (progresso, medalhas, respostas de quiz) e zera a pontuação.
* **DELETE** `/api/progresso/usuario/{usuarioId}/resetar`

---

### ❓ Quiz (`/api/quiz`)

#### 1. Carregar Quiz da Lição
Retorna o quiz, perguntas e opções de resposta associados a uma lição.
* **GET** `/api/quiz/licao/{licaoId}`

#### 2. Submeter Resposta
Salva a resposta do usuário e retorna se está correta com a explicação. Se correta, marca a lição como completa automaticamente.
* **POST** `/api/quiz/submeter/usuario/{usuarioId}`
* **Body:**
    ```json
    {
      "perguntaId": 1,
      "opcaoEscolhidaId": 2
    }
    ```
* **Response:**
    ```json
    {
      "isCorreta": true,
      "explicacaoResposta": "Explicação didática sobre o porquê está correto."
    }
    ```

---

## ⚙️ Configuração e Execução

### Pré-requisitos
* JDK 17 ou superior
* Maven

### Passos
1.  Clone o repositório:
    ```bash
    git clone [https://github.com/kADURICO/BackendProjetoIntegrador.git](https://github.com/kADURICO/BackendProjetoIntegrador.git)
    ```
2.  Configure o banco de dados no arquivo `application.properties`.
3.  Execute o projeto:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 🤝 Contribuição

Projeto desenvolvido como parte do currículo acadêmico/profissional da equipe:

* **Carlos Eduardo Soares**
* **Nikolas Peixoto**
* **Gabriel Christino**
* **Raphael Estrella**

---
