package ru.ilug.business_card_website.domain.post_views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostViews {

    @Id
    private String id;
    private int views;

}
