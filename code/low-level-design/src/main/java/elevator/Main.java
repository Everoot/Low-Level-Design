package elevator;

import elevator.enums.Direction;
import elevator.model.Elevator;
import elevator.model.Request;
import elevator.service.ElevatorService;
import elevator.strategy.FirstElevatorStrategy;

import java.util.List;

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
