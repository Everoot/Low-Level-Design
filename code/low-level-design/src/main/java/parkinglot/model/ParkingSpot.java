package parkinglot.model;

import parkinglot.enums.SpotType;

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
