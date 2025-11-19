package ru.ilug.business_card_website.data.service;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.misc.Extension;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ilug.business_card_website.data.service.markdown.CustomLinkResolverFactory;
import ru.ilug.business_card_website.infrastructure.client.GiteaClient;
import ru.ilug.business_card_website.infrastructure.dto.BlogPostDTO;
import ru.ilug.business_card_website.infrastructure.dto.GiteaFileDTO;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsService {

    private final List<Extension> flexmarkExtensions = List.of(
            TablesExtension.create(),
            AutolinkExtension.create(),
            StrikethroughExtension.create()
    );

    private final Parser parser = Parser.builder()
            .extensions(flexmarkExtensions)
            .build();

    private final GiteaClient gitClient;
    private final PostViewsService postViewsService;

    @Getter
    private Map<String, BlogPostDTO> postMap = new HashMap<>();

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
        return gitClient.fetchDirectoryFiles("").stream()
                .map(this::fetchPost)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(BlogPostDTO::getTitle, p -> p));
    }

    private BlogPostDTO fetchPost(GiteaFileDTO directory) {
        GiteaFileDTO file = gitClient.fetchDirectoryFiles("/" + directory.getName())
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

    private BlogPostDTO loadPost(GiteaFileDTO file, String title, String date) {
        String content = gitClient.fetchFileContent(file.getDownloadUrl());

        if (content == null) return null;

        String baseUrl = gitClient.getContentBaseUrl(title);

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
