package service;

import exception.ContactNotFoundException;
import exception.DatabaseIntegrityException;
import model.Contact;
import repository.ContactRepository;
import util.Validation;

import java.util.List;
import java.util.Optional;

public class ContactService {
    private final ContactRepository contactRepository;
    private final ExportService<Contact> exportService;

    public ContactService(ContactRepository contactRepository, ExportService<Contact> exportService){
        this.contactRepository = contactRepository;
        this.exportService = exportService;
    }

    public void saveContact(Contact contact) throws Exception {
        validateContact(contact);

        Optional<Contact> existing = contactRepository.findByEmail(contact.getEmail());
        if (existing.isPresent()) {
            throw new DatabaseIntegrityException("Este e-mail já está em uso.");
        }

        contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(int id) {
        return contactRepository.findById(id);
    }

    public List<Contact> getContactsByName(String name) {
        return contactRepository.findByName(name);
    }

    public Optional<Contact> getContactByEmail(String email) {
        return contactRepository.findByEmail(email);
    }

    public void updateContact(Contact contact) {
        validateContact(contact);

        if (contactRepository.findById(contact.getId()).isEmpty()) {
            throw new ContactNotFoundException("Não foi possível atualizar: Contato com ID " + contact.getId() + " não encontrado.");
        }

        contactRepository.update(contact);
    }

    public List<Contact> getContactsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }

        return contactRepository.findByCategory(category);
    }

    public void deleteContact(int id) throws Exception {
        if(contactRepository.findById(id).isEmpty()) {
            throw new ContactNotFoundException("Não foi possível excluir: Contato com ID " + id + " não encontrado.");
        }
        contactRepository.delete(id);
    }

    private void validateContact(Contact contact) {
        if (contact.getName() == null || contact.getName().trim().isEmpty()){
            throw new DatabaseIntegrityException("O nome é obrigatório!");
        }

        if (!Validation.isValidEmail(contact.getEmail())) {
            throw new DatabaseIntegrityException("O formato do e-mail é inválido!");
        }

        if (contact.getPhones() != null) {
            for (String phone : contact.getPhones()) {
                if (!Validation.isValidPhone(phone)) {
                    throw new DatabaseIntegrityException("Formato de telefone inválido: " + phone);
                }
            }
        }
    }

    public void exportContacts(String path) throws Exception {
        List<Contact> contacts = contactRepository.findAll();
        exportService.export(contacts, path);
    }

    public List<Contact> getContactsPaged(int limit, int offset) {
        if (limit <= 0) limit = 10;
        return contactRepository.findAllPaged(limit, offset);
    }
}
