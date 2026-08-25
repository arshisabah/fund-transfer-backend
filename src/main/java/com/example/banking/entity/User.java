package com.example.banking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.example.banking.enums.Role role;

    public User() {
    }

    public User(Long id, String name, com.example.banking.enums.Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.example.banking.enums.Role getRole() {
        return role;
    }

    public void setRole(com.example.banking.enums.Role role) {
        this.role = role;
    }
}
