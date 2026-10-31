package org.example.parkinglot;

import org.example.parkinglot.model.ParkingSpot;
import org.example.parkinglot.model.SpotType;
import org.example.parkinglot.model.Ticket;
import org.example.parkinglot.model.VehicleType;
import org.example.parkinglot.service.ParkingLot;

import java.util.List;

/**
 * @Author: eve
 * @Date: 2026-10-03 13:22
 */
public class Main {
    public static void main(String[] args) {

        List<ParkingSpot> spots = List.of(
                new ParkingSpot("M1", SpotType.MOTORCYCLE),
                new ParkingSpot("C1", SpotType.CAR),
                new ParkingSpot("C2", SpotType.CAR),
                new ParkingSpot("L1", SpotType.LARGE)
        );

        ParkingLot parkingLot = new ParkingLot(spots, 500);

        Ticket ticket = parkingLot.enter(VehicleType.CAR);

        System.out.println("Ticket ID: " + ticket.getId());

        System.out.println("Spot ID: " + ticket.getSpotId());

        long fee = parkingLot.exit(ticket.getId());

        System.out.println("Fee: $" + fee / 100.0);
    }
}
