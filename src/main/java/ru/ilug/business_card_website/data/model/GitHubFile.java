package ru.ilug.business_card_website.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubFile {

    private String name;
    @JsonProperty("download_url")
    private String downloadUrl;

}
