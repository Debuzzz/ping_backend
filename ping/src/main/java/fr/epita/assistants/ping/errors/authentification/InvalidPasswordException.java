package fr.epita.assistants.ping.errors.authentification;

import jakarta.ws.rs.NotFoundException;

public class InvalidPasswordException extends NotFoundException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
