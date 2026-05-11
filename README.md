# Sistema de Agenda de Contatos - Console Java

Este projeto consiste em um sistema de gerenciamento de contatos executado via console, utilizando Java com persistência em banco de dados relacional via JDBC. O desenvolvimento foi orientado pelos princípios SOLID para garantir um código robusto, organizado e de fácil manutenção.

## 🚀 Novidades: Bônus Implementados
Nesta versão, o sistema foi expandido com funcionalidades avançadas que demonstram domínio em persistência e arquitetura:
* **Suporte a Múltiplos Telefones**: Relacionamento 1:N no banco de dados com tabela própria (`phones`).
* **Paginação de Resultados**: Listagem otimizada de contatos utilizando `LIMIT` e `OFFSET` no SQL para melhor performance.
* **Exportação CSV**: Módulo profissional de exportação de dados com suporte a caracteres especiais (UTF-8) e injeção de dependência.

## 🎯 Objetivo do Projeto
Desenvolver uma aplicação capaz de realizar o CRUD completo de contatos, integrando conceitos de Programação Orientada a Objetos Avançada e persistência de dados sem o uso de frameworks ORM (como Hibernate ou JPA).

## 🏛️ Arquitetura e Princípios SOLID
A arquitetura do projeto demonstra a aplicação prática dos cinco princípios fundamentais:

* **S - SRP (Single Responsibility Principle)**: Separação total de responsabilidades. A lógica de exportação de arquivos foi movida para o `ExportService`, garantindo que o `ContactService` foque apenas em regras de negócio.
* **O - OCP (Open/Closed Principle)**: O sistema de exportação e o repositório utilizam interfaces. É possível adicionar novos formatos (como PDF) ou mudar o banco de dados sem alterar o código do Menu ou do Service.
* **L - LSP (Liskov Substitution Principle)**: As referências para as implementações são feitas via interfaces, garantindo que qualquer implementação de repositório ou exportação funcione sem quebrar o sistema.
* **I - ISP (Interface Segregation Principle)**: Interfaces específicas para cada domínio de funcionalidade, evitando métodos desnecessários em classes implementadoras.
* **D - DIP (Dependency Inversion Principle)**: O `ContactService` depende de abstrações (`ContactRepository` e `ExportService`), injetadas via construtor na classe `Main`.

## 🛠️ Tecnologias Utilizadas
* **Java 17**: Versão LTS utilizada para o desenvolvimento.
* **JDBC & SQLite**: Persistência relacional utilizando **Transactions (Commit/Rollback)** para garantir a atomicidade ao salvar contatos e seus múltiplos telefones.
* **PreparedStatement**: Segurança contra ataques de **SQL Injection**.
* **Gemini 3 Flash (Google AI)**: Utilizado como copiloto para suporte em refatoração arquitetural, validação de padrões SOLID e documentação técnica.

## ✨ Funcionalidades
1. **Cadastrar Contato**: Nome, e-mail único, categoria e múltiplos telefones vinculados.
2. **Listar Todos**: Exibição simplificada de todos os registros.
3. **Buscar Contato**: Busca detalhada por ID, Nome ou E-mail, exibindo todos os dados e telefones.
4. **Atualizar Contato**: Edição de dados e gestão dinâmica de telefones (adicionar novos ou limpar lista).
5. **Remover Contato**: Exclusão permanente do contato (com remoção em cascata dos telefones associados).
6. **Listar por Categoria**: Filtro por classificações (Amigos, Trabalho, Família, Outros).
7. **Listar com Paginação (Bônus)**: Navegação entre páginas (5 registros por vez) via console.
8. **Exportar para CSV (Bônus)**: Gera o arquivo `agenda_contatos.csv` na raiz do projeto, compatível com Excel.

## 📁 Estrutura de Pacotes
* `model`: Classes de domínio (`Contact`).
* `repository`: Interface e implementação JDBC (`ContactJdbcRepository`).
* `service`: Regras de negócio e interfaces de serviço.
* `service.impl`: Implementações específicas (ex: `ContactCsvExportService`).
* `ui`: Interface de usuário e controle de fluxo (`ContactMenu`).
* `database`: Gerenciamento de conexão e scripts SQL.
* `util`: Validadores e utilitários de sistema.
* `exception`: Exceções personalizadas para controle de erros.

## ⚙️ Configuração e Execução
1. **Banco de Dados**: O sistema gerencia as tabelas `contacts` e `phones` via script SQL.
2. **Execução**:
   - Execute a classe `Main.java` (que contém a injeção manual de dependências).
   - Utilize o menu interativo para navegar pelas funções.
   - O arquivo CSV exportado será criado no diretório raiz onde o projeto está sendo executado.

---
**Desenvolvedor:** Carlos Andrey Bezerra Henrique  
**Disciplina:** Programação Orientada a Objetos Avançada  
**Instituição:** Faculdade Princesa do Oeste - FPO