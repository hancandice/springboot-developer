package me.jeehahn.springbootdeveloper.config.jwt;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Date;

import me.jeehahn.springbootdeveloper.domain.User;
import me.jeehahn.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): should create a token using user information and expiration time")
    @Test
    void generateToken_shouldCreateTokenWithUserAndExpiration() {
        // Arrange
        User testUser = createUser("test@gmail.com", "password1234");
        Duration expirationDuration = Duration.ofDays(7);

        // Act
        String token = tokenProvider.generateToken(testUser, expirationDuration);

        // Assert
        assertNotNull(token, "Token should not be null");
        assertUserIdFromTokenMatches(testUser.getId(), token);
        assertTokenExpirationIsValid(token, expirationDuration);
    }

    @DisplayName("isValidToken() should return false for expired tokens")
    @Test
    void isValidToken_shouldReturnFalseForExpiredTokens() {
        // Arrange
        String expiredToken = createExpiredToken();

        // Act
        boolean isValid = tokenProvider.isValidToken(expiredToken);

        // Assert
        assertFalse(isValid, "Expired token should be invalid");
    }

    @DisplayName("isValidToken() should return true for valid tokens")
    @Test
    void isValidToken_shouldReturnTrueForValidTokens() {
        // Arrange
        String validToken = createValidToken();

        // Act
        boolean isValid = tokenProvider.isValidToken(validToken);

        // Assert
        assertTrue(isValid, "Valid token should be recognized as valid");
    }

    @DisplayName("getAuthentication() should return an Authentication object with correct user details and authorities")
    @Test
    void getAuthentication_shouldReturnCorrectAuthentication() {
        // Arrange
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder().subject(userEmail).build().createToken(jwtProperties);

        // Act
        Authentication authentication = tokenProvider.getAuthentication(token);

        // Assert
        assertEquals(userEmail, ((UserDetails) authentication.getPrincipal()).getUsername());
    }

    // Helper Methods

    private User createUser(String email, String password) {
        return userRepository.save(User.builder()
            .email(email)
            .password(password)
            .build());
    }

    private void assertUserIdFromTokenMatches(Long expectedUserId, String token) {
        Long userIdFromToken = tokenProvider.getUserId(token);
        assertEquals(expectedUserId, userIdFromToken, "User ID from token should match the expected ID");
    }

    private void assertTokenExpirationIsValid(String token, Duration expectedDuration) {
        Date expirationDate = tokenProvider.getExpirationDate(token);
        Date currentDate = new Date();

        assertTrue(expirationDate.after(currentDate), "Token expiration date should be in the future");
        assertTrue(expirationDate.before(new Date(currentDate.getTime() + expectedDuration.toMillis())),
            "Token expiration date should be within the specified duration");
    }

    private String createExpiredToken() {
        return JwtFactory.builder()
            .expiration(new Date(new Date().getTime() - Duration.ofDays(1).toMillis()))
            .build()
            .createToken(jwtProperties);
    }

    private String createValidToken() {
        return JwtFactory.withDefaultValues()
            .createToken(jwtProperties);
    }
}
