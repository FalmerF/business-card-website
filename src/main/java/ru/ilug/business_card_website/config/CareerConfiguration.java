package ru.ilug.business_card_website.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.ilug.business_card_website.infrastructure.dto.BaseInfoDTO;
import ru.ilug.business_card_website.infrastructure.dto.SkillDTO;
import ru.ilug.business_card_website.infrastructure.dto.WorkDTO;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("career")
public class CareerConfiguration {

    private BaseInfoDTO baseInfo;
    private List<SkillDTO> skills;
    private List<WorkDTO> works;

}
