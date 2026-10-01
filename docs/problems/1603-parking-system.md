# 1603. Design Parking System

## Design

There are only three fixed parking-space types, so no map or object hierarchy is necessary. A length-four array lets each index correspond directly to `carType` and avoids repeated branching.

## Java implementation

```java
class ParkingSystem {
    private final int[] available = new int[4];

    public ParkingSystem(int big, int medium, int small) {
        available[1] = big;
        available[2] = medium;
        available[3] = small;
    }

    public boolean addCar(int carType) {
        if (available[carType] == 0) return false;
        available[carType]--;
        return true;
    }
}
```

## Interview extension

If the requirements expand to returning a specific space, handling departures, reservations, or concurrent arrivals, a counter is no longer enough. The design would need parking-space entities, an index of available spaces, a vehicle-to-space mapping, and atomic allocation.

Under the current constraints, `addCar` uses `O(1)` time and space. Avoid overengineering for hypothetical requirements.
