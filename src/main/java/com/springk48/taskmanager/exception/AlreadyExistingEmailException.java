package com.springk48.taskmanager.exception;

public class AlreadyExistingEmailException extends RuntimeException {

    public AlreadyExistingEmailException(String msg) {
        super(msg);
    }

}
