# Locker System

## 1. Goal

Design a simple in-memory package locker system that can:

- maintain lockers of different sizes;
- assign a package through a replaceable selection strategy;
- mark a locker as occupied;
- release and return the package during pickup.

The current implementation focuses on assignment. It does not include access codes, expiration, notifications, users, locations, persistence, or concurrency.

## 2. Project Structure

```text
src/main/java/locker/
├── enums/
│   └── LockerSize.java
├── model/
│   ├── Locker.java
│   └── Package.java
├── service/
│   └── LockerService.java
├── strategy/
│   ├── FirstAvailableLockerStrategy.java
│   └── LockerStrategy.java
└── Main.java
```

## 3. Responsibility of Each Layer

| Layer | Responsibility |
|---|---|
| `enums/` | Defines supported locker and package sizes. |
| `model/` | Owns locker occupancy and package data. |
| `strategy/` | Chooses a locker for a package. |
| `service/` | Coordinates locker assignment. |
| `Main.java` | Runs a small assignment example. |

### 3.1 `enums/`

```java
public enum LockerSize {
    SMALL,
    MEDIUM,
    LARGE
}
```

### 3.2 `model/`

`Package` describes the item that needs a locker:

```java
public class Package {
    private String id;
    private LockerSize size;

    public Package(String id, LockerSize size) {
        this.id = id;
        this.size = size;
    }

    public LockerSize getSize() {
        return size;
    }
}
```

`Locker` owns its occupancy state. Availability is derived from whether `pkg` is `null`:

```java
public class Locker {
    private String id;
    private LockerSize size;
    private Package pkg;

    public Locker(String id, LockerSize size) {
        this.id = id;
        this.size = size;
    }

    public boolean isAvailable() {
        return pkg == null;
    }

    public void assign(Package pkg) {
        this.pkg = pkg;
    }

    public Package pickup() {
        Package result = pkg;
        pkg = null;
        return result;
    }

    public LockerSize getSize() {
        return size;
    }
}
```

### 3.3 `strategy/`

```java
public interface LockerStrategy {
    Locker findLocker(List<Locker> lockers, Package pkg);
}
```

The current strategy returns the first empty locker:

```java
public class FirstAvailableLockerStrategy implements LockerStrategy {
    @Override
    public Locker findLocker(List<Locker> lockers, Package pkg) {
        for (Locker locker : lockers) {
            if (locker.isAvailable()) {
                return locker;
            }
        }
        return null;
    }
}
```

!!! note "Current behavior"
    The strategy receives the package but does not compare `Package.size` with `Locker.size`. A `SMALL` package may therefore be assigned to any first available locker, and an oversized package is not rejected.

### 3.4 `service/`

```java
public class LockerService {
    private List<Locker> lockers;
    private LockerStrategy strategy;

    public LockerService(List<Locker> lockers, LockerStrategy strategy) {
        this.lockers = lockers;
        this.strategy = strategy;
    }

    public Locker assign(Package pkg) {
        Locker locker = strategy.findLocker(lockers, pkg);

        if (locker == null) {
            throw new RuntimeException("No locker available");
        }

        locker.assign(pkg);
        return locker;
    }
}
```

## 4. Main Example

```java
public class Main {
    public static void main(String[] args) {
        Locker l1 = new Locker("L1", LockerSize.SMALL);
        Locker l2 = new Locker("L2", LockerSize.MEDIUM);

        LockerService service = new LockerService(
                List.of(l1, l2),
                new FirstAvailableLockerStrategy()
        );

        Package pkg = new Package("P1", LockerSize.SMALL);
        Locker assigned = service.assign(pkg);

        System.out.println("Assigned locker size: " + assigned.getSize());
    }
}
```

Output:

```text
Assigned locker size: SMALL
```

## 5. Request Flow

```text
Main
  -> creates Package(id, size)
  -> calls LockerService.assign(pkg)
  -> LockerStrategy selects a Locker
  -> Locker.assign(pkg)
  -> service returns the assigned Locker
```

Pickup is handled by calling `assigned.pickup()`, which returns the package and clears the locker.

## 6. Simple Interview Rule

```text
Package        = WHAT needs storage
Locker         = owns occupancy state
LockerStrategy = HOW a locker is selected
LockerService  = DO the assignment workflow
LockerSize     = OPTIONS
```

## 7. Why Use Strategy Pattern?

Selection can evolve independently from assignment. Future strategies can choose an exact-size locker, the smallest locker that fits, or a locker near the pickup entrance without changing `LockerService`.

## 8. Current Limitations

- Package-to-locker size compatibility is not enforced.
- IDs have no getters and are not used for lookup.
- Pickup is not exposed through `LockerService`.
- There are no access tokens, users, expiration times, notifications, or audit records.
- Assignment is not atomic for concurrent callers.

## 9. Complexity

For `n` lockers, assignment is `O(n)` time and `O(1)` auxiliary space. Pickup from an already identified locker is `O(1)`.
