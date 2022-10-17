package com.btest.error;

import java.util.Set;

public class DriverUnSupportedFieldPatchException extends RuntimeException {

    public DriverUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
