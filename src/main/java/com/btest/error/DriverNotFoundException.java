package com.btest.error;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(Long id) {
        super("Driver id not found : " + id);
    }

}
