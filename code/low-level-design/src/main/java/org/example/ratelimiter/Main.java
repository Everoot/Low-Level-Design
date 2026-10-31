package org.example.ratelimiter;

/**
 * @Author: eve
 * @Date: 2026-10-12 15:58
 */
import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Map<String, Object> searchAlgoConfig = new HashMap<>();
        searchAlgoConfig.put("capacity", 3);
        searchAlgoConfig.put("refillRatePerSecond", 1);

        Map<String, Object> searchConfig = new HashMap<>();
        searchConfig.put("endpoint", "/search");
        searchConfig.put("algorithm", "TokenBucket");
        searchConfig.put("algoConfig", searchAlgoConfig);


        Map<String, Object> defaultAlgoConfig = new HashMap<>();
        defaultAlgoConfig.put("capacity", 2);
        defaultAlgoConfig.put("refillRatePerSecond", 1);

        Map<String, Object> defaultConfig = new HashMap<>();
        defaultConfig.put("algorithm", "TokenBucket");
        defaultConfig.put("algoConfig", defaultAlgoConfig);


        RateLimiter rateLimiter = new RateLimiter(
                List.of(searchConfig),
                defaultConfig
        );


        String clientId = "user123";

        System.out.println("=== /search ===");

        for (int i = 1; i <= 5; i++) {

            RateLimitResult result =
                    rateLimiter.allow(
                            clientId,
                            "/search"
                    );

            printResult(i, result);
        }


        System.out.println("\nWait 1.1 seconds...");

        Thread.sleep(1100);


        RateLimitResult afterWait =
                rateLimiter.allow(
                        clientId,
                        "/search"
                );

        System.out.println("\n=== After Refill ===");

        printResult(
                6,
                afterWait
        );


        System.out.println("\n=== Unknown Endpoint ===");

        RateLimitResult defaultResult =
                rateLimiter.allow(
                        "user456",
                        "/unknown"
                );

        printResult(
                1,
                defaultResult
        );
    }


    private static void printResult(
            int requestNumber,
            RateLimitResult result
    ) {

        System.out.println(
                "Request " + requestNumber
                        + " | allowed = "
                        + result.isAllowed()
                        + " | remaining = "
                        + result.getRemaining()
                        + " | retryAfterMs = "
                        + result.getRetryAfterMs()
        );
    }

//    public static void main(String[] args) {
//
//        RateLimiter limiter =
//                createRateLimiter();
//
//        RateLimitResult result =
//                limiter.allow(
//                        "user123",
//                        "/search"
//                );
//
//        System.out.println(
//                result.isAllowed()
//        );
//    }
}
/*
Requirements:
1. Configuration is provided at startup (loaded once)
2. System receives requests with (clientId: string, endpoint: string)
3. Each endpoint has a configuration specifying:
   - Algorithm to use (e.g., "TokenBucket", "SlidingWindowLog", etc.)
   - Algorithm-specific parameters (e.g., capacity, refillRatePerSecond for Token Bucket)
4. System enforces rate limits by checking clientId against the endpoint's configuration
5. Return structured result: (allowed: boolean, remaining: int, retryAfterMs: long | null)
6. If endpoint has no configuration, use a default limit

Out of scope:
- Distributed rate limiting (Redis, coordination)
- Dynamic configuration updates
- Metrics and monitoring
- Config validation beyond basic checks


2. Entities
- RateLimiter
- Limiter
- TokenBucketLimiter
- SlidingWindowLogLimiter
- RateLimitResult

3. Class Design
interface Limiter {
    + RateLimitResult allow(String key}
}

class RateLimiter {
    - Map<String, Limiter> limiters
    - Limiter defaultLimiter

    + RateLimiter(Map<String, Limiter> limiters, Limiter defaultLimiter)
    + RateLimitResult allow(String clientId, String endpoint)
}

Meaning:
endpoint -> Map<String, Limiter>
                    |
                    +--> /login -> TokenBucketLimiter
                    +--> /search -> SlidingWindowLogLimiter
                    +--> default -> defaultLimiter

RateLimiter is the router/orchestrator. It selects a strategy and delegates.


class TokenBucketLimiter implements Limiter {
    - int capacity
    - double refillRate
    - Map<String, Bucket> buckets

    + TokenBucketLimiter(int capacity, double refillRate)
    + RateLimitResult allow(String key)
}

class Bucket {
    - double tokens
    - long lastRefill

    + Bucket(double tokens, long lastRefill)
}


One key/client owns one mutable bucket stat:
clientId -> Bucket
            - tokens
            - lastRefill


class RateLimitResult {
    - boolean allowed
    - int remaining
    - Long retryAfterMs

    + RateLimitResult(boolean allowed, int remaining, Long retryAfterMs)
}


 */