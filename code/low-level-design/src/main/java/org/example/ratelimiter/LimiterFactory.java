package org.example.ratelimiter;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @Author: eve
 * @Date: 2026-10-13 08:04
 */
public class LimiterFactory {

    @SuppressWarnings("unchecked")
    public Limiter create(
            Map<String, Object> externalConfig) {

        String algorithm =
                (String) externalConfig.get("algorithm");

        Map<String, Object> algoConfig =
                (Map<String, Object>)
                        externalConfig.get("algoConfig");

        if (algorithm == null || algoConfig == null) {
            throw new IllegalArgumentException(
                    "Invalid limiter config");
        }

        return switch (algorithm) {

            case "TokenBucket" ->
                    new TokenBucketLimiter(
                            ((Number) algoConfig
                                    .get("capacity"))
                                    .intValue(),

                            ((Number) algoConfig
                                    .get("refillRatePerSecond"))
                                    .intValue()
                    );

            case "SlidingWindowLog" ->
                    new SlidingWindowLogLimiter(
                            ((Number) algoConfig
                                    .get("maxRequests"))
                                    .intValue(),

                            ((Number) algoConfig
                                    .get("windowMs"))
                                    .longValue()
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unknown algorithm: "
                                    + algorithm);
        };
    }
}
