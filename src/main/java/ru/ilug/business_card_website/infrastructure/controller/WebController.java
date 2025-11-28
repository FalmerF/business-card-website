package ru.ilug.business_card_website.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.ilug.business_card_website.config.CareerConfiguration;
import ru.ilug.business_card_website.data.model.BlogPost;
import ru.ilug.business_card_website.data.model.PostViews;
import ru.ilug.business_card_website.data.service.PostsService;
import ru.ilug.business_card_website.data.service.PostViewsService;
import ru.ilug.business_card_website.infrastructure.dto.BlogPostDto;
import ru.ilug.business_card_website.infrastructure.dto.DtoMapper;
import ru.ilug.business_card_website.util.WorkUtil;

@Controller
@EnableCaching
@RequiredArgsConstructor
public class WebController {

    private final CareerConfiguration configuration;
    private final PostsService postsService;
    private final PostViewsService postViewsService;

    @GetMapping("/")
    @Cacheable("indexHtmlCache")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURL());
        model.addAttribute("generalInfo", configuration.getGeneralInfo());
        model.addAttribute("links", configuration.getLinks());
        model.addAttribute("skills", configuration.getSkills());
        model.addAttribute("works", configuration.getWorks());
        model.addAttribute("workExperience", WorkUtil.calculateAndFormatWorkExperience(configuration.getWorks()));

        return "index";
    }

    @GetMapping("/blog")
    public Mono<String> blog(Model model) {
        model.addAttribute("generalInfo", configuration.getGeneralInfo());
        return Flux.fromIterable(postsService.getAllPosts())
                .map(blogPost -> {
                    PostViews views = postViewsService.getPostViews(blogPost.key());
                    return DtoMapper.fromEntity(blogPost, views.getViews());
                }).collectList()
                .map(posts -> {
                    model.addAttribute("posts", posts);
                    return "blog";
                });
    }

    @GetMapping("/post/{key}")
    public String viewPost(@PathVariable String key, Model model) {
        BlogPost blogPost = postsService.findPost(key);

        if (blogPost == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        int views = postViewsService.incrementPostViews(key);
        BlogPostDto blogPostDto = DtoMapper.fromEntity(blogPost, views);

        model.addAttribute("post", blogPostDto);
        return "post";
    }
}
