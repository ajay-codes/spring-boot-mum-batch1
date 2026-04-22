package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "todos", schema = "public")
public class Todo {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private TodoCategory category;
    private boolean completed;
    @ManyToOne(targetEntity = User.class, cascade = jakarta.persistence.CascadeType.REFRESH)
    @JoinTable(name = "user_todos", schema = "public", joinColumns = @jakarta.persistence.JoinColumn(name = "todo_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "user_id"))
    private User user;

}
