package com.koval.storage.exception;

/**
 * Created by Volodymyr Kovalenko
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String fileId) {
        super("Not found " + fileId);
    }
}
