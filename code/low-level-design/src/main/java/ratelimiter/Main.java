package ratelimiter;

import ratelimiter.service.RateLimiterService;
import ratelimiter.strategy.TokenBucketStrategy;

public class Main {
    public static void main(String[] args) {
        RateLimiterService service =
                new RateLimiterService(new TokenBucketStrategy());

        for (int i = 1; i <= 7; i++) {
            System.out.println(
                    "Request " + i + ": " + service.allow("user1")
            );
        }
    }
}
