package org.example.ratelimiter;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @Author: eve
 * @Date: 2026-10-13 08:04
 */
public class SlidingWindowLogLimiter
        implements Limiter {

    private final int maxRequests;
    private final long windowMs;

    private final Map<String, RequestLog> logs =
            new HashMap<>();

    public SlidingWindowLogLimiter(
            int maxRequests,
            long windowMs) {

        if (maxRequests <= 0 || windowMs <= 0) {
            throw new IllegalArgumentException(
                    "maxRequests and windowMs must be positive");
        }

        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
    }

    @Override
    public RateLimitResult allow(String key) {

        RequestLog log =
                getOrCreateLog(key);

        long now =
                System.currentTimeMillis();

        long cutoff =
                now - windowMs;

        while (
                !log.timestamps.isEmpty()
                        && log.timestamps.peek() < cutoff
        ) {
            log.timestamps.poll();
        }

        if (log.timestamps.size() < maxRequests) {

            log.timestamps.add(now);

            return new RateLimitResult(
                    true,
                    maxRequests - log.timestamps.size(),
                    null
            );
        }

        long oldestTimestamp =
                log.timestamps.peek();

        long retryAfterMs =
                (oldestTimestamp + windowMs) - now;

        return new RateLimitResult(
                false,
                0,
                retryAfterMs
        );
    }

    private RequestLog getOrCreateLog(
            String key) {

        return logs.computeIfAbsent(
                key,
                k -> new RequestLog(
                        new ArrayDeque<>()
                )
        );
    }

    private static class RequestLog {

        private final Queue<Long> timestamps;

        private RequestLog(
                Queue<Long> timestamps) {
            this.timestamps = timestamps;
        }
    }
}