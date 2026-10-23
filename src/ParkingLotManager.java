import model.ParkingLot;
import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.TicketStatus;
import model.Vehicle;
import model.VehicleType;
import strategy.BusFeeCalculationStrategy;
import strategy.DefaultCalculationStrategy;
import strategy.DefaultSpotAllocationStrategy;
import strategy.FeeCalculationStrategy;
import strategy.FourWheelerFeeCalculationStrategy;
import strategy.SpotAllocationStrategy;
import strategy.TwoWheelerFeeCalculationStrategy;

import java.time.Duration;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotManager {
    private static ParkingLotManager instance;
    private final Map<VehicleType, FeeCalculationStrategy> feeCalculationStrategies;
    private final FeeCalculationStrategy defaultFeeCalculationStrategy;
    private final SpotAllocationStrategy spotAllocationStrategy;
    private final ParkingLot parkingLot;
    private final Map<String, Ticket> activeTickets;
    private final Object lock = new Object();

    private ParkingLotManager() {
        this.parkingLot = new ParkingLot();
        this.spotAllocationStrategy = new DefaultSpotAllocationStrategy();
        this.defaultFeeCalculationStrategy = new DefaultCalculationStrategy();
        this.feeCalculationStrategies = new EnumMap<>(VehicleType.class);
        this.feeCalculationStrategies.put(VehicleType.TWO_WHEELER, new TwoWheelerFeeCalculationStrategy());
        this.feeCalculationStrategies.put(VehicleType.FOUR_WHEELER, new FourWheelerFeeCalculationStrategy());
        this.feeCalculationStrategies.put(VehicleType.BUS, new BusFeeCalculationStrategy());
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public static synchronized ParkingLotManager getInstance() {
        if (instance == null) {
            instance = new ParkingLotManager();
        }
        return instance;
    }

    public void configureSampleLot() {
        ParkingFloor floor1 = new ParkingFloor(1, java.util.List.of(
                new ParkingSpot("L1", VehicleType.TWO_WHEELER),
                new ParkingSpot("L2", VehicleType.FOUR_WHEELER),
                new ParkingSpot("L3", VehicleType.FOUR_WHEELER),
                new ParkingSpot("L4", VehicleType.TWO_WHEELER),
                new ParkingSpot("L5", VehicleType.BUS)
        ));

        ParkingFloor floor2 = new ParkingFloor(2, java.util.List.of(
                new ParkingSpot("U6", VehicleType.TWO_WHEELER),
                new ParkingSpot("U7", VehicleType.FOUR_WHEELER),
                new ParkingSpot("U8", VehicleType.FOUR_WHEELER),
                new ParkingSpot("U9", VehicleType.TWO_WHEELER),
                new ParkingSpot("U10", VehicleType.BUS)
        ));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);
    }

    public Ticket checkInVehicle(Vehicle vehicle) {
        synchronized (lock) {
            ParkingSpot spot = spotAllocationStrategy.assignSpot(vehicle.getVehicleType(), parkingLot);
            if (spot == null) {
                System.out.println("No available spot for vehicle type: " + vehicle.getVehicleType());
                return null;
            }

            Ticket ticket = new Ticket(generateTicketID(), vehicle, spot);
            activeTickets.put(ticket.getTicketID(), ticket);
            System.out.println("Vehicle checked in. Ticket ID: " + ticket.getTicketID() + ", Spot: " + spot.getSpotId() + ", Vehicle: " + vehicle.getVehicleNumber());
            printAvailability();
            return ticket;
        }
    }

    public Ticket checkOutVehicle(String ticketID) {
        synchronized (lock) {
            Ticket ticket = activeTickets.get(ticketID);
            if (ticket == null) {
                System.out.println("Invalid ticket ID: " + ticketID);
                return null;
            }

            if (ticket.getStatus() == TicketStatus.CLOSED) {
                System.out.println("Ticket already closed: " + ticketID);
                return ticket;
            }

            Date exitDate = new Date();
            long durationInMinutes = Duration.between(ticket.getEntryTime().toInstant(), exitDate.toInstant()).toMinutes();
            FeeCalculationStrategy strategy = feeCalculationStrategies.getOrDefault(ticket.getVehicle().getVehicleType(), defaultFeeCalculationStrategy);
            double ticketFare = strategy.calculateFee(durationInMinutes);

            ticket.closeTicket(ticketFare, exitDate);
            activeTickets.remove(ticketID);

            System.out.println("Vehicle checked out. Ticket ID: " + ticket.getTicketID() + ", Fare: " + ticketFare + ", Duration (mins): " + durationInMinutes);
            printAvailability();
            return ticket;
        }
    }

    public int getAvailableSpotsCount() {
        synchronized (lock) {
            return parkingLot.getAvailableSpotsCount();
        }
    }

    public int getAvailableSpotsCountForType(VehicleType vehicleType) {
        synchronized (lock) {
            return parkingLot.getAvailableSpotsCountForType(vehicleType);
        }
    }

    public Map<String, Ticket> getActiveTickets() {
        return new java.util.HashMap<>(activeTickets);
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public FeeCalculationStrategy getFeeCalculationStrategy(VehicleType vehicleType) {
        return feeCalculationStrategies.getOrDefault(vehicleType, defaultFeeCalculationStrategy);
    }

    public void printAvailability() {
        System.out.println("Available spots: total=" + parkingLot.getAvailableSpotsCount() +
                ", two-wheeler=" + parkingLot.getAvailableSpotsCountForType(VehicleType.TWO_WHEELER) +
                ", four-wheeler=" + parkingLot.getAvailableSpotsCountForType(VehicleType.FOUR_WHEELER) +
                ", bus=" + parkingLot.getAvailableSpotsCountForType(VehicleType.BUS));
    }

    private String generateTicketID() {
        return "TICKET-" + UUID.randomUUID();
    }
}
