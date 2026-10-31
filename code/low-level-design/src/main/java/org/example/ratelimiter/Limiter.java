package org.example.ratelimiter;

/**
 * @Author: eve
 * @Date: 2026-10-13 06:47
 */
public interface Limiter {
    RateLimitResult allow(String key);
}
