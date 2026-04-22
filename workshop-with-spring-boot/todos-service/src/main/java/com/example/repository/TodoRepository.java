package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId")
    List<Todo> findByUserId(Long userId);

    @Query("SELECT t FROM Todo t WHERE t.id = :id AND t.user.id = :userId")
    Optional<Todo> findByIdAndUserId(Long id, Long userId);

}
