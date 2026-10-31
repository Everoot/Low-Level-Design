# Low-Level Design

Interview-focused design notes and runnable Java examples for practicing object modeling, responsibility assignment, extensibility, and implementation tradeoffs.

The goal is not to memorize class diagrams. Start from requirements, identify the behavior that may change, and build the smallest design that satisfies the current scope.

## Design Problems

| System | Core model | Replaceable behavior | Java package |
|---|---|---|---|
| [Pizza Pricing System](problems/Pizza%20Pricing%20System/Pizza%20Pricing%20System.md) | Pizza, PizzaSize, ToppingType | Pricing policy | `pizza` |
| [Parking Lot](problems/Parking%20Lot/%20Parking%20Lot.md) | Vehicle, ParkingSpot, ParkingLot, Ticket | Parking spot selection | `parkinglot` |
| [Rate Limiter](problems/Rate%20Limiter/Rate%20Limiter.md) | TokenBucket, user buckets | Rate-limiting algorithm | `ratelimiter` |
| [Elevator System](problems/Elevator/Elevator.md) | Elevator, Request, Direction | Elevator selection | `elevator` |
| [Locker System](problems/Locker/Locker.md) | Package, Locker, LockerSize | Locker selection | `locker` |

## Common Structure

The examples use the same small package structure so that responsibilities are easy to compare:

```text
<system>/
├── enums/       fixed domain options and states
├── model/       entities and mutable domain state
├── service/     use-case orchestration and public API
├── strategy/    behavior that can be replaced
└── Main.java    executable example
```

Not every design needs every layer. A folder or interface is added only when it expresses a real responsibility or extension point.

## How to Approach an LLD Interview

### 1. Clarify the scope

- What is the primary workflow?
- Which cases must be supported now?
- What is explicitly out of scope?
- What failures should the system report?

### 2. Identify responsibilities

- Which object owns each piece of state?
- Which service coordinates the workflow?
- Which behavior is likely to vary?
- Which values form a closed set and can be enums?

### 3. Define the interaction

Describe the main request from entry point to result before writing code. Keep the public API small and make state transitions explicit.

### 4. Validate the design

- Run the happy path.
- Check empty, full, invalid, and repeated operations.
- State the time and space complexity.
- Explain where concurrency would break the current implementation.
- Separate current requirements from possible extensions.

!!! tip "A useful interview summary"
    **Model = what owns state · Service = what coordinates work · Strategy = how variable behavior is selected · Enum = closed domain options**

## What the Current Code Demonstrates

- Constructor-based dependency injection
- Strategy interfaces for variable policies
- In-memory state and deterministic examples
- Small services that delegate instead of owning every responsibility
- Explicit limitations instead of speculative production complexity

The implementations are intentionally minimal. Their documentation calls out missing production concerns such as validation, persistence, distributed state, concurrency, and richer error handling.

## Run the Java Examples

The source code is under `code/low-level-design/src/main/java` and targets Java 21.

```bash
cd code/low-level-design
mvn compile
java -cp target/classes parkinglot.Main
```

Replace `parkinglot` with `pizza`, `elevator`, `locker`, or `ratelimiter` to run another example.

## Recommended Review Order

1. **Pizza Pricing System** — learn the basic Strategy pattern.
2. **Parking Lot** — coordinate multiple domain objects and issue tickets.
3. **Rate Limiter** — reason about per-user state, time, and concurrency.
4. **Elevator System** — separate request modeling from scheduling policy.
5. **Locker System** — add resource assignment and occupancy state.
