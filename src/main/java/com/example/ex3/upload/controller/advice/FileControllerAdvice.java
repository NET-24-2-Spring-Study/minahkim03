package com.example.ex3.upload.controller.advice;

import com.example.ex3.upload.exception.UploadNotSupportedException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class FileControllerAdvice {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity.badRequest().body(Map.of("error", "File too large"));
    }

    @ExceptionHandler(UploadNotSupportedException.class)
    public ResponseEntity<?> handleUploadNotSupportedException(UploadNotSupportedException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
