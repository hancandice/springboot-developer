package me.jeehahn.springbootdeveloper.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import me.jeehahn.springbootdeveloper.domain.Article;

@Getter
public class ArticleViewResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
    }

    @Builder
    public ArticleViewResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
