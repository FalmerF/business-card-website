package ru.ilug.business_card_website.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ilug.business_card_website.config.CareerConfiguration;
import ru.ilug.business_card_website.infrastructure.dto.BlogPostDTO;
import ru.ilug.business_card_website.data.service.PostsService;
import ru.ilug.business_card_website.data.service.PostViewsService;

import java.util.Collection;

@Controller
@EnableCaching
@RequiredArgsConstructor
public class WebController {

    private final CareerConfiguration configuration;
    private final PostsService postsService;
    private final PostViewsService postViewsService;

    @GetMapping("/")
    @Cacheable("indexHtmlCache")
    public String index(Model model) {
        model.addAttribute("skills", configuration.getSkills());
        model.addAttribute("works", configuration.getWorks());

        return "index";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        Collection<BlogPostDTO> posts = postsService.getPostMap().values();
        model.addAttribute("posts", posts);

        return "blog";
    }

    @GetMapping("/post/{slug}")
    public String fullPost(@PathVariable String slug, Model model) {
        int views = postViewsService.incrementPostViews(slug);

        BlogPostDTO post = postsService.getPostMap().get(slug);
        post.setViews(views);

        model.addAttribute("post", post);
        return "post";
    }
}
