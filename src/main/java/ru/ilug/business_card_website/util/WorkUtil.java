package ru.ilug.business_card_website.util;

import ru.ilug.business_card_website.infrastructure.dto.WorkDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

public class WorkUtil {

    public static String calculateAndFormatWorkExperience(List<WorkDto> works) {
        Period workExperience = calculateWorkExperience(works);
        return formatWorkExperience(workExperience);
    }

    public static Period calculateWorkExperience(List<WorkDto> works) {
        List<Period> periods = new java.util.ArrayList<>(works.stream()
                .map(WorkUtil::getWorkPeriodDuration)
                .toList());

        while (periods.size() > 1) {
            Period period = periods.remove(0).plus(periods.remove(0));
            periods.add(period);
        }

        return periods.get(0).normalized();
    }

    public static Period getWorkPeriodDuration(WorkDto work) {
        WorkDto.Period period = work.getPeriod();

        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate startLocalDate = LocalDate.ofInstant(Instant.ofEpochMilli(period.getStartMillis()), zoneId);

        long endMillis = period.getEndMillis();
        LocalDate endLocalDate = LocalDate.ofInstant(endMillis == -1 ? Instant.now() : Instant.ofEpochMilli(endMillis), zoneId);

        return Period.between(startLocalDate, endLocalDate);
    }

    public static String formatWorkExperience(Period period) {
        int years = period.getYears();
        int months = period.getMonths();

        StringBuilder result = new StringBuilder();
        if (years > 0) {
            result.append(years).append(" ");

            if (years == 1) {
                result.append("год");
            } else if (years < 5) {
                result.append("года");
            } else {
                result.append("лет");
            }
        }
        if (months > 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }

            result.append(months).append(" ");
            if (months == 1) {
                result.append("месяц");
            } else if (months < 5) {
                result.append("месяца");
            } else {
                result.append("месяцев");
            }
        }

        return result.toString();
    }

}
