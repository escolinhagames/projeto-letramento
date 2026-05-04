# Projeto Letramento

Este projeto Java usa Maven e está estruturado como uma aplicação Spring Boot.

## Como abrir

1. Abra o VS Code.
2. Selecione `Arquivo` > `Abrir Pasta...` e escolha `c:\Users\lucas\projeto-letramento`.
3. O arquivo principal do projeto está em `src/main/java/br/com/projeto_letramento/projeto_letramento/ProjetoLetramentoApplication.java`.

## Como executar

No terminal do projeto, execute:

```powershell
./mvnw spring-boot:run
```

Para executar com configurações de desenvolvimento (application-dev.properties):

```powershell
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Se estiver usando Windows PowerShell, também pode executar:

```powershell
.\mvnw.cmd spring-boot:run
```

Ou com perfil dev:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

## Configuração

As configurações ficam em `src/main/resources/application.properties`.

## Estrutura principal

- `src/main/java`: código-fonte Java
- `src/main/resources`: recursos e configurações
- `pom.xml`: definição do Maven
