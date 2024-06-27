package me.jeehahn.springbootdeveloper.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.Article;
import me.jeehahn.springbootdeveloper.dto.ArticleListViewResponse;
import me.jeehahn.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = mapToArticleListViewResponses(blogService.findAll());
        model.addAttribute("articles", articles);
        return "articleList";
    }

    private List<ArticleListViewResponse> mapToArticleListViewResponses(List<Article> articles) {
        return articles.stream()
            .map(ArticleListViewResponse::new)
            .toList();
    }
}
