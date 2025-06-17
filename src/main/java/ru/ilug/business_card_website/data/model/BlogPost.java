package ru.ilug.business_card_website.data.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public final class BlogPost {

    private final String title;
    private final String date;
    private final String previewHtmlContent;
    private final String htmlContent;
    private int views;

}
