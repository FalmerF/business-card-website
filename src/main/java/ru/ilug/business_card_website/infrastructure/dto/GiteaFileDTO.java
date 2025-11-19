package ru.ilug.business_card_website.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GiteaFileDTO {

    private String name;
    @JsonProperty("download_url")
    private String downloadUrl;

}
