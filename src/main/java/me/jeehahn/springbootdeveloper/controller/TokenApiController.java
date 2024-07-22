package me.jeehahn.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.jeehahn.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.jeehahn.springbootdeveloper.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        try {
            String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
            CreateAccessTokenResponse response = new CreateAccessTokenResponse(newAccessToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Log the exception or handle it accordingly
            // e.g., log.error("Failed to create access token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
