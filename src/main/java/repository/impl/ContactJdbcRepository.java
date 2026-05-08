package repository.impl;

import database.DatabaseConnection;
import exception.DatabaseIntegrityException;
import exception.ContactNotFoundException; // Importando a nova exceção
import model.Contact;
import repository.ContactRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactJdbcRepository implements ContactRepository {

    @Override
    public void save(Contact contact) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            int generateId = insertContact(connection, contact);
            contact.setId(generateId);
            insertPhones(connection, contact);
        } catch (SQLException exception) {
            if (exception.getMessage().contains("CHECK constraint failed")) {
                throw new DatabaseIntegrityException("A categoria '" + contact.getCategory() + "' não é permitida pelo banco de dados.", exception);
            }
            throw new DatabaseIntegrityException("Erro ao salvar contato completo", exception);
        }
    }

    private int insertContact(Connection conn, Contact contact) throws SQLException {
        String sql = "INSERT INTO contacts (name, email, category) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getEmail());
            preparedStatement.setString(3, contact.getCategory());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("Falha ao obter ID do contato.");
            }
        }
    }

    private void insertPhones(Connection conn, Contact contact) throws SQLException {
        if (contact.getPhones() == null || contact.getPhones().isEmpty()) return;

        String sql = "INSERT INTO phones (contact_id, phone_number) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            for (String phone : contact.getPhones()) {
                preparedStatement.setInt(1, contact.getId());
                preparedStatement.setString(2, phone);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                contacts.add(mapResultSetToContact(resultSet));
            }
        } catch (SQLException exception) {
            throw new DatabaseIntegrityException("Erro ao listar contatos", exception);
        }
        return contacts;
    }

    @Override
    public void update(Contact contact) {
        String sql = "UPDATE contacts SET name = ?, email = ?, category = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getEmail());
            preparedStatement.setString(3, contact.getCategory());
            preparedStatement.setInt(4, contact.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ContactNotFoundException("Não foi possível atualizar: Contato com ID " + contact.getId() + " não existe.");
            }

        } catch (SQLException exception) {
            throw new DatabaseIntegrityException("Erro ao atualizar contato", exception);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new ContactNotFoundException("Não foi possível remover: Contato com ID " + id + " não encontrado.");
            }

        } catch (SQLException exception) {
            throw new DatabaseIntegrityException("Erro ao deletar contato", exception);
        }
    }

    @Override
    public Optional<Contact> findByEmail(String email) {
        String sql = "SELECT * FROM contacts WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToContact(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseIntegrityException("Erro ao buscar por email", exception);
        }
        return Optional.empty();
    }

    @Override
    public List<Contact> findByName(String name) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE name LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    contacts.add(mapResultSetToContact(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseIntegrityException("Erro ao buscar por nome", exception);
        }
        return contacts;
    }

    @Override
    public Optional<Contact> findById(int id) {
        String sql = "SELECT * FROM contacts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseIntegrityException("Erro ao buscar contato por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Contact> findByCategory(String category) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE category = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseIntegrityException("Erro ao filtrar por categoria", e);
        }
        return contacts;
    }

    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id"));
        contact.setName(rs.getString("name"));
        contact.setEmail(rs.getString("email"));
        contact.setCategory(rs.getString("category"));
        return contact;
    }
}