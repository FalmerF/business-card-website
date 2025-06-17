package ru.ilug.business_card_website.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ilug.business_card_website.data.model.PostViews;

@Repository
public interface PostViewsRepository extends JpaRepository<PostViews, String> {
}
