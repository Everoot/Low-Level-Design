package ratelimiter.strategy;

public interface RateLimitStrategy {
    boolean allow(String userId);
}
