package ru.ilug.business_card_website.web;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ilug.business_card_website.data.model.BlogPost;
import ru.ilug.business_card_website.data.model.Skill;
import ru.ilug.business_card_website.service.GitHubMarkdownService;
import ru.ilug.business_card_website.service.PostViewsService;

import java.util.Collection;
import java.util.List;

@Controller
@EnableCaching
@RequiredArgsConstructor
public class WebController {

    private final static List<Skill> SKILLS = List.of(
            Skill.of("Java 8-17", "java.svg"),
            Skill.of("Spring", "spring.svg"),
            Skill.of("Hibernate", "hibernate.svg"),
            Skill.of("JavaFX", "java.svg"),
            Skill.of("PostgreSQL", "postgresql.svg"),
            Skill.of("MongoDB", "mongodb.svg"),
            Skill.of("Docker", "docker.svg"),
            Skill.of("HashiCorp Vault", "vault.svg"),
            Skill.of("Gradle", "gradle.svg")
    );

    private final GitHubMarkdownService gitHubMarkdownService;
    private final PostViewsService postViewsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("skills", SKILLS);

        Collection<BlogPost> posts = gitHubMarkdownService.getPostMap().values();
        model.addAttribute("posts", posts);

        return "index";
    }

    @GetMapping("/post/{slug}")
    public String fullPost(@PathVariable String slug, Model model) {
        int views = postViewsService.incrementPostViews(slug);

        BlogPost post = gitHubMarkdownService.getPostMap().get(slug);
        post.setViews(views);

        model.addAttribute("post", post);
        return "post";
    }
}
