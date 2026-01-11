package com.example.githubproxy;

import java.util.List;

public record RepositoryResponse(
        String repositoryName,
        String ownerLogin,
        List<BranchResponse> branches
) {}

