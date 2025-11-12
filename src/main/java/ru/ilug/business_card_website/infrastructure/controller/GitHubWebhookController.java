package ru.ilug.business_card_website.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ilug.business_card_website.data.service.markdown.GitHubMarkdownService;
import ru.ilug.business_card_website.infrastructure.GitHubWebhookVerifier;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private final GitHubMarkdownService gitHubMarkdownService;
    private final GitHubWebhookVerifier gitHubWebhookVerifier;

    @PostMapping("/github")
    public ResponseEntity<String> handleGitHubWebhook(
            @RequestHeader("X-Hub-Signature") String signature,
            @RequestBody String payload) {

        if (!gitHubWebhookVerifier.isValidSignature(signature, payload)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        gitHubMarkdownService.updatePosts();

        return ResponseEntity.ok("Webhook processed successfully");
    }
}
