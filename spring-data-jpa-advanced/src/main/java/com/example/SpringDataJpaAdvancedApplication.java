package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SpringDataJpaAdvancedApplication {

	@Transactional
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringDataJpaAdvancedApplication.class, args);
		// BoyGirlService boyGirlService = context.getBean(BoyGirlService.class);
		// boyGirlService.doSomething();
		ProjectService projectService = context.getBean(ProjectService.class);
		// projectService.createProjectWithEmployees();
		// projectService.getProjectWithEmployees(1L);
		projectService.getEmployeesByProjectName("Project Alpha");
	}
}
