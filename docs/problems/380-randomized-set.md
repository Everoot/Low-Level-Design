# 380. Insert Delete GetRandom O(1)

## The core tradeoff

A hash set supports `O(1)` insertion and deletion but not uniform random access. An array supports `O(1)` random access, but deleting from the middle requires shifting elements. Swap the target with the last element, remove the tail, and keep a value-to-index map in sync.

## Java implementation

```java
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class RandomizedSet {
    private final List<Integer> values = new ArrayList<>();
    private final Map<Integer, Integer> index = new HashMap<>();

    public boolean insert(int val) {
        if (index.containsKey(val)) return false;
        index.put(val, values.size());
        values.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = index.get(val);
        if (i == null) return false;
        int lastValue = values.get(values.size() - 1);
        values.set(i, lastValue);
        index.put(lastValue, i);
        values.remove(values.size() - 1);
        index.remove(val);
        return true;
    }

    public int getRandom() {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}
```

The same steps remain correct when the target is already the last element. All three operations are `O(1)` on average, with `O(n)` space.
