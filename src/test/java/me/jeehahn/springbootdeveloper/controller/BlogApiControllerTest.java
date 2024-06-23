package me.jeehahn.springbootdeveloper.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import me.jeehahn.springbootdeveloper.domain.Article;
import me.jeehahn.springbootdeveloper.dto.AddArticleRequest;
import me.jeehahn.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: Successfully adds a blog article")
    @Test
    public void addArticle() throws Exception {

        // Given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // Serialize to JSON
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // When
        ResultActions result = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

        // Then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).hasSize(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: Successfully retrieves all articles")
    @Test
    public void findAllArticles() throws Exception {

        // Given
        final String url = "/api/articles";
        final String title1 = "title 1";
        final String content1 = "content 1";
        final String title2 = "title 2";
        final String content2 = "content 2";

        Article article1 = Article.builder().title(title1).content(content1).build();
        Article article2 = Article.builder().title(title2).content(content2).build();
        blogRepository.save(article1);
        blogRepository.save(article2);

        // When
        ResultActions result = mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value(title1))
            .andExpect(jsonPath("$[0].content").value(content1))
            .andExpect(jsonPath("$[1].title").value(title2))
            .andExpect(jsonPath("$[1].content").value(content2));

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).hasSize(2);
        assertThat(articles.get(0).getTitle()).isEqualTo(title1);
        assertThat(articles.get(0).getContent()).isEqualTo(content1);
        assertThat(articles.get(1).getTitle()).isEqualTo(title2);
        assertThat(articles.get(1).getContent()).isEqualTo(content2);
    }

    @DisplayName("findArticleById: Successfully retrieves an article by ID")
    @Test
    public void findArticleById() throws Exception {

        // Given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        Article article = Article.builder().title(title).content(content).build();
        Article savedArticle = blogRepository.save(article);

        // When
        ResultActions result = mockMvc.perform(get(url + "/" + savedArticle.getId())
            .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.content").value(content));

        Article foundArticle = blogRepository.findById(savedArticle.getId()).orElse(null);
        assertThat(foundArticle).isNotNull();
        assertThat(foundArticle.getTitle()).isEqualTo(title);
        assertThat(foundArticle.getContent()).isEqualTo(content);
    }

    @DisplayName("deleteArticleById: Successfully deletes an article by ID")
    @Test
    public void deleteArticleById() throws Exception {

        // Given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        Article article = Article.builder().title(title).content(content).build();
        Article savedArticle = blogRepository.save(article);

        // When
        ResultActions result = mockMvc.perform(delete(url + "/" + savedArticle.getId())
            .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.content").value(content));;

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }
}
