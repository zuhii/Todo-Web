package com.example.demo.persistance;

import com.example.demo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{

    //?1은 메서드의 매개변수 순서 위치
    @Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1")
    List<TodoEntity> findByUserId(String userId);
}
