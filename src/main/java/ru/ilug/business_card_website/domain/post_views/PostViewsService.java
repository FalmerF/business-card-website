package ru.ilug.business_card_website.domain.post_views;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewsService {

    private final PostViewsRepository postViewsRepository;

    public PostViews getPostViewsById(String postId) {
        return postViewsRepository.findById(postId).orElse(new PostViews(postId, 0));
    }

    @Transactional
    public int incrementPostViewsById(String postId) {
        PostViews postViews = getPostViewsById(postId);
        postViews.setViews(postViews.getViews() + 1);
        postViewsRepository.save(postViews);

        return postViews.getViews();
    }

}
