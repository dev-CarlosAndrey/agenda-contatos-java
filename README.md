# Sistema de Agenda de Contatos - Console Java

Este projeto consiste em um sistema de gerenciamento de contatos executado via console, utilizando Java com persistência em banco de dados relacional via JDBC. O desenvolvimento foi orientado pelos princípios SOLID para garantir um código robusto, organizado e de fácil manutenção.

## 🚀 Objetivo do Projeto
Desenvolver uma aplicação capaz de realizar o CRUD completo de contatos, integrando conceitos de Programação Orientada a Objetos Avançada e persistência de dados sem o uso de frameworks ORM.

## 🏗️ Arquitetura e Princípios SOLID
A arquitetura do projeto foi desenhada para demonstrar a aplicação dos cinco princípios SOLID:

* **S - Single Responsibility Principle (Responsabilidade Única)**: O projeto é separado em pacotes distintos. A `DatabaseConnection` gerencia apenas a conexão, o `ContactRepository` cuida do acesso a dados, o `ContactService` trata as validações e o `ContactMenu` gerencia a interface.
* **O - Open/Closed Principle (Aberto/Fechado)**: Utilizamos interfaces para os repositórios (`ContactRepository`), permitindo que novas implementações de banco de dados sejam criadas sem alterar o código existente.
* **L - Liskov Substitution Principle (Substituição de Liskov)**: O sistema utiliza referências de interfaces, garantindo que as implementações possam ser substituídas sem quebrar o comportamento esperado.
* **I - Interface Segregation Principle (Segregação de Interfaces)**: As interfaces são específicas para as necessidades do domínio (ex: CRUD e Buscas), evitando interfaces gigantescas e métodos desnecessários.
* **D - Dependency Inversion Principle (Inversão de Dependências)**: O `ContactService` recebe a interface do repositório via injeção de dependência por construtor, não dependendo de uma implementação JDBC específica.

## 🛠️ Tecnologias Utilizadas
* **Java 17**: Linguagem base do projeto.
* **JDBC**: Comunicação exclusiva com o banco de dados.
* **SQLite**: Banco de dados relacional (arquivo local).
* **PreparedStatement**: Utilizado em todas as queries para prevenir SQL Injection.
* **SLF4J**: Implementação de log para monitoramento de eventos.
* **Gemini 1.5 Flash (Google AI)**: Utilizado como copiloto de desenvolvimento para auxílio na refatoração de código, implementação de padrões SOLID e documentação técnica.

## 📋 Funcionalidades (CRUD)
O sistema oferece as seguintes operações via menu numérico:
1. **Cadastrar Contato**: Inserção com validação de campos obrigatórios (nome e e-mail único).
2. **Listar Todos**: Exibição tabular organizada de todos os registros.
3. **Buscar Contato**: Localização por nome (parcial) ou e-mail exato.
4. **Atualizar Contato**: Edição de dados existentes identificados pelo ID.
5. **Remover Contato**: Exclusão permanente com necessidade de confirmação.
6. **Listar por Categoria**: Filtro por classificações (Amigo, Trabalho, Família, Outro).
7. **Sair**: Encerramento seguro do sistema.

## 📂 Estrutura de Pacotes
O projeto segue a organização sugerida para separação de responsabilidades:
* `model`: Classes de domínio (`Contact`).
* `repository`: Interface `ContactRepository`.
* `repository.impl`: Implementação JDBC (`ContactJdbcRepository`).
* `service`: Regras de negócio e validações.
* `ui`: Menus e interação com o usuário (`ContactMenu`).
* `database`: Gerenciamento da conexão e `schema.sql`.
* `exception`: Exceções personalizadas como `ContactNotFoundException`.

## 🔧 Configuração e Execução
1. **Banco de Dados**: O arquivo `schema.sql` na raiz contém o script de criação das tabelas.
2. **Execução**:
    * Certifique-se de que o JDK 17 está instalado.
    * Compile o projeto garantindo que os drivers JDBC e SLF4J estejam no classpath.
    * Execute a classe `Main.java`, que realiza a injeção manual de dependências.

---
**Desenvolvedor:** Carlos Andrey Bezerra Henrique <br>
Desenvolvido como critério de avaliação para a disciplina de **Programação Orientada a Objetos Avançada**.