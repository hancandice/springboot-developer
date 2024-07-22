package me.jeehahn.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.RefreshToken;
import me.jeehahn.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Unexpected userId"));
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(()-> new IllegalArgumentException("Unexpected token"));
    }
}
