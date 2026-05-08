package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Contact {
    private Integer id;
    private String name;
    private String email;
    private String category;
    private List<String> phones = new ArrayList<>();
    private LocalDateTime createdAt;

    public Contact(){}

    public Contact(String name, String email, String category) {
        this.name = name;
        this.email = email;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
