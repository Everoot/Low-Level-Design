package parkinglot.strategy;

import parkinglot.model.ParkingLot;
import parkinglot.model.ParkingSpot;
import parkinglot.model.Vehicle;

public interface ParkingStrategy {
    ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);
}
