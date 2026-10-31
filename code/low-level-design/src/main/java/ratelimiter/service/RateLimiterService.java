package ratelimiter.service;

import ratelimiter.strategy.RateLimitStrategy;

public class RateLimiterService {
    private RateLimitStrategy strategy;

    public RateLimiterService(RateLimitStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean allow(String userId) {
        return strategy.allow(userId);
    }
}
