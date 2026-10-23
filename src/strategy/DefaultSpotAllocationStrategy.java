package strategy;

import model.ParkingLot;
import model.ParkingFloor;
import model.ParkingSpot;
import model.VehicleType;

public class DefaultSpotAllocationStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot assignSpot(VehicleType vehicleType, ParkingLot parkingLot) {
        for (ParkingFloor floor : parkingLot.getParkingFloors()) {
            for (ParkingSpot spot : floor.getParkingSpots()) {
                if (spot.getVehicleType() == vehicleType && !spot.isOccupied()) {
                    spot.occupySpot();
                    return spot;
                }
            }
        }
        return null;
    }
}
