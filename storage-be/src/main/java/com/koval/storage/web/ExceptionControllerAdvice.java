package com.koval.storage.web;

import com.koval.storage.domain.ErrorMessage;
import com.koval.storage.exception.EntityNotFoundException;
import com.koval.storage.exception.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Volodymyr Kovalenko
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(StorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleStorageException(StorageException ex) {
        return new ErrorMessage("StorageException", ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorMessage("EntityNotFoundException", ex.getMessage());
    }
}
