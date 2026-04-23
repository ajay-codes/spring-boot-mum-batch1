package com.example;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;

    @ManyToMany(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinTable(name = "employees_projects", joinColumns = @jakarta.persistence.JoinColumn(name = "employee_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "project_id"))
    private List<Project> projects;

}
