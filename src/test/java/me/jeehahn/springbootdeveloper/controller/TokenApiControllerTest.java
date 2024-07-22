package me.jeehahn.springbootdeveloper.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import me.jeehahn.springbootdeveloper.config.jwt.JwtFactory;
import me.jeehahn.springbootdeveloper.config.jwt.JwtProperties;
import me.jeehahn.springbootdeveloper.domain.RefreshToken;
import me.jeehahn.springbootdeveloper.domain.User;
import me.jeehahn.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.jeehahn.springbootdeveloper.repository.RefreshTokenRepository;
import me.jeehahn.springbootdeveloper.repository.UserRepository;
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
class TokenApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken: Successfully creates a new access token")
    @Test
    public void createNewAccessToken() throws Exception {

        // Given

        final String url = "/api/token";
        User testUser = userRepository.save(User.builder()
            .email("test1234@email.com")
            .password("test1234")
            .build());

        String refreshToken = JwtFactory.builder()
            .claims(Map.of("id", testUser.getId()))
            .subject(testUser.getEmail())
            .build()
            .createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        final CreateAccessTokenRequest createAccessTokenRequest =
            new CreateAccessTokenRequest();

        createAccessTokenRequest.setRefreshToken(refreshToken);

        final String requestBody = objectMapper.writeValueAsString(createAccessTokenRequest);

        // When
        ResultActions result = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

        // Then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}