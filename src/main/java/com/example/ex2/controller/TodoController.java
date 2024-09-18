package com.example.ex2.controller;

import com.example.ex2.dto.PageRequestDTO;
import com.example.ex2.dto.TodoDTO;
import com.example.ex2.service.TodoService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v/todos")
@Log4j2
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("")
    public ResponseEntity<TodoDTO> register(@RequestBody TodoDTO todoDTO) {
        log.info("register.............");
        log.info(todoDTO);

        todoDTO.setMno(null);

        return ResponseEntity.ok(todoService.register(todoDTO));
    }

    @GetMapping("/{mno}")
    public ResponseEntity<TodoDTO> read(@PathVariable("mno") Long mno) {
        log.info("read................");
        log.info(mno);

        return ResponseEntity.ok(todoService.read(mno));
    }

    @PutMapping("/{mno}")
    public ResponseEntity<TodoDTO> modify(@PathVariable("mno") Long mno, @RequestBody TodoDTO todoDTO) {

        log.info("modify..............");
        log.info(mno);
        log.info(todoDTO);

        todoDTO.setMno(mno);
        TodoDTO modifiedTodoDTO = todoService.modify(todoDTO);

        return ResponseEntity.ok(modifiedTodoDTO);
    }

    @DeleteMapping("/{mno}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable("mno") Long mno) {

        log.info("remove..............");
        log.info(mno);

        todoService.remove(mno);

        Map<String, String> result = Map.of("result", "success");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<TodoDTO>> list(@Validated PageRequestDTO pageRequestDTO) {

        log.info("list................");

        return ResponseEntity.ok(todoService.getList(pageRequestDTO));
    }
}
