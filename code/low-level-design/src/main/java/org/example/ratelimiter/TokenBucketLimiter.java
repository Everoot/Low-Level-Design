package org.example.ratelimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: eve
 * @Date: 2026-10-13 06:48
 */
public class TokenBucketLimiter implements Limiter {

    private final int capacity;
    private final int refillRatePerSecond;

    private final Map<String, TokenBucket> buckets =
            new HashMap<>();

    public TokenBucketLimiter(
            int capacity,
            int refillRatePerSecond) {

        if (capacity <= 0 || refillRatePerSecond <= 0) {
            throw new IllegalArgumentException(
                    "capacity and refill rate must be positive");
        }

        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    @Override
    public RateLimitResult allow(String key) {

        TokenBucket bucket =
                getOrCreateBucket(key);

        long now =
                System.currentTimeMillis();

        long elapsed =
                now - bucket.lastRefillTime;

        double tokensToAdd =
                (elapsed * refillRatePerSecond)
                        / 1000.0;

        bucket.tokens =
                Math.min(
                        capacity,
                        bucket.tokens + tokensToAdd
                );

        bucket.lastRefillTime = now;

        if (bucket.tokens >= 1.0) {

            bucket.tokens -= 1.0;

            return new RateLimitResult(
                    true,
                    (int) Math.floor(bucket.tokens),
                    null
            );
        }

        double tokensNeeded =
                1.0 - bucket.tokens;

        long retryAfterMs =
                (long) Math.ceil(
                        (tokensNeeded * 1000.0)
                                / refillRatePerSecond
                );

        return new RateLimitResult(
                false,
                0,
                retryAfterMs
        );
    }

    private TokenBucket getOrCreateBucket(
            String key) {

        return buckets.computeIfAbsent(
                key,
                k -> new TokenBucket(
                        capacity,
                        System.currentTimeMillis()
                )
        );
    }

    private static class TokenBucket {
        private double tokens;
        private long lastRefillTime;

        private TokenBucket(
                double initialTokens,
                long time) {
            this.tokens = initialTokens;
            this.lastRefillTime = time;
        }
    }
}