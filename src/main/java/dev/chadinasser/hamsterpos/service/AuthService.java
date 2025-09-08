package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.dto.AuthRequestDto;
import dev.chadinasser.hamsterpos.dto.AuthResponseDto;
import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.RoleType;
import dev.chadinasser.hamsterpos.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AuthResponseDto register(AuthRequestDto request) {
        Optional<Role> role = roleService.findByRole(RoleType.USER);
        if (role.isEmpty()) {
            return null;
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role.get()).build();

        userService.addUser(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDto.builder().token(jwtToken).build();
    }

    public AuthResponseDto login(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If the above line does not throw an exception, the user is authenticated
        var user = userService.findByUsername(request.getUsername());

        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDto.builder().token(jwtToken).build();
    }
}
