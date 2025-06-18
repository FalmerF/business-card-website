package ru.ilug.business_card_website;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.ilug.business_card_website.data.model.Skill;
import ru.ilug.business_card_website.data.model.Work;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("career")
public class CareerConfiguration {

    private List<Skill> skills;
    private List<Work> works;

}
