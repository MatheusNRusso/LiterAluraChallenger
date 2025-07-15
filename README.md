---
# 📘 Challenge Literalura

Este projeto Java Spring Boot integra dados da [API do Projeto Gutenberg](https://gutendex.com/) e tem como objetivo permitir que usuários consultem, filtrem e exportem informações sobre livros e autores clássicos da literatura mundial.

A aplicação oferece funcionalidades completas, incluindo persistência no banco de dados, exportação de dados em múltiplos formatos e geração de estatísticas literárias, tudo acessível por meio de um menu interativo no terminal.

---

## ✨ Funcionalidades

- 🔍 Buscar livros por título
- 📚 Listar livros registrados
- 👤 Listar autores registrados
- 📆 Filtrar autores por intervalo de nascimento
- 🌍 Listar livros por idioma
- ⭐ Top 5 livros mais baixados
- 🔠 Buscar autores por nome
- 📤 Exportar dados (livros/autores) em **JSON** ou **CSV**
- 📊 Exibir estatísticas como:
    - Total de livros e autores
    - Média de downloads por idioma
    - Livro mais baixado por idioma
    - Autor com mais livros
    - Expectativa de vida média dos autores

---

## 🛠 Tecnologias utilizadas

- Java 17
- Spring Boot 3
- Maven
- JPA + Hibernate
- MariaDB (ou qualquer banco relacional)
- Jackson (JSON)
- OpenCSV (CSV)
- Flyway (migrations)

---

## 🚀 Como executar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/MatheusNRusso/LiterAluraChallenger.git
````

2. Configure o banco de dados em `src/main/resources/application.properties`.

3. Execute o projeto:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Acompanhe as instruções no terminal para interagir com o menu.

---

## 🗂 Estrutura

```
📁 src
 ┣ 📂 main
 ┃ ┣ 📂 java
 ┃ ┃ ┗━━ br.com.mylocal.literalura
 ┃ ┃     ┣ 📁 model          ← Entidades Book e Author
 ┃ ┃     ┣ 📁 service        ← Serviços de negócio, exportação e estatísticas
 ┃ ┃     ┣ 📁 repository     ← Interfaces JPA para acesso ao banco
 ┃ ┃     ┣ 📁 dto            ← Data Transfer Objects para exportação
 ┃ ┃     ┣ 📁 principal      ← Classe Principal com menu e fluxo principal
 ┃ ┗ 📂 resources
 ┃   ┗━━ application.properties
```

---

## 📎 Link do repositório

🔗 [https://github.com/MatheusNRusso/LiterAluraChallenger](https://github.com/MatheusNRusso/LiterAluraChallenger)

---

## 🙌 Contribuição

Este projeto foi desenvolvido como parte do Challenge da formação **Java Backend da Alura**.

```

---