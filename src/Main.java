import model.*;

import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Single entry point for the parking lot manager
        ParkingLotManager manager = ParkingLotManager.getInstance();


        manager.configureSampleLot();


        Ticket ticket1 = manager.checkInVehicle(new Vehicle("KA-01-AB-1234", VehicleType.FOUR_WHEELER));
        Ticket ticket2 = manager.checkInVehicle(new Vehicle("KA-05-CD-5678", VehicleType.TWO_WHEELER));
        Ticket ticket3 = manager.checkInVehicle(new Vehicle("MH-12-XY-9999", VehicleType.BUS));


        manager.printAvailability();


        if (ticket1 != null) {
            System.out.println("Checking out four-wheeler ticket: " + ticket1.getTicketID());
            manager.checkOutVehicle(ticket1.getTicketID());
        }

        if (ticket2 != null) {
            System.out.println("Checking out two-wheeler ticket: " + ticket2.getTicketID());
            manager.checkOutVehicle(ticket2.getTicketID());
        }

        if (ticket3 != null) {
            System.out.println("Checking out bus ticket: " + ticket3.getTicketID());
            manager.checkOutVehicle(ticket3.getTicketID());
        }

        System.out.println("Final available spots: " + manager.getAvailableSpotsCount());
    }
}