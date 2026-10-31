package org.example.parkinglot.service;

import org.example.parkinglot.model.ParkingSpot;
import org.example.parkinglot.model.SpotType;
import org.example.parkinglot.model.Ticket;
import org.example.parkinglot.model.VehicleType;

import java.util.*;

public class ParkingLot {
    private final List<ParkingSpot> spots;
    private final Set<String> occupiedSpots;
    private final Map<String, Ticket> activeTickets;
    private final long hourlyRateCents;

    public ParkingLot(List<ParkingSpot> spots, long hourlyRateCents) {
        this.spots = spots;
        this.hourlyRateCents = hourlyRateCents;
        this.occupiedSpots = new HashSet<>();
        this.activeTickets = new HashMap<>();

    }

    public Ticket enter(VehicleType vehicleType){
        ParkingSpot spot = findAvailableSpot(vehicleType);

        if (spot == null) {
            throw new RuntimeException("No available spot for vehicle type: " + vehicleType);
        }

        occupiedSpots.add(spot.getId());

        String ticketId = UUID.randomUUID().toString();

        long entryTime = System.currentTimeMillis();

        Ticket ticket = new Ticket(ticketId, spot.getId(), vehicleType, entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;

    }

    public long exit(String ticketId){
        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException("Ticket ID cannot be null or empty");
        }

        Ticket ticket = activeTickets.get(ticketId);

        if (ticket == null) {
            throw new RuntimeException("Invalid ticket ID: " + ticketId);
        }

        long exitTime = System.currentTimeMillis();
        long fee = calculateFee(ticket.getEntryTime(), exitTime);

        occupiedSpots.remove(ticket.getSpotId());

        activeTickets.remove(ticketId);

        return fee;
    }

    private ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        SpotType requiredSpotType = mapVehicleTypeToSpotType(vehicleType);

        for (ParkingSpot spot : spots) {
            if (spot.getSpotType() == requiredSpotType && !occupiedSpots.contains(spot.getId())) {
                return spot;
            }
        }

        return null;
    }

    private SpotType mapVehicleTypeToSpotType(VehicleType vehicleType) {
        return switch (vehicleType){
            case CAR -> SpotType.MOTORCYCLE;
            case MOTORCYCLE -> SpotType.MOTORCYCLE;
            case LARGE -> SpotType.LARGE;
        };
    }

    private long calculateFee(long entryTime, long exitTime) {
        long durationMillis = exitTime - entryTime;

        long oneHourMillis = 60 * 60 * 1000;

        long hours = durationMillis / oneHourMillis;

        if (durationMillis % oneHourMillis > 0) {
            hours++;
        }

        if (hours == 0) {
            hours = 1; // Minimum charge for 1 hour
        }

        return hours * hourlyRateCents;
    }

}
