package elevator.model;

import elevator.enums.Direction;

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
