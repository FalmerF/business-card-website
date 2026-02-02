package ru.ilug.business_card_website.domain.blog_post;

import java.util.List;

public record RawBlogPost(String key, String title, String description, String date, List<String> keyWords,
                          boolean hide, String content) {

}
