package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistance.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.server.ExportException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService() {
        //TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        //TodoEntity 저장
        repository.save(entity);
        //TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        //Validations
        validate(entity);

        //엔티티를 데이터베이스에 저장하고 로그를 남긴다
        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());

        //저장된 엔티티를 포함하는 새 리스트를 리턴한다
        return repository.findByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entitiy connot be null");
        }
        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> updata(final TodoEntity entity) {
        //1.저장할 엔티티가 유효한지 확인한다. Create Todo에서 구현
        validate(entity);

        //2.넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다.
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            //3.반환된 TodoEntity가 존재하면 값을 새 entity값으로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            //4.데이터베이스에 새 값을 저장한다.
            repository.save(todo);
        });

        //Retrieve 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴한다.
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        //1.저장할 엔티티가 유효한지 확인한다.
        validate(entity);

        try {
            //2.엔티티를 삭제한다.
            repository.delete(entity);
        } catch (Exception e) {
            //3.exception 발생 시 id와 exception을 로깅한다.
            log.error("error deleting entity " + entity.getId());

            //4.컨트롤러로 exception을 보낸다. 데이터베이스 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        //5.새 Todo 리스트를 가져와 리턴한다.
        return retrieve(entity.getUserId());
    }
}
