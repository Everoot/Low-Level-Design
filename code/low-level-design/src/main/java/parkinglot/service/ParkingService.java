package parkinglot.service;

import parkinglot.model.ParkingLot;
import parkinglot.model.ParkingSpot;
import parkinglot.model.Ticket;
import parkinglot.model.Vehicle;
import parkinglot.strategy.ParkingStrategy;

import java.util.UUID;

public class ParkingService {
    private ParkingLot parkingLot;
    private ParkingStrategy strategy;

    public ParkingService(ParkingLot parkingLot, ParkingStrategy strategy) {
        this.parkingLot = parkingLot;
        this.strategy = strategy;
    }

    public Ticket park(Vehicle vehicle) {
        ParkingSpot spot = strategy.findSpot(parkingLot, vehicle);

        if (spot == null) {
            throw new RuntimeException("No available spot");
        }

        spot.park(vehicle);

        return new Ticket(
                UUID.randomUUID().toString(),
                vehicle,
                spot
        );
    }

    public void unpark(Ticket ticket) {
        ticket.getSpot().removeVehicle();
    }
}
