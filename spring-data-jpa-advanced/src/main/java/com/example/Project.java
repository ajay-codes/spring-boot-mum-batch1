package com.example;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Generated;
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
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(fetch = jakarta.persistence.FetchType.LAZY, cascade = { jakarta.persistence.CascadeType.PERSIST,
            jakarta.persistence.CascadeType.MERGE })
    @JoinTable(name = "employees_projects", joinColumns = @jakarta.persistence.JoinColumn(name = "project_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "employee_id"))
    private List<Employee> employees = new ArrayList<>();

}
