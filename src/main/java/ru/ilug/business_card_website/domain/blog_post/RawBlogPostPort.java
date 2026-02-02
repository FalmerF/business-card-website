package ru.ilug.business_card_website.domain.blog_post;

import java.util.List;

public interface RawBlogPostPort {

    List<RawBlogPost> fetchAll();

    String getBaseUrlForContentOfBlogPost(String blogPostId);

}
