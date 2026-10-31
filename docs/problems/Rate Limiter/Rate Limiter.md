# Rate Limiter

## 1. Goal

Design a small in-memory rate limiter that can:

- evaluate requests independently for each user ID;
- allow a limited number of requests;
- delegate the limiting algorithm through a replaceable strategy.

The provided implementation demonstrates the shape of a token-bucket design. Each user receives five tokens, and each allowed request consumes one token.

## 2. Project Structure

```text
src/main/java/ratelimiter/
├── model/
│   └── TokenBucket.java
├── service/
│   └── RateLimiterService.java
├── strategy/
│   ├── RateLimitStrategy.java
│   └── TokenBucketStrategy.java
└── Main.java
```

## 3. Responsibility of Each Layer

| Layer | Responsibility |
|---|---|
| `model/` | Owns token capacity and remaining-token state. |
| `strategy/` | Defines and implements the limiting algorithm. |
| `service/` | Exposes a simple `allow(userId)` API. |
| `Main.java` | Sends repeated requests for manual verification. |

### 3.1 `model/`

```java
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
```

`TokenBucket` owns the mutable token count. The `capacity` is stored, although the current implementation does not use it after construction because tokens are never refilled.

### 3.2 `strategy/`

```java
public interface RateLimitStrategy {
    boolean allow(String userId);
}
```

`TokenBucketStrategy` creates one bucket per user:

```java
public class TokenBucketStrategy implements RateLimitStrategy {
    private Map<String, TokenBucket> buckets = new HashMap<>();

    @Override
    public boolean allow(String userId) {
        buckets.putIfAbsent(userId, new TokenBucket(5));
        return buckets.get(userId).consume();
    }
}
```

### 3.3 `service/`

```java
public class RateLimiterService {
    private RateLimitStrategy strategy;

    public RateLimiterService(RateLimitStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean allow(String userId) {
        return strategy.allow(userId);
    }
}
```

The service contains no algorithm-specific logic. It provides a stable API while the injected strategy controls behavior.

## 4. Main Example

```java
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
```

Output:

```text
Request 1: true
Request 2: true
Request 3: true
Request 4: true
Request 5: true
Request 6: false
Request 7: false
```

## 5. Request Flow

```text
Caller
  -> RateLimiterService.allow(userId)
  -> TokenBucketStrategy finds or creates the user's bucket
  -> TokenBucket.consume()
  -> true when a token was consumed, otherwise false
```

## 6. Simple Interview Rule

```text
TokenBucket       = STATE
RateLimitStrategy = HOW requests are limited
RateLimiterService = API / DO
userId            = partition key
```

## 7. Why Use Strategy Pattern?

Rate-limiting algorithms have different fairness, memory, and burst characteristics. The service can switch to fixed-window, sliding-window, leaky-bucket, or a complete refillable token-bucket implementation without changing callers.

## 8. Important Limitation

!!! warning "This is not a complete token bucket"
    A real token bucket refills tokens over time up to its capacity. The current `TokenBucket` never refills, so it behaves as a lifetime quota of five successful requests per user.

Additional limitations:

- Bucket capacity is hard-coded to `5`.
- `HashMap` and token updates are not thread-safe.
- User buckets are never removed, so memory grows with unique user IDs.
- State is local to one process and is not shared across application instances.
- Null or blank user IDs are not validated.

## 9. Complexity

Each request is `O(1)` average time for the hash-map lookup and token update. Space is `O(u)`, where `u` is the number of unique user IDs.

## 10. Production Extensions

- Add refill rate and last-refill timestamp to `TokenBucket`.
- Inject capacity and refill configuration.
- Make token consumption atomic.
- Add bucket expiration or cleanup.
- Use Redis or another shared store for distributed enforcement.
- Return a richer result containing `allowed`, remaining tokens, and retry timing.
