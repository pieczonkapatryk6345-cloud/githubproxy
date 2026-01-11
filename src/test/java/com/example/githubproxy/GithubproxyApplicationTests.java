package com.example.githubproxy;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@WireMockTest(httpPort = 8089)
class GithubproxyApplicationTests {

	@LocalServerPort
	int port;

	@Test
	void shouldReturnRepositoriesWithoutForks() throws Exception {

		stubFor(get("/users/john/repos")
				.willReturn(okJson("""
                    [
                      {
                        "name": "repo1",
                        "fork": false,
                        "owner": { "login": "john" }
                      },
                      {
                        "name": "repo-forked",
                        "fork": true,
                        "owner": { "login": "john" }
                      }
                    ]
                """)));

		stubFor(get("/repos/john/repo1/branches")
				.willReturn(okJson("""
                    [
                      {
                        "name": "main",
                        "commit": { "sha": "abc123" }
                      }
                    ]
                """)));

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/users/john/repositories"))
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("repo1", "john", "main", "abc123");
		assertThat(response.body()).doesNotContain("repo-forked");
	}

	@Test
	void shouldReturn404WhenUserNotFound() throws Exception {

		stubFor(get("/users/missing/repos")
				.willReturn(notFound()));

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/users/missing/repositories"))
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertThat(response.statusCode()).isEqualTo(404);
		assertThat(response.body()).contains("User 'missing' not found");
	}
}