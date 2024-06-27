package me.jeehahn.springbootdeveloper.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.Article;
import me.jeehahn.springbootdeveloper.dto.ArticleListViewResponse;
import me.jeehahn.springbootdeveloper.dto.ArticleViewResponse;
import me.jeehahn.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/articles/{id}")
    public String getArticleById(@PathVariable Long id, Model model) {
        Optional<Article> articleOpt = Optional.ofNullable(blogService.findById(id));
        if (articleOpt.isPresent()) {
            Article article = articleOpt.get();
            ArticleViewResponse articleViewResponse = new ArticleViewResponse(article);
            model.addAttribute("article", articleViewResponse);
            return "article";
        } else {
            return "error/404";
        }
    }

    private List<ArticleListViewResponse> mapToArticleListViewResponses(List<Article> articles) {
        return articles.stream()
            .map(ArticleListViewResponse::new)
            .toList();
    }

    @GetMapping("/new-article")
    public String createArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
