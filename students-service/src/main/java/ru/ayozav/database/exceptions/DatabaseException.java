package ru.ayozav.database.exceptions;

public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super("[DATABASE EXCEPTION]" + message);
    }
}
