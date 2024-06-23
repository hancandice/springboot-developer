package me.jeehahn.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.Article;
import me.jeehahn.springbootdeveloper.dto.AddArticleRequest;
import me.jeehahn.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        Article article = request.toEntity();
        return blogRepository.save(article);
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }
}
