package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.AuthRequestDto;
import dev.chadinasser.hamsterpos.dto.AuthResponseDto;
import dev.chadinasser.hamsterpos.dto.RefreshTokenRequestDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints for user authentication and registration"
)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticate user and return JWT token",
            security = {}
    )
    public ResponseDto<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest) {
        return new ResponseDto<>(HttpStatus.OK, authService.login(authRequest));
    }

    @PostMapping("/register")
    @Operation(
            summary = "User Registration",
            description = "Register a new user and return JWT token",
            security = {}
    )
    public ResponseDto<AuthResponseDto> register(@Valid @RequestBody AuthRequestDto registerRequest) {
        return new ResponseDto<>(HttpStatus.CREATED, authService.register(registerRequest));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh Token",
            description = "Refresh JWT token using a valid refresh token",
            security = {}
    )
    public ResponseDto<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        return new ResponseDto<>(HttpStatus.OK, authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "User Logout",
            description = "Logout user and invalidate refresh token"
    )
    public ResponseDto<Void> logout(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        System.out.println("Logout request received for token: " + refreshTokenRequest.getRefreshToken());
        authService.logout(refreshTokenRequest);
        System.out.println("Logout successful for token: " + refreshTokenRequest.getRefreshToken());
        return new ResponseDto<>(HttpStatus.OK, null);
    }
}
