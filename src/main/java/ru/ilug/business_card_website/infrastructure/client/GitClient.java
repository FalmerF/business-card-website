package ru.ilug.business_card_website.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.ilug.business_card_website.infrastructure.dto.GiteaFileDto;
import ru.ilug.business_card_website.infrastructure.dto.MetadataDto;

import java.util.List;

public interface GitClient {

    List<GiteaFileDto> fetchDirectoryFiles(String path);

    String fetchFileContent(String downloadUrl);

    MetadataDto fetchMetadataContent(String downloadUrl) throws JsonProcessingException;

    String getBaseUrlForBlogPost(String blogPostId);

}
