package com.example.githubproxy;

public record Branch (
    String name,
    Commit commit
) {}
