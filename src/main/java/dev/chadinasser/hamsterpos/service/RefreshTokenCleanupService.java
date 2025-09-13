package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenCleanupService {
    private final RefreshTokenRepo refreshTokenRepo;

    @Scheduled(fixedRateString = "${application.security.jwt.cleanupScheduleInterval}")
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            LocalDateTime now = LocalDateTime.now();
            refreshTokenRepo.deleteByExpiresAtBefore(now);
            log.info("Cleaned up expired refresh tokens before: {}", now);
        } catch (Exception e) {
            log.error("Error occurred while cleaning up expired refresh tokens", e);
        }
    }
}
