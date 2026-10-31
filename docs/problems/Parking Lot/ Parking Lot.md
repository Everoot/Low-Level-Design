# Parking Lot

## 1. Goal

Design a small in-memory parking system that can:

- maintain a collection of parking spots;
- select an available spot through a replaceable strategy;
- park a vehicle and issue a ticket;
- unpark a vehicle using that ticket.

The implementation is intentionally small for interview practice. It demonstrates object responsibilities and the Strategy pattern without adding payment, persistence, reservations, floors, or concurrency.

## 2. Current Scope

### Implemented

- Vehicle types: `CAR`, `TRUCK`, `MOTORCYCLE`
- Spot types: `COMPACT`, `LARGE`, `MOTORCYCLE`
- In-memory spot availability
- First-available spot selection
- Ticket creation with a generated UUID
- Parking and unparking
- Replaceable parking-selection strategy

### Not implemented

- Vehicle-to-spot compatibility rules
- Entry and exit timestamps
- Fee calculation or payment
- Ticket lookup by ID
- Multiple floors or entrances
- Reservations
- Thread safety

!!! note "Important implementation detail"
    `FirstAvailableStrategy` currently returns the first empty spot. It receives the `Vehicle`, but it does not yet compare `VehicleType` with `SpotType`.

## 3. Project Structure

```text
src/main/java/parkinglot/
├── enums/
│   ├── SpotType.java
│   └── VehicleType.java
├── model/
│   ├── ParkingLot.java
│   ├── ParkingSpot.java
│   ├── Ticket.java
│   └── Vehicle.java
├── service/
│   └── ParkingService.java
├── strategy/
│   ├── FirstAvailableStrategy.java
│   └── ParkingStrategy.java
└── Main.java
```

## 4. Responsibilities

| Component | Responsibility |
|---|---|
| `Vehicle` | Stores the license plate and `VehicleType`. |
| `ParkingSpot` | Owns spot identity, spot type, and the currently parked vehicle. |
| `ParkingLot` | Holds the collection of parking spots. |
| `Ticket` | Connects one vehicle to its assigned spot and stores a generated ID. |
| `ParkingStrategy` | Defines how a spot is selected. |
| `FirstAvailableStrategy` | Scans the lot and returns the first available spot. |
| `ParkingService` | Coordinates selection, parking, ticket creation, and unparking. |
| `Main` | Builds a small scenario for manual verification. |

The central ownership rule is simple: `ParkingSpot` owns occupancy state, while `ParkingService` orchestrates the use case.

## 5. Domain Model

### Vehicle and spot types

```java
public enum VehicleType {
    CAR,
    TRUCK,
    MOTORCYCLE
}

public enum SpotType {
    COMPACT,
    LARGE,
    MOTORCYCLE
}
```

These enums define closed sets of domain values. Compatibility between them is a policy that can be added to the allocation strategy.

### Vehicle

```java
public class Vehicle {
    private String plate;
    private VehicleType type;

    public Vehicle(String plate, VehicleType type) {
        this.plate = plate;
        this.type = type;
    }

    public String getPlate() {
        return plate;
    }

    public VehicleType getType() {
        return type;
    }
}
```

### ParkingSpot

```java
public class ParkingSpot {
    private String id;
    private SpotType type;
    private Vehicle vehicle;

    public ParkingSpot(String id, SpotType type) {
        this.id = id;
        this.type = type;
    }

    public boolean isAvailable() {
        return vehicle == null;
    }

    public void park(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void removeVehicle() {
        vehicle = null;
    }

    public String getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }
}
```

`ParkingSpot` does not expose a separate mutable `available` flag. Availability is derived from whether a vehicle is present, avoiding two pieces of state that could disagree.

### ParkingLot and Ticket

```java
public class ParkingLot {
    private List<ParkingSpot> spots;

    public ParkingLot(List<ParkingSpot> spots) {
        this.spots = spots;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }
}
```

```java
public class Ticket {
    private String id;
    private Vehicle vehicle;
    private ParkingSpot spot;

    public Ticket(String id, Vehicle vehicle, ParkingSpot spot) {
        this.id = id;
        this.vehicle = vehicle;
        this.spot = spot;
    }

    public ParkingSpot getSpot() {
        return spot;
    }
}
```

