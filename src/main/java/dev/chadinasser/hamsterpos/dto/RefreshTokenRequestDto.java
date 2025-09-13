package dev.chadinasser.hamsterpos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshTokenRequestDto {
    private final String refreshToken;
}
