package elevator.service;

import elevator.model.Elevator;
import elevator.model.Request;
import elevator.strategy.ElevatorStrategy;

import java.util.List;

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
