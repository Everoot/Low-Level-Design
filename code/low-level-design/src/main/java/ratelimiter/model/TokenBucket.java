package ratelimiter.model;

public class TokenBucket {
    private int capacity;
    private int tokens;

    public TokenBucket(int capacity) {
        this.capacity = capacity;
        this.tokens = capacity;
    }

    public boolean consume() {
        if (tokens == 0) {
            return false;
        }

        tokens--;
        return true;
    }
}
