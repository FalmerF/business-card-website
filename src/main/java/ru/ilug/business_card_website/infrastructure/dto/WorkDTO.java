package ru.ilug.business_card_website.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ilug.business_card_website.util.WorkUtil;

import java.time.Period;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDTO {

    private String name;
    private String legalName;
    private String icon;
    private String position;
    private Period period;
    private List<String> tasks;
    private List<String> progress;
    private List<String> stack;

    public String getFormatedPeriod() {
        java.time.Period p = WorkUtil.getWorkPeriodDuration(this);
        return WorkUtil.formatWorkExperience(p);
    }

    @Data
    public static class Period {

        private String displayText;
        private long startMillis;
        private long endMillis;

    }

}
