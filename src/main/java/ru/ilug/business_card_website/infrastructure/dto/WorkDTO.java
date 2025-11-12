package ru.ilug.business_card_website.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDTO {

    private String name;
    private String legalName;
    private String icon;
    private String position;
    private String period;
    private String tasks;
    private List<String> stack;

}
