package org.acrobatt.project.except;

public class UserNotFoundException extends Exception {

    /**
     * The constructor
     */
    public UserNotFoundException() {
    }

    /**
     * The constructor with a message
     * @param message the message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
