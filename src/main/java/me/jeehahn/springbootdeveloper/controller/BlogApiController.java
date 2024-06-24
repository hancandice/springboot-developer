package me.jeehahn.springbootdeveloper.controller;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.Article;
import me.jeehahn.springbootdeveloper.dto.AddArticleRequest;
import me.jeehahn.springbootdeveloper.dto.ArticleResponse;
import me.jeehahn.springbootdeveloper.dto.UpdateArticleRequest;
import me.jeehahn.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        URI location = URI.create(String.format("/api/articles/%s", savedArticle.getId()));
        return ResponseEntity.created(location).body(savedArticle);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<Article> articles = blogService.findAll();
        List<ArticleResponse> articlesResponse = articles.stream()
            .map(ArticleResponse::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(articlesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findArticleById(@PathVariable Long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok(new ArticleResponse(article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResponse> deleteArticleById(@PathVariable Long id) {
        Article deletedArticle = blogService.findById(id);
        blogService.delete(id);
        return ResponseEntity.ok(new ArticleResponse(deletedArticle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticleById(@PathVariable Long id,
        @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);
        return ResponseEntity.ok(new ArticleResponse(updatedArticle));
    }
}
