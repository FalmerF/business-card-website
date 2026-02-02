package ru.ilug.business_card_website.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ilug.business_card_website.domain.blog_post.BlogPostService;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class GiteaWebhookController {

    private final BlogPostService blogPostService;

    @PostMapping("/gitea")
    public void handleGiteaWebhook() {
        blogPostService.updatePosts();
    }
}
