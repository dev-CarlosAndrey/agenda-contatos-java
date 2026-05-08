package ui;

import model.Contact;
import service.ContactService;
import util.Validation;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ContactMenu {
    private final ContactService contactService;
    private final Scanner scanner;

    public ContactMenu(ContactService contactService) {
        this.contactService = contactService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option = -1;
        while (option != 7) {
            System.out.println("\n--- AGENDA DE CONTATOS ---");
            System.out.println("1. Cadastrar Contato");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar Contato");
            System.out.println("4. Atualizar Contato");
            System.out.println("5. Remover Contato");
            System.out.println("6. Listar por Categoria");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
                handleOption(option);
            } catch (Exception exception) {
                System.out.println("Erro: " +exception.getMessage());
            }
        }
    }

    private void handleOption(int option) throws Exception {
        switch (option) {
            case 1 -> register();
            case 2 -> listAll();
            case 3 -> search();
            case 4 -> update();
            case 5 -> remove();
            case 6 -> listByCategory();
            case 7 -> System.out.println("Saindo do sistema...");
            default -> System.out.println("Opção inválida!");
        }
    }

    private void register() {
        try {
            System.out.println("\n--- NOVO CADASTRO ---");
            System.out.print("Nome: ");
            String name = scanner.nextLine();
            String email;
            while (true){
                System.out.print("E-mail: ");
                email = scanner.nextLine();
                if (Validation.isValidEmail(email)){
                    break;
                }
                System.out.println("Formato de e-mail inválido! Tente novamente (ex: usuario@email.com).");
            }

            System.out.print("Categoria (Amigos, Trabalho, Familia, Outros): ");
            String category = scanner.nextLine();

            Contact contact = new Contact(name, email, category);

            System.out.print("Digite o telefone (ou deixe vazio para finalizar): ");
            String phoneInput = scanner.nextLine();

            while (!phoneInput.isEmpty()) {

                String cleanPhone = phoneInput.replaceAll("[\\s()+-]", "");

                if (Validation.isValidPhone(cleanPhone)){
                    contact.getPhones().add(cleanPhone);
                    System.out.println("Telefone adicionado! Digite outro (ou deixe vazio): ");
                } else {
                    System.out.println("Formato de telefone inválido! Use DDD + números (ex: 88999448003).");
                    System.out.print("Tente novamente: ");
                }
                phoneInput = scanner.nextLine();
            }

            contactService.saveContact(contact);
            System.out.println("Contato salvo com sucesso!");
        } catch (Exception e) {
            System.out.println("Não foi possível salvar: " + e.getMessage());
        }
    }

    private void listAll() {
        System.out.println("\n--- LISTA DE TODOS OS CONTATOS ---");
        System.out.printf("%-5s | %-20s | %-25s | %-10s\n", "ID", "NOME", "E-MAIL", "CATEGORIA");
        contactService.getAllContacts().forEach(c ->
                System.out.printf("%-5d | %-20s | %-25s | %-10s\n", c.getId(), c.getName(), c.getEmail(), c.getCategory())
        );
    }

    private void remove() throws Exception {
        System.out.print("Digite o ID do contato que deseja remover: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Tem certeza que deseja excluir este contato? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            contactService.deleteContact(id);
            System.out.println("Contato removido com sucesso.");
        } else {
            System.out.println("Operação de remoção cancelada.");
        }
    }

    private void search() {
        System.out.println("\n--- BUSCAR CONTATO ---");
        System.out.println("1. Por ID");
        System.out.println("2. Por Nome");
        System.out.println("3. Por E-mail");
        System.out.print("Escolha o tipo de busca: ");

        try {
            int type = Integer.parseInt(scanner.nextLine());
            switch (type) {
                case 1 -> {
                    System.out.println("Digite o ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    contactService.getContactById(id).ifPresentOrElse(
                            this::displayContactDetail,
                            () -> System.out.println("Contato não encontrado.")
                    );
                }
                case 2 -> {
                    System.out.print("Digite o Nome: ");
                    String name = scanner.nextLine();
                    var contacts = contactService.getContactsByName(name);
                    if (contacts.isEmpty()) {
                        System.out.println("Nenhum contato encontrado.");
                    } else {
                        contacts.forEach(this::displayContactDetail);
                    }
                }
                case 3 -> {
                    System.out.print("Digite o E-mail: ");
                    String email = scanner.nextLine();
                    contactService.getContactByEmail(email).ifPresentOrElse(
                            this::displayContactDetail,
                            () -> System.out.println("⚠️ E-mail não cadastrado.")
                    );
                }
                default -> System.out.println("Opção de busca inválida.");
            }
        } catch (Exception exception) {
            System.out.println("Erro na busca: " + exception.getMessage());
        }
    }

    private void displayContactDetail(Contact c) {
        System.out.println("\n----------------------------");
        System.out.println("ID: " + c.getId());
        System.out.println("Nome: " + c.getName());
        System.out.println("E-mail: " + c.getEmail());
        System.out.println("Categoria: " + c.getCategory());

        if (c.getPhones() != null && !c.getPhones().isEmpty()) {
            System.out.println("Telefones:");
            c.getPhones().forEach(p -> System.out.println("  - " + p));
        } else {
            System.out.println("Telefones: (Nenhum cadastrado)");
        }
        System.out.println("----------------------------");
    }

    private void update() {
        try {
            System.out.print("Digite o ID do contato que deseja atualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Optional<Contact> optionalContact = contactService.getContactById(id);

            if (optionalContact.isPresent()) {
                Contact contact = optionalContact.get();
                System.out.println("\n--- Editando: " + contact.getName() + " ---");

                System.out.print("Novo Nome (ou Enter para manter '" + contact.getName() + "'): ");
                String newName = scanner.nextLine();
                if (!newName.isEmpty()) contact.setName(newName);

                while (true) {
                    System.out.print("Novo E-mail (ou Enter para manter '" + contact.getEmail() + "'): ");
                    String newEmail = scanner.nextLine();
                    if (newEmail.isEmpty()) break;
                    if (Validation.isValidEmail(newEmail)) {
                        contact.setEmail(newEmail);
                        break;
                    }
                    System.out.println("Formato de e-mail inválido!");
                }

                System.out.println("Telefones atuais: " + (contact.getPhones().isEmpty() ? "Nenhum" : contact.getPhones()));
                System.out.print("Deseja (A)dicionar novo, (L)impar todos ou (M)anter atual? ");
                String phoneOp = scanner.nextLine().toUpperCase();

                if (phoneOp.equals("A")) {
                    System.out.print("Digite o novo telefone (DDDNúmero): ");
                    String newPhone = scanner.nextLine().replaceAll("[\\s()+-]", "");
                    if (Validation.isValidPhone(newPhone)) {
                        contact.getPhones().add(newPhone);
                    } else {
                        System.out.println("Formato inválido!");
                    }
                } else if (phoneOp.equals("L")) {
                    contact.getPhones().clear();
                }

                contactService.updateContact(contact);
                System.out.println("Contato atualizado com sucesso!");

            } else {
                System.out.println("Contato não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


    private void listByCategory() {
        System.out.println("\n--- FILTRAR POR CATEGORIA ---");
        System.out.print("Digite a categoria (Friend, Work, Family, Other): ");
        String category = scanner.nextLine();

        List<Contact> contacts = contactService.getContactsByCategory(category);

        if (contacts.isEmpty()) {
            System.out.println("Nenhum contato encontrado nesta categoria.");
        } else {
            System.out.println("\n--- CONTATOS NA CATEGORIA: " + category.toUpperCase() + " ---");
            System.out.printf("%-5s | %-20s | %-25s\n", "ID", "NOME", "E-MAIL");
            contacts.forEach(c ->
                    System.out.printf("%-5d | %-20s | %-25s\n", c.getId(), c.getName(), c.getEmail())
            );
        }
    }

}
