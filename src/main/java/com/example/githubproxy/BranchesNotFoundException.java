package com.example.githubproxy;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BranchesNotFoundException extends RuntimeException {
    public BranchesNotFoundException(String username) {
        super("For user '" + username + "' not founded any branches");
    }
}