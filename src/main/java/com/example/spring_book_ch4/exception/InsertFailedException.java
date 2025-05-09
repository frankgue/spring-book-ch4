package com.example.spring_book_ch4.exception;

import org.springframework.dao.DataAccessException;

public class InsertFailedException extends DataAccessException {
    public InsertFailedException(String msg) {
        super(msg);
    }
}
