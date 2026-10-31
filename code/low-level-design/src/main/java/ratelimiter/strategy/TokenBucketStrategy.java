package ratelimiter.strategy;

import ratelimiter.model.TokenBucket;

import java.util.HashMap;
import java.util.Map;

public class TokenBucketStrategy implements RateLimitStrategy {
    private Map<String, TokenBucket> buckets = new HashMap<>();

    @Override
    public boolean allow(String userId) {
        buckets.putIfAbsent(userId, new TokenBucket(5));
        return buckets.get(userId).consume();
    }
}
