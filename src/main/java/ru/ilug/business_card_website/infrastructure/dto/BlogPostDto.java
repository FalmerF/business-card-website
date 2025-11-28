package ru.ilug.business_card_website.infrastructure.dto;

import java.util.List;

public record BlogPostDto(String key, String title, String description, String date, List<String> keyWords,
                          String previewHtmlContent, String htmlContent, int views) {
}