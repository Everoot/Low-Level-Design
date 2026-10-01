# 981. Time Based Key-Value Store

## Deriving the design from constraints

Timestamps for the same key are strictly increasing, so writes can append directly to an array. A lookup needs the last version whose `timestamp <= target`, which is a binary-search problem.

## Java implementation

```java
import java.util.*;

class TimeMap {
    private record Entry(int timestamp, String value) {}
    private final Map<String, List<Entry>> history = new HashMap<>();

    public void set(String key, String value, int timestamp) {
        history.computeIfAbsent(key, k -> new ArrayList<>())
               .add(new Entry(timestamp, value));
    }

    public String get(String key, int timestamp) {
        List<Entry> entries = history.get(key);
        if (entries == null) return "";

        int left = 0, right = entries.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (entries.get(mid).timestamp() <= timestamp) left = mid + 1;
            else right = mid;
        }
        return left == 0 ? "" : entries.get(left - 1).value();
    }
}
```

The search finds the first position where `timestamp > target`; the answer is the previous element. `set` is `O(1)` and `get` is `O(log m)`, where `m` is the number of versions for that key.
