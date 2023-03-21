package com.weather.project.service.exception;

public class DataNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(Exception e) {
        super(e);
    }

    public DataNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
