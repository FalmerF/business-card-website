package ru.ilug.business_card_website.data.model;

import java.util.List;

public record BlogPost(String key, String title, String description, String date, List<String> keyWords,
                       String previewHtmlContent, String htmlContent) {

}
