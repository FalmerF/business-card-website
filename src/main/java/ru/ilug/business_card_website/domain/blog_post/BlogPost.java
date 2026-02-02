package ru.ilug.business_card_website.domain.blog_post;

import java.util.List;

public record BlogPost(String key, String title, String description, String date, List<String> keyWords, boolean hide,
                       String previewHtmlContent, String htmlContent) {

}
