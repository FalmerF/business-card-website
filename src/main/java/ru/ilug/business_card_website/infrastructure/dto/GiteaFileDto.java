package ru.ilug.business_card_website.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GiteaFileDto {

    private String name;
    @JsonProperty("download_url")
    private String downloadUrl;

}
