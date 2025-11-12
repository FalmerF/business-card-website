package ru.ilug.business_card_website.data.service.markdown;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.misc.Extension;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ilug.business_card_website.infrastructure.dto.BlogPostDTO;
import ru.ilug.business_card_website.infrastructure.dto.GitHubFileDTO;
import ru.ilug.business_card_website.data.service.PostViewsService;
import ru.ilug.business_card_website.infrastructure.client.GitHubClient;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubMarkdownService {

    private final List<Extension> flexmarkExtensions = List.of(
            TablesExtension.create(),
            AutolinkExtension.create()
    );

    private final Parser parser = Parser.builder()
            .extensions(flexmarkExtensions)
            .build();

    private final GitHubClient gitHubClient;
    private final PostViewsService postViewsService;

    @Getter
    private Map<String, BlogPostDTO> postMap = new HashMap<>();

    @Value("${github.owner}")
    private String owner;
    @Value("${github.repo}")
    private String repo;
    @Value("${github.branch}")
    private String branch;

    @PostConstruct
    public void init() {
        updatePosts();
    }

    @Async
    public void updatePosts() {
        log.info("Updating posts...");
        postMap = fetchAllPosts();
        log.info("Posts successful updated!");
    }

    private Map<String, BlogPostDTO> fetchAllPosts() {
        return gitHubClient.fetchDirectoryFiles("").stream()
                .map(this::fetchPost)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(BlogPostDTO::getTitle, p -> p));
    }

    private BlogPostDTO fetchPost(GitHubFileDTO directory) {
        GitHubFileDTO file = gitHubClient.fetchDirectoryFiles("/" + directory.getName())
                .stream().filter(f -> f.getName().endsWith(".md"))
                .findFirst()
                .orElse(null);

        if (file == null) {
            return null;
        }

        String postDate = file.getName();
        postDate = postDate.substring(0, postDate.length() - 3);

        return loadPost(file, directory.getName(), postDate);
    }

    private BlogPostDTO loadPost(GitHubFileDTO file, String title, String date) {
        String content = gitHubClient.fetchFileContent(file.getDownloadUrl());

        if (content == null) return null;

        String baseUrl = String.format("https://raw.githubusercontent.com/%s/%s/refs/heads/%s/%s/", owner, repo, branch, title);

        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(flexmarkExtensions)
                .linkResolverFactory(new CustomLinkResolverFactory(baseUrl))
                .build();

        String previewContent;
        String[] parts = content.split("<!-- more -->");

        if (parts.length != 2) {
            previewContent = content.substring(0, Math.min(500, content.length()));
        } else {
            previewContent = parts[0];
        }

        String previewHtml = renderer.render(parser.parse(previewContent));
        String html = renderer.render(parser.parse(content));

        return new BlogPostDTO(title, date, previewHtml, html, postViewsService.getPostViews(title).getViews());
    }
}
