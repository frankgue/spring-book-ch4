package com.example.spring_book_ch4.exception;

import org.springframework.dao.DataAccessException;

public class DeleteFailedException extends DataAccessException {
    public DeleteFailedException(String msg) {
        super(msg);
    }
}
