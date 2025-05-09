package com.example.spring_book_ch4.exception;

import org.springframework.dao.DataAccessException;

public class UpdateFailedException extends DataAccessException {
    public UpdateFailedException(String msg) {
        super(msg);
    }
}
