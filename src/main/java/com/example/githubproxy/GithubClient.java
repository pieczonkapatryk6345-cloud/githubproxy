package com.example.githubproxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getRepositories(String user) {
        return restClient.get()
                .uri("/users/{user}/repos", user)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Map<String, Object>> getBranches(String owner, String repo) {
        try {
            List<Map<String, Object>> result = restClient.get()
                    .uri("/repos/{owner}/{repo}/branches", owner, repo)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return result != null ? result : List.of();
        } catch (HttpClientErrorException.NotFound e) {
            return List.of();
        }
    }
}
