package ru.ilug.business_card_website.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ilug.business_card_website.data.model.GitHubFile;

import java.util.List;

@Component
public class GitHubWebClient {

    private final WebClient webClient;

    @Value("${github.owner}")
    private String owner;
    @Value("${github.repo}")
    private String repo;
    @Value("${github.branch}")
    private String branch;

    public GitHubWebClient(@Value("${github.token}") String token) {
        webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + token)
                .build();
    }

    public List<GitHubFile> fetchDirectoryFiles(String path) {
        return webClient.get()
                .uri(String.format("/repos/%s/%s/contents%s?ref=%s", owner, repo, path, branch))
                .retrieve()
                .bodyToFlux(GitHubFile.class)
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