The ticket is the result of a successful parking operation. In the current implementation, unparking uses the ticket object directly rather than looking it up by ID.

## 6. Strategy Pattern

The service depends on the `ParkingStrategy` interface rather than a concrete selection algorithm:

```java
public interface ParkingStrategy {
    ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);
}
```

The current strategy performs a linear scan:

```java
public class FirstAvailableStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle) {
        for (ParkingSpot spot : lot.getSpots()) {
            if (spot.isAvailable()) {
                return spot;
            }
        }
        return null;
    }
}
```

This design allows future policies such as `NearestAvailableStrategy`, `BestFitStrategy`, or `VehicleCompatibleStrategy` without changing `ParkingService`.

## 7. Service Workflow

### Park

1. Ask the configured strategy for a spot.
2. Throw an error when no spot is available.
3. Store the vehicle in the selected spot.
4. Create and return a ticket with a UUID.

### Unpark

1. Read the assigned spot from the ticket.
2. Remove the vehicle from that spot.

```java
public class ParkingService {
    private ParkingLot parkingLot;
    private ParkingStrategy strategy;

    public ParkingService(ParkingLot parkingLot, ParkingStrategy strategy) {
        this.parkingLot = parkingLot;
        this.strategy = strategy;
    }

    public Ticket park(Vehicle vehicle) {
        ParkingSpot spot = strategy.findSpot(parkingLot, vehicle);

        if (spot == null) {
            throw new RuntimeException("No available spot");
        }

        spot.park(vehicle);

        return new Ticket(
                UUID.randomUUID().toString(),
                vehicle,
                spot
        );
    }

    public void unpark(Ticket ticket) {
        ticket.getSpot().removeVehicle();
    }
}
```

## 8. Example

```java
public class Main {
    public static void main(String[] args) {
        ParkingSpot s1 = new ParkingSpot("S1", SpotType.COMPACT);
        ParkingSpot s2 = new ParkingSpot("S2", SpotType.LARGE);

        ParkingLot lot = new ParkingLot(List.of(s1, s2));
        ParkingService service = new ParkingService(
                lot,
                new FirstAvailableStrategy()
        );

        Vehicle car = new Vehicle("ABC123", VehicleType.CAR);
        Ticket ticket = service.park(car);

        System.out.println("Parked at: " + ticket.getSpot().getId());
        service.unpark(ticket);
    }
}
```

Expected output:

```text
Parked at: S1
```

## 9. Complexity

Let `n` be the number of parking spots.

| Operation | Time | Space |
|---|---:|---:|
| `park` | `O(n)` because the strategy scans the spot list | `O(1)` excluding the returned ticket |
| `unpark` | `O(1)` | `O(1)` |

## 10. Improvements

### Enforce compatibility

Add a compatibility check to the strategy, for example:

```java
private boolean canFit(Vehicle vehicle, ParkingSpot spot) {
    return switch (vehicle.getType()) {
        case MOTORCYCLE -> true;
        case CAR -> spot.getType() == SpotType.COMPACT
                 || spot.getType() == SpotType.LARGE;
        case TRUCK -> spot.getType() == SpotType.LARGE;
    };
}
```

### Support ticket lookup and fees

- Store active tickets in `Map<String, Ticket>`.
- Add entry and exit timestamps to `Ticket`.
- Introduce a `PricingStrategy` for fee calculation.
- Mark a ticket closed after a successful exit.

### Handle concurrency

Spot selection and occupation must become one atomic operation. Otherwise, two entrances can select the same available spot before either marks it occupied. Possible solutions include synchronized allocation, per-spot locking, or an atomic reservation operation in a repository.

## 11. Interview Summary

```text
ParkingSpot     = owns occupancy state
ParkingLot      = owns the spot collection
ParkingStrategy = chooses a spot
ParkingService  = coordinates the workflow
Ticket          = records the assignment
```

Start with this minimal design, identify its assumptions, and add complexity only when the interviewer expands the requirements.
