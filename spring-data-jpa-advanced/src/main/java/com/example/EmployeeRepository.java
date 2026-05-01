package com.example;

import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends org.springframework.data.jpa.repository.JpaRepository<Employee, Long> {

    // JPQL
    @Query("SELECT e FROM Employee e JOIN e.projects p WHERE p.name =:projectName")
    // Native SQL
    // @Query(value = "SELECT e.* FROM employees e JOIN employees_projects ep ON
    // e.id = ep.employee_id JOIN projects p ON ep.project_id = p.id WHERE p.name =
    // :projectName", nativeQuery = true)
    public java.util.List<Employee> findByProjects_Name(String projectName);

}
