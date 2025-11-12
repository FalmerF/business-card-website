package ru.ilug.business_card_website.infrastructure.dto;

import lombok.*;

@Data
@AllArgsConstructor
public final class BlogPostDTO {

    private final String title;
    private final String date;
    private final String previewHtmlContent;
    private final String htmlContent;
    private int views;

}
