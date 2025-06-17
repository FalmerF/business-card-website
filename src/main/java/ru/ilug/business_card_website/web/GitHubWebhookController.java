package ru.ilug.business_card_website.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ilug.business_card_website.service.GitHubMarkdownService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private final GitHubMarkdownService gitHubMarkdownService;

    @Value("${github.webhook.secret}")
    private String githubWebhookSecret;

    @PostMapping("/github")
    public ResponseEntity<String> handleGitHubWebhook(
            @RequestHeader("X-Hub-Signature") String signature,
            @RequestBody String payload) {

        if (!isValidSignature(signature, payload)) {
            return ResponseEntity.status(401).body("Invalid signature");
        }

        gitHubMarkdownService.updatePosts();

        return ResponseEntity.ok("Webhook processed successfully");
    }

    private boolean isValidSignature(String signature, String payload) {
        if (signature == null || githubWebhookSecret == null) {
            return false;
        }

        try {
            String expectedSignature = "sha1=" + calculateHMAC(payload, githubWebhookSecret);
            return MessageDigest.isEqual(expectedSignature.getBytes(), signature.getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    private String calculateHMAC(String data, String key) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(rawHmac);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
