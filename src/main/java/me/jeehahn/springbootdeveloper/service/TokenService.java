package me.jeehahn.springbootdeveloper.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.config.jwt.TokenProvider;
import me.jeehahn.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public String createNewAccessToken(String refreshToken) {
        if (tokenProvider.isValidToken(refreshToken)) {
            Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
            User user =  userService.findById(userId);
            return tokenProvider.generateToken(user, Duration.ofHours(2));
        }
        throw new IllegalArgumentException("Unexpected refreshToken");
    }
}
