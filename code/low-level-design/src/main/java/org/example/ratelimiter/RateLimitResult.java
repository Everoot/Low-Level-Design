package org.example.ratelimiter;

/**
 * @Author: eve
 * @Date: 2026-10-13 06:45
 */
public final class RateLimitResult {
    private final boolean allowed;
    private final int remaining;
    private final Long retryAfterMs;

    public RateLimitResult(
            boolean allowed,
            int remaining,
            Long retryAfterMs) {
        this.allowed = allowed;
        this.remaining = remaining;
        this.retryAfterMs = retryAfterMs;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public int getRemaining() {
        return remaining;
    }

    public Long getRetryAfterMs() {
        return retryAfterMs;
    }
}