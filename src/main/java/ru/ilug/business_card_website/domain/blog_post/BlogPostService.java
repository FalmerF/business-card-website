package ru.ilug.business_card_website.domain.blog_post;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.misc.Extension;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ilug.business_card_website.domain.markdown.CustomLinkResolverFactory;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final List<Extension> flexmarkExtensions;
    private final Parser flexmarkParser;
    private final RawBlogPostPort rawBlogPostPort;

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
        return postMap.values().stream().filter(p -> !p.hide()).toList();
    }

    @Async
    public void updatePosts() {
        log.info("Updating posts...");
        postMap = fetchAllPosts();
        log.info("Posts successful updated!");
    }

    private Map<String, BlogPost> fetchAllPosts() {
        return rawBlogPostPort.fetchAll().stream()
                .map(this::renderPost)
                .collect(Collectors.toMap(BlogPost::key, p -> p));
    }

    private BlogPost renderPost(RawBlogPost rawBlogPost) {
        String baseUrl = rawBlogPostPort.getBaseUrlForContentOfBlogPost(rawBlogPost.key());

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
                rawBlogPost.date(), rawBlogPost.keyWords(), rawBlogPost.hide(), previewHtml, html
        );
    }
}
