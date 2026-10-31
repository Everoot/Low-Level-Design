package parkinglot.model;

import java.util.List;

public class ParkingLot {
    private List<ParkingSpot> spots;

    public ParkingLot(List<ParkingSpot> spots) {
        this.spots = spots;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }
}
