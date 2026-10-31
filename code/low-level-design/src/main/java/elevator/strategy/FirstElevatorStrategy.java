package elevator.strategy;

import elevator.model.Elevator;
import elevator.model.Request;

import java.util.List;

public class FirstElevatorStrategy implements ElevatorStrategy {
    @Override
    public Elevator select(List<Elevator> elevators, Request request) {
        return elevators.get(0);
    }
}
