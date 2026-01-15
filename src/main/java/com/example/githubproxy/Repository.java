package com.example.githubproxy;

public record Repository (
    Boolean fork,
    String name,
    Owner owner
) {}
