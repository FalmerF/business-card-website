package ru.ilug.business_card_website.infrastructure.dto;

import ru.ilug.business_card_website.data.model.BlogPost;

public class DtoMapper {

    public static BlogPostDto fromEntity(BlogPost blogPost, int views) {
        return new BlogPostDto(
                blogPost.key(),
                blogPost.title(),
                blogPost.description(),
                blogPost.date(),
                blogPost.keyWords(),
                blogPost.previewHtmlContent(),
                blogPost.htmlContent(),
                views
        );
    }

}
