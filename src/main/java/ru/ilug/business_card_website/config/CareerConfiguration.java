package ru.ilug.business_card_website.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.ilug.business_card_website.infrastructure.dto.GeneralInfoDto;
import ru.ilug.business_card_website.infrastructure.dto.LinkDto;
import ru.ilug.business_card_website.infrastructure.dto.SkillDto;
import ru.ilug.business_card_website.infrastructure.dto.WorkDto;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("career")
public class CareerConfiguration {

    private GeneralInfoDto generalInfo;
    private List<LinkDto> links;
    private List<SkillDto> skills;
    private List<WorkDto> works;

}
