package ru.ilug.business_card_website.infrastructure.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public final class BlogPostDTO {

    private final String key;
    private final String title;
    private final String description;
    private final String date;
    private final List<String> keyWords;
    private final String previewHtmlContent;
    private final String htmlContent;
    private int views;

}
