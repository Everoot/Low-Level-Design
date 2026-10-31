# Low-Level Design

Interview-focused low-level design notes, Java implementations, and visual cheat sheets.

The goal of this repository is to practice turning requirements into clear object models, responsibilities, interfaces, and extensible implementations without overengineering.

## Repository Structure

```text
.
├── docs/                       # Design notes and interview walkthroughs
│   └── problems/
├── code/
│   └── low-level-design/       # Java 21 Maven project
├── cheatsheets/                # Visual review materials
└── zensical.toml               # Documentation site configuration
```

## Design Notes

- [Pizza Pricing System](docs/problems/Pizza%20Pricing%20System/Pizza%20Pricing%20System.md)
- [Parking Lot](docs/problems/Parking%20Lot/%20Parking%20Lot.md)
- [Rate Limiter](docs/problems/Rate%20Limiter/Rate%20Limiter.md)
- [Elevator System](docs/problems/Elevator/Elevator.md)
- [Locker System](docs/problems/Locker/Locker.md)

Each design starts with requirements and scope, then identifies core entities, responsibilities, relationships, extension points, and implementation tradeoffs.

## Code

The Java workspace is located at [`code/low-level-design`](code/low-level-design). It uses Java 21 and Maven.

Each system should live in its own package and follow a small, consistent structure:

```text
code/low-level-design/
├── pom.xml
└── src/main/java/
    ├── parkinglot/
    ├── pizza/
    ├── elevator/
    ├── locker/
    └── ratelimiter/
```

A design package can contain:

```text
<system>/
├── enums/       # Fixed domain values and states, when needed
├── model/       # Domain entities and value objects
├── service/     # Use cases and orchestration
├── strategy/    # Replaceable policies and algorithms
└── Main.java    # Small executable scenario for manual verification
```

Not every design needs every folder. Add an abstraction only when the requirements need interchangeable behavior or a clear extension point.

### Design Guidelines

- Keep domain state close to the object that owns it.
- Use services to coordinate workflows across multiple objects.
- Use strategies for behavior that may vary, such as allocation, pricing, scheduling, or rate limiting.
- Prefer enums for a closed set of domain values.
- Keep `Main.java` small and focused on demonstrating the primary flow and important edge cases.
- Start with an in-memory implementation unless persistence or distributed behavior is explicitly required.

### Build the Java Project

```bash
cd code/low-level-design
mvn compile
```

Run a compiled example by replacing `<system>` with its package name:

```bash
java -cp target/classes <system>.Main
```

For example:

```bash
java -cp target/classes parkinglot.Main
```

## Documentation Preview

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install zensical
zensical serve
```

Open `http://localhost:8000` in a browser.
