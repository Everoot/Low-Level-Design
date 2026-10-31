# Elevator System

## 1. Goal

Design a simple in-memory elevator system that can:

- manage multiple elevators;
- receive a floor request with a direction;
- select an elevator through a replaceable strategy;
- move the selected elevator to the requested floor.

The implementation is intentionally small. It focuses on responsibilities and the Strategy pattern rather than door control, intermediate stops, request queues, capacity, failures, or concurrency.

## 2. Project Structure

```text
src/main/java/elevator/
├── enums/
│   └── Direction.java
├── model/
│   ├── Elevator.java
│   └── Request.java
├── service/
│   └── ElevatorService.java
├── strategy/
│   ├── ElevatorStrategy.java
│   └── FirstElevatorStrategy.java
└── Main.java
```

## 3. Responsibility of Each Layer

| Layer | Responsibility |
|---|---|
| `enums/` | Defines `UP`, `DOWN`, and `IDLE`. |
| `model/` | Stores elevator state and request data. |
| `strategy/` | Selects which elevator should handle a request. |
| `service/` | Coordinates selection and movement. |
| `Main.java` | Creates a small executable example. |

### 3.1 `enums/`

```java
public enum Direction {
    UP,
    DOWN,
    IDLE
}
```

### 3.2 `model/`

`Elevator` owns its identity, current floor, and movement direction:

```java
public class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
    }

    public void moveTo(int floor) {
        if (floor > currentFloor) {
            direction = Direction.UP;
        } else if (floor < currentFloor) {
            direction = Direction.DOWN;
        }

        currentFloor = floor;
        direction = Direction.IDLE;
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
}
```

`Request` is an immutable request description from the caller's perspective:

```java
public class Request {
    private int floor;
    private Direction direction;

    public Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }
}
```

### 3.3 `strategy/`

```java
public interface ElevatorStrategy {
    Elevator select(List<Elevator> elevators, Request request);
}
```

The current strategy always selects the first elevator:

```java
public class FirstElevatorStrategy implements ElevatorStrategy {
    @Override
    public Elevator select(List<Elevator> elevators, Request request) {
        return elevators.get(0);
    }
}
```

### 3.4 `service/`

```java
public class ElevatorService {
    private List<Elevator> elevators;
    private ElevatorStrategy strategy;

    public ElevatorService(List<Elevator> elevators, ElevatorStrategy strategy) {
        this.elevators = elevators;
        this.strategy = strategy;
    }

    public void request(Request request) {
        Elevator elevator = strategy.select(elevators, request);
        elevator.moveTo(request.getFloor());
    }
}
```

## 4. Main Example

```java
public class Main {
    public static void main(String[] args) {
        Elevator e1 = new Elevator(1);
        Elevator e2 = new Elevator(2);

        ElevatorService service = new ElevatorService(
                List.of(e1, e2),
                new FirstElevatorStrategy()
        );

        service.request(new Request(5, Direction.UP));

        System.out.println("Elevator 1 floor: " + e1.getCurrentFloor());
    }
}
```

Output:

```text
Elevator 1 floor: 5
```

## 5. Request Flow

```text
Main
  -> creates Request(floor, direction)
  -> calls ElevatorService.request(request)
  -> ElevatorStrategy selects an Elevator
  -> Elevator.moveTo(floor)
  -> Elevator returns to IDLE
```

## 6. Simple Interview Rule

```text
Elevator         = STATE
Request          = INPUT
ElevatorStrategy = HOW an elevator is selected
ElevatorService  = DO the request workflow
Direction        = OPTIONS / STATE
```

## 7. Why Use Strategy Pattern?

Elevator selection is the behavior most likely to change. The first version always chooses index `0`; later implementations can choose the nearest idle elevator, minimize direction changes, or balance load without changing `ElevatorService`.

## 8. Current Limitations

- `FirstElevatorStrategy` fails when the elevator list is empty.
- Request direction is stored but not used for selection.
- Movement is instantaneous; there is no queue or intermediate floor handling.
- Direction changes to `UP` or `DOWN` and immediately returns to `IDLE` in one method call.
- Floor ranges, capacity, doors, emergencies, and concurrency are not modeled.

## 9. Complexity

With `FirstElevatorStrategy`, selection and movement are both `O(1)`. A strategy that scans `n` elevators would usually require `O(n)` time.
