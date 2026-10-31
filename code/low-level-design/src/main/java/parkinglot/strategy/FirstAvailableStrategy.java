package parkinglot.strategy;

import parkinglot.model.ParkingLot;
import parkinglot.model.ParkingSpot;
import parkinglot.model.Vehicle;

public class FirstAvailableStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle) {
        for (ParkingSpot spot : lot.getSpots()) {
            if (spot.isAvailable()) {
                return spot;
            }
        }
        return null;
    }
}
