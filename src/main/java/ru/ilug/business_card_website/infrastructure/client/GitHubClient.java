package ru.ilug.business_card_website.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ilug.business_card_website.infrastructure.dto.GitHubFileDTO;

import java.util.List;

@Component
public class GitHubClient {

    private final WebClient webClient;

    @Value("${github.owner}")
    private String owner;
    @Value("${github.repo}")
    private String repo;
    @Value("${github.branch}")
    private String branch;

    public GitHubClient(@Value("${github.token}") String token) {
        webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + token)
                .build();
    }

    public List<GitHubFileDTO> fetchDirectoryFiles(String path) {
        return webClient.get()
                .uri(String.format("/repos/%s/%s/contents%s?ref=%s", owner, repo, path, branch))
                .retrieve()
                .bodyToFlux(GitHubFileDTO.class)
                .collectList()
                .block();
    }

    public String fetchFileContent(String downloadUrl) {
        return webClient.get()
                .uri(downloadUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
