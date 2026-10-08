package com.springk48.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String msg) {
        super(msg);
    }

}
