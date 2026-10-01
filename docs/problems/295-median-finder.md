# 295. Find Median from Data Stream

## Design

The max heap `lower` stores the smaller half, while the min heap `upper` stores the larger half. The boundary values stay ordered, the size difference never exceeds one, and `lower` is allowed to hold the extra element.

## Java implementation

```java
import java.util.Collections;
import java.util.PriorityQueue;

class MedianFinder {
    private final PriorityQueue<Integer> lower =
        new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> upper = new PriorityQueue<>();

    public void addNum(int num) {
        if (lower.isEmpty() || num <= lower.peek()) lower.offer(num);
        else upper.offer(num);

        if (lower.size() > upper.size() + 1) upper.offer(lower.poll());
        else if (upper.size() > lower.size()) lower.offer(upper.poll());
    }

    public double findMedian() {
        if (lower.size() > upper.size()) return lower.peek();
        return ((long) lower.peek() + upper.peek()) / 2.0;
    }
}
```

!!! warning "Integer overflow"
    Cast to `long` before adding two `int` values. Otherwise, extreme inputs can overflow before division by `2.0`.

**Complexity:** insertion is `O(log n)`, lookup is `O(1)`, and space is `O(n)`.
