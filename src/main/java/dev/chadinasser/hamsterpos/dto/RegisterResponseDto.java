package dev.chadinasser.hamsterpos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegisterResponseDto {
    private UUID id;
    private String email;
}
