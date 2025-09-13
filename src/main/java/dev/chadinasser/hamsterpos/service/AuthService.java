package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.dto.*;
import dev.chadinasser.hamsterpos.exception.InvalidRefreshTokenException;
import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.RoleType;
import dev.chadinasser.hamsterpos.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private LoginResponseDto generateBothTokensForUser(String email) {
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public RegisterResponseDto register(AuthRequestDto request) {
        Optional<Role> role = roleService.findByRole(RoleType.USER);
        if (role.isEmpty()) {
            throw new RuntimeException("Default USER role not found");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role.get()).build();

        userService.addUser(user);
        return new RegisterResponseDto(user.getId(), user.getEmail());
    }

    @Transactional
    public LoginResponseDto login(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If the above line does not throw an exception, the user is authenticated
        User user = userService.findByEmail(request.getEmail());
        return generateBothTokensForUser(user.getEmail());
    }

    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        if (!jwtService.isRefreshTokenValid(request.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }
        String email = jwtService.extractEmail(request.getRefreshToken());
        String accessToken = jwtService.generateAccessToken(email);
        return new LoginResponseDto(accessToken, null);
    }

    public void logout(RefreshTokenRequestDto request) {
        jwtService.invalidateRefreshToken(request.getRefreshToken());
    }
}
