package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.dto.AuthRequestDto;
import dev.chadinasser.hamsterpos.dto.AuthResponseDto;
import dev.chadinasser.hamsterpos.dto.RefreshTokenRequestDto;
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

    private AuthResponseDto generateBothTokensForUser(String username) {
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);
        return AuthResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Transactional
    public AuthResponseDto register(AuthRequestDto request) {
        Optional<Role> role = roleService.findByRole(RoleType.USER);
        if (role.isEmpty()) {
            return null;
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role.get()).build();

        userService.addUser(user);
        return generateBothTokensForUser(user.getUsername());
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If the above line does not throw an exception, the user is authenticated
        User user = userService.findByUsername(request.getUsername());

        return generateBothTokensForUser(user.getUsername());
    }

    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        if (!jwtService.isRefreshTokenValid(request.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }
        String username = jwtService.extractUsername(request.getRefreshToken());
        String accessToken = jwtService.generateAccessToken(username);
        return AuthResponseDto.builder().accessToken(accessToken).build();
    }

    @Transactional
    public void logout(RefreshTokenRequestDto request) {
        jwtService.invalidateRefreshToken(request.getRefreshToken());
    }
}
