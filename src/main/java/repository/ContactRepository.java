package repository;

import model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    void save(Contact contact);
    List<Contact> findAll();
    void update(Contact contact);
    void delete(int id);
    List<Contact> findAllPaged(int limit, int offset);

    Optional<Contact> findById(int id);
    List<Contact> findByName(String name);
    Optional<Contact> findByEmail(String email);
    List<Contact> findByCategory(String category);
}
