package me.jeehahn.springbootdeveloper.dto;

import lombok.Getter;

@Getter
public class CreateAccessTokenRequest {
    private String refreshToken;
}
