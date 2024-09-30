package com.example.ex3.product.exception;

public class ProductTaskException extends RuntimeException {
    private int code;
    private String message;

    public ProductTaskException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
