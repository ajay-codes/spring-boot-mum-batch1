package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.entity.Todo;
import com.example.repository.TodoRepository;

@SpringBootApplication
public class TodosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodosServiceApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner commandLineRunner(TodoRepository todoRepository) {
	// return args -> {
	// Todo todo1 = new Todo();
	// todo1.setTitle("Learn Spring Boot");
	// todo1.setCompleted(false);
	// todoRepository.save(todo1);
	// };
	// }

}
