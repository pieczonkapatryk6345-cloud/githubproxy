package com.example.githubproxy;

public record ErrorResponse(
        int status,
        String message
) {}
