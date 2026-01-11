package com.example.githubproxy;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Service
public class GithubService {

    private final GithubClient client;

    GithubService(GithubClient client) {
        this.client = client;
    }

    public List<RepositoryResponse> getRepositories(String user) {
        try {
            List<Map<String, Object>> repos = client.getRepositories(user);

            return repos.stream()
                    .filter(repo -> !(Boolean) repo.get("fork"))
                    .map(repo -> {
                        String repoName = (String) repo.get("name");
                        String ownerLogin = (String) ((Map<?, ?>) repo.get("owner")).get("login");

                        List<Map<String, Object>> branches = client.getBranches(ownerLogin, repoName);

                        List<BranchResponse> branchResponses =
                                branches.isEmpty()
                                        ? List.of()
                                        : branches.stream()
                                        .map(branch -> new BranchResponse(
                                                (String) branch.get("name"),
                                                (String) ((Map<?, ?>) branch.get("commit")).get("sha")
                                        ))
                                        .toList();

                        return new RepositoryResponse(repoName, ownerLogin, branchResponses);
                    })
                    .toList();
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException(user);
        }
    }
}
