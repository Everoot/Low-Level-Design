package elevator.strategy;

import elevator.model.Elevator;
import elevator.model.Request;

import java.util.List;

public interface ElevatorStrategy {
    Elevator select(List<Elevator> elevators, Request request);
}
