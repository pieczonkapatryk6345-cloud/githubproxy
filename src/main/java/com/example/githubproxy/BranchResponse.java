package com.example.githubproxy;

public record BranchResponse(
        String name,
        String lastCommitSha
) {}
