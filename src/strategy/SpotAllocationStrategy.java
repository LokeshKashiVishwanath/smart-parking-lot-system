package strategy;

import model.ParkingLot;
import model.ParkingSpot;
import model.VehicleType;

public interface SpotAllocationStrategy {
    ParkingSpot assignSpot(VehicleType vehicleType, ParkingLot parkingLot);
}
