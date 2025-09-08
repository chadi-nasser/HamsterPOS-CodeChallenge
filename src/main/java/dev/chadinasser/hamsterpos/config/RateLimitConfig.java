package dev.chadinasser.hamsterpos.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
    @Value("${application.rate.limit.capacity}")
    private long capacity;

    @Bean
    public Bucket bucket() {
        Bandwidth limit = Bandwidth.builder().capacity(capacity).refillGreedy(capacity, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }
}
