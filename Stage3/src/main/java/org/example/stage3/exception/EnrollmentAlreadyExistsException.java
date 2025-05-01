// EnrollmentAlreadyExistsException.java
package org.example.stage3.exception;

public class EnrollmentAlreadyExistsException extends RuntimeException {
    public EnrollmentAlreadyExistsException(String message) {
        super(message);
    }
}