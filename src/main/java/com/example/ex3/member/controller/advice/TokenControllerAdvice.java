package com.example.ex3.member.controller.advice;

import com.example.ex3.member.exception.MemberTaskException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class TokenControllerAdvice {

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<Map<String, String>> handleTaskException(MemberTaskException exception) {
        log.error(exception.getMessage());

        String msg = exception.getMsg();
        int status = exception.getCode();

        Map<String, String> map = Map.of("error", msg);
        return ResponseEntity.status(status).body(map);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        log.info("handleAccessDeniedException..............");
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", exception.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    }
}
