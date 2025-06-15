package ru.ilug.business_card_website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        List<String> skills = List.of(
                "Java 8-17", "Spring Framework", "Hibernate/JPA", "JavaFX", "OpenGL", "Vulkan API", "GLSL", "PostgreSQL", "MognoDB", "HashiCorp Vault", "Docker",
                "REST API", "Gradle", "Git"
        );
        model.addAttribute("skills", skills);

        return "index";
    }

}
