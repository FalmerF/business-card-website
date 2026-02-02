package ru.ilug.business_card_website.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ilug.business_card_website.infrastructure.dto.GiteaFileDto;
import ru.ilug.business_card_website.infrastructure.dto.MetadataDto;

import java.util.List;

@Component
public class GiteaClient implements GitClient {

    private final String owner;
    private final String repository;
    private final String branch;
    private final WebClient webClient;

    private final JsonMapper jsonMapper = new JsonMapper();

    public GiteaClient(@Value("${git.base-url}") String baseUrl, @Value("${git.owner}") String owner,
                       @Value("${git.password}") String password, @Value("${git.repository}") String repository,
                       @Value("${git.branch}") String branch) {
        this.owner = owner;
        this.repository = repository;
        this.branch = branch;

        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(owner, password))
                .build();
    }

    @Override
    public List<GiteaFileDto> fetchDirectoryFiles(String path) {
        return webClient.get()
                .uri(String.format("/api/v1/repos/%s/%s/contents%s?ref=%s", owner, repository, path, branch))
                .retrieve()
                .bodyToFlux(GiteaFileDto.class)
                .collectList()
                .block();
    }

    @Override
    public String fetchFileContent(String downloadUrl) {
        return webClient.get()
                .uri(downloadUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public MetadataDto fetchMetadataContent(String downloadUrl) throws JsonProcessingException {
        String content = fetchFileContent(downloadUrl);
        return jsonMapper.readValue(content, MetadataDto.class);
    }

    @Override
    public String getBaseUrlForBlogPost(String blogPostId) {
        return String.format("https://git.ilug.ru/%s/%s/raw/branch/%s/%s/", owner, repository, branch, blogPostId);
    }
}
