# 933. Number of Recent Calls

## Design

Each `ping(t)` keeps only requests in the inclusive interval `[t - 3000, t]`. Because `t` is strictly increasing, expired timestamps can only appear at the front, so a queue is sufficient.

## Java implementation

```java
import java.util.ArrayDeque;
import java.util.Deque;

class RecentCounter {
    private final Deque<Integer> calls = new ArrayDeque<>();

    public int ping(int t) {
        calls.offerLast(t);
        while (calls.peekFirst() < t - 3000) calls.pollFirst();
        return calls.size();
    }
}
```

!!! note "Why `<` instead of `<=`?"
    The left endpoint `t - 3000` is part of the valid interval, so remove only timestamps strictly smaller than it.

Each timestamp enters and leaves the queue at most once, so each operation is amortized `O(1)`. Space depends on the number of requests in the 3000 ms window.
