package me.jeehahn.springbootdeveloper.dto;

import lombok.Getter;
import me.jeehahn.springbootdeveloper.domain.Article;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;
    private final Long id;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.id = article.getId();
    }
}
