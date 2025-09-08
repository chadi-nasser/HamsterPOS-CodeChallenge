package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.AuthRequestDto;
import dev.chadinasser.hamsterpos.dto.AuthResponseDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<AuthResponseDto>> login(@Valid @RequestBody AuthRequestDto authRequest) {
        return new ResponseDto<>(HttpStatus.OK, authService.login(authRequest)).toResponseEntity();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<AuthResponseDto>> register(@Valid @RequestBody AuthRequestDto registerRequest) {
        return new ResponseDto<>(HttpStatus.CREATED, authService.register(registerRequest)).toResponseEntity();
    }
}
