# 🚀 Instruções para Rodar Backend + Frontend

## 📋 Pré-requisitos

- **Java 21+** (para o Backend Spring Boot)
- **Node.js 18+** (para o Frontend Angular)
- **npm** (incluso no Node.js)
- **Git**

---

## 🔧 Configuração

### Backend (Spring Boot) - Porta 8080

```bash
# 1. Entrar no diretório do backend
cd C:\Users\lucas\projeto-letramento

# 2. Compilar o projeto
.\mvnw clean install

# 3. Iniciar o servidor Spring Boot
.\mvnw spring-boot:run

# 4 Iniciar com Spring Boot caso ja possua compilação presente
./mvnw.cmd spring-boot:run

# ✅ Backend rodando em: http://localhost:8080
```

### Frontend (Angular) - Porta 4200

```bash
# 1. Entrar no diretório do frontend
cd C:\Users\lucas\projeto-letramento-front

# 2. Instalar dependências (primeira execução)
npm install

# 3. Iniciar o servidor de desenvolvimento Angular
npm start

# ✅ Frontend rodando em: http://localhost:4200
```

---

## 🔌 Configuração de API

### Backend endpoints:
```
http://localhost:8080/api/jogos/listar          → Lista os 4 jogos
http://localhost:8080/api/bingo/sala/criar      → Cria sala de Bingo
http://localhost:8080/api/professores           → Gerencia professores
```

### CORS - Já Configurado ✅
O backend aceita requisições do frontend:
- **Origem permitida**: `http://localhost:4200`
- **Métodos**: GET, POST, PUT, DELETE
- **Headers**: Todos permitidos

---

## 📂 Estrutura de Módulos

```
projeto-letramento/ (Backend - Spring Boot)
├── src/main/java/br/com/projeto_letramento/projeto_letramento/
│   ├── controller/
│   │   ├── GameSelectionController.java       ← Seleção de jogos
│   │   ├── BingoSalaController.java           ← API do Bingo
│   │   ├── BingoPaginasController.java        ← Páginas do Bingo
│   │   ├── AuthController.java                ← Autenticação
│   │   └── ProfessorController.java           ← Gerenciar professores
│   ├── service/
│   │   ├── BingoService.java                  ← Lógica do Bingo
│   │   └── ProfessorService.java              ← Gerenciar professores
│   ├── model/
│   │   ├── SalaJogo.java                      ← Model do Bingo
│   │   └── ProfessorModel.java                ← Model do professor
│   ├── infra/
│   │   └── cors/
│   │       └── CorsConfig.java                ← Configuração CORS ✅
│   └── repository/
│       └── ProfessorRepository.java

projeto-letramento-front/ (Frontend - Angular)
├── src/
│   ├── app/
│   │   ├── pages/
│   │   │   ├── login/                         ← Tela de login
│   │   │   ├── dashboard/                     ← Seleção de jogos
│   │   │   ├── aluno/                         ← Interface do aluno
│   │   │   ├── index/                         ← Página inicial
│   │   │   └── cadastro/                      ← Cadastro de professor
│   │   ├── services/                          ← Consumo de APIs
│   │   └── guards/                            ← Auth guards
│   └── index.html
├── package.json
└── angular.json
```

---

## 🎮 Fluxo da Aplicação

1. **Usuário acessa** `http://localhost:4200`
2. **Frontend Angular** é carregado
3. **Tela de Login** é exibida (se necessário)
4. **Dashboard** mostra os 4 jogos disponíveis
5. **Ao clicar em um jogo**, frontend faz requisição para:
   ```
   GET http://localhost:8080/api/jogos/1
   POST http://localhost:8080/api/bingo/sala/criar?professor=João
   ```
6. **Backend responde** com JSON
7. **Frontend renderiza** a interface do jogo

---

## ⚠️ Troubleshooting

### "CORS Error" no Console
- ✅ CORS já está configurado
- Se ainda assim falhar, verifique se backend está rodando em `http://localhost:8080`

### "Failed to connect to localhost:8080"
- ⚠️ Backend não está rodando
- Execute `.\mvnw spring-boot:run` na pasta `projeto-letramento`

### "npm: comando não encontrado"
- ⚠️ Node.js não está instalado
- Baixe em: https://nodejs.org/

### Build do Angular falhando
- Execute: `npm install` na pasta `projeto-letramento-front`

---

## 📌 Integração do Bingo

Os arquivos do Bingo foram integrados ao backend:
- ✅ **SalaJogo.java** (Model)
- ✅ **BingoService.java** (Service)
- ✅ **BingoSalaController.java** (REST API)
- ✅ **GameSelectionController.java** (Seleção de jogos)

O frontend Angular ainda precisa ser adaptado para chamar os endpoints `/api/bingo/sala/` em vez dos que estavam antes.

---

## 🚀 Próximos Passos

1. **Testar Bingo**: Abra o frontend e tente criar uma sala
2. **Adicionar outros 3 jogos**: Sigam o padrão MVC do Bingo
3. **Integrar autenticação**: JWT já está configurado no backend
4. **Banco de dados**: Postgres já está configurado no backend

---

## 📞 Resumo de Portas

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **Frontend** | http://localhost:4200 | Angular SPA |
| **Backend** | http://localhost:8080 | Spring Boot API |
| **BD** | localhost:5432 | PostgreSQL (se configurado) |

