package ru.ilug.business_card_website.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    private String name;
    private String icon;

    public static Skill of(String name, String icon) {
        return new Skill(name, icon);
    }
}
