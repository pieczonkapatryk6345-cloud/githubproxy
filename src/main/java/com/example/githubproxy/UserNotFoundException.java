package com.example.githubproxy;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User '" + username + "' not found");
    }
}