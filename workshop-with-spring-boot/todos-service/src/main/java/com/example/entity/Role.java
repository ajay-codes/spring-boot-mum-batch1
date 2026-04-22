package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles", schema = "public")
public class Role {
    @Id
    private Long id;
    private String name;

    public Role() {
    }

    public Role(Long id) {
    }
}
