package com.example.ex2.repository.search;

import com.example.ex2.dto.TodoDTO;
import com.example.ex2.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoSearch {
    Page<TodoEntity> search1(Pageable pageable);
    Page<TodoDTO> searchDTO(Pageable pageable);
}
