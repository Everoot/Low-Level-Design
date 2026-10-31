package parkinglot.model;

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
