package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.exception.InvalidRefreshTokenException;
import dev.chadinasser.hamsterpos.model.RefreshToken;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.repo.RefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final RefreshTokenRepo refreshTokenRepo;
    @Value("${application.security.jwt.secret}")
    private String privateKey;
    @Value("${application.security.jwt.accessTokenExpiration}")
    private long accessTokenExpiration; // in milliseconds
    @Value("${application.security.jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration; // in milliseconds

    public JwtService(RefreshTokenRepo refreshTokenRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
    }

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenExpiration, JwtTokenType.ACCESS);
    }

    @Transactional
    public String generateRefreshToken(String username) {
        String token = generateToken(username, refreshTokenExpiration, JwtTokenType.REFRESH);

        // Save the refresh token to database with expiration time
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
        refreshTokenRepo.save(refreshToken);

        return token;
    }

    public String generateToken(String username, long expirationTime, JwtTokenType tokenType) {
        return Jwts.builder()
                .subject(username)
                .claim("type", tokenType.name())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expirationTime))) // Token valid for 1 hour
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            RefreshToken refreshToken = refreshTokenRepo.findByToken(token).orElseThrow(
                    InvalidRefreshTokenException::new
            );

            if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                refreshTokenRepo.deleteByToken(token);
                return false;
            }

            JwtTokenType tokenType = extractTokenType(token);
            if (tokenType != JwtTokenType.REFRESH) {
                return false;
            }

            return isNotTokenExpired(token);
        } catch (Exception e) {
            // Token is malformed or invalid
            return false;
        }
    }

    @Transactional
    public void invalidateRefreshToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

    public boolean isTokenValid(String token, User userDetails) {
        final String username = extractUsername(token);
        JwtTokenType tokenType = extractTokenType(token);
        if (tokenType != JwtTokenType.ACCESS) {
            return false;
        }
        return (username.equals(userDetails.getUsername())) && isNotTokenExpired(token);
    }

    private boolean isNotTokenExpired(String token) {
        return !extractExpiration(token).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public JwtTokenType extractTokenType(String token) {
        return extractClaim(token, claims -> JwtTokenType.valueOf(claims.get("type", String.class)));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
