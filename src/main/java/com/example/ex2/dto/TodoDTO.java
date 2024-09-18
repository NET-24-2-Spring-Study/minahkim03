package com.example.ex2.dto;

import com.example.ex2.entity.TodoEntity;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TodoDTO {
    private Long mno;

    @NotEmpty
    private String title;

    @NotEmpty
    private String writer;

    @FutureOrPresent
    private LocalDate dueDate;

    public TodoDTO(TodoEntity todoEntity) {
        this.mno = todoEntity.getMno();
        this.title = todoEntity.getTitle();
        this.writer = todoEntity.getWriter();
        this.dueDate = todoEntity.getDueDate();
    }

    public TodoEntity toEntity() {
        return TodoEntity.builder()
            .mno(mno)
            .title(title)
            .writer(writer)
            .dueDate(dueDate)
            .build();
    }

}
