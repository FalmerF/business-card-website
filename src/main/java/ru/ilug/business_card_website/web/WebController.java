package ru.ilug.business_card_website.web;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ilug.business_card_website.data.model.BlogPost;
import ru.ilug.business_card_website.service.GitHubMarkdownService;
import ru.ilug.business_card_website.service.PostViewsService;

import java.util.Collection;
import java.util.List;

@Controller
@EnableCaching
@RequiredArgsConstructor
public class WebController {

    private final GitHubMarkdownService gitHubMarkdownService;
    private final PostViewsService postViewsService;

    @GetMapping("/")
    public String index(Model model) {
        List<String> skills = List.of(
                "Java 8-17", "Spring Framework", "Hibernate/JPA", "JavaFX", "OpenGL", "Vulkan API", "GLSL",
                "PostgreSQL", "MognoDB", "HashiCorp Vault", "Docker", "REST API", "Gradle", "Git"
        );
        model.addAttribute("skills", skills);

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
