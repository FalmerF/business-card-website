package ru.ilug.business_card_website.infrastructure.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class MetadataDto {

    private String displayName;
    private String description = "";
    private String pubDate;
    private List<String> keyWords = Collections.emptyList();
    private boolean hide = false;

}
