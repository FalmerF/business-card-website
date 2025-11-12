package ru.ilug.business_card_website.data.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ilug.business_card_website.data.model.PostViews;
import ru.ilug.business_card_website.data.repository.PostViewsRepository;

@Service
@RequiredArgsConstructor
public class PostViewsService {

    private final PostViewsRepository repository;

    public PostViews getPostViews(String name) {
        return repository.findById(name).orElse(new PostViews(name, 0));
    }

    @Transactional
    public int incrementPostViews(String name) {
        PostViews postViews = getPostViews(name);
        postViews.setViews(postViews.getViews() + 1);
        repository.save(postViews);

        return postViews.getViews();
    }

}
