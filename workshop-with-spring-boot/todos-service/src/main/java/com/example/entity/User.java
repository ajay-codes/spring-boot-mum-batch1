package com.example.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    @OneToMany(targetEntity = Role.class, cascade = jakarta.persistence.CascadeType.REFRESH, orphanRemoval = true)
    @JoinTable(name = "user_roles", schema = "public", joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "role_id"))
    List<Role> roles = new ArrayList<>();
}
