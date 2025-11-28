package ru.ilug.business_card_website.data.service;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.misc.Extension;
import jakarta.annotation.Nullable;
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
import ru.ilug.business_card_website.infrastructure.dto.MetadataDTO;

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
                .collect(Collectors.toMap(BlogPostDTO::getKey, p -> p));
    }

    @Nullable
    private BlogPostDTO fetchPost(GiteaFileDTO directory) {
        String key = directory.getName();

        List<GiteaFileDTO> files = getPostDirectoryFiles(key);
        String markdownContent = findAndFetchMarkdownContent(files);

        if (markdownContent == null) {
            return null;
        }

        MetadataDTO metadata = findAndFetchMetadataContent(files);

        if (metadata == null) {
            return null;
        }

        String baseUrl = gitClient.getContentBaseUrl(key);

        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(flexmarkExtensions)
                .linkResolverFactory(new CustomLinkResolverFactory(baseUrl))
                .build();

        String previewContent;
        String[] parts = markdownContent.split("<!-- more -->");

        if (parts.length != 2) {
            previewContent = markdownContent.substring(0, Math.min(500, markdownContent.length()));
        } else {
            previewContent = parts[0];
        }

        String previewHtml = renderer.render(parser.parse(previewContent));
        String html = renderer.render(parser.parse(markdownContent));

        return new BlogPostDTO(key, metadata.getDisplayName(), metadata.getDescription(), metadata.getPubDate(),
                metadata.getKeyWords(), previewHtml, html,
                postViewsService.getPostViews(key).getViews()
        );
    }

    private List<GiteaFileDTO> getPostDirectoryFiles(String directory) {
        return gitClient.fetchDirectoryFiles("/" + directory);
    }

    @Nullable
    private String findAndFetchMarkdownContent(List<GiteaFileDTO> files) {
        GiteaFileDTO markdownFile = files.stream()
                .filter(f -> f.getName().endsWith(".md"))
                .findFirst()
                .orElse(null);

        if (markdownFile == null) {
            return null;
        }

        return gitClient.fetchFileContent(markdownFile.getDownloadUrl());
    }

    @Nullable
    private MetadataDTO findAndFetchMetadataContent(List<GiteaFileDTO> files) {
        GiteaFileDTO metadataFile = files.stream()
                .filter(f -> ".metadata.json".equals(f.getName()))
                .findFirst()
                .orElse(null);

        if (metadataFile == null) {
            return null;
        }

        try {
            return gitClient.fetchMetadataContent(metadataFile.getDownloadUrl());
        } catch (Exception e) {
            log.error("Error on fetch metadata file", e);
            return null;
        }
    }
}
