package ru.ilug.business_card_website.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class GitHubWebhookVerifier {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    @Value("${github.webhook.secret}")
    private String githubWebhookSecret;

    public boolean isValidSignature(String signature, String payload) {
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
