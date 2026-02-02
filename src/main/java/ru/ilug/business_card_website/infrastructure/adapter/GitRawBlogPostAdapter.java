package ru.ilug.business_card_website.infrastructure.adapter;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ilug.business_card_website.domain.blog_post.RawBlogPost;
import ru.ilug.business_card_website.domain.blog_post.RawBlogPostPort;
import ru.ilug.business_card_website.infrastructure.client.GitClient;
import ru.ilug.business_card_website.infrastructure.dto.GiteaFileDto;
import ru.ilug.business_card_website.infrastructure.dto.MetadataDto;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitRawBlogPostAdapter implements RawBlogPostPort {

    private final GitClient gitClient;

    @Override
    public List<RawBlogPost> fetchAll() {
        return gitClient.fetchDirectoryFiles("").stream()
                .map(this::fetchRawBlogPost)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public String getBaseUrlForContentOfBlogPost(String blogPostId) {
        return gitClient.getBaseUrlForBlogPost(blogPostId);
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
                metadata.getKeyWords(), metadata.isHide(), markdownContent
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
}
