package com.example.githubproxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GithubClient {

    private final RestClient restClient;

    public GithubClient(
            @Value("${github.api.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .build();
    }

    public List<Repository> getRepositories(String user) {
        return restClient.get()
                .uri("/users/{user}/repos", user)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        (req, res) -> {
                            throw new UserNotFoundException(user);
                        }
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Branch> getBranches(String owner, String repo) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        (req, res) -> {
                            throw new BranchesNotFoundException(owner);
                        }
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
