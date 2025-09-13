package dev.chadinasser.hamsterpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
