package ru.ilug.business_card_website.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ilug.business_card_website.data.service.PostsService;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class GiteaWebhookController {

    private final PostsService postsService;

    @PostMapping("/gitea")
    public void handleGiteaWebhook() {
        postsService.updatePosts();
    }
}
