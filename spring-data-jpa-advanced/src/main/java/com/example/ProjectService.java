package com.example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public void createProjectWithEmployees() {

        Employee emp1 = new Employee();
        emp1.setName("John Doe");
        employeeRepository.save(emp1);

        Employee emp2 = new Employee();
        emp2.setName("Jane Smith");
        employeeRepository.save(emp2);

        Project project = new Project();
        project.setName("Project Alpha");
        project.getEmployees().add(emp1);
        project.getEmployees().add(emp2);
        projectRepository.save(project);
    }

    public void getProjectWithEmployees(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        System.out.println("Project Name: " + project.getName());
        System.out.println("Employees:");
        for (Employee emp : project.getEmployees()) {
            System.out.println("- " + emp.getName());
        }
    }

    public void getEmployeesByProjectName(String projectName) {
        java.util.List<Employee> employees = employeeRepository.findByProjects_Name(projectName);
        System.out.println("Employees working on " + projectName + ":");
        for (Employee emp : employees) {
            System.out.println("- " + emp.getName());
        }
    }

}
