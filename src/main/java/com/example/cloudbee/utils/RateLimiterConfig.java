package com.example.cloudbee.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Value("${rate.limit.requests.per.second}")
    private double rateLimit;

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(rateLimit);
    }
}