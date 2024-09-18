package com.example.ex2.service;

import com.example.ex2.dto.PageRequestDTO;
import com.example.ex2.dto.TodoDTO;
import com.example.ex2.entity.TodoEntity;
import com.example.ex2.exception.EntityNotFoundException;
import com.example.ex2.repository.TodoRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoDTO register(TodoDTO todoDTO) {

        TodoEntity todoEntity = todoDTO.toEntity();
        todoRepository.save(todoEntity);
        return new TodoDTO(todoEntity);
    }

    public TodoDTO read(Long mno) {
        Optional<TodoDTO> result = todoRepository.getDTO(mno);
        TodoDTO todoDTO = result.orElseThrow(
            () -> new EntityNotFoundException("Todo " + mno + " not found")
        );
        return todoDTO;
    }

    public void remove(Long mno) {
        Optional<TodoEntity> result = todoRepository.findById(mno);
        TodoEntity todoEntity = result.orElseThrow(
            () -> new EntityNotFoundException("Todo " + mno + " not found")
        );
        todoRepository.delete(todoEntity);
    }

    public TodoDTO modify(TodoDTO todoDTO) {
        Optional<TodoEntity> result = todoRepository.findById(todoDTO.getMno());
        TodoEntity todoEntity = result.orElseThrow(
            () -> new EntityNotFoundException("Todo " + todoDTO.getMno() + " not found")
        );
        todoEntity.changeTitle(todoDTO.getTitle());
        todoEntity.changeWriter(todoDTO.getWriter());
        todoEntity.changeDueDate(todoDTO.getDueDate());

        return new TodoDTO(todoEntity);
    }

    public Page<TodoDTO> getList(PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by("mno").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        return todoRepository.searchDTO(pageable);
    }
}
