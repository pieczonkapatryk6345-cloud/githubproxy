package com.example.githubproxy;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GithubService {

    private final GithubClient client;

    GithubService(GithubClient client) {
        this.client = client;
    }

    public List<RepositoryResponse> getRepositories(String user) {
            List<Repository> repos = client.getRepositories(user);
            return repos.stream()
                    .filter(repo -> !repo.fork())
                    .map(repo -> {
                        String repoName = repo.name();
                        String ownerLogin = repo.owner().login();

                        List<Branch> branches = client.getBranches(ownerLogin, repoName);

                        List<BranchResponse> branchResponses =
                                branches.isEmpty()
                                        ? List.of()
                                        : branches.stream()
                                        .map(branch -> new BranchResponse(
                                                branch.name(),
                                                branch.commit().sha()
                                        ))
                                        .toList();
                        return new RepositoryResponse(repoName, ownerLogin, branchResponses);
                    })
                    .toList();
        }
    }
