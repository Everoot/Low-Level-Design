# 146. LRU Cache

## Requirements

The cache has a fixed capacity, and both `get` and `put` must run in `O(1)`. Reading or updating an entry makes it the most recently used; once the cache exceeds its capacity, evict the least recently used entry.

A hash map alone cannot track usage order, while a linked list alone cannot locate a key in constant time. Combine them:

- `Map<key, Node>` locates any node in constant time.
- A doubly linked list removes any node, moves it to the front, or evicts the tail in constant time.

## Core invariants

The node after the head sentinel is always the most recently used, and the node before the tail sentinel is always the least recently used. The map and list contain exactly the same real nodes.

## Java implementation

```java
import java.util.HashMap;
import java.util.Map;

class LRUCache {
    private static class Node {
        int key, value;
        Node prev, next;
        Node(int key, int value) { this.key = key; this.value = value; }
    }

    private final int capacity;
    private final Map<Integer, Node> nodes = new HashMap<>();
    private final Node head = new Node(0, 0);
    private final Node tail = new Node(0, 0);

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        Node node = nodes.get(key);
        if (node == null) return -1;
        moveToFront(node);
        return node.value;
    }

    public void put(int key, int value) {
        Node node = nodes.get(key);
        if (node != null) {
            node.value = value;
            moveToFront(node);
            return;
        }
        Node fresh = new Node(key, value);
        nodes.put(key, fresh);
        addFirst(fresh);
        if (nodes.size() > capacity) {
            Node lru = tail.prev;
            remove(lru);
            nodes.remove(lru.key);
        }
    }

    private void moveToFront(Node node) { remove(node); addFirst(node); }
    private void addFirst(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
```

## Common pitfalls

- Updating an existing key also counts as an access.
- Eviction must remove the entry from both the list and the map.
- Sentinel nodes eliminate special cases for empty and single-node lists.

**Complexity:** `get` and `put` are both `O(1)`; space is `O(capacity)`.
