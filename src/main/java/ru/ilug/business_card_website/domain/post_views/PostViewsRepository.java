package ru.ilug.business_card_website.domain.post_views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewsRepository extends JpaRepository<PostViews, String> {
}
