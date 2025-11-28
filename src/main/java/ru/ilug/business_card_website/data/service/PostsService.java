package ru.ilug.business_card_website.data.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.misc.Extension;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ilug.business_card_website.data.model.RawBlogPost;
import ru.ilug.business_card_website.data.service.markdown.CustomLinkResolverFactory;
import ru.ilug.business_card_website.infrastructure.client.GiteaClient;
import ru.ilug.business_card_website.data.model.BlogPost;
import ru.ilug.business_card_website.infrastructure.dto.GiteaFileDto;
import ru.ilug.business_card_website.infrastructure.dto.MetadataDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsService {

    private final List<Extension> flexmarkExtensions;
    private final Parser flexmarkParser;
    private final GiteaClient gitClient;

    private Map<String, BlogPost> postMap = new HashMap<>();

    @PostConstruct
    public void init() {
        updatePosts();
    }

    @Nullable
    public BlogPost findPost(String key) {
        return postMap.get(key);
    }

    public Collection<BlogPost> getAllPosts() {
        return postMap.values();
    }

    @Async
    public void updatePosts() {
        log.info("Updating posts...");
        postMap = fetchAllPosts();
        log.info("Posts successful updated!");
    }

    private Map<String, BlogPost> fetchAllPosts() {
        return gitClient.fetchDirectoryFiles("").stream()
                .map(this::fetchRawBlogPost)
                .filter(Objects::nonNull)
                .map(this::renderPost)
                .collect(Collectors.toMap(BlogPost::key, p -> p));
    }

    @Nullable
    private RawBlogPost fetchRawBlogPost(GiteaFileDto directory) {
        String key = directory.getName();

        List<GiteaFileDto> files = getPostDirectoryFiles(key);
        String markdownContent = findAndFetchMarkdownContent(files);

        if (markdownContent == null) {
            return null;
        }

        MetadataDto metadata = findAndFetchMetadataContent(files);

        if (metadata == null) {
            return null;
        }

        return new RawBlogPost(key, metadata.getDisplayName(), metadata.getDescription(), metadata.getPubDate(),
                metadata.getKeyWords(), markdownContent
        );
    }

    private List<GiteaFileDto> getPostDirectoryFiles(String directory) {
        return gitClient.fetchDirectoryFiles("/" + directory);
    }

    @Nullable
    private String findAndFetchMarkdownContent(List<GiteaFileDto> files) {
        GiteaFileDto markdownFile = files.stream()
                .filter(f -> f.getName().endsWith(".md"))
                .findFirst()
                .orElse(null);

        if (markdownFile == null) {
            return null;
        }

        return gitClient.fetchFileContent(markdownFile.getDownloadUrl());
    }

    @Nullable
    private MetadataDto findAndFetchMetadataContent(List<GiteaFileDto> files) {
        GiteaFileDto metadataFile = files.stream()
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

    private BlogPost renderPost(RawBlogPost rawBlogPost) {
        String baseUrl = gitClient.getContentBaseUrl(rawBlogPost.key());

        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(flexmarkExtensions)
                .linkResolverFactory(new CustomLinkResolverFactory(baseUrl))
                .build();

        String content = rawBlogPost.content();

        String previewContent;
        String[] parts = content.split("<!-- more -->");

        if (parts.length != 2) {
            previewContent = content.substring(0, Math.min(500, content.length()));
        } else {
            previewContent = parts[0];
        }

        String previewHtml = renderer.render(flexmarkParser.parse(previewContent));
        String html = renderer.render(flexmarkParser.parse(content));

        return new BlogPost(rawBlogPost.key(), rawBlogPost.title(), rawBlogPost.description(),
                rawBlogPost.date(), rawBlogPost.keyWords(), previewHtml, html
        );
    }
}
