package parkinglot;

import parkinglot.enums.SpotType;
import parkinglot.enums.VehicleType;
import parkinglot.model.ParkingLot;
import parkinglot.model.ParkingSpot;
import parkinglot.model.Ticket;
import parkinglot.model.Vehicle;
import parkinglot.service.ParkingService;
import parkinglot.strategy.FirstAvailableStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ParkingSpot s1 = new ParkingSpot("S1", SpotType.COMPACT);
        ParkingSpot s2 = new ParkingSpot("S2", SpotType.LARGE);

        ParkingLot lot = new ParkingLot(List.of(s1, s2));

        ParkingService service = new ParkingService(
                lot,
                new FirstAvailableStrategy()
        );

        Vehicle car = new Vehicle("ABC123", VehicleType.CAR);

        Ticket ticket = service.park(car);

        System.out.println("Parked at: " + ticket.getSpot().getId());

        service.unpark(ticket);
    }
}
